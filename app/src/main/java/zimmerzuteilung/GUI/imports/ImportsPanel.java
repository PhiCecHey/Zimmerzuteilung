package zimmerzuteilung.GUI.imports;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import zimmerzuteilung.GUI.Gui;
import zimmerzuteilung.imports.ImportFiles;
import zimmerzuteilung.log.Log;

public class ImportsPanel extends JPanel{
    class SubPanel extends JPanel {
        JLabel label;
        JTextField field;
        JButton button;
    
        public SubPanel(String labelText) {
            this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
            this.setMaximumSize(new Dimension(2000, 50));
            this.label = new JLabel(labelText);
            this.add(label);
            this.field = new JTextField();
            this.add(field);
            this.button = new JButton("...");
            SubPanel.buttonFileChooser(button, field);
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

    private ArrayList<SubPanel> importsPanels = new ArrayList<>();
    private JButton importsButton = new JButton("Dateien einlesen");
    public JTextArea importsArea = new JTextArea();
    private JScrollPane importsScroll = new JScrollPane(importsArea);

    public ImportsPanel(){
        this.imports();
        this.init();
    }

    private void init(){
        this.importsArea.setLineWrap(true);
        this.importsArea.setEditable(false);
        this.add(this.importsButton);
        JPanel filler = new JPanel();
        filler.setMaximumSize(Gui.row);
        this.add(filler);
        this.add(importsScroll);
    }

    private void imports() {
        String[] importLabelStrings = new String[] { "Zimmer: ", "Personenbezogene Daten der Schüler:innen: ",
                "Gruppen: ", "Zimmerwünsche" };

        for (String s : importLabelStrings) {
            SubPanel i = new SubPanel(s);
            this.importsPanels.add(i);
            this.add(i);
            JPanel filler = new JPanel();
            filler.setMaximumSize(Gui.row);
            this.add(filler);
        }

        this.importsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                // clean
                importsArea.setText("");
                Log.clear();
                ImportFiles.clear();

                for (int i = 0; i < 4; i++) {
                    byte fileFound = 0;
                    try {
                        if (i == 0) {
                            if (ImportFiles.importBuildings(new File(importsPanels.get(i).field.getText())) != null) {
                                fileFound = 1;
                            }
                        } else if (i == 1) {
                            if (ImportFiles.importStudents(new File(importsPanels.get(i).field.getText())) != null) {
                                fileFound = 1;
                            }
                        } else if (i == 2) {
                            if (ImportFiles.importTeams(new File(importsPanels.get(i).field.getText())) != null) {
                                fileFound = 1;
                            }
                        } else if (i == 3) {
                            if (ImportFiles.importWishes(new File(importsPanels.get(i).field.getText())) != null) {
                                fileFound = 1;
                            }
                        }
                    } catch (Exception e) {
                        fileFound = -1;
                        importsPanels.get(i).field.setBackground(Color.red);
                        e.printStackTrace();
                        importsArea.append("\n" + e.toString());
                    }
                    if (fileFound == 1) {
                        importsPanels.get(i).field.setBackground(Color.green);
                    }
                }
                importsArea.append(Log.log());
            }
        });
    }

}

