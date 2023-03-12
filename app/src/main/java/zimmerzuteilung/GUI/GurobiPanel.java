package zimmerzuteilung.GUI;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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
            this.setMaximumSize(Gui.row);
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
        JPanel b1Panel, b2Panel, r1Panel, r2Panel;

        WishPanel() {
            this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
            //this.setMaximumSize(dimPanel);
            //this.add(new Filler(100, Gui.row.getHeight()));

            this.b1Panel = new JPanel();
            this.b1Panel.setMaximumSize(dimPanel);
            this.b1Panel.setLayout(new BoxLayout(b1Panel, BoxLayout.LINE_AXIS));
            this.b1Panel.add(new Filler(50, Gui.row.height));
            this.b1Panel.add(new JLabel("Erstwunsch Internat Bonus: "));
            this.b1Field = new JTextField();
            this.b1Field.setMaximumSize(dimField);
            this.b1Field.setText("5");
            this.b1Panel.add(b1Field);
            this.add(this.b1Panel);

            this.r1Panel = new JPanel();
            this.r1Panel.setMaximumSize(dimPanel);
            this.r1Panel.setLayout(new BoxLayout(r1Panel, BoxLayout.LINE_AXIS));
            this.r1Panel.add(new Filler(50, Gui.row.height));
            this.r1Panel.add(new JLabel("Erstwunsch Zimmer Bonus: "));
            this.r1Field = new JTextField();
            this.r1Field.setMaximumSize(dimField);
            this.r1Field.setText("10");
            this.r1Panel.add(r1Field);
            this.add(this.r1Panel);

            this.r2Panel = new JPanel();
            this.r2Panel.setMaximumSize(dimPanel);
            this.r2Panel.setLayout(new BoxLayout(r2Panel, BoxLayout.LINE_AXIS));
            this.r2Panel.add(new Filler(50, Gui.row.height));
            this.r2Panel.add(new JLabel("Zweitwunsch Zimmer Bonus: "));
            this.r2Field = new JTextField();
            this.r2Field.setMaximumSize(dimField);
            this.r2Field.setText("8");
            this.r2Panel.add(r2Field);
            this.add(this.r2Panel);

            this.b2Panel = new JPanel();
            this.b2Panel.setMaximumSize(dimPanel);
            this.b2Panel.setLayout(new BoxLayout(b2Panel, BoxLayout.LINE_AXIS));
            this.b2Panel.add(new Filler(50, Gui.row.height));
            this.b2Panel.add(new JLabel("Zweitwunsch Internat Bonus: "));
            this.b2Field = new JTextField();
            this.b2Field.setMaximumSize(dimField);
            this.b2Field.setText("5");
            this.b2Panel.add(b2Field);
            this.add(this.b2Panel);
        }

    }

    class ReservationPanel extends JPanel {
        JTextField resField;

        ReservationPanel() {
            this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
            this.setMaximumSize(dimPanel);
            this.add(new Filler(50, Gui.row.getHeight()));
            this.add(new JLabel("Reservierungsbonus: "));
            this.resField = new JTextField();
            this.resField.setMaximumSize(dimField);
            this.resField.setText("50");
            this.add(this.resField);
        }
    }

    class GradePanel extends JPanel {
        JPanel twelvePanel, elevenPanel, tenPanel;
        JTextField twelveField, elevenField, tenField;

        GradePanel() {
            this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
            //this.setMaximumSize(dimPanel);
            
            this.twelvePanel = new JPanel();
            this.twelvePanel.setLayout(new BoxLayout(this.twelvePanel, BoxLayout.LINE_AXIS));
            this.twelvePanel.setMaximumSize(dimPanel);
            this.twelvePanel.add(new Filler(50, Gui.row.getHeight()));
            this.twelvePanel.add(new JLabel("12er Bonus: "));
            this.twelveField = new JTextField();
            this.twelvePanel.add(twelveField);
            this.twelveField.setMaximumSize(dimField);
            this.add(this.twelvePanel);
            
            this.elevenPanel = new JPanel();
            this.elevenPanel.setLayout(new BoxLayout(elevenPanel, BoxLayout.LINE_AXIS));
            this.elevenPanel.setMaximumSize(dimPanel);
            this.elevenPanel.add(new Filler(50, Gui.row.getHeight()));
            this.elevenPanel.add(new JLabel("11er Bonus: "));
            this.elevenField = new JTextField();
            this.elevenField.setMaximumSize(dimField);
            this.elevenPanel.add(elevenField);
            this.add(this.elevenPanel);
            
            this.tenPanel = new JPanel();
            this.tenPanel.setLayout(new BoxLayout(tenPanel, BoxLayout.LINE_AXIS));
            this.tenPanel.setMaximumSize(dimPanel);
            this.tenPanel.add(new Filler(50, Gui.row.getHeight()));
            this.tenPanel.add(new JLabel("10er Bonus: "));
            this.tenField = new JTextField();
            this.tenField.setMaximumSize(dimField);
            this.tenPanel.add(tenField);
            this.add(tenPanel);

            this.twelveField.setText("7");
            this.elevenField.setText("5");
            this.tenField.setText("3");
        }
    }

    private static Dimension dimPanel = new Dimension(Gui.row.width, Gui.row.height);
    private static Dimension dimField = new Dimension(80, Gui.row.height);

    private CheckBoxPanel oneRoomPerTeam, oneTeamPerRoom, maxStudentsPerRoom, respectWish, respectRes, respectGradePriv;

    public ArrayList<Gurobi.RULES> gurobiRules = new ArrayList<>();

    private GradePanel gradePanel;
    private WishPanel wishPanel;
    private ReservationPanel resPanel;
    private JButton save;
    private JPanel buttonPanel;

    public GurobiPanel() {
        this.init();
    }

    private void init() {
        this.oneRoomPerTeam = new CheckBoxPanel("Ein Zimmer pro Team", false);
        this.add(this.oneRoomPerTeam);
        this.add(new Filler(100, Gui.row.getHeight()));

        this.oneTeamPerRoom = new CheckBoxPanel("Ein Team pro Zimmer", false);
        this.add(this.oneTeamPerRoom);
        this.add(new Filler(100, Gui.row.getHeight()));

        this.maxStudentsPerRoom = new CheckBoxPanel("maximale Anzahl an Schüler:innen pro Zimmer einhalten", false);
        this.add(this.maxStudentsPerRoom);
        this.add(new Filler(100, Gui.row.getHeight()));

        this.respectWish = new CheckBoxPanel("Zimmerwünsche respektieren", true);
        this.add(this.respectWish);
        this.wishPanel = new WishPanel();
         this.add(this.wishPanel);
        this.add(new Filler(100, Gui.row.getHeight()));

        this.respectRes = new CheckBoxPanel("Zimmerreservierungen respektieren", true);
        this.add(this.respectRes);
        this.resPanel = new ReservationPanel();
        this.add(this.resPanel);
        this.add(new Filler(100, Gui.row.getHeight()));

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
        this.add(new Filler(100, (3*Gui.row.getHeight())));
        
        // TODO: why is save button not alligned left?
        this.buttonPanel = new JPanel();
        this.buttonPanel.setLayout(new BoxLayout(this.buttonPanel, BoxLayout.LINE_AXIS));
        this.buttonPanel.add(this.save);
        this.add(this.buttonPanel);
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
