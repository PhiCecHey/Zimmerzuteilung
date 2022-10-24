package zimmerzuteilung.algorithmen;

import java.util.ArrayList;
import zimmerzuteilung.objekte.*;
import gurobi.*;

public class Gurobi {
    public enum RULES {
        // constraints
        oneRoomPerTeam, oneTeamPerRoom, maxStudentsPerRoom,
        // other rules
        respectWish;
    }

    public static void calculate(final ArrayList<Gurobi.RULES> rules,
            final ArrayList<Room> rooms, final ArrayList<Team> teams) {

        try {
            // ============================= MODEL =============================

            GRBEnv env = new GRBEnv();
            GRBModel model = new GRBModel(env);
            model.set(GRB.StringAttr.ModelName, "zimmerzuteilung");

            // =========================== VARIABLES ===========================

            // zuordnung
            Allocations allocations = new Allocations(rooms.size(),
                    teams.size());

            for (int r = 0; r < rooms.size(); ++r) {
                for (int t = 0; t < teams.size(); ++t) {
                    String st = "zuteilung_" + rooms.get(r).id()
                            + "_" + teams.get(t).id();
                    allocations.set(r, t, new Allocation(
                            rooms.get(r), teams.get(t),
                            model.addVar(0.0, 1.0, 0.0, GRB.BINARY, st)));
                }
            }

            // ========================== CONSTRAINTS ==========================

            Gurobi.addConstraints(rules, model, allocations, rooms, teams);

            // =========================== OBJECTIVE ===========================

            GRBLinExpr objective = Gurobi.calculateObjectiveLinExpr(
                    allocations);

            model.setObjective(objective, GRB.MAXIMIZE);

            // ============================ OPTIMIZE ===========================

            model.optimize();

            // ============================= PRINT =============================

            GRBVar[][] grbVars = Gurobi.getGRBVars(allocations);
            double[][] x = model.get(GRB.DoubleAttr.X, grbVars);

            System.out.println(
                    "\n===================== ALLOCATION =====================");
            for (int r = 0; r < rooms.size(); r++) {
                String str = "";
                for (int t = 0; t < teams.size(); t++) {
                    str += x[r][t] + "   ";
                }
                System.out.println(str);
            }

            System.out.println(
                    "\n==================== SCORE MATRIX ====================");
            for (int r = 0; r < rooms.size(); ++r) {
                String str = "";
                for (int s = 0; s < teams.size(); ++s) {
                    str += allocations.get(r, s).getScore() + " ";
                }
                System.out.println(str);
            }

            // ============================= CLEAN =============================

            model.dispose();
            env.dispose();

        } catch (GRBException e) {
            System.out.println("Error code: " + e.getErrorCode() + ". "
                    + e.getMessage());
        }
    }

    // ============================== CONSTRAINTS ==============================

    private static void addConstraints(final ArrayList<Gurobi.RULES> rules,
            final GRBModel model, final Allocations allocations,
            ArrayList<Room> rooms, ArrayList<Team> teams) {
        if (rules.contains(Gurobi.RULES.maxStudentsPerRoom)) {
            maxStudentsPerRoom(model, allocations, rooms, teams);
        }
        if (rules.contains(Gurobi.RULES.oneRoomPerTeam)) {
            oneRoomPerTeam(model, allocations, rooms, teams);
        }
        if (rules.contains(Gurobi.RULES.oneTeamPerRoom)) {
            oneTeamPerRoom(model, allocations, rooms, teams);
        }
        if (rules.contains(Gurobi.RULES.respectWish)) {
            respectWish(allocations, 10, 5, 3, 5);
        }
    }

    private static void oneRoomPerTeam(final GRBModel model,
            final Allocations allocations, ArrayList<Room> rooms,
            ArrayList<Team> teams) {
        try {
            GRBLinExpr expr;
            for (int t = 0; t < teams.size(); ++t) {
                expr = new GRBLinExpr();
                for (int r = 0; r < rooms.size(); ++r) {
                    expr.addTerm(1.0, allocations.get(r, t).grbVar());
                }
                String st = "oneRoomPerTeam_" + String.valueOf(t);
                model.addConstr(expr, GRB.EQUAL, 1.0, st);
            }
        } catch (GRBException e) {
            System.out.println("Error code: " + e.getErrorCode() + ". "
                    + e.getMessage());
        }
    }

    private static void oneTeamPerRoom(final GRBModel model,
            final Allocations allocations, ArrayList<Room> rooms,
            ArrayList<Team> teams) {
        try {
            GRBLinExpr expr;
            for (int r = 0; r < rooms.size(); ++r) {
                expr = new GRBLinExpr();
                for (int t = 0; t < teams.size(); ++t) {
                    expr.addTerm(1.0, allocations.get(r, t).grbVar());
                }
                String st = "oneTeamPerRoom_" + String.valueOf(r);
                model.addConstr(expr, GRB.LESS_EQUAL, 1.0, st);
            }
        } catch (GRBException e) {
            System.out.println("Error code: " + e.getErrorCode() + ". "
                    + e.getMessage());
        }
    }

    private static void maxStudentsPerRoom(final GRBModel model,
            final Allocations allocations,
            ArrayList<Room> rooms, ArrayList<Team> teams) {
        try {
            GRBLinExpr expr;
            for (int r = 0; r < rooms.size(); ++r) {
                expr = new GRBLinExpr();
                for (int t = 0; t < teams.size(); ++t) {
                    expr.addTerm(allocations.get(r, t).team().members.size(),
                            allocations.get(r, t).grbVar());
                }
                String st = "maxStudentsPerRoom_" + String.valueOf(r);
                model.addConstr(
                        expr, GRB.LESS_EQUAL, rooms.get(r).getCapacity(), st);
            }
        } catch (GRBException e) {
            System.out.println("Error code: " + e.getErrorCode() + ". "
                    + e.getMessage());
        }
    }

    private static void respectWish(final Allocations allocations,
            final float b1, final float r1, final float r2, final float b2) {
        for (int r = 0; r < allocations.nRooms(); ++r) {
            for (int t = 0; t < allocations.nTeams(); ++t) {
                Allocation allocation = allocations.get(r, t);
                Wish wish = allocations.get(r, t).team().wish();

                if (wish.building1().hasRoom(allocation.room())) {
                    allocation.addToScore(b1);
                    if (wish.room1().id() == allocation.room().id()) {
                        allocation.addToScore(r1);
                    } else if (wish.room2().id() == allocation.room().id()) {
                        allocation.addToScore(r2);
                    }
                } else if (wish.building2().hasRoom(allocation.room())) {
                    allocation.addToScore(b2);
                }
            }
        }
    }

    // =============================== OBJECTIVE ===============================

    private static GRBVar[][] getGRBVars(final Allocations allocations) {
        int nRooms = allocations.nRooms();
        int nTeams = allocations.nTeams();
        GRBVar[][] grbvars = new GRBVar[nRooms][nTeams];
        for (int r = 0; r < nRooms; ++r) {
            for (int t = 0; t < nTeams; ++t) {
                grbvars[r][t] = allocations.get(r, t).grbVar();
            }
        }
        return grbvars;
    }

    private static GRBLinExpr calculateObjectiveLinExpr(
            final Allocations allocations) {
        GRBLinExpr objective = new GRBLinExpr();
        for (int r = 0; r < allocations.nRooms(); ++r) {
            for (int t = 0; t < allocations.nTeams(); ++t) {
                objective.addTerm(allocations.get(r, t).getScore(),
                        allocations.get(r, t).grbVar());
            }
        }
        return objective;
    }

}
