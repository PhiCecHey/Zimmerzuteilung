package zimmerzuteilung.GUI;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ImportsPanel extends JPanel {
    JLabel label;
    JTextField field;
    JButton button;

    public ImportsPanel(String labelText) {
        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        this.setMaximumSize(new Dimension(2000, 50));
        this.label = new JLabel(labelText);
        this.add(label);
        this.field = new JTextField();
        this.add(field);
        this.button = new JButton("...");
        ImportsPanel.buttonFileChooser(button, field);
        this.add(button);
    }

    private static void buttonFileChooser(JButton b, JTextField f) {
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                Gui.changeFont(fileChooser, Gui.mainFrame.getFont().getSize());
                int rueckgabeWert = fileChooser.showOpenDialog(null);
                if (rueckgabeWert == JFileChooser.APPROVE_OPTION) {
                    f.setText(fileChooser.getSelectedFile().getAbsolutePath());
                }
            }
        });
    }
}