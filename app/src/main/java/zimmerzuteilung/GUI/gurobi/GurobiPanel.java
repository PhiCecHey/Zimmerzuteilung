package zimmerzuteilung.GUI.gurobi;

import java.awt.Dimension;
import java.util.ArrayList;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import zimmerzuteilung.algorithms.Gurobi;
import zimmerzuteilung.imports.ImportFiles;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import zimmerzuteilung.algorithms.Gurobi;
import zimmerzuteilung.algorithms.GurobiValues;

public class GurobiPanel extends JPanel {
    class CheckBoxPanel extends JPanel {
        JLabel label;
        JCheckBox box;

        CheckBoxPanel(String labelText, boolean enabled) {
            this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
            this.setMaximumSize(new Dimension(2000, 50));
            this.box = new JCheckBox();
            this.box.setSelected(true);
            this.box.setEnabled(enabled);
            this.add(box);
            this.label = new JLabel(labelText);
            this.add(label);
        }
    }

    class WishPanel extends JPanel {
        JTextField b1Field, b2Field, r1Field, r2Field;

        WishPanel() {
            this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
            this.setMaximumSize(dimPanel);
            this.add(new JLabel("      "));
            this.add(new JLabel("Erstwunsch Internat Bonus: "));
            this.b1Field = new JTextField();
            this.b1Field.setMaximumSize(dimField);
            this.b1Field.setText("5");
            this.add(b1Field);
            this.add(new JLabel("      "));
            this.add(new JLabel("Erstwunsch Zimmer Bonus: "));
            this.r1Field = new JTextField();
            this.r1Field.setMaximumSize(dimField);
            this.r1Field.setText("10");
            this.add(r1Field);
            this.add(new JLabel("      "));
            this.add(new JLabel("Zweitwunsch Zimmer Bonus: "));
            this.r2Field = new JTextField();
            this.r2Field.setMaximumSize(dimField);
            this.r2Field.setText("8");
            this.add(r2Field);
            this.add(new JLabel("      "));
            this.add(new JLabel("Zweitwunsch Internat Bonus: "));
            this.b2Field = new JTextField();
            this.b2Field.setMaximumSize(dimField);
            this.b2Field.setText("5");
            this.add(b2Field);
        }

    }

    class ReservationPanel extends JPanel {
        JTextField resField;

        ReservationPanel() {
            this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
            this.setMaximumSize(dimPanel);
            this.add(new JLabel("      "));
            this.add(new JLabel("Reservierungsbonus: "));
            this.resField = new JTextField();
            this.resField.setMaximumSize(dimField);
            this.resField.setText("50");
            this.add(this.resField);
        }
    }

    class GradePanel extends JPanel {
        JTextField twelvField, elevenField, tenField;

        GradePanel() {
            this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
            this.setMaximumSize(dimPanel);
            this.add(new JLabel("      "));
            this.add(new JLabel("12er Bonus: "));
            this.twelvField = new JTextField();
            this.add(twelvField);
            this.twelvField.setMaximumSize(dimField);
            this.add(new JLabel("      "));
            this.add(new JLabel("11er Bonus:"));
            this.elevenField = new JTextField();
            this.elevenField.setMaximumSize(dimField);
            this.add(elevenField);
            this.add(new JLabel("      "));
            this.add(new JLabel("10er Bonus: "));
            this.tenField = new JTextField();
            this.tenField.setMaximumSize(dimField);
            this.add(tenField);
            this.twelvField.setText("7");
            this.elevenField.setText("5");
            this.tenField.setText("3");
        }
    }

    private static Dimension dimPanel = new Dimension(1800, 50);
    private static Dimension dimField = new Dimension(80, 50);

    private CheckBoxPanel oneRoomPerTeam, oneTeamPerRoom, maxStudentsPerRoom, respectWish, respectRes, respectGradePriv;

    public ArrayList<Gurobi.RULES> gurobiRules = new ArrayList<>();

    private GradePanel gradePanel;
    private WishPanel wishPanel;
    private ReservationPanel resPanel;
    private JButton save;

    public GurobiPanel() {
        this.init();
    }

    private void init() {
        this.oneRoomPerTeam = new CheckBoxPanel("Ein Zimmer pro Team", false);
        this.add(this.oneRoomPerTeam);
        this.add(new JLabel("      "));

        this.oneTeamPerRoom = new CheckBoxPanel("Ein Team pro Zimmer", false);
        this.add(this.oneTeamPerRoom);
        this.add(new JLabel("      "));

        this.maxStudentsPerRoom = new CheckBoxPanel("maximale Anzahl an Schüler:innen pro Zimmer einhalten", false);
        this.add(this.maxStudentsPerRoom);
        this.add(new JLabel("      "));

        this.respectWish = new CheckBoxPanel("Zimmerwünsche respektieren", true);
        this.add(this.respectWish);
        this.wishPanel = new WishPanel();
        this.add(this.wishPanel);
        this.add(new JLabel("      "));

        this.respectRes = new CheckBoxPanel("Zimmerreservierungen respektieren", true);
        this.add(this.respectRes);
        this.resPanel = new ReservationPanel();
        this.add(this.resPanel);
        this.add(new JLabel("      "));

        this.respectGradePriv = new CheckBoxPanel("12er, 11er, 10er Privileg respektieren", true);
        this.add(this.respectGradePriv);
        this.gradePanel = new GradePanel();
        this.add(this.gradePanel);

        this.save = new JButton("speichern");
        this.save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                getRules();
            }
        });
        this.add(new JLabel("      "));
        this.add(new JLabel("      "));
        this.add(new JLabel("      "));
        this.add(new JLabel("      "));
        this.add(new JLabel("      "));
        this.add(new JLabel("      "));
        this.add(save);
    }

    private void getRules() {
        if (this.oneRoomPerTeam.box.isSelected()) {
            this.gurobiRules.add(Gurobi.RULES.oneRoomPerTeam);
        }
        if (this.oneTeamPerRoom.box.isSelected()) {
            this.gurobiRules.add(Gurobi.RULES.oneTeamPerRoom);
        }
        if (this.maxStudentsPerRoom.box.isSelected()) {
            this.gurobiRules.add(Gurobi.RULES.maxStudentsPerRoom);
        }
        if (this.respectWish.box.isSelected()) {
            this.gurobiRules.add(Gurobi.RULES.respectWish);
            try {
                GurobiValues.building1 = Float.parseFloat(this.wishPanel.b1Field.getText());
                GurobiValues.room1 = Float.parseFloat(this.wishPanel.r1Field.getText());
                GurobiValues.room2 = Float.parseFloat(this.wishPanel.r2Field.getText());
                GurobiValues.building2 = Float.parseFloat(this.wishPanel.b2Field.getText());
            } catch (Exception e) {
                // TODO
                System.out.println(e);
                System.out.println(e.getStackTrace());
            }
        }
        if (this.respectRes.box.isSelected()) {
            this.gurobiRules.add(Gurobi.RULES.respectReservations);
        }
        if (this.respectGradePriv.isFontSet()) {
            this.gurobiRules.add(Gurobi.RULES.respectGradePrivilege);
        }
    }
}
