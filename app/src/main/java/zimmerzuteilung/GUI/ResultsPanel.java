package zimmerzuteilung.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import zimmerzuteilung.algorithms.Gurobi;
import zimmerzuteilung.importsExports.ExportFiles;
import zimmerzuteilung.importsExports.ImportFiles;

public class ResultsPanel extends JPanel {
    class ExportPanel extends JPanel {
        JLabel label;
        JTextField field;
        JButton fileChooserButton, exportResultsButton;

        public ExportPanel(String labelText) {
            this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
            this.setMaximumSize(new Dimension(2000, 50));

            this.label = new JLabel(labelText);
            this.add(label);
            this.field = new JTextField();
            this.add(field);

            this.fileChooserButton = new JButton("...");
            ExportPanel.buttonFileChooser(fileChooserButton, field);
            this.add(fileChooserButton);

            JLabel filler = new JLabel("          ");
            this.add(filler);

            this.exportResultsButton = new JButton("Ergebnisse als csv exportieren");
            ExportPanel.exportResults(exportResultsButton, field);
            this.add(exportResultsButton);
        }

        private static void buttonFileChooser(JButton b, JTextField f) {
            b.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JFileChooser fileChooser = new JFileChooser();
                    Gui.changeFont(fileChooser, Gui.mainFrame.getFont().getSize());
                    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    fileChooser.setAcceptAllFileFilterUsed(false);
                    if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                        Color c = new Color(0f, 0f, 1f, 0.2f);
                        f.setBackground(c);
                        String currentDir = fileChooser.getCurrentDirectory().getAbsolutePath();
                        if (!currentDir.endsWith("/")) {
                            currentDir += "/";
                        }
                        f.setText(currentDir += "Zimmerzuteilung_Ergebnisse.csv");
                    }
                }
            });
        }

        private static void exportResults(JButton b, JTextField f) {
            b.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    File file = new File(f.getText());
                    if (ExportFiles.eportToCsv(file) == true) {
                        f.setBackground(new Color(0f, 1f, 0f, 0.2f));
                    } else {
                        f.setBackground(new Color(1f, 0f, 0f, 0.2f));
                    }
                }
            });
        }
    }

    public JTextArea showResults;
    private JScrollPane scroll;
    private JButton calcResults;
    private ExportPanel export;

    public ResultsPanel() {
        this.init();
        this.calcResult();
    }

    private void init() {
        this.showResults = new JTextArea();
        this.scroll = new JScrollPane(this.showResults);
        this.calcResults = new JButton("Ergebnisse berechnen");

        this.export = new ExportPanel("Ergebnisse speichern unter:");

        this.add(calcResults);
        this.add(scroll);
        this.add(export);
    }

    private void calcResult() {
        this.calcResults.addActionListener(new ActionListener() {
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

    private void exportResult() {
        this.export.fileChooserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                // TODO
            }
        });
    }

}
