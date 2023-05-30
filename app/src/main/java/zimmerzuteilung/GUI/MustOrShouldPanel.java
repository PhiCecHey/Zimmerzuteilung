package zimmerzuteilung.GUI;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MustOrShouldPanel extends JPanel {
    RadioPanel radioPanel1, radioPanel2;
    JTextField field;

    MustOrShouldPanel(String heading, String description1, String description2, String description3, float value) {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        this.add(new GroupPanel(
                new Component[] { new JLabel(heading), new Filler(Gui.row.width, Gui.row.height) },
                "row"));

        this.radioPanel1 = new RadioPanel(description1, true);
        this.add(radioPanel1);

        this.radioPanel2 = new RadioPanel(description2, false);
        this.add(radioPanel2);

        this.field = new JTextField(String.valueOf(value));
        this.field.setMaximumSize(new Dimension(280, Gui.row.height));
        this.field.setBackground(Colors.greyTransp);
        CheckUserInput.checkForNegative(this.radioPanel2.radio, this.field);
        this.field.setEditable(false);
        CheckUserInput.checkForNegative(this.radioPanel2.radio, this.field);
        this.add(new GroupPanel(
                new Component[] { new Filler(150, Gui.row.height), new JLabel(description3), this.field,
                        new Filler(Gui.row.width, Gui.row.height) },
                "row"));

        ButtonGroup bg = new ButtonGroup();
        bg.add(this.radioPanel1.radio);
        bg.add(this.radioPanel2.radio);
        
        CheckUserInput.checkSelected(radioPanel2.radio, field);
    }
}
