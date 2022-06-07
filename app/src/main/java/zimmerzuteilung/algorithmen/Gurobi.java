package zimmerzuteilung.algorithmen;

import java.util.List;
import zimmerzuteilung.objekte.*;
import gurobi.*;

public class Gurobi {
    public enum CONSTRAINTS {
        oneRoomPerStudent, maxStudentsPerRoom, onlySameSex;
    }

    static class Combination {
        GRBVar grbvar;
        Student student;
        Room room;
        double score = 1;

        Combination(GRBVar g, Room r, Student s) {
            this.grbvar = g;
            this.room = r;
            this.student = s;
        }
    }

    public static void calculate(List<Gurobi.CONSTRAINTS> constraints, School school) {
        Student[] aStudents = school.getStudents();
        Room[] aRooms = school.getRooms();

        try {
            // ============================== MODEL ===============================

            GRBEnv env = new GRBEnv();
            GRBModel model = new GRBModel(env);
            model.set(GRB.StringAttr.ModelName, "zimmerzuteilung");

            // ============================= VARIABLES ============================

            // zuordnung
            Combination[][] roomsStudents = new Combination[aRooms.length][aStudents.length];

            for (int r = 0; r < aRooms.length; ++r) {
                for (int s = 0; s < aStudents.length; ++s) {
                    String st = "zuteilung_" + String.valueOf(r) + "_" + String.valueOf(s);
                    roomsStudents[r][s] = new Combination(
                            model.addVar(0.0, 1.0, 0.0, GRB.BINARY, st), aRooms[r], aStudents[s]);
                }
            }

            // ============================= OBJECTIVE =============================

            double[][] scoreMatrix = Gurobi.calculateScores(roomsStudents,
                    aRooms.length, aStudents.length);

            GRBLinExpr objective = Gurobi.calculateObjectiveLinExpr(roomsStudents,
                    scoreMatrix, aRooms.length, aStudents.length);

            model.setObjective(objective, GRB.MAXIMIZE);

            // ============================ CONSTRAINTS ============================

            Gurobi.addConstraints(constraints, model, roomsStudents, aRooms, aStudents);

            // ============================== OPTIMIZE =============================

            model.optimize();

            // =============================== PRINT ===============================

            GRBVar[][] grbVars = new GRBVar[aRooms.length][aStudents.length];

            for (int r = 0; r < aRooms.length; ++r) {
                for (int s = 0; s < aStudents.length; ++s) {
                    grbVars[r][s] = roomsStudents[r][s].grbvar;
                }
            }

            double[][] x = model.get(GRB.DoubleAttr.X, grbVars);

            System.out.println("OUTPUT:");
            for (int r = 0; r < aRooms.length; r++) {
                String str = "";
                for (int s = 0; s < aStudents.length; s++) {
                    str += x[r][s] + "   ";
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

    private static boolean addConstraints(List<Gurobi.CONSTRAINTS> constraints,
            GRBModel model, Combination[][] roomsStudents, Room[] aRooms, Student[] aStudents) {
        boolean everythingWorked = true;
        for (var c : constraints) {
            Gurobi.addConstraintToModel(c, model, roomsStudents, aRooms, aStudents);
        }
        return everythingWorked;
    }

    private static boolean addConstraintToModel(Gurobi.CONSTRAINTS constraint,
            GRBModel model, Combination[][] roomsStudents, Room[] aRooms, Student[] aStudents) {
        switch (constraint) {
            case oneRoomPerStudent:
                oneRoomPerStudent(model, roomsStudents, aRooms, aStudents);
                return true;
            case maxStudentsPerRoom:
                maxStudentsPerRoom(model, roomsStudents, aRooms, aStudents);
                return true;
            case onlySameSex:
                onlySameSex(model, roomsStudents, aRooms, aStudents);
                return true;
            default:
                return false;
        }
    }

    private static void oneRoomPerStudent(GRBModel model,
            Combination[][] roomsStudents, Room[] aRooms, Student[] aStudents) {
        try {
            GRBLinExpr expr;
            for (int s = 0; s < aStudents.length; ++s) {
                expr = new GRBLinExpr();
                for (int z = 0; z < aRooms.length; ++z) {
                    expr.addTerm(1.0, roomsStudents[z][s].grbvar);
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
            Combination[][] roomsStudents, Room[] aRooms, Student[] aStudents) {
        try {
            GRBLinExpr expr;
            for (int z = 0; z < aRooms.length; ++z) {
                expr = new GRBLinExpr();
                for (int s = 0; s < aStudents.length; ++s) {
                    expr.addTerm(1.0, roomsStudents[z][s].grbvar);
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
            Combination[][] roomsStudents, Room[] aRooms, Student[] aStudents) {
        try {
            GRBLinExpr expr;
            for (int s1 = 0; s1 < aStudents.length; ++s1) {
                for (int s2 = 0; s2 < aStudents.length; ++s2) {
                    for (int z = 0; z < aRooms.length; ++z) {
                        expr = new GRBLinExpr();
                        if (s1 != s2) {
                            String st = "onlySameSex_" + String.valueOf(z) + "_" + String.valueOf(s1) + "_"
                                    + String.valueOf(s2);
                            expr.addTerm(1, roomsStudents[z][s1].grbvar);
                            expr.addTerm(1, roomsStudents[z][s2].grbvar);
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

    private static double[][] getScoreMatrix(Combination[][] roomsStudents, int nRooms, int nStudents) {
        double[][] scoreMatrix = new double[nRooms][nStudents];
        for (int r = 0; r < nRooms; ++r) {
            for (int s = 0; s < nStudents; ++s) {
                scoreMatrix[r][s] = roomsStudents[r][s].score;
            }
        }
        return scoreMatrix;
    }

    private static GRBLinExpr calculateObjectiveLinExpr(Combination[][] roomsStudents,
            double[][] scoreMatrix, int nRooms, int nStudents) {
        GRBLinExpr objective = new GRBLinExpr();
        for (int r = 0; r < nRooms; ++r) {
            for (int s = 0; s < nStudents; ++s) {
                objective.addTerm(scoreMatrix[r][s], roomsStudents[r][s].grbvar);
            }
        }
        return objective;
    }

    private static double[][] calculateScores(Combination[][] roomsStudents,
            int nRooms, int nStudents) {
                roomsStudents[0][0].score = 10000;


        return Gurobi.getScoreMatrix(roomsStudents, nRooms, nStudents);
    }
}
