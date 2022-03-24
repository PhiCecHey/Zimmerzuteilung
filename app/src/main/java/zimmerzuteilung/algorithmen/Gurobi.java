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

            // ------------------------ CONSTRAINTS ------------------------

            // -------------------------- OPTIMIZE -------------------------

            // --------------------------- PRINT ---------------------------

        } catch (GRBException e) {
            System.out.println("Error code: " + e.getErrorCode() + ". " +
                    e.getMessage());
        }
    }
}
