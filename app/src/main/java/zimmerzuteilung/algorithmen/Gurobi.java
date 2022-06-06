package zimmerzuteilung.algorithmen;

import zimmerzuteilung.objekte.*;
import zimmerzuteilung.objekte.Student.Sex;
import gurobi.*;

public class Gurobi {
    public static void main(String[] args) {

        try {

            // --------------------------- MODEL ---------------------------
            GRBEnv env = new GRBEnv();
            GRBModel model = new GRBModel(env);
            model.set(GRB.StringAttr.ModelName, "zimmerzuteilung");

            // ------------------------- VARIABLES -------------------------
            int anzahlSchueler = 7, anzahlZimmer = 4;

            Student[] arraySchueler = new Student[anzahlSchueler];
            for (int s = 0; s < anzahlSchueler; ++s) {
                if (s % 2 == 0) {
                    arraySchueler[s] = new Student(Integer.toString(s), Sex.m);
                } else {
                    arraySchueler[s] = new Student(Integer.toString(s), Sex.f);
                }
            }

            GRBVar[][] zimmerSchueler = new GRBVar[anzahlZimmer][anzahlSchueler]; // befindet sich schüler schon im
            // zimmer?
            // [Schüler][Zimmer]

            for (int z = 0; z < anzahlZimmer; ++z) {
                for (int s = 0; s < anzahlSchueler; ++s) {
                    String st = "zuteilung_" + String.valueOf(z) + "_" + String.valueOf(s);
                    zimmerSchueler[z][s] = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, st);
                }
            }

            // ------------------------ CONSTRAINTS ------------------------
            GRBLinExpr expr;

            einZimmerProSchueler(model, anzahlSchueler, anzahlZimmer, zimmerSchueler);
            maxSchuelerProZimmer(model, anzahlSchueler, anzahlZimmer, zimmerSchueler);
            keineGemischtenZimmer(model, anzahlSchueler, anzahlZimmer,
                    zimmerSchueler, arraySchueler);

            // -------------------------- OPTIMIZE -------------------------
            model.optimize();

            // --------------------------- PRINT ---------------------------
            double[][] x = model.get(GRB.DoubleAttr.X, zimmerSchueler);

            System.out.println("OUTPUT:");
            for (int z = 0; z < anzahlZimmer; z++) {
                String str = "";
                for (int s = 0; s < anzahlSchueler; s++) {
                    str += x[z][s] + "   ";
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

    private static void einZimmerProSchueler(GRBModel model, int anzahlSchueler, int anzahlZimmer,
            GRBVar[][] zimmerSchueler) {
        try {
            GRBLinExpr expr;

            // jeder schüler darf nur in 1 zimmer / each value appears once per row
            for (int s = 0; s < anzahlSchueler; ++s) {
                expr = new GRBLinExpr();
                for (int z = 0; z < anzahlZimmer; ++z) {
                    expr.addTerm(1.0, zimmerSchueler[z][s]);
                }
                String st = "einZimmerProSchüler_" + String.valueOf(s);
                model.addConstr(expr, GRB.EQUAL, 1.0, st);
            }
        } catch (GRBException e) {
            System.out.println("Error code: " + e.getErrorCode() + ". " +
                    e.getMessage());
        }
    }

    private static void maxSchuelerProZimmer(GRBModel model, int anzahlSchueler, int anzahlZimmer,
            GRBVar[][] zimmerSchueler) {
        try {
            GRBLinExpr expr;
            int max = 3;
            for (int z = 0; z < anzahlZimmer; ++z) {
                expr = new GRBLinExpr();
                for (int s = 0; s < anzahlSchueler; ++s) {
                    expr.addTerm(1.0, zimmerSchueler[z][s]);
                }
                String st = "maxZweiSchülerProZimmer_" + String.valueOf(z);
                model.addConstr(expr, GRB.LESS_EQUAL, max, st);
            }
        } catch (GRBException e) {
            System.out.println("Error code: " + e.getErrorCode() + ". " +
                    e.getMessage());
        }
    }

    private static void keineGemischtenZimmer(GRBModel model, int anzahlSchueler, int anzahlZimmer,
            GRBVar[][] zimmerSchueler, Student[] arraySchueler) {
        try {
            GRBLinExpr expr;
            for (int s1 = 0; s1 < anzahlSchueler; ++s1) {
                for (int s2 = 0; s2 < anzahlSchueler; ++s2) {
                    for (int z = 0; z < anzahlZimmer; ++z) {
                        expr = new GRBLinExpr();
                        if (s1 != s2) {
                            String st = "keineGemischtenZimmer_" + String.valueOf(z) + "_" + String.valueOf(s1) + "_"
                                    + String.valueOf(s2);
                            expr.addTerm(1, zimmerSchueler[z][s1]);
                            expr.addTerm(1, zimmerSchueler[z][s2]);
                            if (arraySchueler[s1].getSex().equals(arraySchueler[s2].getSex())) {
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
