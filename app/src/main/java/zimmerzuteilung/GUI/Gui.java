package zimmerzuteilung.GUI;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

import zimmerzuteilung.GUI.help.HelpPanel;
import zimmerzuteilung.GUI.imports.ImportsPanel;
import zimmerzuteilung.algorithms.Gurobi;
import zimmerzuteilung.imports.ImportFiles;

public class Gui {
    private static Dimension window = new Dimension(1800, 900);
    public static Dimension row = new Dimension(1800, 50);

    public static JFrame mainFrame = new JFrame("Landesschule Pforte Zimmerzuteilung");
    private static JTabbedPane tabs = new JTabbedPane();
    private static JPanel help = new HelpPanel();
    private static JPanel imports = new ImportsPanel();
    private static JPanel gurobi = new JPanel();
    private static JPanel result = new JPanel();
    

    // results
    public static JTextArea resultArea = new JTextArea();
    private static JScrollPane resultScroll = new JScrollPane(resultArea);
    private static JButton resultButton = new JButton("Ergebnis berechnen");

    public static void changeFont(Component component, int size) {
        Font font = Gui.mainFrame.getFont();
        font = new Font(font.getName(), font.getStyle(), size);
        component.setFont(font);
        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                changeFont(child, size);
            }
        }
    }

    public static void init() {
        Gui.mainFrame.setSize(window);
        Gui.mainFrame.setVisible(true);
        Gui.mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Gui.mainFrame.setContentPane(tabs);
        Gui.tabs.addTab("Informationen", Gui.help);
        Gui.tabs.addTab("Dateien einlesen", imports);
        Gui.tabs.addTab("Gurobi", gurobi);
        Gui.tabs.addTab("Ergebnis", result);

        // layouts
        Gui.help.setLayout(new BoxLayout(help, BoxLayout.PAGE_AXIS));
        gurobi.setLayout(new BoxLayout(gurobi, BoxLayout.PAGE_AXIS));
        imports.setLayout(new BoxLayout(imports, BoxLayout.PAGE_AXIS));
        result.setLayout(new BoxLayout(result, BoxLayout.PAGE_AXIS));

        // results
        Gui.result.add(resultButton);
        Gui.result.add(resultScroll);
        Gui.result();

        Gui.changeFont(mainFrame, 24);
    }


    private static void result() {
        Gui.resultButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    ArrayList<Gurobi.RULES> rules = new ArrayList<>();
                    rules.add(Gurobi.RULES.maxStudentsPerRoom);
                    rules.add(Gurobi.RULES.oneRoomPerTeam);
                    rules.add(Gurobi.RULES.oneTeamPerRoom);
                    rules.add(Gurobi.RULES.respectReservations);
                    rules.add(Gurobi.RULES.respectWish);
                    rules.add(Gurobi.RULES.respectGradePrivilege);

                    Gurobi g = new Gurobi(rules, ImportFiles.buildings(), ImportFiles.teams());
                    g.calculate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
