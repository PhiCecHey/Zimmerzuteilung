package zimmerzuteilung.algorithms;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import org.decimal4j.util.DoubleRounder;

import gurobi.GRB;
import gurobi.GRBEnv;
import gurobi.GRBException;
import gurobi.GRBLinExpr;
import gurobi.GRBModel;
import gurobi.GRBVar;
import zimmerzuteilung.Exceptions.*;
import zimmerzuteilung.GUI.Gui;
import zimmerzuteilung.log.*;
import zimmerzuteilung.objects.Allocation;
import zimmerzuteilung.objects.Allocations;
import zimmerzuteilung.objects.Building;
import zimmerzuteilung.objects.Room;
import zimmerzuteilung.objects.Student;
import zimmerzuteilung.objects.Team;
import zimmerzuteilung.objects.Wish;

public class Gurobi {
    public enum RULES {
        // constraints
        oneRoomPerTeam, oneTeamPerRoom, maxStudentsPerRoom,
        // other rules
        respectWish, respectReservations, respectGradePrivilege;
    }

    private static ArrayList<Gurobi.RULES> rules = new ArrayList<>();

    private static ArrayList<Team> teams = new ArrayList<>();
    private static ArrayList<Team> invalidTeam = new ArrayList<>();
    private static ArrayList<Student> students = new ArrayList<>();
    // private static ArrayList<Building> buildings = new ArrayList<>();
    private static ArrayList<Room> rooms = new ArrayList<>();

    private static Allocations allocations;

    private GRBModel model;
    private GRBEnv env;
    private GRBLinExpr objective;
    private GRBVar[][] grbVars;
    private double[][] results;

    public Gurobi(final ArrayList<Gurobi.RULES> r, final ArrayList<Building> b, final ArrayList<Team> t) {
        Gurobi.rules = r;
        Gurobi.teams = t;
        // Gurobi.buildings = b;
        for (Building building : b) {
            for (Room room : building.rooms()) {
                Gurobi.rooms.add(room);
            }
        }
        for (Team team : t) {
            for (Student s : team.members()) {
                Gurobi.students.add(s);
            }
        }

        Gurobi.allocations = new Allocations(rooms.size(), Gurobi.teams.size());

        try {
            this.env = new GRBEnv();
            this.model = new GRBModel(env);
            this.model.set(GRB.StringAttr.ModelName, "zimmerzuteilung");
        } catch (GRBException e) {
            System.out.println("Error code: " + e.getErrorCode() + ". "
                    + e.getMessage());
        }
    }

    public void calculate() {
        try {
            // ------------------------------------------------VARIABLES------------------------------------------------

            // zuordnung
            for (int r = 0; r < Gurobi.rooms.size(); ++r) {
                for (int t = 0; t < Gurobi.teams.size(); ++t) {
                    String st = "zuteilung_" + Gurobi.rooms.get(r).id() + "_" + Gurobi.teams.get(t).id();
                    Gurobi.allocations.set(r, t, new Allocation(Gurobi.rooms.get(r), Gurobi.teams.get(t),
                            this.model.addVar(0.0, 1.0, 0.0, GRB.BINARY, st)));
                }
            }

            // -----------------------------------------------CONSTRAINTS-----------------------------------------------

            this.addConstraints();

            // ------------------------------------------------OBJECTIVE------------------------------------------------

            this.objective = Gurobi.calculateObjectiveLinExpr(0.1, 1);
            this.model.setObjective(this.objective, GRB.MAXIMIZE);

            // -------------------------------------------------OPTIMIZE------------------------------------------------

            this.model.optimize();

            // -----------------------------------------------GetResults------------------------------------------------

            this.extractResults();

            // --------------------------------------------------PRINT--------------------------------------------------

            String printGui = this.print(false);
            if (printGui == null) {
                Gui.result.showResults.setText("Wurden die Dateien richtig eingelesen?");
            } else {
                Gui.result.showResults.setText(printGui);
            }
            String printConsole = this.print(true);
            System.out.println(printConsole);

            // --------------------------------------------------CLEAN--------------------------------------------------

            model.dispose();
            env.dispose();
            if (printGui != null) {
                Gui.result.showResults.append("\n\nBerechnung erfolgreich.");
            }

        } catch (GRBException e) {
            System.out.println("Error code: " + e.getErrorCode() + ". "
                    + e.getMessage());
            Log.append("Ein Fehler ist während der Berechnung aufgetreten.");
            Gui.result.showResults.append("\n\nEin Fehler ist während der Berechnung aufgetreten.");
        }
        Gui.result.showResults.append("\nBerechnung beendet.");
    }

