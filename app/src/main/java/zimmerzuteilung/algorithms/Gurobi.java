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
import zimmerzuteilung.Config;
import zimmerzuteilung.Exceptions.RoomOccupiedException;
import zimmerzuteilung.Log;
import zimmerzuteilung.GUI.Gui;
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
        respectWish, respectReservations, respectGradePrivilege, respectRoomGender, addExtraRandomness, respectPrevRoom;
    }

    private ArrayList<Gurobi.RULES> rules;

    private ArrayList<Team> teams;
    private ArrayList<Team> invalidTeam;
    private ArrayList<Student> students;
    private ArrayList<Building> buildings;
    private ArrayList<Room> rooms;

    // TODO: bad design
    public static ArrayList<Room> unoccupiedRooms = new ArrayList<>();
    public static ArrayList<Team> unallocatedTeams = new ArrayList<>();

    private Allocations allocations;

    private GRBModel model;
    private GRBEnv env;
    private GRBLinExpr objective;
    private GRBVar[][] grbVars;
    private double[][] results;

    public Gurobi(final ArrayList<Gurobi.RULES> r, final ArrayList<Building> b, final ArrayList<Team> t) {
        Log.clear();
        this.rooms = new ArrayList<>();
        this.students = new ArrayList<>();
        this.invalidTeam = new ArrayList<>();
        this.buildings = b;
        this.rules = r;
        this.teams = t;

        System.out.println(this.rules);

        for (Building building : this.buildings) {
            for (Room room : building.rooms()) {
                this.rooms.add(room);
            }
        }
        for (Team team : t) {
            for (Student s : team.members()) {
                this.students.add(s);
            }
        }

        this.unoccupyAllRooms();
        this.allocations = new Allocations(rooms.size(), this.teams.size());

        try {
            this.env = new GRBEnv();
            this.model = new GRBModel(env);
            this.model.set(GRB.StringAttr.ModelName, "zimmerzuteilung");
        } catch (GRBException e) {
            System.err.println("Error code: " + e.getErrorCode() + ". " + e.getMessage());
        }
    }

    public void calculate() throws GRBException {
        try {
            // ------------------------------------------------VARIABLES------------------------------------------------

            // zuordnung
            for (int r = 0; r < this.rooms.size(); ++r) {
                for (int t = 0; t < this.teams.size(); ++t) {
                    String st = "zuteilung_" + this.rooms.get(r).id() + "_" + this.teams.get(t).id();
                    this.allocations.set(r, t, new Allocation(this.rooms.get(r), this.teams.get(t),
                            this.model.addVar(0.0, 1.0, 0.0, GRB.BINARY, st)));
                }
            }

            // -----------------------------------------------CONSTRAINTS-----------------------------------------------

            this.addConstraints();

            // ------------------------------------------------OBJECTIVE------------------------------------------------

            this.objective = this.calculateObjectiveLinExpr(0.1, 1,
                    this.rules.contains(Gurobi.RULES.addExtraRandomness));
            this.model.setObjective(this.objective, GRB.MAXIMIZE);

            // -------------------------------------------------OPTIMIZE------------------------------------------------

            this.model.optimize();

            // -----------------------------------------------GetResults------------------------------------------------

            boolean worked = this.extractResults();

            // --------------------------------------------------PRINT--------------------------------------------------

            String printGui = this.print(false, worked);
            if (printGui == null) {
                Gui.result.showResults.append("Wurden die Dateien richtig eingelesen?\n");
            } else {
                Gui.result.showResults.append(printGui + "\n\n");
            }
            String printConsole = this.print(true, worked);
            System.out.println(printConsole);

            // --------------------------------------------------CLEAN--------------------------------------------------

            model.dispose();
            env.dispose();

            System.out.println("FERTIG");

        } catch (GRBException e) {
            System.out.println("Error code: " + e.getErrorCode() + ". "
                    + e.getMessage());
            Log.append("Ein Fehler ist waehrend der Berechnung aufgetreten.");
            Gui.result.showResults.append("\n\nEin Fehler ist waehrend der Berechnung aufgetreten.");
            model.dispose();
            throw e;
        }
        Gui.result.showResults.append("\n\nBerechnung beendet.\n\n");
    }

    // ---------------------------------------------------CONSTRAINTS---------------------------------------------------

    // tested, works
    private void addConstraints() {
        if (this.rules.contains(Gurobi.RULES.maxStudentsPerRoom)) {
            this.maxStudentsPerRoom();
        } else {
            this.maxStudentsPerRoomAlternative(Config.maxStudentsPerRoom);
        }
        if (this.rules.contains(Gurobi.RULES.oneRoomPerTeam)) {
            this.oneRoomPerTeam();
        }
        if (this.rules.contains(Gurobi.RULES.oneTeamPerRoom)) {
            this.oneTeamPerRoom();
        }
        if (this.rules.contains(Gurobi.RULES.respectWish)) {
            this.respectWish(Config.scoreBuilding1, Config.scoreRoom1, Config.scoreRoom2, Config.scoreBuilding2);
        }
        if (this.rules.contains(Gurobi.RULES.respectGradePrivilege)) {
            this.respectGradePrivilege(Config.scoreTwelve, Config.scoreEleven, Config.scoreTen);
        }
        if (this.rules.contains(Gurobi.RULES.respectReservations)) {
            this.respectReservations();
        } else {
            this.respectReservationsAlternative(Config.scoreReservation);
        }
        if (this.rules.contains(Gurobi.RULES.respectRoomGender)) {
            this.respectRoomGender();
        } else {
            this.respectRoomGenderAlternative(Config.scoreGender);
        }
        if (this.rules.contains(Gurobi.RULES.respectPrevRoom)) {
            this.respectPrevRoom();
        } else {
            this.respectPrevAlternative(Config.scoreStayInRoom, Config.scoreStayInBuilding);
        }
    }

    // tested, works
    /**
     * Guaranties max one room per team.
     * 
     * @param model
     * @param allocations
     * @param rooms
     * @param teams
     */
    private void oneRoomPerTeam() {
        try {
            GRBLinExpr expr;
            for (int t = 0; t < this.teams.size(); ++t) {
                expr = new GRBLinExpr();
                for (int r = 0; r < this.rooms.size(); ++r) {
                    expr.addTerm(1.0, this.allocations.get(r, t).grbVar());
                }
                String st = "oneRoomPerTeam_" + String.valueOf(t);
                this.model.addConstr(expr, GRB.EQUAL, 1.0, st);
            }
        } catch (GRBException e) {
            System.out.println("Error code: " + e.getErrorCode() + ". " + e.getMessage());
        }
    }

    // tested, works
    /**
     * Guaranties max one team per room.
     * 
     * @param model:       GRBModel object
     * @param allocations: Allocations object
     * @param rooms:       list of rooms
     * @param teams:       list of teams
     */
    private void oneTeamPerRoom() {
        try {
            GRBLinExpr expr;
            for (int r = 0; r < this.rooms.size(); ++r) {
                expr = new GRBLinExpr();
                for (int t = 0; t < this.teams.size(); ++t) {
                    expr.addTerm(1.0, this.allocations.get(r, t).grbVar());
                }
                String st = "oneTeamPerRoom_" + String.valueOf(r);
                this.model.addConstr(expr, GRB.LESS_EQUAL, 1.0, st);
            }
        } catch (GRBException e) {
            System.out.println("Error code: " + e.getErrorCode() + ". " + e.getMessage());
        }
    }

    // tested, works
    private void maxStudentsPerRoom() {
        try {
            GRBLinExpr expr;
            for (int r = 0; r < this.rooms.size(); ++r) {
                expr = new GRBLinExpr();
                for (int t = 0; t < this.teams.size(); ++t) {
                    expr.addTerm(allocations.get(r, t).team().members().size(),
                            this.allocations.get(r, t).grbVar());
                }
                String st = "maxStudentsPerRoom_" + String.valueOf(r);
                this.model.addConstr(expr, GRB.LESS_EQUAL, this.rooms.get(r).capacity(), st);
            }
        } catch (GRBException e) {
            System.out.println("Error code: " + e.getErrorCode() + ". " + e.getMessage());
        }
    }

    // tested, works
    private void maxStudentsPerRoomAlternative(float scoreMaxStudents) {
        for (int r = 0; r < this.allocations.nRooms(); ++r) {
            for (int t = 0; t < this.allocations.nTeams(); ++t) {
                Allocation allocation = this.allocations.get(r, t);
                int numStudents = allocation.team().members().size();
                int maxStudents = allocation.room().capacity();
                if (numStudents > maxStudents) {
                    allocation.addToScore(scoreMaxStudents);
                }
            }
        }
    }

    // tested, works
    private void respectReservations() {
        try {
            GRBLinExpr expr;
            for (int r = 0; r < this.rooms.size(); ++r) {
                expr = new GRBLinExpr();
                for (int t = 0; t < this.teams.size(); ++t) {
                    Allocation alloc = this.allocations.get(r, t);
                    if (alloc.room().reserved()) {
                        expr.addTerm(1, alloc.grbVar());
                    }
                }
                String st = "respectRoomReservation_" + String.valueOf(r);
                this.model.addConstr(expr, GRB.EQUAL, 0, st);
            }
        } catch (GRBException e) {
            System.out.println("Error code: " + e.getErrorCode() + ". " + e.getMessage());
        }
    }

    // tested, works
    /**
     * Adds res from the allocation score so that the respective room is
     * reserved for ninth graders.
     * 
     * @param res: importance of the reservation, higher value represents
     *             higher importance
     */
    private void respectReservationsAlternative(final float res) {
        for (int r = 0; r < this.allocations.nRooms(); ++r) {
            for (int t = 0; t < this.allocations.nTeams(); ++t) {
                Allocation allocation = this.allocations.get(r, t);
                if (allocation.room().reserved()) {
                    allocation.addToScore(res);
                }
            }
        }
    }

    // tested, works
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
    private void respectWish(final float b1, final float r1, final float r2, final float b2) {
        for (int r = 0; r < this.allocations.nRooms(); ++r) {
            for (int t = 0; t < this.allocations.nTeams(); ++t) {
                Allocation allocation = this.allocations.get(r, t);
                Wish wish = allocation.team().wish();

                if (wish.building1() == null || wish.building2() == null || wish.room1() == null
                        || wish.room2() == null) {
                    if (!this.invalidTeam.contains(allocation.team())) {
                        System.err.println("Das Team " + allocation.team().name()
                                + " hat keinen (vollstaendigen) Zimmerwunsch abgegeben und somit kann kein "
                                + "Zimmer zugewiesen werden!");
                        this.invalidTeam.add(allocation.team());
                        Log.append("Das Team " + allocation.team().name()
                                + " hat keinen (vollstaendigen) Zimmerwunsch abgegeben!");
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

    // tested, works
    /**
     * Respects the grade of the students of a team. If all students are in 12th
     * grade, they have highest privilege to get that room, followed by 11th
     * graders, then 10th graders.
     * 
     * @param twelve
     * @param eleven
     * @param ten
     */
    private void respectGradePrivilege(final float twelve, final float eleven, final float ten) {
        for (int r = 0; r < this.allocations.nRooms(); ++r) {
            for (int t = 0; t < this.allocations.nTeams(); ++t) {
                Allocation allocation = this.allocations.get(r, t);
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
                    if (!this.invalidTeam.contains(allocation.team())) {
                        System.err.println("Das Team " + allocation.team().name()
                                + " hat keinen (vollstaendigen) Zimmerwunsch abgegeben!");
                        this.invalidTeam.add(allocation.team());
                        Log.append("Das Team " + allocation.team().name()
                                + " hat keinen (vollstaendigen) Zimmerwunsch abgegeben!");
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

    // tested, works
    private void respectRoomGender() {
        try {
            GRBLinExpr expr;
            for (int r = 0; r < this.rooms.size(); ++r) {
                for (int t = 0; t < this.teams.size(); ++t) {
                    Allocation alloc = allocations.get(r, t);
                    boolean genderMatches = alloc.team().gender() == alloc.room().gender();
                    String st = "respectRoomGender_" + String.valueOf(r);
                    if (!genderMatches) {
                        // team may not be in this room
                        expr = new GRBLinExpr();
                        expr.addTerm(1, alloc.grbVar());
                        this.model.addConstr(expr, GRB.EQUAL, 0, st);
                    }
                }
            }
        } catch (GRBException e) {
            System.out.println("Error code: " + e.getErrorCode() + ". " + e.getMessage());
        }
    }

    // tested, works
    private void respectRoomGenderAlternative(float scoreGender) {
        for (int r = 0; r < this.allocations.nRooms(); ++r) {
            for (int t = 0; t < this.allocations.nTeams(); ++t) {
                Allocation allocation = this.allocations.get(r, t);
                if (allocation.room().gender() != allocation.team().gender()) {
                    allocation.addToScore(scoreGender);
                }
            }
        }
    }

    // tested, works
    /**
     * If a team wants to stay in their previous room, make sure that they will.
     */
    private void respectPrevRoom() {
        try {
            GRBLinExpr expr;
            for (int r = 0; r < this.rooms.size(); ++r) {
                for (int t = 0; t < this.teams.size(); ++t) {
                    Allocation alloc = allocations.get(r, t);
                    // if this room is not this teams first wish, continue
                    if (alloc.team().wish().room1() == null
                            || (alloc.room().id() != alloc.team().wish().room1().id())) {
                        continue;
                    }
                    // if team wants to stay in room, make sure it stays in room
                    if (alloc.team().canStayInRoom()) {
                        expr = new GRBLinExpr();
                        expr.addTerm(1, alloc.grbVar());
                        String st = "respectPrevRoom_" + String.valueOf(r);
                        this.model.addConstr(expr, GRB.EQUAL, 1, st);
                    }
                }
            }
        } catch (GRBException e) {
            System.out.println("Error code: " + e.getErrorCode() + ". " + e.getMessage());
        }
    }

    // tested, works
    private void respectPrevAlternative(float scorePrevRoom, float scorePrevBuilding) {
        for (int r = 0; r < this.allocations.nRooms(); ++r) {
            for (int t = 0; t < this.allocations.nTeams(); ++t) {
                Allocation allocation = this.allocations.get(r, t);
                Wish wish = allocation.team().wish();

                if (wish.building1() == null || wish.building2() == null || wish.room1() == null
                        || wish.room2() == null) {
                    continue;
                } else if (wish.building1().containsRoom(allocation.room())) {
                    if (allocation.team().canStayInBuilding()) {
                        allocation.addToScore(scorePrevBuilding);
                    }
                    if (wish.room1().id() == allocation.room().id()) {
                        if (allocation.team().canStayInRoom()) {
                            allocation.addToScore(scorePrevRoom);
                        }
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
    private GRBVar[][] getGRBVars() {
        int nRooms = this.allocations.nRooms();
        int nTeams = this.allocations.nTeams();
        GRBVar[][] grbvars = new GRBVar[nRooms][nTeams];
        for (int r = 0; r < nRooms; ++r) {
            for (int t = 0; t < nTeams; ++t) {
                grbvars[r][t] = this.allocations.get(r, t).grbVar();
            }
        }
        return grbvars;
    }

    private GRBLinExpr calculateObjectiveLinExpr(final double min, final double max, boolean addExtraRandomness) {
        GRBLinExpr objective = new GRBLinExpr();
        for (int r = 0; r < this.allocations.nRooms(); ++r) {
            for (int t = 0; t < this.allocations.nTeams(); ++t) {
                double random;
                if (addExtraRandomness) {
                    random = ThreadLocalRandom.current().nextDouble(min, max + Config.scoreRandom);
                } else {
                    random = ThreadLocalRandom.current().nextDouble(min, max);
                }
                Allocation currentAlloc = this.allocations.get(r, t);
                currentAlloc.score(currentAlloc.score() + random);
                objective.addTerm(currentAlloc.score(), currentAlloc.grbVar());
            }
        }
        return objective;
    }

    // ------------------------------------------------------PRINT------------------------------------------------------

    private boolean extractResults() {
        this.grbVars = this.getGRBVars();
        try {
            int optimstatus = model.get(GRB.IntAttr.Status);
            if (optimstatus != GRB.OPTIMAL) {
                System.out.println("Model infeasible.");
                Log.append("\n\n\nEs konnte keine Zuteilung gefunden werden. Eine Zuteilung koennte nach Aenderung "
                        + "der Parameter im Tab Gurobi moeglich sein.");
                model.dispose();
                env.dispose();
                return false;
            }
            this.results = this.model.get(GRB.DoubleAttr.X, grbVars);
        } catch (GRBException e) {
            System.err.println("Problem in function extractResults()");
            e.printStackTrace();
            return false;
        }

        boolean worked = true;

        for (int r = 0; r < this.rooms.size(); r++) {
            for (int t = 0; t < this.teams.size(); t++) {
                if (this.results[r][t] != 0) {
                    Team team = this.teams.get(t);
                    Room room = this.rooms.get(r);
                    try { // tested, works
                        boolean allocateRoom = team.allocateRoom(room);
                        boolean allocateTeam = room.allocateTeam(team);
                        // if room is empty or several teams may be in the room:
                        if ((allocateRoom || !this.rules.contains(Gurobi.RULES.oneRoomPerTeam))
                                && (allocateTeam || !this.rules.contains(Gurobi.RULES.oneTeamPerRoom))) {
                            double score = this.allocations.get(r, t).score();
                            score = DoubleRounder.round(score, 1);
                            team.score((float) score);
                        } else if (!allocateRoom) {
                            throw new RoomOccupiedException("Team " + team.name() + " wurde bereits das Zimmer "
                                    + room.officialRoomNumber() + " zugeordnet.");
                        } else if (!allocateTeam) {
                            throw new RoomOccupiedException(
                                    "Dem Zimmer " + room.officialRoomNumber() + " wurde bereits das Team "
                                            + team.name() + " zugeordnet.");
                        } else {
                            int debug = 3;
                        }
                    } catch (RoomOccupiedException e) {
                        e.printStackTrace();
                        worked = false;
                    }
                }
            }
        }
        return worked;
    }

    private String print(boolean all, boolean worked) {
        String print = Log.log() + "\n\n\n";
        if (!worked) {
            return print;
        }

        if (all) {
            print += "\n-------------------- SCORE MATRIX --------------------\n";
        }

        // maximum score:
        double max = 0;
        for (int t = 0; t < this.teams.size(); ++t) {
            double m = 0;
            for (int r = 0; r < this.rooms.size(); ++r) {
                if (m < this.allocations.get(r, t).score()) {
                    m = this.allocations.get(r, t).score();
                }
            }
            max += m;
        }

        print += "Maximum score: \t" + max;

        // current score:
        double cur = 0;
        for (int r = 0; r < this.rooms.size(); r++) {
            for (int t = 0; t < this.teams.size(); t++) {
                if (this.results[r][t] == 1) {
                    cur += this.allocations.get(r, t).score();
                }
            }
        }
        print += "\nCurrent score: \t\t" + cur;
        print += "\nDifference: \t\t" + Math.abs(max - cur) + "\n\n";

        if (max == 0 && cur == 0) {
            return null;
        }

        String teamNames = "";
        for (int t = 0; t < this.teams.size(); t++) {
            teamNames += this.teams.get(t).name() + "\t";
        }

        // score matrix:
        if (all) {
            print += "\n\n--------------------- Score matrix: ---------------------";
            print += "\n" + "ZimmerNr" + teamNames;
            for (int r = 0; r < this.rooms.size(); ++r) {
                String str = "";
                for (int s = 0; s < this.teams.size(); ++s) {
                    str += DoubleRounder.round(this.allocations.get(r, s).score(), 1) + "\t\t";
                }
                print += "\n" + this.rooms.get(r).officialRoomNumber() + str;
            }

            print += "\n\n--------------------- ALLOCATION ---------------------";
        }

        print += "\n" + "ZimmerNr" + "\t" + teamNames;

        for (int r = 0; r < this.rooms.size(); r++) {
            String allocated = "";
            for (int t = 0; t < this.teams.size(); t++) {
                if (this.results[r][t] == 0) {
                    allocated += "\t        -\t";
                } else {
                    allocated += "\t        #\t";
                }
            }
            if (allocated.contains("#")) {
                print += "\n" + this.rooms.get(r).officialRoomNumber() + allocated + " ";
            }
        }
        return print;
    }

    // tested, works
    private void unoccupyAllRooms() {
        for (Room room : this.rooms) {
            room.unallocateTeams();
        }
        for (Team team : this.teams) {
            team.score(0);
            team.unallocateRoom();
        }
    }

    // tested, works
    public ArrayList<Room> unoccupiedRooms() {
        for (Room room : this.rooms) {
            if (room.allocatedTeams().size() == 0) {
                Gurobi.unoccupiedRooms.add(room);
            }
        }
        return Gurobi.unoccupiedRooms;
    }

    // tested, works
    public ArrayList<Team> unallocatedTeams() {
        for (Team team : this.teams) {
            if (team.allocatedRooms().size() == 0) {
                Gurobi.unallocatedTeams.add(team);
            }
        }
        return Gurobi.unallocatedTeams;
    }
}
