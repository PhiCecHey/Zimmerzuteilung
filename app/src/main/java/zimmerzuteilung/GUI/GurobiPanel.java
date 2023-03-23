package zimmerzuteilung.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import zimmerzuteilung.algorithms.Gurobi;
import zimmerzuteilung.algorithms.Config;

public class GurobiPanel extends JPanel {
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

        this.save = new JButton("anwenden");
        this.save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                getRules();
            }
        });

        this.add(new Filler(100, (3 * Gui.row.getHeight())));

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
                Config.building1 = Float.parseFloat(this.wishPanel.b1Field.getText());
                Config.room1 = Float.parseFloat(this.wishPanel.r1Field.getText());
                Config.room2 = Float.parseFloat(this.wishPanel.r2Field.getText());
                Config.building2 = Float.parseFloat(this.wishPanel.b2Field.getText());
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