    // ---------------------------------------------------CONSTRAINTS---------------------------------------------------

    private void addConstraints() {
        if (rules.contains(Gurobi.RULES.maxStudentsPerRoom)) {
            this.maxStudentsPerRoom();
        }
        if (rules.contains(Gurobi.RULES.oneRoomPerTeam)) {
            this.oneRoomPerTeam();
        }
        if (rules.contains(Gurobi.RULES.oneTeamPerRoom)) {
            this.oneTeamPerRoom();
        }
        if (rules.contains(Gurobi.RULES.respectWish)) {
            Gurobi.respectWish(Config.scoreBuilding1, Config.scoreRoom1, Config.scoreRoom2, Config.scoreBuilding2);
        }
        if (rules.contains(Gurobi.RULES.respectGradePrivilege)) {
            Gurobi.respectGradePrivilege(Config.scoreTwelve, Config.scoreEleven, Config.scoreTen);
        }
        if (rules.contains(Gurobi.RULES.respectReservations)) {
            Gurobi.respectReservations(Config.scoreReservation);
        }
    }

    /**
     * Garanties max one room per team.
     * 
     * @param model
     * @param allocations
     * @param rooms
     * @param teams
     */
    private void oneRoomPerTeam() {
        try {
            GRBLinExpr expr;
            for (int t = 0; t < Gurobi.teams.size(); ++t) {
                expr = new GRBLinExpr();
                for (int r = 0; r < Gurobi.rooms.size(); ++r) {
                    expr.addTerm(1.0, Gurobi.allocations.get(r, t).grbVar());
                }
                String st = "oneRoomPerTeam_" + String.valueOf(t);
                this.model.addConstr(expr, GRB.EQUAL, 1.0, st);
            }
        } catch (GRBException e) {
            System.out.println("Error code: " + e.getErrorCode() + ". " + e.getMessage());
        }
    }

    /**
     * Garanties max one team per room.
     * 
     * @param model:       GRBModel object
     * @param allocations: Allocations object
     * @param rooms:       list of rooms
     * @param teams:       list of teams
     */
    private void oneTeamPerRoom() {
        try {
            GRBLinExpr expr;
            for (int r = 0; r < Gurobi.rooms.size(); ++r) {
                expr = new GRBLinExpr();
                for (int t = 0; t < Gurobi.teams.size(); ++t) {
                    expr.addTerm(1.0, Gurobi.allocations.get(r, t).grbVar());
                }
                String st = "oneTeamPerRoom_" + String.valueOf(r);
                this.model.addConstr(expr, GRB.LESS_EQUAL, 1.0, st);
            }
        } catch (GRBException e) {
            System.out.println("Error code: " + e.getErrorCode() + ". " + e.getMessage());
        }
    }

    /**
     * Garanties no more students per room than the respective room capacity.
     */
    private void maxStudentsPerRoom() {
        try {
            GRBLinExpr expr;
            for (int r = 0; r < Gurobi.rooms.size(); ++r) {
                expr = new GRBLinExpr();
                for (int t = 0; t < Gurobi.teams.size(); ++t) {
                    expr.addTerm(allocations.get(r, t).team().members().size(),
                            Gurobi.allocations.get(r, t).grbVar());
                }
                String st = "maxStudentsPerRoom_" + String.valueOf(r);
                this.model.addConstr(expr, GRB.LESS_EQUAL, Gurobi.rooms.get(r).capacity(), st);
            }
        } catch (GRBException e) {
            System.out.println("Error code: " + e.getErrorCode() + ". " + e.getMessage());
        }
    }

