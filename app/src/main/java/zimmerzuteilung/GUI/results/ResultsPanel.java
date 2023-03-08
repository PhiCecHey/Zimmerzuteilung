package zimmerzuteilung.GUI.results;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import zimmerzuteilung.algorithms.Gurobi;
import zimmerzuteilung.imports.ImportFiles;

public class ResultsPanel extends JPanel {
    public JTextArea resultArea = new JTextArea();
    private JScrollPane resultScroll = new JScrollPane(resultArea);
    private JButton resultButton = new JButton("Ergebnis berechnen");

    public ResultsPanel() {
        this.init();
        this.result();
    }

    private void init() {
        this.add(resultButton);
        this.add(resultScroll);
    }

    private void result() {
        this.resultButton.addActionListener(new ActionListener() {
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
