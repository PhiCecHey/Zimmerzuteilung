package zimmerzuteilung.GUI;

import javax.swing.*;
import javax.swing.Box.Filler;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.EventListener;

public class Gui {
    private static Dimension window = new Dimension(1800, 900);
    private static Dimension row = new Dimension(1800, 50);

    public static void changeFont(Component component) {
        Font font = new Font("serif", Font.PLAIN, 24);
        component.setFont(font);
        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                changeFont(child);
            }
        }
    }

    public static void gui() {

        JFrame mainFrame = new JFrame("Landesschule Pforte Zimmerzuteilung");
        mainFrame.setSize(window);
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();
        mainFrame.setContentPane(tabs);

        JPanel help = new JPanel();
        JPanel imports = new JPanel();
        JPanel gurobi = new JPanel();

        tabs.addTab("Informationen", help);
        tabs.addTab("Dateien einlesen", imports);
        tabs.addTab("Gurobi", gurobi);

        // ---------------------------------------------------Help-----------------------------------------------------
        JTextArea helpTextArea = new JTextArea();
        help.add(helpTextArea);
        // TODO
        helpTextArea.append("Informationen und Hilfestellungen über das Programm");
        // -------------------------------------------------Imports----------------------------------------------------
        imports.setLayout(new BoxLayout(imports, BoxLayout.PAGE_AXIS));

        ArrayList<Imports> importsPanels = new ArrayList<>();
        String[] importLabelStrings = new String[] { "Zimmer: ", "Personenbezogene Daten der Schüler:innen: ",
                "Gruppen: ", "Zimmerwünsche" };

        for (String s : importLabelStrings) {
            Imports i = new Imports(s);
            importsPanels.add(i);
            imports.add(i);
            JPanel filler = new JPanel();
            filler.setMaximumSize(row);
            imports.add(filler);
        }

        // --------------------------------------------------Gurobi----------------------------------------------------

        // ------------------------------------------------------------------------------------------------------------
        Gui.changeFont(mainFrame);
    }

}
