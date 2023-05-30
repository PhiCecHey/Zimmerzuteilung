package zimmerzuteilung.GUI;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class StayInRoomPanel extends JPanel {
    RadioPanel radioPanel1, radioPanel2;
    JTextField fieldStayInRoom, fieldStayInBuilding;

    StayInRoomPanel(String heading, String desc1, String desc2, String desc3, String desc4, float valueBuilding,
            float valueRoom) {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        this.add(new GroupPanel(
                new Component[] { new JLabel(heading), new Filler(Gui.row.width, Gui.row.height) },
                "row"));

        this.radioPanel1 = new RadioPanel(desc1, true);
        this.add(radioPanel1);

        this.radioPanel2 = new RadioPanel(desc2, false);
        this.add(radioPanel2);

        this.fieldStayInBuilding = new JTextField(String.valueOf(valueBuilding));
        this.fieldStayInBuilding.setMaximumSize(new Dimension(280, Gui.row.height));
        this.fieldStayInBuilding.setBackground(Colors.greyTransp);
        CheckUserInput.checkForNegative(this.radioPanel2.radio, this.fieldStayInBuilding);
        this.fieldStayInBuilding.setEditable(false);
        CheckUserInput.checkForNegative(this.radioPanel2.radio, this.fieldStayInBuilding);
        this.add(new GroupPanel(
                new Component[] { new Filler(150, Gui.row.height), new JLabel(desc3), this.fieldStayInBuilding,
                        new Filler(Gui.row.width, Gui.row.height) },
                "row"));

        this.fieldStayInRoom = new JTextField(String.valueOf(valueRoom));
        this.fieldStayInRoom.setMaximumSize(new Dimension(280, Gui.row.height));
        this.fieldStayInRoom.setBackground(Colors.greyTransp);
        CheckUserInput.checkForNegative(this.radioPanel2.radio, this.fieldStayInRoom);
        this.fieldStayInRoom.setEditable(false);
        CheckUserInput.checkForNegative(this.radioPanel2.radio, this.fieldStayInRoom);
        this.add(new GroupPanel(
                new Component[] { new Filler(150, Gui.row.height), new JLabel(desc4), this.fieldStayInRoom,
                        new Filler(Gui.row.width, Gui.row.height) },
                "row"));

        ButtonGroup bg = new ButtonGroup();
        bg.add(this.radioPanel1.radio);
        bg.add(this.radioPanel2.radio);

        CheckUserInput.checkSelected(radioPanel2.radio, fieldStayInRoom);
        CheckUserInput.checkSelected(radioPanel2.radio, fieldStayInBuilding);
    }
}
