package zimmerzuteilung.GUI;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import gurobi.GRBException;
import zimmerzuteilung.algorithms.Gurobi;
import zimmerzuteilung.importsExports.ExportFiles;
import zimmerzuteilung.importsExports.ImportFiles;

public class ResultsPanel extends JPanel {
    public JTextArea showResults;
    private JScrollPane scroll;
    private JButton calcResults, exportResultsButton;
    private ChooseFilePanel chooseFolder;

    public ResultsPanel() {
        this.init();
        this.calcResult();
    }

    private void init() {
        this.showResults = new JTextArea();
        this.scroll = new JScrollPane(this.showResults);
        this.calcResults = new JButton("Ergebnisse berechnen");

        this.add(calcResults);
        this.add(scroll);

        this.chooseFolder = new ChooseFilePanel("Ergebnisse speichern unter:", "folder");
        this.exportResultsButton = new JButton("Ergebnisse als csv exportieren");
        ResultsPanel.exportResults(exportResultsButton, this.chooseFolder.field);
        this.add(new GroupPanel(
                new Component[] { this.chooseFolder, new Filler(50, Gui.row.height), this.exportResultsButton },
                "row"));
    }

    private void calcResult() {
        this.calcResults.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                showResults.setText(
                        "Falls die Berechnung nicht beendet wird, bitte überprüfen Sie Ihre Gurobi Optimizer "
                                + "Installation und die dazugehörige Lizenz!\n"
                                + "https://www.gurobi.com/downloads/gurobi-software/\n\n");
                showResults.append("Berechnung gestartet...\n\n");

                Gurobi g = new Gurobi(GurobiPanel.rules(), ImportFiles.buildings(), ImportFiles.teams());
                g.calculate();
            }
        });
    }

    private static void exportResults(JButton b, JTextField f) {
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                File file = new File(f.getText());
                if (ExportFiles.eportToCsv(file) == true) {
                    f.setBackground(Colors.greenTransp);
                } else {
                    f.setBackground(Colors.redTransp);
                }
            }
        });
    }
}
