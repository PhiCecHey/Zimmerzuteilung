package zimmerzuteilung.algorithmen;

import gurobi.*;

public class Gurobi {
    public static void main(String[] args) {

        try {

            // --------------------------- MODEL ---------------------------
            GRBEnv env = new GRBEnv();
            GRBModel model = new GRBModel(env);
            model.set(GRB.StringAttr.ModelName, "zimmerzuteilung");

            // ------------------------- VARIABLES -------------------------
            int s = 7, z = 3;
            GRBVar[][] vars = new GRBVar[z][s]; // befindet sich schüler schon im zimmer? [Schüler][Zimmer]

            for (int i = 0; i < z; i++) {
                for (int j = 0; j < s; j++) {
                    String st = "zuteilung_" + String.valueOf(i) + "_" + String.valueOf(j);
                    vars[i][j] = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, st);
                }
            }

            // ------------------------ CONSTRAINTS ------------------------
            GRBLinExpr expr;

            // jeder schüler darf nur in 1 zimmer / each value appears once per row
            for (int i = 0; i < s; i++) {
                expr = new GRBLinExpr();
                for (int j = 0; j < z; j++) {
                    expr.addTerm(1.0, vars[j][i]);
                }
                String st = "einZimmerProSchüler_" + String.valueOf(i);
                model.addConstr(expr, GRB.EQUAL, 1.0, st);
            }

            // max 2 leute pro zimmer / each value apperas max twice per collumn
            for (int i = 0; i < z; i++) {
                expr = new GRBLinExpr();
                for (int j = 0; j < s; j++) {
                    expr.addTerm(1.0, vars[i][j]);
                }
                String st = "maxZweiSchülerProZimmer_" + String.valueOf(i);
                model.addConstr(expr, GRB.LESS_EQUAL, 3.0, st);

            }

            // -------------------------- OPTIMIZE -------------------------
            model.optimize();

            // --------------------------- PRINT ---------------------------
            double[][] x = model.get(GRB.DoubleAttr.X, vars);

            System.out.println("OUTPUT:");
            for (int i = 0; i < z; i++) {
                String str = "";
                for (int j = 0; j < s; j++) {
                    str += x[i][j] + "   ";
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
}
