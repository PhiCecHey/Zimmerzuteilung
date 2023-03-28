package zimmerzuteilung.GUI;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import zimmerzuteilung.Config;
import zimmerzuteilung.algorithms.Gurobi;

public class GurobiPanel extends JPanel {
    public ArrayList<Gurobi.RULES> gurobiRules = new ArrayList<>();
    private CheckBoxPanel oneRoomPerTeam, oneTeamPerRoom, respectWishPanel, respectGradePrivPanel;
    private MustOrShouldPanel maxStudentsPerRoom, respectResPanel, respectRoomGenderPanel;
    private GradePanel gradePanel;
    private WishPanel wishPanel;
    private JButton save;
    private JTextArea area;

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
        String heading, descript1, descript2, descript3;
        float value;

        heading = "Zimmerreservierungen respektieren";
        descript1 = "Unbedingt reservierte Zimmer freihalten";
        descript2 = "Zimmer freihalten falls möglich: ";
        descript3 = "Malus für nicht reservierte Zimmer: ";
        value = Config.scoreReservation;
        this.respectResPanel = new MustOrShouldPanel(heading, descript1, descript2, descript3, value);

        topRight.add(this.respectResPanel);

        topRight.add(new JLabel("       "));
        topRight.add(new JLabel("       "));
        topRight.add(new JLabel("       "));

        heading = "Maximale Anzahl an Schüler:innen pro Zimmer einhalten";
        descript1 = "Immer maximale Anzahl an Schüler:innen pro Zimmer einhalten";
        descript2 = "Möglichst maximale Anzahl an Schüler:innen pro Zimmer einhalten: ";
        descript3 = "Malus bei Nichteinhalten: ";
        value = Config.maxStudentsPerRoom;
        this.maxStudentsPerRoom = new MustOrShouldPanel(heading, descript1, descript2, descript3, value);
        topRight.add(maxStudentsPerRoom);

        topRight.add(new JLabel("       "));
        topRight.add(new JLabel("       "));
        topRight.add(new JLabel("       "));

        heading = "Mädchen-/Jungszimmer berücksichtigen";
        descript1 = "Immer berücksichtigen";
        descript2 = "Möglichst berücksichtigen: ";
        descript3 = "Malus bei Nichtberücksichtigung: ";
        value = Config.scoreGender;
        this.respectRoomGenderPanel = new MustOrShouldPanel(heading, descript1, descript2, descript3, value);
        topRight.add(this.respectRoomGenderPanel);

        topRight.add(new JLabel("             "));
        topRight.add(new JLabel("             "));
        topRight.add(new JLabel("             "));
        topRight.add(new JLabel("             "));
        topRight.add(new JLabel("             "));
        topRight.add(new JLabel("             "));
        topRight.add(new JLabel("             "));
    }

    private void initTopLeft(GroupPanel topLeft) {
        this.oneRoomPerTeam = new CheckBoxPanel("Genau ein Zimmer pro Team", true);
        topLeft.add(this.oneRoomPerTeam);

        topLeft.add(new JLabel("             "));

        this.oneTeamPerRoom = new CheckBoxPanel("Ein Team pro Zimmer", true);
        topLeft.add(oneTeamPerRoom);

        topLeft.add(new JLabel("             "));

        this.respectWishPanel = new CheckBoxPanel("Zimmerwünsche respektieren", true);
        topLeft.add(this.respectWishPanel);
        this.wishPanel = new WishPanel();
        topLeft.add(this.wishPanel);

        topLeft.add(new JLabel("             "));

        this.respectGradePrivPanel = new CheckBoxPanel("12er, 11er, 10er Privileg respektieren", true);
        topLeft.add(this.respectGradePrivPanel);
        this.gradePanel = new GradePanel();
        topLeft.add(this.gradePanel);
    }

    void initBottom(GroupPanel bottom) {
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.PAGE_AXIS));

        this.area = new JTextArea("\n\n");
        this.area.setMaximumSize(new Dimension(Gui.row.width, 200));
        area.setEditable(false);
        bottom.add(this.area);

        this.save = new JButton("anwenden");
        this.save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                getRules();
            }
        });
        bottom.add(this.save);
    }

    private void getRules() {
        this.area.setText("");
        if (this.oneRoomPerTeam.box.isSelected()) {
            this.gurobiRules.add(Gurobi.RULES.oneRoomPerTeam);
        }

        if (this.oneTeamPerRoom.box.isSelected()) {
            this.gurobiRules.add(Gurobi.RULES.oneTeamPerRoom);
        }

        if (this.maxStudentsPerRoom.radioPanel1.radio.isSelected()) {
            this.gurobiRules.add(Gurobi.RULES.maxStudentsPerRoom);
        } else if (this.maxStudentsPerRoom.radioPanel2.radio.isSelected()) {
            boolean worked = true;
            try {
                Config.maxStudentsPerRoom = Float.parseFloat(this.maxStudentsPerRoom.field.getText());
            } catch (NumberFormatException e) {
                worked = false;
            }
            if (worked) {
                this.maxStudentsPerRoom.field.setBackground(Colors.greenTransp);
            } else {
                this.area.append("Max Anzahl an Schülern pro Zimmer: Bitte eine negative Zahl eintragen!\n");
            }
        }

        if (this.respectResPanel.radioPanel1.radio.isSelected()) {
            this.gurobiRules.add(Gurobi.RULES.respectReservations);
        } else if (this.respectResPanel.radioPanel2.radio.isSelected()) {
            boolean worked = true;
            try {
                Config.scoreReservation = Float.parseFloat(this.maxStudentsPerRoom.field.getText());
            } catch (NumberFormatException e) {
                worked = false;
            }
            if (worked) {
                this.respectResPanel.field.setBackground(Colors.greenTransp);
            } else {
                this.area.append("Reservierte Zimmer: Bitte eine negative Zahl eintragen!\n");
            }
        }

        if (this.respectRoomGenderPanel.radioPanel1.radio.isSelected()) {
            this.gurobiRules.add(Gurobi.RULES.respectRoomGender);
        } else if (this.respectRoomGenderPanel.radioPanel2.radio.isSelected()) {
            boolean worked = true;
            try {
                Config.scoreGender = Float.parseFloat(this.respectRoomGenderPanel.field.getText());
            } catch (NumberFormatException e) {
                worked = false;
            }
            if (worked) {
                this.respectRoomGenderPanel.field.setBackground(Colors.greenTransp);
            } else {
                this.area.append("Geschlecht berücksichtigen: Bitte eine negative Zahl eintragen!\n");
            }
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
