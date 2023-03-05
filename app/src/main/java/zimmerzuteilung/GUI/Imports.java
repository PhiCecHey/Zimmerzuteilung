package zimmerzuteilung.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Imports extends JPanel {
    JLabel label;
    JTextField field;
    JButton button;

    public Imports(String labelText) {
        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        this.setMaximumSize(new Dimension(2000, 50));
        this.label = new JLabel(labelText);
        this.add(label);
        this.field = new JTextField();
        this.add(field);
        this.button = new JButton("...");
        Imports.buttonFileChooser(button, field);
        this.add(button);
    }

    private static void buttonFileChooser(JButton b, JTextField f) {
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int rueckgabeWert = fileChooser.showOpenDialog(null);
                if (rueckgabeWert == JFileChooser.APPROVE_OPTION) {
                    f.setText(fileChooser.getSelectedFile().getAbsolutePath());
                }
            }
        });
    }
}