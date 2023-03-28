package zimmerzuteilung.GUI;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class RadioPanel extends JPanel {
    JRadioButton radio;

    RadioPanel(String label, boolean enabled) {
        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        this.add(new Filler(50, Gui.row.height));
        this.radio = new JRadioButton();
        this.radio.setSelected(enabled);
        this.add(radio);
        this.add(new JLabel(label));
        this.add(new Filler(Gui.row.width, Gui.row.height));
    }
}