    /**
     * Respects the wishes of the students regarding their rooms.
     * 
     * @param b1: importance of assigning the team to a room in their
     *            first wish building
     * @param r1: importance of assigning the team to their first wish
     *            room
     * @param r2: importance of assigning the team to their second wish
     *            room
     * @param b2: importance of assigning the team to a room in their
     *            second wish building
     */
    private static void respectWish(final float b1, final float r1, final float r2, final float b2) {
        for (int r = 0; r < Gurobi.allocations.nRooms(); ++r) {
            for (int t = 0; t < Gurobi.allocations.nTeams(); ++t) {
                Allocation allocation = Gurobi.allocations.get(r, t);
                Wish wish = allocation.team().wish();

                if (wish.building1() == null || wish.building2() == null || wish.room1() == null
                        || wish.room2() == null) {
                    if (!Gurobi.invalidTeam.contains(allocation.team())) {
                        System.err.println("Das Team " + allocation.team().name()
                                + " hat keinen (vollständigen) Zimmerwunsch abgegeben!");
                        Gurobi.invalidTeam.add(allocation.team());
                        Log.append("Das Team " + allocation.team().name()
                                + " hat keinen (vollständigen) Zimmerwunsch abgegeben!");
                    }
                }

                else if (wish.building1().containsRoom(allocation.room())) {
                    allocation.addToScore(b1);
                    if (wish.room1().id() == allocation.room().id()) {
                        allocation.addToScore(r1);
                    } else if (wish.room2().id() == allocation.room().id()) {
                        allocation.addToScore(r2);
                    }
                } else if (wish.building2().containsRoom(allocation.room())) {
                    allocation.addToScore(b2);
                }
            }
        }
    }

    /**
     * Subtracts res from the allocation score so that the respective room is
     * reserved for ninth graders.
     * 
     * @param res: importance of the reservation, higher value represents
     *             higher
     *             importance
     */
    private static void respectReservations(final float res) {
        for (int r = 0; r < Gurobi.allocations.nRooms(); ++r) {
            for (int t = 0; t < Gurobi.allocations.nTeams(); ++t) {
                Allocation allocation = Gurobi.allocations.get(r, t);
                if (allocation.room().reserved()) {
                    allocation.addToScore(-res);
                }
            }
        }
    }

    /**
     * Respects the grade of the students of a team. If all students are in 12th
     * grade, they have highest privilege to get that room, followed by 11th
     * graders, then 10th graders.
     * 
     * @param twelve
     * @param eleven
     * @param ten
     */
    private static void respectGradePrivilege(final float twelve, final float eleven, final float ten) {
        for (int r = 0; r < Gurobi.allocations.nRooms(); ++r) {
            for (int t = 0; t < Gurobi.allocations.nTeams(); ++t) {
                Allocation allocation = Gurobi.allocations.get(r, t);
                Wish wish = allocation.team().wish();
                Team team = allocation.team();

                int grade = team.members().get(0).grade();
                for (Student student : team.members()) {
                    if (grade != student.grade()) {
                        grade = 0;
                        break;
                    }
                }
                if (grade == 0) {
                    continue;
                }

                if (wish.building1() == null || wish.building2() == null || wish.room1() == null
                        || wish.room2() == null) {
                    if (!Gurobi.invalidTeam.contains(allocation.team())) {
                        System.err.println("Das Team " + allocation.team().name()
                                + " hat keinen (vollständigen) Zimmerwunsch abgegeben!");
                        Gurobi.invalidTeam.add(allocation.team());
                        Log.append("Das Team " + allocation.team().name()
                                + " hat keinen (vollständigen) Zimmerwunsch abgegeben!");
                    }
                } else {
                    float score = 0;
                    if (grade == 10) {
                        score = ten;
                    } else if (grade == 11) {
                        score = eleven;
                    } else if (grade == 12) {
                        score = twelve;
                    }

                    if (wish.room1().id() == allocation.room().id() || wish.room2().id() == allocation.room().id()) {
                        allocation.addToScore(score);
                    }
                }
            }
        }
    }

    // ----------------------------------------------------OBJECTIVE----------------------------------------------------

    /**
     * Calculates the objective with respect to the allocations
     * 
     * @return gurobi objective
     */
    private static GRBVar[][] getGRBVars() {
        int nRooms = Gurobi.allocations.nRooms();
        int nTeams = Gurobi.allocations.nTeams();
        GRBVar[][] grbvars = new GRBVar[nRooms][nTeams];
        for (int r = 0; r < nRooms; ++r) {
            for (int t = 0; t < nTeams; ++t) {
                grbvars[r][t] = Gurobi.allocations.get(r, t).grbVar();
            }
        }
        return grbvars;
    }

