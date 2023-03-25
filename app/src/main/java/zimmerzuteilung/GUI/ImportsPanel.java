package zimmerzuteilung.GUI;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import zimmerzuteilung.Exceptions.WarningException;
import zimmerzuteilung.importsExports.ImportFiles;
import zimmerzuteilung.log.Log;

public class ImportsPanel extends JPanel {
    private ArrayList<ChooseFilePanel> importsPanels = new ArrayList<>();
    private JButton importsButton;
    public JTextArea importsArea;
    private JScrollPane importsScroll;

    public ImportsPanel() {
        this.imports();
        this.init();
        this.importButton();
    }

    private void init() {
        this.importsArea = new JTextArea();
        this.importsArea.setLineWrap(true);
        this.importsArea.setEditable(false);

        this.importsButton = new JButton("Dateien einlesen");
        this.add(this.importsButton);

        this.add(new Filler(Gui.row.width, Gui.row.height));

        this.importsScroll = new JScrollPane(importsArea);
        this.add(importsScroll);
    }

    private void imports() {
        String[] importLabelStrings = new String[] { "Zimmer: ", "Personenbezogene Daten der Schüler:innen: ",
                "Gruppen: ", "Zimmerwünsche" };

        for (String s : importLabelStrings) {
            ChooseFilePanel i = new ChooseFilePanel(s, "file");
            ImportsPanel.checkForFile(i.field);

            this.importsPanels.add(i);
            this.add(i);
            this.add(new Filler(Gui.row.width, Gui.row.height));
        }

        // TODO: remove following
        this.importsPanels.get(0).field.setText("/home/philine/Documents/Link to files/Internatszimmer.csv");
        this.importsPanels.get(1).field.setText("/home/philine/Documents/Link to files/persDaten2.csv");
        this.importsPanels.get(2).field.setText("/home/philine/Documents/Link to files/gruppen2.csv");
        this.importsPanels.get(3).field.setText("/home/philine/Documents/Link to files/Zimmerwahl2.csv");
    }

    private void importButton() {
        this.importsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                // clean
                importsArea.setText("");
                Log.clear();
                ImportFiles.clear();

                int i = 0;
                try {
                    if (!ImportFiles.importBuildings(new File(importsPanels.get(i).field.getText()))) {
                        importsPanels.get(0).field.setBackground(Colors.yellowTransp);
                    } else {
                        importsPanels.get(0).field.setBackground(Colors.greenTransp);
                    }
                    i = 1;

                    if (!ImportFiles.importStudents(new File(importsPanels.get(i).field.getText()))) {
                        importsPanels.get(1).field.setBackground(Colors.yellowTransp);
                    } else {
                        importsPanels.get(1).field.setBackground(Colors.greenTransp);
                    }
                    i = 2;

                    if (!ImportFiles.importTeams(new File(importsPanels.get(i).field.getText()))) {
                        importsPanels.get(2).field.setBackground(Colors.yellowTransp);
                    } else {
                        importsPanels.get(2).field.setBackground(Colors.greenTransp);
                    }
                    i = 3;

                    if (!ImportFiles.importWishes(new File(importsPanels.get(i).field.getText()))) {
                        importsPanels.get(3).field.setBackground(Colors.yellowTransp);
                    } else {
                        importsPanels.get(3).field.setBackground(Colors.greenTransp);
                    }
                    i = 4;
                } catch (Exception e) {
                    importsPanels.get(i).field.setBackground(Colors.redTransp);
                    e.printStackTrace();
                }
                importsArea.append(Log.log());
            }
        });
    }

    public static void checkForFile(JTextField textField) {
        // Listen for changes in the text
        textField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                warn();
            }

            public void removeUpdate(DocumentEvent e) {
                warn();
            }

            public void insertUpdate(DocumentEvent e) {
                warn();
            }

            public void warn() {
                File f = new File(textField.getText());
                if (f.exists() && !f.isDirectory()) {
                    textField.setBackground(Colors.blueTransp);
                } else {
                    textField.setBackground(Colors.redTransp);
                }
            }
        });
    }
}
