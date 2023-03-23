package zimmerzuteilung.GUI;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import zimmerzuteilung.algorithms.Config;

import java.awt.Component;
import java.awt.Dimension;

class GradePanel extends JPanel {
    private static Dimension dimPanel = new Dimension(Gui.row.width, Gui.row.height);
    private static Dimension dimField = new Dimension(80, Gui.row.height);

    JPanel twelvePanel, elevenPanel, tenPanel;
    JTextField twelveField, elevenField, tenField;

    GradePanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        // this.setMaximumSize(dimPanel);

        this.twelveField = new JTextField();
        this.twelveField.setMaximumSize(dimField);
        this.add(new GroupPanel(
                new Component[] { new Filler(50, Gui.row.getHeight()), new JLabel("12er Bonus: "), this.twelveField },
                "row", dimPanel));

        this.elevenField = new JTextField();
        this.elevenField.setMaximumSize(dimField);
        this.add(new GroupPanel(
                new Component[] { new Filler(50, Gui.row.getHeight()), new JLabel("11er Bonus: "), this.elevenField },
                "row", dimPanel));

        this.tenField = new JTextField();
        this.tenField.setMaximumSize(dimField);
        this.add(new GroupPanel(
                new Component[] { new Filler(50, Gui.row.getHeight()), new JLabel("10er Bonus: "), this.tenField },
                "row", dimPanel));

        this.twelveField.setText(Float.toString(Config.twelve));
        this.elevenField.setText(Float.toString(Config.eleven));
        this.tenField.setText(Float.toString(Config.ten));
    }
}