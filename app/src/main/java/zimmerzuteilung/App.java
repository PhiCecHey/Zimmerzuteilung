package zimmerzuteilung;

import java.io.File;
import java.util.ArrayList;

import zimmerzuteilung.algorithms.Gurobi;
import zimmerzuteilung.imports.ImportFiles;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        File alle = new File("app/files/alle.csv");
        File gruppen = new File("app/files/gruppen.csv");
        File zimmerwahl = new File("app/files/Zimmerwahl.csv");
        try {
            ImportFiles.importBuildings(alle);
            ImportFiles.importTeams(gruppen);
            ImportFiles.importWishes(zimmerwahl);

            ArrayList<Gurobi.RULES> rules = new ArrayList<>();
            rules.add(Gurobi.RULES.maxStudentsPerRoom);
            rules.add(Gurobi.RULES.oneRoomPerTeam);
            rules.add(Gurobi.RULES.oneTeamPerRoom);
            rules.add(Gurobi.RULES.respectReservations);
            rules.add(Gurobi.RULES.respectWish);

            Gurobi g = new Gurobi(rules, ImportFiles.buildings(), ImportFiles.teams());
            g.calculate();

            int a = 3;
        } catch (Exception e) {
            System.out.println(alle.getAbsolutePath());
            System.out.println(gruppen.getAbsolutePath());
            System.out.print(zimmerwahl.getAbsolutePath());
            System.out.println("Working Directory = " + System.getProperty("user.dir"));
            e.printStackTrace();
        }
    }
}
