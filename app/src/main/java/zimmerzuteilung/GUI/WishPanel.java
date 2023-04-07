package zimmerzuteilung.GUI;

import javax.swing.BoxLayout;
import javax.swing.JLabel;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JTextField;

import zimmerzuteilung.Config;

class WishPanel extends JPanel {
    private static Dimension dimPanel = new Dimension(Gui.row.width, Gui.row.height);
    private static Dimension dimField = new Dimension(150, Gui.row.height);

    JTextField b1Field, b2Field, r1Field, r2Field;

    WishPanel(boolean check) {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        this.b1Field = new JTextField();
        this.b1Field.setMaximumSize(dimField);
        this.b1Field.setText(Float.toString(Config.scoreBuilding1));
        this.add(new GroupPanel(
                new Component[] { new Filler(50, Gui.row.height), new JLabel("Erstwunsch Internat Bonus: "), b1Field },
                "row", dimPanel));

        this.r1Field = new JTextField();
        this.r1Field.setMaximumSize(dimField);
        this.r1Field.setText(Float.toString(Config.scoreRoom1));
        this.add(new GroupPanel(
                new Component[] { new Filler(50, Gui.row.height), new JLabel("Erstwunsch Zimmer Bonus: "), r1Field },
                "row", dimPanel));

        this.r2Field = new JTextField();
        this.r2Field.setMaximumSize(dimField);
        this.r2Field.setText(Float.toString(Config.scoreRoom2));
        this.add(new GroupPanel(
                new Component[] { new Filler(50, Gui.row.height), new JLabel("Zweitwunsch Zimmer Bonus: "), r2Field },
                "row", dimPanel));

        this.b2Field = new JTextField();
        this.b2Field.setMaximumSize(dimField);
        this.b2Field.setText(Float.toString(Config.scoreBuilding1));
        this.add(new GroupPanel(
                new Component[] { new Filler(50, Gui.row.height), new JLabel("Zweitwunsch Internat Bonus: "), b2Field },
                "row", dimPanel));

        if (check) {
            CheckUserInput.checkForPositive(this.b1Field);
            CheckUserInput.checkForPositive(this.r1Field);
            CheckUserInput.checkForPositive(this.r2Field);
            CheckUserInput.checkForPositive(this.b2Field);
        }
    }

}
