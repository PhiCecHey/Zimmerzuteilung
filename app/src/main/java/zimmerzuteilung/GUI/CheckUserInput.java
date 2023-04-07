package zimmerzuteilung.GUI;

import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class CheckUserInput {

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
                    if (!textField.getText().endsWith(".csv")) {
                        textField.setBackground(Colors.yellowTransp);
                    } else {
                        textField.setBackground(Colors.blueTransp);
                    }
                } else {
                    textField.setBackground(Colors.redTransp);
                }
            }
        });
    }

    public static void checkForFloat(JCheckBox box, JTextField textField) {
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
                if (box.isSelected()) {
                    boolean worked = true;
                    try {
                        Float.parseFloat(textField.getText());
                    } catch (NumberFormatException e) {
                        worked = false;
                    }
                    if (worked) {
                        textField.setBackground(Colors.blueTransp);
                    } else {
                        textField.setBackground(Colors.redTransp);
                    }
                }
            }
        });
    }

    public static void checkForPositive(JCheckBox box, JTextField textField) {
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
                if (box.isSelected()) {
                    float value = -1;
                    boolean worked = true;
                    try {
                        value = Float.parseFloat(textField.getText());
                    } catch (NumberFormatException e) {
                        worked = false;
                    }
                    if (worked) {
                        if (value >= 0) {
                            textField.setBackground(Colors.blueTransp);
                        } else {
                            textField.setBackground(Colors.yellowTransp);
                        }
                    } else {
                        textField.setBackground(Colors.redTransp);
                    }
                }
            }
        });
    }

    public static void checkForNegative(JCheckBox box, JTextField textField) {
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
                if (box.isSelected()) {
                    float value = 1;
                    boolean worked = true;
                    try {
                        value = Float.parseFloat(textField.getText());
                    } catch (NumberFormatException e) {
                        worked = false;
                    }
                    if (worked) {
                        if (value < 0) {
                            textField.setBackground(Colors.blueTransp);
                        } else {
                            textField.setBackground(Colors.yellowTransp);
                        }
                    } else {
                        textField.setBackground(Colors.redTransp);
                    }
                }
            }
        });
    }

    public static void checkForNegative(JRadioButton box, JTextField textField) {
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
                if (box.isSelected()) {
                    float value = 1;
                    boolean worked = true;
                    try {
                        value = Float.parseFloat(textField.getText());
                    } catch (NumberFormatException e) {
                        worked = false;
                    }
                    if (worked) {
                        if (value < 0) {
                            textField.setBackground(Colors.blueTransp);
                        } else {
                            textField.setBackground(Colors.yellowTransp);
                        }
                    } else {
                        textField.setBackground(Colors.redTransp);
                    }
                }
            }
        });
    }

    public static void checkSelected(JCheckBox box, JTextField[] fields) {
        box.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent arg0) {
                if (box.isSelected()) {
                    for (JTextField field : fields) {
                        field.setBackground(field.getForeground());
                        field.setForeground(Color.BLACK);
                        field.setEditable(true);
                    }
                } else {
                    for (JTextField field : fields) {
                        field.setForeground(field.getBackground());
                        field.setBackground(Colors.greyTransp);
                        field.setEditable(false);
                    }
                }
            }
        });
    }

    public static void checkSelected(JCheckBox box, JTextField[] fields, String FloatPosNeg) {
        box.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent arg0) {
                if (box.isSelected()) {
                    for (JTextField field : fields) {
                        field.setEditable(true);
                        field.setBackground(Colors.blueTransp);
                        if (FloatPosNeg.toLowerCase().contains("neg")) {
                            CheckUserInput.checkForNegative(box, field);
                        } else if (FloatPosNeg.toLowerCase().contains("pos")) {
                            CheckUserInput.checkForPositive(box, field);
                        } else {
                            CheckUserInput.checkForFloat(box, field);
                        }
                    }
                } else {
                    for (JTextField field : fields) {
                        field.setBackground(Colors.greyTransp);
                        field.setEditable(false);
                    }
                }
            }
        });
    }

    static void checkSelected(JRadioButton radio, JTextField field) {
        radio.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent arg0) {
                if (radio.isSelected()) {
                    field.setBackground(Color.WHITE);
                    field.setEditable(true);
                } else {
                    field.setEditable(false);
                    field.setBackground(Colors.greyTransp);
                }
            }

        });
    }
}
