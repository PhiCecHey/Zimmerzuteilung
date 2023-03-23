package zimmerzuteilung.GUI;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