    /**
     * TODO
     * 
     * @param min
     * @param max
     * @return
     */
    private static GRBLinExpr calculateObjectiveLinExpr(final double min, final double max) {
        GRBLinExpr objective = new GRBLinExpr();
        for (int r = 0; r < Gurobi.allocations.nRooms(); ++r) {
            for (int t = 0; t < Gurobi.allocations.nTeams(); ++t) {
                double random = ThreadLocalRandom.current().nextDouble(min, max);
                Allocation currentAlloc = Gurobi.allocations.get(r, t);
                Gurobi.allocations.get(r, t).score(currentAlloc.score() + random);
                objective.addTerm(currentAlloc.score(), currentAlloc.grbVar());
            }
        }
        return objective;
    }

    // ------------------------------------------------------PRINT------------------------------------------------------

    private void extractResults() {
        this.grbVars = Gurobi.getGRBVars();
        try {
            this.results = this.model.get(GRB.DoubleAttr.X, grbVars);
        } catch (GRBException e) {
            System.out.println("Problem in function extractResults()");
            e.printStackTrace();
        }

        for (int r = 0; r < Gurobi.rooms.size(); r++) {
            for (int t = 0; t < Gurobi.teams.size(); t++) {
                if (this.results[r][t] != 0) {
                    Team team = Gurobi.teams.get(t);
                    Room room = Gurobi.rooms.get(r);
                    try {
                        boolean allocateRoom = team.allocateRoom(room);
                        boolean allocateTeam = room.allocateTeam(team);
                        if (allocateRoom == false) {
                            throw new RoomOccupiedException("Team " + team.name() + " wurde bereits das Zimmer "
                                    + room.officialRoomNumber() + " zugeordnet.");

                        }
                        if (allocateTeam == false) {
                            throw new RoomOccupiedException(
                                    "Dem Zimmer " + room.officialRoomNumber() + " wurde bereits das Team "
                                            + team.name() + " zugeordnet.");
                        }
                        if (allocateRoom && allocateTeam) {
                            double score = Gurobi.allocations.get(r, t).score();
                            score = DoubleRounder.round(score, 1);
                            team.score((float) score);
                        }
                    } catch (RoomOccupiedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private String print(boolean all) {
        String print = "";
        if (all) {
            print += "\n-------------------- SCORE MATRIX --------------------\n";
        }

        // maximum score:
        double max = 0;
        for (int t = 0; t < Gurobi.teams.size(); ++t) {
            double m = 0;
            for (int r = 0; r < Gurobi.rooms.size(); ++r) {
                if (m < Gurobi.allocations.get(r, t).score()) {
                    m = Gurobi.allocations.get(r, t).score();
                }
            }
            max += m;
        }

        print += "Maximum score: \t" + max;

        // current score:
        double cur = 0;
        for (int r = 0; r < Gurobi.rooms.size(); r++) {
            for (int t = 0; t < Gurobi.teams.size(); t++) {
                if (this.results[r][t] == 1) {
                    cur += Gurobi.allocations.get(r, t).score();
                }
            }
        }
        print += "\nCurrent score: \t\t" + cur;
        print += "\nDifference: \t\t" + Math.abs(max - cur) + "\n\n";

        if (max == 0 && cur == 0) {
            return null;
        }

        String teamNames = "";
        for (int t = 0; t < Gurobi.teams.size(); t++) {
            teamNames += Gurobi.teams.get(t).name() + "\t";
        }

        // score matrix:
        if (all) {
            print += "\n\n--------------------- Score matrix: ---------------------";
            print += "\n" + teamNames + "ZimmerNr";
            for (int r = 0; r < Gurobi.rooms.size(); ++r) {
                String str = "";
                for (int s = 0; s < Gurobi.teams.size(); ++s) {
                    str += DoubleRounder.round(Gurobi.allocations.get(r, s).score(), 1) + "\t";
                }
                print += "\n" + str + Gurobi.rooms.get(r).officialRoomNumber();
            }

            print += "\n\n--------------------- ALLOCATION ---------------------";
        }

        print += "\n" + teamNames + "ZimmerNr";

        for (int r = 0; r < Gurobi.rooms.size(); r++) {
            String allocated = "";
            for (int t = 0; t < Gurobi.teams.size(); t++) {
                if (this.results[r][t] == 0) {
                    allocated += " - \t";
                } else {
                    allocated += " # \t";
                }
            }
            if (allocated.contains("#")) {
                print += "\n" + allocated + " " + Gurobi.rooms.get(r).officialRoomNumber();
            }
        }
        return print;
    }
}
