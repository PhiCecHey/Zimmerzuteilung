package zimmerzuteilung.GUI;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
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
        this.field.setMaximumSize(new Dimension(150, Gui.row.height));
        field.setBackground(Colors.greyTransp);
        CheckUserInput.checkForNegative(field);
        field.setEditable(false);
        CheckUserInput.checkForNegative(this.field);
        this.add(new GroupPanel(
                new Component[] { new Filler(150, Gui.row.height), new JLabel(description3), this.field,
                        new Filler(Gui.row.width, Gui.row.height) },
                "row"));

        ButtonGroup bg = new ButtonGroup();
        bg.add(this.radioPanel1.radio);
        bg.add(this.radioPanel2.radio);
        
        MustOrShouldPanel.radioListener(radioPanel2.radio, field);
    }

    static void radioListener(JRadioButton radio2, JTextField field) {
        radio2.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent arg0) {
                if (radio2.isSelected()) {
                    field.setBackground(Colors.transp);
                    field.setEditable(true);
                } else {
                    field.setEditable(false);
                    field.setBackground(Colors.greyTransp);
                }
            }

        });
    }
}