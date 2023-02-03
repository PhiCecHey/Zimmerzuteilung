package zimmerzuteilung;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import zimmerzuteilung.algorithms.Gurobi;
import zimmerzuteilung.imports.ImportFiles;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        File zimmer = new File("app/files/Internatszimmer.csv");
        File gruppen = new File("app/files/gruppen.csv");
        File zimmerwahl = new File("app/files/Zimmerwahl.csv");
        File persDaten = new File("app/files/persDaten.csv");
        try {
            ImportFiles.importBuildings(zimmer);
            ImportFiles.importStudents(persDaten);
            ImportFiles.importTeams(gruppen);
            var debug = ImportFiles.importWishes(zimmerwahl);

            ArrayList<Gurobi.RULES> rules = new ArrayList<>();
            rules.add(Gurobi.RULES.maxStudentsPerRoom);
            rules.add(Gurobi.RULES.oneRoomPerTeam);
            rules.add(Gurobi.RULES.oneTeamPerRoom);
            rules.add(Gurobi.RULES.respectReservations);
            rules.add(Gurobi.RULES.respectWish);

            Gurobi g = new Gurobi(rules, ImportFiles.buildings(), ImportFiles.teams());
            g.calculate();

            int a = 3;
        } catch (FileNotFoundException e) {
            System.out.println(zimmer.getAbsolutePath());
            System.out.println(gruppen.getAbsolutePath());
            System.out.println(zimmerwahl.getAbsolutePath());
            System.out.println("Working Directory = " + System.getProperty("user.dir"));
            System.out.println();
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
