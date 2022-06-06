package zimmerzuteilung.algorithmen;

import java.util.List;
import zimmerzuteilung.objekte.*;
import zimmerzuteilung.objekte.Student.SEX;
import gurobi.*;

public class Gurobi {
    public enum CONSTRAINTS {
        oneRoomPerStudent, maxStudentsPerRoom, onlySameSex;
    }

    public static void calculate(List<Gurobi.CONSTRAINTS> constraints, School school) {
        Student[] aStudents = school.getStudents();
        Room[] aRooms = school.getRooms();

        try {
            // --------------------------- MODEL ---------------------------
            GRBEnv env = new GRBEnv();
            GRBModel model = new GRBModel(env);
            model.set(GRB.StringAttr.ModelName, "zimmerzuteilung");

            // ------------------------- VARIABLES -------------------------

            GRBVar[][] roomsStudents = new GRBVar[aRooms.length][aStudents.length]; // zuordnung

            for (int z = 0; z < aRooms.length; ++z) {
                for (int s = 0; s < aStudents.length; ++s) {
                    String st = "zuteilung_" + String.valueOf(z) + "_" + String.valueOf(s);
                    roomsStudents[z][s] = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, st);
                }
            }

            // ------------------------ CONSTRAINTS ------------------------

            Gurobi.addConstraints(constraints, model, roomsStudents, aStudents, aRooms);

            // -------------------------- OPTIMIZE -------------------------
            model.optimize();

            // --------------------------- PRINT ---------------------------
            double[][] x = model.get(GRB.DoubleAttr.X, roomsStudents);

            System.out.println("OUTPUT:");
            for (int r = 0; r < aRooms.length; r++) {
                String str = "";
                for (int s = 0; s < aStudents.length; s++) {
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
            GRBModel model, GRBVar[][] roomsStudents, Student[] aStudents, Room[] aRooms) {
        boolean everythingWorked = true;
        for (var c : constraints) {
            Gurobi.addConstraintToModel(c, model, roomsStudents, aStudents, aRooms);
        }
        return everythingWorked;
    }

    private static boolean addConstraintToModel(Gurobi.CONSTRAINTS constraint,
            GRBModel model, GRBVar[][] roomsStudents, Student[] aStudents, Room[] aRooms) {
        switch (constraint) {
            case oneRoomPerStudent:
                oneRoomPerStudent(model, roomsStudents, aStudents, aRooms);
                return true;
            case maxStudentsPerRoom:
                maxStudentsPerRoom(model, roomsStudents, aStudents, aRooms);
                return true;
            case onlySameSex:
                onlySameSex(model, roomsStudents, aStudents, aRooms);
                return true;
            default:
                return false;
        }
    }

    private static void oneRoomPerStudent(GRBModel model, GRBVar[][] roomsStudents,
            Student[] aStudents, Room[] aRooms) {
        try {
            GRBLinExpr expr;
            for (int s = 0; s < aStudents.length; ++s) {
                expr = new GRBLinExpr();
                for (int z = 0; z < aRooms.length; ++z) {
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

    private static void maxStudentsPerRoom(GRBModel model, GRBVar[][] roomsStudents,
            Student[] aStudents, Room[] aRooms) {
        try {
            GRBLinExpr expr;
            for (int z = 0; z < aRooms.length; ++z) {
                expr = new GRBLinExpr();
                for (int s = 0; s < aStudents.length; ++s) {
                    expr.addTerm(1.0, roomsStudents[z][s]);
                }
                String st = "maxStudentsPerRoom_" + String.valueOf(z);
                model.addConstr(expr, GRB.LESS_EQUAL, aRooms[z].getCapacity(), st);
            }
        } catch (GRBException e) {
            System.out.println("Error code: " + e.getErrorCode() + ". " +
                    e.getMessage());
        }
    }

    private static void onlySameSex(GRBModel model, GRBVar[][] roomsStudents,
            Student[] aStudents, Room[] aRooms) {
        try {
            GRBLinExpr expr;
            for (int s1 = 0; s1 < aStudents.length; ++s1) {
                for (int s2 = 0; s2 < aStudents.length; ++s2) {
                    for (int z = 0; z < aRooms.length; ++z) {
                        expr = new GRBLinExpr();
                        if (s1 != s2) {
                            String st = "onlySameSex_" + String.valueOf(z) + "_" + String.valueOf(s1) + "_"
                                    + String.valueOf(s2);
                            expr.addTerm(1, roomsStudents[z][s1]);
                            expr.addTerm(1, roomsStudents[z][s2]);
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

}
