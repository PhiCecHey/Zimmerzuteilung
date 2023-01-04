package zimmerzuteilung.algorithms;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import gurobi.GRB;
import gurobi.GRBEnv;
import gurobi.GRBException;
import gurobi.GRBLinExpr;
import gurobi.GRBModel;
import gurobi.GRBVar;
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
        respectWish, respectReservations;
    }

    private static ArrayList<Gurobi.RULES> rules = new ArrayList<>();

    private static ArrayList<Team> teams = new ArrayList<>();
    private static ArrayList<Student> students = new ArrayList<>();
    // private static ArrayList<Building> buildings = new ArrayList<>();
    private static ArrayList<Room> rooms = new ArrayList<>();

    private static Allocations allocations;

    private GRBModel model;
    private GRBEnv env;
    private GRBLinExpr objective;
    private GRBVar[][] grbVars;

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

        int debug = 3;

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

            // --------------------------------------------------PRINT--------------------------------------------------

            this.grbVars = Gurobi.getGRBVars();
            double[][] x = this.model.get(GRB.DoubleAttr.X, grbVars);

            System.out.println(
                    "\n--------------------- ALLOCATION ---------------------");
            for (int r = 0; r < Gurobi.rooms.size(); r++) {
                String str = "";
                for (int t = 0; t < Gurobi.teams.size(); t++) {
                    str += x[r][t] + "\t";
                }
                System.out.println(str);
            }

            System.out.println(
                    "\n-------------------- SCORE MATRIX --------------------");

            // maximum score:
            double max = 0;
            for (int t = 0; t < Gurobi.teams.size(); ++t) {
                double m = 0;
                for (int r = 0; r < Gurobi.rooms.size(); ++r) {
                    if (m < Gurobi.allocations.get(r, t).getScore()) {
                        m = Gurobi.allocations.get(r, t).getScore();
                    }
                }
                max += m;
            }
            System.out.println("Maximum score: " + max);

            // current score:
            double cur = 0;
            for (int r = 0; r < Gurobi.rooms.size(); r++) {
                for (int t = 0; t < Gurobi.teams.size(); t++) {
                    if (x[r][t] == 1) {
                        cur += Gurobi.allocations.get(r, t).getScore();
                    }
                }
            }
            System.out.println("Current score: " + cur);

            // score matrix:
            System.out.println("Score matrix:");
            for (int r = 0; r < Gurobi.rooms.size(); ++r) {
                String str = "";
                for (int s = 0; s < Gurobi.teams.size(); ++s) {
                    str += Gurobi.allocations.get(r, s).getScore() + "\t";
                }
                System.out.println(str);
            }

            // --------------------------------------------------CLEAN--------------------------------------------------

            model.dispose();
            env.dispose();

        } catch (GRBException e) {
            System.out.println("Error code: " + e.getErrorCode() + ". "
                    + e.getMessage());
        }
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
            Gurobi.respectWish(10, 5, 3, 5);
        }
        if (rules.contains(Gurobi.RULES.respectReservations)) {
            Gurobi.respectReservations(50);
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
                this.model.addConstr(expr, GRB.LESS_EQUAL, Gurobi.rooms.get(r).getCapacity(), st);
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
                Wish wish = Gurobi.allocations.get(r, t).team().wish();

                if (wish.building1() == null) {
                    int debug = 3;
                }
                if (wish.building1().containsRoom(allocation.room())) {
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
                allocation.addToScore(-res);
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
                Gurobi.allocations.get(r, t).setScore(Gurobi.allocations.get(r, t).getScore() + random);
                objective.addTerm(allocations.get(r, t).getScore(), Gurobi.allocations.get(r, t).grbVar());
            }
        }
        return objective;
    }

}
