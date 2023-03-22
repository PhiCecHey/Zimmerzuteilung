package zimmerzuteilung;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;

import com.formdev.flatlaf.FlatLightLaf;

import zimmerzuteilung.GUI.Gui;
import zimmerzuteilung.algorithms.Gurobi;
import zimmerzuteilung.importsExports.ImportFiles;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        // theme
        FlatLightLaf.setup();
        com.formdev.flatlaf.intellijthemes.FlatCyanLightIJTheme.setup();
        Gui.init();
        //programm();

    }

    public static void programm() {
        File zimmer = new File("files/Internatszimmer.csv");
        File gruppen = new File("files/gruppen.csv");
        File zimmerwahl = new File("files/Zimmerwahl.csv");
        File persDaten = new File("files/persDaten.csv");

        try {
            ImportFiles.importBuildings(zimmer);
            ImportFiles.importStudents(persDaten);
            ImportFiles.importTeams(gruppen);
            ImportFiles.importWishes(zimmerwahl);
        } catch (Exception e) {
            System.out.println(zimmer.getAbsolutePath());
            e.printStackTrace();
        }
        ArrayList<Gurobi.RULES> rules = new ArrayList<>();
        rules.add(Gurobi.RULES.maxStudentsPerRoom);
        rules.add(Gurobi.RULES.oneRoomPerTeam);
        rules.add(Gurobi.RULES.oneTeamPerRoom);
        rules.add(Gurobi.RULES.respectGradePrivilege);
        rules.add(Gurobi.RULES.respectReservations);
        rules.add(Gurobi.RULES.respectWish);
        Gurobi g = new Gurobi(rules, ImportFiles.buildings(), ImportFiles.teams());
        g.calculate();
    }
}
