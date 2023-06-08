package zimmerzuteilung.GUI;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.commons.text.StringEscapeUtils;

import gurobi.GRBException;
import zimmerzuteilung.algorithms.Gurobi;
import zimmerzuteilung.importsExports.ExportFiles;
import zimmerzuteilung.importsExports.ImportFiles;
import zimmerzuteilung.objects.Team;

public class ResultsPanel extends JPanel {
    public JTextArea showResults;
    private JScrollPane scroll;
    private JButton calcResults, exportResultsButton;
    private ChooseFilePanel chooseFolder;
    private JTextField deliminator;

    public ResultsPanel() {
        this.init();
        this.calcResult();
    }

    private void init() {
        this.showResults = new JTextArea();
        this.scroll = new JScrollPane(this.showResults);
        this.calcResults = new JButton("Ergebnisse berechnen");
        this.deliminator = new JTextField(";");
        this.deliminator.setMaximumSize(new Dimension(30, Gui.row.height));

        this.add(calcResults);
        this.add(scroll);

        this.chooseFolder = new ChooseFilePanel("Ergebnisse speichern unter:", "folder");
        this.exportResultsButton = new JButton("Ergebnisse als csv exportieren");
        this.add(new GroupPanel(
                new Component[] { this.chooseFolder, new Filler(50, Gui.row.height), new JLabel("Trennzeichen: "),
                        this.deliminator, this.exportResultsButton },
                "row"));

        ResultsPanel.exportResults(exportResultsButton, this.chooseFolder.field, this.deliminator);
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
                try {
                    g.calculate();
                } catch (GRBException e) {
                    showResults.append("Berechnung konnte nicht durchgefuehrt werden.");
                }
            }
        });
    }

    private static void exportResults(JButton b, JTextField path, JTextField deliminator) {
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                File file = new File(path.getText());
                if (ExportFiles.eportToCsv(file, StringEscapeUtils.unescapeJava(deliminator.getText()))) {
                    path.setBackground(Colors.greenTransp);
                } else {
                    path.setBackground(Colors.redTransp);
                }
            }
        });
    }
}
