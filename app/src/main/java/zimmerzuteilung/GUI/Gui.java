package zimmerzuteilung.GUI;

import javax.swing.*;
import zimmerzuteilung.imports.ImportFiles;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;

public class Gui {
    private static Dimension window = new Dimension(1800, 900);
    private static Dimension row = new Dimension(1800, 50);

    public static void changeFont(Component component, int size) {
        Font font = new Font("serif", Font.PLAIN, size);
        component.setFont(font);
        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                changeFont(child, size);
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
        help.setLayout(new BoxLayout(help, BoxLayout.PAGE_AXIS));

        JTextArea helpText = new JTextArea();
        helpText.setEditable(false);
        help.add(helpText);
        // TODO
        helpText.append("Informationen und Hilfestellungen über das Programm");

        JButton plus = new JButton("+");
        plus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                Gui.changeFont(mainFrame, mainFrame.getFont().getSize() + 1);
            }
        });

        JButton minus = new JButton("-");
        minus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                Gui.changeFont(mainFrame, mainFrame.getFont().getSize() - 1);
            }
        });

        JPanel zoom = new JPanel();
        zoom.setLayout(new BoxLayout(zoom, BoxLayout.LINE_AXIS));
        help.add(zoom);
        zoom.add(plus);
        zoom.add(minus);
        // -------------------------------------------------Imports----------------------------------------------------
        imports.setLayout(new BoxLayout(imports, BoxLayout.PAGE_AXIS));

        ArrayList<ImportsPanel> importsPanels = new ArrayList<>();
        String[] importLabelStrings = new String[] { "Zimmer: ", "Personenbezogene Daten der Schüler:innen: ",
                "Gruppen: ", "Zimmerwünsche" };

        for (String s : importLabelStrings) {
            ImportsPanel i = new ImportsPanel(s);
            importsPanels.add(i);
            imports.add(i);
            JPanel filler = new JPanel();
            filler.setMaximumSize(row);
            imports.add(filler);
        }

        JButton importsButton = new JButton("Dateien einlesen");
        JTextArea importsArea = new JTextArea();
        importsArea.setLineWrap(true);
        importsArea.setEditable(false);

        importsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                importsArea.setText("");
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
                        importsPanels.get(i).field.setBackground(Color.red);
                        e.printStackTrace();
                        importsArea.append("\n" + e.toString());
                    }
                    if (fileFound == 1) {
                        importsPanels.get(i).field.setBackground(Color.green);
                    }
                }

            }
        });
        imports.add(importsButton);
        JPanel filler = new JPanel();
        filler.setMaximumSize(row);
        imports.add(filler);
        imports.add(importsArea);

        // --------------------------------------------------Gurobi----------------------------------------------------

        // ------------------------------------------------------------------------------------------------------------
        Gui.changeFont(mainFrame, 24);
    }

}
