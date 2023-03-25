package zimmerzuteilung.GUI;

import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import zimmerzuteilung.algorithms.Gurobi;
import zimmerzuteilung.algorithms.Config;

public class GurobiPanel extends JPanel {
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

        this.save = new JButton("anwenden");
        this.save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                getRules();
            }
        });

        this.add(new Filler(100, (3 * Gui.row.getHeight())));
        this.add(this.save);
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
            GurobiPanel.checkUserInput(this.wishPanel.b1Field, "b1");
            GurobiPanel.checkUserInput(this.wishPanel.r1Field, "r1");
            GurobiPanel.checkUserInput(this.wishPanel.r2Field, "r2");
            GurobiPanel.checkUserInput(this.wishPanel.b2Field, "b2");
        } else {
            this.wishPanel.b1Field.setBackground(Colors.yellowTransp);
            this.wishPanel.r1Field.setBackground(Colors.yellowTransp);
            this.wishPanel.r2Field.setBackground(Colors.yellowTransp);
            this.wishPanel.b2Field.setBackground(Colors.yellowTransp);
        }
        if (this.respectRes.box.isSelected()) {
            this.gurobiRules.add(Gurobi.RULES.respectReservations);
            GurobiPanel.checkUserInput(this.resPanel.resField, "res");
        } else {
            this.resPanel.resField.setBackground(Colors.yellowTransp);
        }
        if (this.respectGradePriv.box.isSelected()) {
            this.gurobiRules.add(Gurobi.RULES.respectGradePrivilege);
            GurobiPanel.checkUserInput(this.gradePanel.twelveField, "12");
            GurobiPanel.checkUserInput(this.gradePanel.elevenField, "11");
            GurobiPanel.checkUserInput(this.gradePanel.tenField, "10");
        } else {
            this.gradePanel.twelveField.setBackground(Colors.yellowTransp);
            this.gradePanel.elevenField.setBackground(Colors.yellowTransp);
            this.gradePanel.tenField.setBackground(Colors.yellowTransp);
        }
    }

    private static byte checkUserInput(JTextField field, String configName) {
        byte worked = 0;
        float config = 0;
        try {
            String text = field.getText();
            if (text != null && !text.equals("")) {
                worked = 1;
                config = Float.parseFloat(field.getText());
            }
        } catch (Exception e) {
            worked = -1;
            field.setBackground(Colors.redTransp);
        }
        if (worked == 1) {
            field.setBackground(Colors.greenTransp);
            if (configName.toLowerCase().contains("b1")) {
                Config.scoreBuilding1 = config;
            } else if (configName.toLowerCase().contains("r1")) {
                Config.scoreRoom1 = config;
            } else if (configName.toLowerCase().contains("r2")) {
                Config.scoreRoom2 = config;
            } else if (configName.toLowerCase().contains("b2")) {
                Config.scoreBuilding2 = config;
            } else if (configName.toLowerCase().contains("res")) {
                Config.scoreReservation = config;
            } else if (configName.toLowerCase().contains("12")) {
                Config.scoreTwelve = config;
            } else if (configName.toLowerCase().contains("11")) {
                Config.scoreEleven = config;
            } else if (configName.toLowerCase().contains("10")) {
                Config.scoreTen = config;
            } else {
                System.out.println("Problem in function checkUserInput()");
            }
        }
        return worked;
    }
}
