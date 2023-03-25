package zimmerzuteilung.GUI;

import javax.swing.BoxLayout;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import zimmerzuteilung.algorithms.Config;

class ReservationPanel extends JPanel {
    JTextField resField;
    private static Dimension dimPanel = new Dimension(Gui.row.width, Gui.row.height);
    private static Dimension dimField = new Dimension(80, Gui.row.height);

    ReservationPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        this.setMaximumSize(dimPanel);
        this.add(new Filler(50, Gui.row.getHeight()));
        this.add(new JLabel("Reservierungsbonus: "));
        this.resField = new JTextField();
        this.resField.setMaximumSize(dimField);
        this.resField.setText(Float.toString(Config.scoreReservation));
        this.add(this.resField);
    }
}