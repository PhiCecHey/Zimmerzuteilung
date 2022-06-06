package zimmerzuteilung.algorithmen;

import java.util.List;
import zimmerzuteilung.objekte.*;
import zimmerzuteilung.objekte.Student.Sex;
import gurobi.*;

public class Gurobi {
    public enum CONSTRAINTS {
        oneRoomPerStudent, maxStudentsPerRoom, onlySameSex;
    }

    public static void calculate(List<Gurobi.CONSTRAINTS> constraints,
            int nStudents, int nRooms) {

        try {
            // --------------------------- MODEL ---------------------------
            GRBEnv env = new GRBEnv();
            GRBModel model = new GRBModel(env);
            model.set(GRB.StringAttr.ModelName, "zimmerzuteilung");

            // ------------------------- VARIABLES -------------------------
            Student[] aStudents = new Student[nStudents];
            for (int s = 0; s < nStudents; ++s) {
                if (s % 2 == 0) {
                    aStudents[s] = new Student(Integer.toString(s), Sex.m);
                } else {
                    aStudents[s] = new Student(Integer.toString(s), Sex.f);
                }
            }

            GRBVar[][] roomsStudents = new GRBVar[nRooms][nStudents]; // zuordnung

            for (int z = 0; z < nRooms; ++z) {
                for (int s = 0; s < nStudents; ++s) {
                    String st = "zuteilung_" + String.valueOf(z) + "_" + String.valueOf(s);
                    roomsStudents[z][s] = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, st);
                }
            }

            // ------------------------ CONSTRAINTS ------------------------

            Gurobi.addConstraints(constraints, model, nStudents, nRooms,
                    roomsStudents, aStudents);

            // -------------------------- OPTIMIZE -------------------------
            model.optimize();

            // --------------------------- PRINT ---------------------------
            double[][] x = model.get(GRB.DoubleAttr.X, roomsStudents);

            System.out.println("OUTPUT:");
            for (int r = 0; r < nRooms; r++) {
                String str = "";
                for (int s = 0; s < nStudents; s++) {
                    str += x[r][s] + "   ";
                }
                System.out.println(str);
            }

            // --------------------------- CLEAN ---------------------------
            model.dispose();
            env.dispose();

        } catch (GRBException e) {
            System.out.println("Error code: " + e.getErrorCode() + ". " +
                    e.getMessage());
        }
    }

    private static boolean addConstraints(List<Gurobi.CONSTRAINTS> constraints,
            GRBModel model, int nStudents, int nRooms, GRBVar[][] roomsStudents,
            Student[] aStudents) {
        boolean everythingWorked = true;
        for (var c : constraints) {
            Gurobi.addConstraintToModel(c, model, nStudents, nRooms, roomsStudents, aStudents);
        }
        return everythingWorked;
    }

    private static boolean addConstraintToModel(Gurobi.CONSTRAINTS constraint,
            GRBModel model, int nStudents, int nRooms, GRBVar[][] roomsStudents,
            Student[] aStudents) {
        switch (constraint) {
            case oneRoomPerStudent:
                oneRoomPerStudent(model, nStudents, nRooms, roomsStudents);
                return true;
            case maxStudentsPerRoom:
                maxStudentsPerRoom(model, nStudents, nRooms, roomsStudents);
                return true;
            case onlySameSex:
                onlySameSex(model, nStudents, nRooms, roomsStudents, aStudents);
                return true;
            default:
                return false;
        }
    }

    private static void oneRoomPerStudent(GRBModel model, int nStudents, int nRooms,
            GRBVar[][] roomsStudents) {
        try {
            GRBLinExpr expr;

            // jeder schüler darf nur in 1 zimmer / each value appears once per row
            for (int s = 0; s < nStudents; ++s) {
                expr = new GRBLinExpr();
                for (int z = 0; z < nRooms; ++z) {
                    expr.addTerm(1.0, roomsStudents[z][s]);
                }
                String st = "oneRoomPerStudent_" + String.valueOf(s);
                model.addConstr(expr, GRB.EQUAL, 1.0, st);
            }
        } catch (GRBException e) {
            System.out.println("Error code: " + e.getErrorCode() + ". " +
                    e.getMessage());
        }
    }

    private static void maxStudentsPerRoom(GRBModel model, int nStudents, int nRooms,
            GRBVar[][] roomsStudents) {
        try {
            GRBLinExpr expr;
            int max = 3;
            for (int z = 0; z < nRooms; ++z) {
                expr = new GRBLinExpr();
                for (int s = 0; s < nStudents; ++s) {
                    expr.addTerm(1.0, roomsStudents[z][s]);
                }
                String st = "maxStudentsPerRoom_" + String.valueOf(z);
                model.addConstr(expr, GRB.LESS_EQUAL, max, st);
            }
        } catch (GRBException e) {
            System.out.println("Error code: " + e.getErrorCode() + ". " +
                    e.getMessage());
        }
    }

    private static void onlySameSex(GRBModel model, int nStudents, int nRooms,
            GRBVar[][] roomsStudents, Student[] aStudents) {
        try {
            GRBLinExpr expr;
            for (int s1 = 0; s1 < nStudents; ++s1) {
                for (int s2 = 0; s2 < nStudents; ++s2) {
                    for (int z = 0; z < nRooms; ++z) {
                        expr = new GRBLinExpr();
                        if (s1 != s2) {
                            String st = "onlySameSex_" + String.valueOf(z) + "_" + String.valueOf(s1) + "_"
                                    + String.valueOf(s2);
                            expr.addTerm(1, roomsStudents[z][s1]);
                            expr.addTerm(1, roomsStudents[z][s2]);
                            if (aStudents[s1].getSex().equals(aStudents[s2].getSex())) {
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

}
