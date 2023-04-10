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
import javax.swing.filechooser.FileNameExtensionFilter;

class ChooseFilePanel extends JPanel {
    JLabel label;
    JTextField field;
    JButton button;

    public ChooseFilePanel(String labelText, String fileOrFolder) {
        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        this.setMaximumSize(new Dimension(2000, 50));

        this.label = new JLabel(labelText);
        this.add(label);

        this.field = new JTextField();
        this.add(field);

        this.button = new JButton("...");
        if (fileOrFolder.toLowerCase().equals("folder")) {
            ChooseFilePanel.chooseFolder(button, field);
        } else if (fileOrFolder.toLowerCase().equals("file")) {
            ChooseFilePanel.chooseFile(button, field);
        }
        this.add(button);
    }

    private static void chooseFile(JButton b, JTextField f) {
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                Gui.changeFont(fileChooser, Gui.mainFrame.getFont().getSize());
                fileChooser.setAcceptAllFileFilterUsed(false);
                FileNameExtensionFilter fileFilterCsv = new FileNameExtensionFilter("CSV", "csv");
                FileNameExtensionFilter fileFilterTxt = new FileNameExtensionFilter("TEXT", "txt");
                fileChooser.addChoosableFileFilter(fileFilterCsv);
                fileChooser.addChoosableFileFilter(fileFilterTxt);
                int rueckgabeWert = fileChooser.showOpenDialog(null);
                if (rueckgabeWert == JFileChooser.APPROVE_OPTION) {
                    f.setText(fileChooser.getSelectedFile().getAbsolutePath());
                }
            }
        });
    }

    private static void chooseFolder(JButton b, JTextField f) {
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                Gui.changeFont(fileChooser, Gui.mainFrame.getFont().getSize());
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fileChooser.setAcceptAllFileFilterUsed(false);
                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    f.setBackground(Colors.blueTransp);
                    // TODO: only parent dir returned ?!
                    String currentDir = fileChooser.getCurrentDirectory().getAbsolutePath();
                    if (!currentDir.endsWith("/")) {
                        currentDir += "/";
                    }
                    f.setText(currentDir += "Zimmerzuteilung_Ergebnisse.csv");
                }
            }
        });
    }
}