package zimmerzuteilung.GUI;

import java.io.File;
import java.util.Iterator;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class CheckUserInput {
    public static void check(JTextField textField) {
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

    public static void checkForFloat(JTextField textField) {
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
                float value;
                boolean worked = true;
                try {
                    value = Float.parseFloat(textField.getText());
                } catch (NumberFormatException e) {
                    worked = false;
                }
                if (worked) {
                    textField.setBackground(Colors.blueTransp);
                } else {
                    textField.setBackground(Colors.redTransp);
                }
            }
        });
    }

    public static void checkForPositive(JTextField textField) {
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
        });
    }

    public static void checkForNegative(JTextField textField) {
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
        });
    }
}
