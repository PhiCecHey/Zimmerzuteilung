package zimmerzuteilung.GUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

import zimmerzuteilung.GUI.Log.Log;
import zimmerzuteilung.algorithms.Gurobi;
import zimmerzuteilung.imports.ImportFiles;

public class Gui {
    private static Dimension window = new Dimension(1800, 900);
    private static Dimension row = new Dimension(1800, 50);

    public static JFrame mainFrame = new JFrame("Landesschule Pforte Zimmerzuteilung");
    private static JTabbedPane tabs = new JTabbedPane();
    private static JPanel help = new JPanel();
    private static JPanel imports = new JPanel();
    private static JPanel gurobi = new JPanel();
    private static JPanel result = new JPanel();

    // help
    public static JTextArea helpText = new JTextArea();
    private static JScrollPane helpScroll = new JScrollPane(helpText);
    private static JButton plus = new JButton("+");
    private static JButton minus = new JButton("-");

    // imports
    private static ArrayList<ImportsPanel> importsPanels = new ArrayList<>();
    private static JButton importsButton = new JButton("Dateien einlesen");
    public static JTextArea importsArea = new JTextArea();
    private static JScrollPane importsScroll = new JScrollPane(importsArea);

    // gurobi

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

        // tabs
        Gui.tabs.addTab("Informationen", Gui.help);
        Gui.tabs.addTab("Dateien einlesen", imports);
        Gui.tabs.addTab("Gurobi", gurobi);
        Gui.tabs.addTab("Ergebnis", result);

        // layouts
        Gui.help.setLayout(new BoxLayout(help, BoxLayout.PAGE_AXIS));
        gurobi.setLayout(new BoxLayout(gurobi, BoxLayout.PAGE_AXIS));
        imports.setLayout(new BoxLayout(imports, BoxLayout.PAGE_AXIS));
        result.setLayout(new BoxLayout(result, BoxLayout.PAGE_AXIS));

        // help
        Gui.helpText.setEditable(false);
        help.add(helpScroll);
        JPanel zoom = new JPanel();
        zoom.setLayout(new BoxLayout(zoom, BoxLayout.LINE_AXIS));
        help.add(zoom);
        zoom.add(minus);
        zoom.add(plus);
        Gui.help();

        // imports
        Gui.imports();
        Gui.importsArea.setLineWrap(true);
        Gui.importsArea.setEditable(false);
        Gui.imports.add(Gui.importsButton);
        JPanel filler = new JPanel();
        filler.setMaximumSize(row);
        Gui.imports.add(filler);
        Gui.imports.add(importsScroll);

        // gurobi

        // results
        Gui.result.add(resultScroll);
        Gui.result.add(resultButton);
        Gui.result();

        Gui.changeFont(mainFrame, 24);
    }

    private static void imports() {
        String[] importLabelStrings = new String[] { "Zimmer: ", "Personenbezogene Daten der Schüler:innen: ",
                "Gruppen: ", "Zimmerwünsche" };

        for (String s : importLabelStrings) {
            ImportsPanel i = new ImportsPanel(s);
            Gui.importsPanels.add(i);
            imports.add(i);
            JPanel filler = new JPanel();
            filler.setMaximumSize(row);
            imports.add(filler);
        }

        Gui.importsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                // clean
                Gui.importsArea.setText("");
                Log.clear();
                ImportFiles.clear();

                for (int i = 0; i < 4; i++) {
                    byte fileFound = 0;
                    try {
                        if (i == 0) {
                            if (ImportFiles.importBuildings(new File(importsPanels.get(i).field.getText())) != null) {
                                fileFound = 1;
                            }
                        } else if (i == 1) {
                            if (ImportFiles.importStudents(new File(importsPanels.get(i).field.getText())) != null) {
                                fileFound = 1;
                            }
                        } else if (i == 2) {
                            if (ImportFiles.importTeams(new File(importsPanels.get(i).field.getText())) != null) {
                                fileFound = 1;
                            }
                        } else if (i == 3) {
                            if (ImportFiles.importWishes(new File(importsPanels.get(i).field.getText())) != null) {
                                fileFound = 1;
                            }
                        }
                    } catch (Exception e) {
                        fileFound = -1;
                        Gui.importsPanels.get(i).field.setBackground(Color.red);
                        e.printStackTrace();
                        Gui.importsArea.append("\n" + e.toString());
                    }
                    if (fileFound == 1) {
                        Gui.importsPanels.get(i).field.setBackground(Color.green);
                    }
                }
                Gui.importsArea.append(Log.log());
            }
        });
    }

    private static void help() {
        // TODO
        Gui.helpText.append("Informationen und Hilfestellungen über das Programm");
        Gui.plus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                Gui.changeFont(mainFrame, Gui.mainFrame.getFont().getSize() + 1);
            }
        });

        Gui.minus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                Gui.changeFont(mainFrame, Gui.mainFrame.getFont().getSize() - 1);
            }
        });
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
