package zimmerzuteilung.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import zimmerzuteilung.algorithms.Gurobi;
import zimmerzuteilung.algorithms.Config;

public class GurobiPanel extends JPanel {
    public ArrayList<Gurobi.RULES> gurobiRules = new ArrayList<>();
    private CheckBoxPanel respectWishPanel, respectResPanel, respectGradePrivPanel;
    private MustOrShouldPanel oneRoomPerTeam, oneTeamPerRoom, maxStudentsPerRoom, respectWishField,
            respectResField, respectGradePrivField;
    private GradePanel gradePanel;
    private WishPanel wishPanel;
    private ReservationPanel resPanel;
    private JButton save;

    public GurobiPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        GroupPanel top = new GroupPanel("row");
        GroupPanel bottom = new GroupPanel("column");
        GroupPanel topRight = new GroupPanel("column");
        GroupPanel topLeft = new GroupPanel("column");
        top.add(topLeft);
        top.add(topRight);
        this.add(top);
        this.add(bottom);
        this.initTopRight(topRight);
        this.initTopLeft(topLeft);
        this.initBottom(bottom);
    }

    private void initTopRight(GroupPanel topRight) {
        String heading = "Ein Zimmer pro Team";
        String descript1 = "Genau ein Zimmer pro Team";
        String descript2 = "Möglichst ein Zimmer pro Team: ";
        this.oneRoomPerTeam = new MustOrShouldPanel(heading, descript1, descript2, "Malus bei Nichteinhalten: ");
        topRight.add(this.oneRoomPerTeam);

        topRight.add(new Filler(100, Gui.row.getHeight()));

        heading = "Ein Team pro Zimmer";
        descript1 = "Genau ein Team pro Zimmer";
        descript2 = "Möglichst ein Team pro Zimmer: ";
        this.oneTeamPerRoom = new MustOrShouldPanel(heading, descript1, descript2, "Malus bei Nichteinhalten: ");
        topRight.add(oneTeamPerRoom);

        topRight.add(new Filler(100, Gui.row.getHeight()));

        heading = "Maximale Anzahl an Schüler:innen pro Zimmer einhalten";
        descript1 = "Immer maximale Anzahl an Schüler:innen pro Zimmer einhalten";
        descript2 = "Möglichst maximale Anzahl an Schüler:innen pro Zimmer einhalten: ";
        this.maxStudentsPerRoom = new MustOrShouldPanel(heading, descript1, descript2, "Malus bei Nichteinhalten: ");
        topRight.add(maxStudentsPerRoom);

        topRight.add(new Filler(Gui.row.width, 5000));
    }

    private void initTopLeft(GroupPanel topLeft) {
        this.respectWishPanel = new CheckBoxPanel("Zimmerwünsche respektieren", true);
        topLeft.add(this.respectWishPanel);
        this.wishPanel = new WishPanel();
        topLeft.add(this.wishPanel);
        topLeft.add(new Filler(100, Gui.row.getHeight()));

        this.respectResPanel = new CheckBoxPanel("Zimmerreservierungen respektieren", true);
        topLeft.add(this.respectResPanel);
        this.resPanel = new ReservationPanel();
        topLeft.add(this.resPanel);
        topLeft.add(new Filler(100, Gui.row.getHeight()));

        this.respectGradePrivPanel = new CheckBoxPanel("12er, 11er, 10er Privileg respektieren", true);
        topLeft.add(this.respectGradePrivPanel);
        this.gradePanel = new GradePanel();
        topLeft.add(this.gradePanel);

        topLeft.add(new Filler(Gui.row.width, 5000));
    }

    void initBottom(GroupPanel bottom) {
        this.save = new JButton("anwenden");
        this.save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                getRules();
            }
        });

        bottom.add(new Filler(100, (3 * Gui.row.getHeight())));
        bottom.add(this.save);
    }

    private void getRules() {
        if (this.oneRoomPerTeam.radioPanel1.radio.isSelected()) {
            this.gurobiRules.add(Gurobi.RULES.oneRoomPerTeam);
        }
        if (this.oneTeamPerRoom.radioPanel1.radio.isSelected()) {
            this.gurobiRules.add(Gurobi.RULES.oneTeamPerRoom);
        }
        if (this.maxStudentsPerRoom.radioPanel1.radio.isSelected()) {
            this.gurobiRules.add(Gurobi.RULES.maxStudentsPerRoom);
        }
        if (this.respectWishPanel.box.isSelected()) {
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
        if (this.respectResPanel.box.isSelected()) {
            this.gurobiRules.add(Gurobi.RULES.respectReservations);
            GurobiPanel.checkUserInput(this.resPanel.resField, "res");
        } else {
            this.resPanel.resField.setBackground(Colors.yellowTransp);
        }
        if (this.respectGradePrivPanel.box.isSelected()) {
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
