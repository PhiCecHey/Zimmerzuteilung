package zimmerzuteilung.algorithmen;

import java.util.List;
import java.util.ArrayList;
import zimmerzuteilung.objekte.*;
import gurobi.*;

public class Gurobi {
    public enum RULES {
        // constraints
        oneRoomPerStudent, maxStudentsPerRoom, onlySameSex,
        // other rules
        respectRoomWishes, respectMateWishes;
    }

    public static void calculate(List<Gurobi.RULES> constraints, School school) {
        Student[] aStudents = school.getStudents();
        Room[] aRooms = school.getRooms();

        try {
            // ============================== MODEL ===============================

            GRBEnv env = new GRBEnv();
            GRBModel model = new GRBModel(env);
            model.set(GRB.StringAttr.ModelName, "zimmerzuteilung");

            // ============================= VARIABLES ============================

            // zuordnung
            Allocations allocations = new Allocations(aRooms.length, aStudents.length);

            for (int r = 0; r < aRooms.length; ++r) {
                for (int s = 0; s < aStudents.length; ++s) {
                    String st = "zuteilung_" + String.valueOf(r) + "_" + String.valueOf(s);
                    allocations.set(r, s, new Allocation(
                            aRooms[r], aStudents[s], model.addVar(0.0, 1.0, 0.0, GRB.BINARY, st)));
                }
            }

            // ============================= OBJECTIVE =============================

            List<Gurobi.RULES> rules = new ArrayList<Gurobi.RULES>();

            double[][] scoreMatrix = Gurobi.calculateScores(allocations, rules, new float[] { 9, 8, 6 },
                    new float[] { 10, 10, 10 });

            GRBLinExpr objective = Gurobi.calculateObjectiveLinExpr(allocations, scoreMatrix);

            model.setObjective(objective, GRB.MAXIMIZE);

            // ============================ CONSTRAINTS ============================

            Gurobi.addConstraints(constraints, model, allocations, aRooms, aStudents);

            // ============================== OPTIMIZE =============================

            model.optimize();

            // =============================== PRINT ===============================

            GRBVar[][] grbVars = Gurobi.getGRBVars(allocations);
            double[][] x = model.get(GRB.DoubleAttr.X, grbVars);

            System.out.println("\n===================== ALLOCATION =====================");
            for (int r = 0; r < aRooms.length; r++) {
                String str = "";
                for (int s = 0; s < aStudents.length; s++) {
                    str += x[r][s] + "   ";
                }
                System.out.println(str);
            }

            System.out.println("\n==================== SCORE MATRIX ====================");
            for (int r = 0; r < aRooms.length; ++r) {
                String str = "";
                for (int s = 0; s < aStudents.length; ++s) {
                    str += scoreMatrix[r][s] + " ";
                }
                System.out.println(str);
            }

            // =============================== CLEAN ===============================

            model.dispose();
            env.dispose();

        } catch (GRBException e) {
            System.out.println("Error code: " + e.getErrorCode() + ". " +
                    e.getMessage());
        }
    }

    // ================================ CONSTRAINTS ================================

    private static void addConstraints(List<Gurobi.RULES> rules,
            GRBModel model, Allocations allocations, Room[] aRooms, Student[] aStudents) {
        if (rules.contains(Gurobi.RULES.maxStudentsPerRoom)) {
            maxStudentsPerRoom(model, allocations, aRooms, aStudents);
        }
        if (rules.contains(Gurobi.RULES.oneRoomPerStudent)) {
            oneRoomPerStudent(model, allocations, aRooms, aStudents);
        }
        if (rules.contains(Gurobi.RULES.onlySameSex)) {
            onlySameSex(model, allocations, aRooms, aStudents);
        }
    }

    private static void oneRoomPerStudent(GRBModel model,
            Allocations allocations, Room[] aRooms, Student[] aStudents) {
        try {
            GRBLinExpr expr;
            for (int s = 0; s < aStudents.length; ++s) {
                expr = new GRBLinExpr();
                for (int z = 0; z < aRooms.length; ++z) {
                    expr.addTerm(1.0, allocations.get(z, s).grbVar());
                }
                String st = "oneRoomPerStudent_" + String.valueOf(s);
                model.addConstr(expr, GRB.EQUAL, 1.0, st);
            }
        } catch (GRBException e) {
            System.out.println("Error code: " + e.getErrorCode() + ". " +
                    e.getMessage());
        }
    }

    private static void maxStudentsPerRoom(GRBModel model,
            Allocations allocations, Room[] aRooms, Student[] aStudents) {
        try {
            GRBLinExpr expr;
            for (int z = 0; z < aRooms.length; ++z) {
                expr = new GRBLinExpr();
                for (int s = 0; s < aStudents.length; ++s) {
                    expr.addTerm(1.0, allocations.get(z, s).grbVar());
                }
                String st = "maxStudentsPerRoom_" + String.valueOf(z);
                model.addConstr(expr, GRB.LESS_EQUAL, aRooms[z].getCapacity(), st);
            }
        } catch (GRBException e) {
            System.out.println("Error code: " + e.getErrorCode() + ". " +
                    e.getMessage());
        }
    }

