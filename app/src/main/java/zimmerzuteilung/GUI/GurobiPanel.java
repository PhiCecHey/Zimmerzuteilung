package zimmerzuteilung.GUI;

import java.awt.Component;
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
    private static ArrayList<Gurobi.RULES> gurobiRules = new ArrayList<>();
    private CheckBoxPanel oneRoomPerTeam, oneTeamPerRoom, respectWishPanel, respectGradePrivPanel, randomPanel;
    private MustOrShouldPanel maxStudentsPerRoom, respectReservationPanel, respectRoomGenderPanel;
    private StayInRoomPanel stayInRoomPanel;
    private GradePanel gradePanel;
    private WishPanel wishPanel;
    private JButton save;
    private JTextField randomField;
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
        float value1;

        heading = "Zimmer reservieren";
        descript1 = "Unbedingt reservierte Zimmer freihalten";
        descript2 = "Falls moeglich reservierte Zimmer freihalten: ";
        descript3 = "Bonus fuer nicht reservierte Zimmer: ";
        value1 = Config.scoreReservation;
        this.respectReservationPanel = new MustOrShouldPanel(heading, descript1, descript2, descript3, value1);
        topRight.add(this.respectReservationPanel);

        topRight.add(new JLabel("       "));
        topRight.add(new JLabel("       "));
        topRight.add(new JLabel("       "));

        heading = "Maximale Anzahl an Schueler:innen pro Zimmer";
        descript1 = "Immer maximale Belegung einhalten";
        descript2 = "Moeglichst maximale Belegung einhalten: ";
        descript3 = "Bonus bei Nichteinhalten: ";
        value1 = Config.maxStudentsPerRoom;
        this.maxStudentsPerRoom = new MustOrShouldPanel(heading, descript1, descript2, descript3, value1);
        topRight.add(maxStudentsPerRoom);

        topRight.add(new JLabel("       "));
        topRight.add(new JLabel("       "));
        topRight.add(new JLabel("       "));

        heading = "Maedchenteams in Maedchenzimmer und Jungenteams in Jungenzimmer";
        descript1 = "Immer einhalten";
        descript2 = "Moeglichst einhalten: ";
        descript3 = "Bonus bei Nichteinhalten: ";
        value1 = Config.scoreGender;
        this.respectRoomGenderPanel = new MustOrShouldPanel(heading, descript1, descript2, descript3, value1);
        topRight.add(this.respectRoomGenderPanel);

        topRight.add(new JLabel("       "));
        topRight.add(new JLabel("       "));
        topRight.add(new JLabel("       "));

        heading = "Alle Teams, die in ihrem vorherigen Zimmer bleiben wollen, bleiben in diesem";
        descript1 = "Immer einhalten";
        descript2 = "Moeglichst einhalten";
        descript3 = "Bonus fuer das Bleiben im Erstwunschinternat: ";
        String descript4 = "Bonus fuer das Bleiben im Erstwunschzimmer: ";
        value1 = Config.scoreStayInRoom;
        float value2 = Config.scoreStayInBuilding;
        this.stayInRoomPanel = new StayInRoomPanel(heading, descript1, descript2, descript3, descript4, value1, value2);
        topRight.add(this.stayInRoomPanel);
    }

    private void initTopLeft(GroupPanel topLeft) {
        this.oneRoomPerTeam = new CheckBoxPanel("Genau ein Zimmer pro Team", true);
        topLeft.add(this.oneRoomPerTeam);

        topLeft.add(new JLabel("             "));

        this.oneTeamPerRoom = new CheckBoxPanel("Maximal ein Team pro Zimmer", true);
        topLeft.add(oneTeamPerRoom);

        topLeft.add(new JLabel("             "));

        this.respectWishPanel = new CheckBoxPanel("Zimmerwuensche respektieren", true);
        this.wishPanel = new WishPanel(this.respectWishPanel.box);
        CheckUserInput.checkSelected(this.respectWishPanel.box, new JTextField[] { this.wishPanel.b1Field,
                this.wishPanel.r1Field, this.wishPanel.r2Field, this.wishPanel.b2Field });
        topLeft.add(this.respectWishPanel);
        topLeft.add(this.wishPanel);

        topLeft.add(new JLabel("             "));

        this.respectGradePrivPanel = new CheckBoxPanel("12er, 11er, 10er Privileg respektieren", true);
        this.gradePanel = new GradePanel(this.respectGradePrivPanel.box);
        CheckUserInput.checkSelected(this.respectGradePrivPanel.box, new JTextField[] { this.gradePanel.tenField,
                this.gradePanel.elevenField, this.gradePanel.twelveField });
        topLeft.add(this.respectGradePrivPanel);
        topLeft.add(this.gradePanel);

        topLeft.add(new JLabel("             "));

        this.randomPanel = new CheckBoxPanel("Extra Zufall: ", true);
        this.randomPanel.setMaximumSize(this.randomPanel.getMinimumSize());
        // this.randomPanel.box.setSelected(false);
        this.randomField = new JTextField(Float.toString(Config.scoreRandom));
        this.randomField.setMaximumSize(new Dimension(300, Gui.row.height));
        CheckUserInput.checkSelected(this.randomPanel.box, new JTextField[] { this.randomField });
        CheckUserInput.checkForPositive(this.randomPanel.box, this.randomField);
        topLeft.add(new GroupPanel(new Component[] { this.randomPanel, this.randomField, new Filler(Gui.row.width, 1) },
                "row"));
    }

    void initBottom(GroupPanel bottom) {
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.PAGE_AXIS));
        bottom.add(new JLabel("   "));
        bottom.add(new JLabel("   "));

        this.save = new JButton("anwenden");
        this.save.setBackground(Colors.blueTransp);
        this.save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                determineRules();
                save.setBackground(Colors.greenTransp);
            }
        });
        bottom.add(new GroupPanel(new Component[] { new Filler(Gui.row.width / 2, this.save.getHeight()), this.save,
                new Filler(Gui.row.width / 2, this.save.getHeight()) }, "row"));

        bottom.add(new Filler(Gui.row.width, 2 * Gui.row.height));

        this.area = new JTextArea("\n\n");
        area.setEditable(false);
        bottom.add(this.area);
    }

    private void determineRules() {
        this.area.setText("");
        GurobiPanel.gurobiRules.clear();

        if (this.oneRoomPerTeam.box.isSelected()) {
            GurobiPanel.gurobiRules.add(Gurobi.RULES.oneRoomPerTeam);
        }

        if (this.oneTeamPerRoom.box.isSelected()) {
            GurobiPanel.gurobiRules.add(Gurobi.RULES.oneTeamPerRoom);
        }

        if (this.randomPanel.box.isSelected()) {
            boolean worked = true;
            try {
                Config.scoreRandom = Float.parseFloat(this.randomField.getText());
            } catch (NumberFormatException e) {
                worked = false;
            }
            if (worked) {
                GurobiPanel.gurobiRules.add(Gurobi.RULES.addExtraRandomness);
                this.randomField.setBackground(Colors.greenTransp);
            } else {
                this.area.append("Zufall: Bitte eine positive Zahl eintragen!\n");
            }
        }

        if (this.maxStudentsPerRoom.radioPanel1.radio.isSelected()) {
            GurobiPanel.gurobiRules.add(Gurobi.RULES.maxStudentsPerRoom);
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
                this.area.append("Max Anzahl an Schuelern pro Zimmer: Bitte eine negative Zahl eintragen!\n");
            }
        }

        if (this.respectReservationPanel.radioPanel1.radio.isSelected()) {
            GurobiPanel.gurobiRules.add(Gurobi.RULES.respectReservations);
        } else if (this.respectReservationPanel.radioPanel2.radio.isSelected()) {
            boolean worked = true;
            try {
                Config.scoreReservation = Float.parseFloat(this.respectReservationPanel.field.getText());
            } catch (NumberFormatException e) {
                worked = false;
            }
            if (worked) {
                this.respectReservationPanel.field.setBackground(Colors.greenTransp);
            } else {
                this.area.append("Reservierte Zimmer: Bitte eine negative Zahl eintragen!\n");
            }
        }

        if (this.respectRoomGenderPanel.radioPanel1.radio.isSelected()) {
            GurobiPanel.gurobiRules.add(Gurobi.RULES.respectRoomGender);
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
                this.area.append("Geschlecht beruecksichtigen: Bitte eine negative Zahl eintragen!\n");
            }
        }

        if (this.stayInRoomPanel.radioPanel1.radio.isSelected()) {
            GurobiPanel.gurobiRules.add(Gurobi.RULES.respectPrevRoom);
        } else if (this.stayInRoomPanel.radioPanel2.radio.isSelected()) {
            boolean worked = true;
            try {
                Config.scoreStayInBuilding = Float.parseFloat(this.stayInRoomPanel.fieldStayInBuilding.getText());
            } catch (NumberFormatException e) {
                worked = false;
            }
            if (worked) {
                this.stayInRoomPanel.fieldStayInBuilding.setBackground(Colors.greenTransp);
            } else {
                this.area.append("Im vorherigen Internat bleiben: Bitte eine negative Zahl eintragen!\n");
            }
            worked = true;
            try {
                Config.scoreStayInRoom = Float.parseFloat(this.stayInRoomPanel.fieldStayInRoom.getText());
            } catch (NumberFormatException e) {
                worked = false;
            }
            if (worked) {
                this.stayInRoomPanel.fieldStayInRoom.setBackground(Colors.greenTransp);
            } else {
                this.area.append("Im vorherigen Zimmer bleiben: Bitte eine negative Zahl eintragen!\n");
            }
        }

        if (this.respectWishPanel.box.isSelected()) {
            GurobiPanel.gurobiRules.add(Gurobi.RULES.respectWish);
            boolean worked = GurobiPanel.checkUserInput(this.wishPanel.b1Field, "b1");
            if (!worked) {
                this.area.append("Erstwusnschinternat Bonus: Bitte eine positive Zahl eintragen!\n");
            }
            worked = GurobiPanel.checkUserInput(this.wishPanel.r1Field, "r1");
            if (!worked) {
                this.area.append("Erstwusnschzimmer Bonus: Bitte eine positive Zahl eintragen!\n");
            }
            worked = GurobiPanel.checkUserInput(this.wishPanel.r2Field, "r2");
            if (!worked) {
                this.area.append("Zweitwunschzimmer Bonus: Bitte eine positive Zahl eintragen!\n");
            }
            worked = GurobiPanel.checkUserInput(this.wishPanel.b2Field, "b2");
            if (!worked) {
                this.area.append("Zweitwunschinternat Bonus: Bitte eine positive Zahl eintragen!\n");
            }
        }

        if (this.respectGradePrivPanel.box.isSelected()) {
            GurobiPanel.gurobiRules.add(Gurobi.RULES.respectGradePrivilege);
            boolean worked = GurobiPanel.checkUserInput(this.gradePanel.twelveField, "12");
            if (!worked) {
                this.area.append("12er Privileg: Bitte eine positive Zahl eintragen!\n");
            }
            worked = GurobiPanel.checkUserInput(this.gradePanel.elevenField, "11");
            if (!worked) {
                this.area.append("11er Privileg: Bitte eine positive Zahl eintragen!\n");
            }
            worked = GurobiPanel.checkUserInput(this.gradePanel.tenField, "10");
            if (!worked) {
                this.area.append("10er Privileg: Bitte eine positive Zahl eintragen!\n");
            }
        }
    }

    public static ArrayList<Gurobi.RULES> rules() {
        return GurobiPanel.gurobiRules;
    }

    private static boolean checkUserInput(JTextField field, String configName) {
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
            } else if (configName.toLowerCase().contains("stay") && configName.toLowerCase().contains("building")) {
                Config.scoreStayInBuilding = config;
            } else if (configName.toLowerCase().contains("stay") && configName.toLowerCase().contains("room")) {
                Config.scoreStayInRoom = config;
            } else {
                System.out.println("Problem in function checkUserInput()");
            }
        }
        if (worked == 1) {
            return true;
        }
        return false;
    }
}