    private static void onlySameSex(GRBModel model,
            Allocations allocations, Room[] aRooms, Student[] aStudents) {
        try {
            GRBLinExpr expr;
            for (int s1 = 0; s1 < aStudents.length; ++s1) {
                for (int s2 = 0; s2 < aStudents.length; ++s2) {
                    for (int r = 0; r < aRooms.length; ++r) {
                        expr = new GRBLinExpr();
                        if (s1 != s2) {
                            String st = "onlySameSex_" + String.valueOf(r) + "_" + String.valueOf(s1) + "_"
                                    + String.valueOf(s2);
                            expr.addTerm(1, allocations.get(r, s1).grbVar());
                            expr.addTerm(1, allocations.get(r, s2).grbVar());
                            if (aStudents[s1].getSex() == aStudents[s2].getSex()) {
                                // gleiches geschlecht
                                model.addConstr(expr, GRB.LESS_EQUAL, 2, st);
                            } else {
                                // unterschiedliches geschlecht
                                model.addConstr(expr, GRB.LESS_EQUAL, 1, st);
                            }
                        }
                    }
                }
            }
        } catch (GRBException e) {
            System.out.println("Error code: " + e.getErrorCode() + ". " +
                    e.getMessage());
        }
    }

    // ================================= OBJECTIVE =================================

    private static double[][] getScoreMatrix(Allocations allocations) {
        double[][] scoreMatrix = new double[allocations.nRooms()][allocations.nStudents()];
        for (int r = 0; r < allocations.nRooms(); ++r) {
            for (int s = 0; s < allocations.nStudents(); ++s) {
                scoreMatrix[r][s] = allocations.get(r, s).getScore();
            }
        }
        return scoreMatrix;
    }

    private static GRBVar[][] getGRBVars(Allocations allocations) {
        GRBVar[][] grbvars = new GRBVar[allocations.nRooms()][allocations.nStudents()];
        for (int r = 0; r < allocations.nRooms(); ++r) {
            for (int s = 0; s < allocations.nStudents(); ++s) {
                grbvars[r][s] = allocations.get(r, s).grbVar();
            }
        }
        return grbvars;
    }

    private static GRBLinExpr calculateObjectiveLinExpr(Allocations allocations,
            double[][] scoreMatrix) {
        GRBLinExpr objective = new GRBLinExpr();
        for (int r = 0; r < allocations.nRooms(); ++r) {
            for (int s = 0; s < allocations.nStudents(); ++s) {
                objective.addTerm(scoreMatrix[r][s], allocations.get(r, s).grbVar());
            }
        }
        return objective;
    }

    private static double[][] calculateScores(Allocations allocations,
            List<Gurobi.RULES> rules, float[] roomScores, float[] mateScores) {

        if (rules.contains(Gurobi.RULES.respectRoomWishes)) {
            Gurobi.respectRoomWishes(allocations, roomScores);
        }
        if (rules.contains(Gurobi.RULES.respectMateWishes)) {
            Gurobi.respectMateWishes(allocations, mateScores);
        }

        return Gurobi.getScoreMatrix(allocations);
    }

    private static void respectRoomWishes(Allocations allocations, float[] roomScores)
            throws ArrayIndexOutOfBoundsException {
        for (int r = 0; r < allocations.nRooms(); ++r) {
            for (int s = 0; s < allocations.nStudents(); ++s) {
                Room room = allocations.get(r, s).room();
                Student student = allocations.get(r, s).student();
                Wish wish = student.getWish();

                if (roomScores.length != wish.rooms().length) {
                    throw new ArrayIndexOutOfBoundsException("respectRoomWishes");
                }

                for (int i = 0; i < roomScores.length; ++i) {
                    if (wish.rooms()[i].id() == room.id()) {
                        allocations.get(r, s).setScore(roomScores[i]);
                    }
                }
            }
        }
    }

    private static void respectMateWishes(Allocations allocations,
            float[] mateScores) throws ArrayIndexOutOfBoundsException {
        for (int r = 0; r < allocations.nRooms(); ++r) {
            for (int s1 = 0; s1 < allocations.nStudents(); ++s1) {
                Student student1 = allocations.get(r, s1).student();
                Wish wish = student1.getWish();

                if (mateScores.length != wish.mates().length) {
                    throw new ArrayIndexOutOfBoundsException("respectMateWishes");
                }

                for (int s2 = 0; s2 < allocations.nStudents(); ++s2) {
                    for (int i = 0; i < mateScores.length; ++i) {
                        allocations.get(r, s1).setScore(mateScores[i]);
                    }
                }
            }
        }
    }
}
