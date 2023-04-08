package zimmerzuteilung.GUI;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class HelpPanel extends JPanel {
    private JTextArea helpText;
    private JScrollPane helpScroll;
    private JButton plus, minus, zero;

    public HelpPanel() {
        this.init();
        this.addActionListeners();
    }

    private void init() {
        this.helpText = new JTextArea();
        this.helpText.setEditable(false);

        this.helpScroll = new JScrollPane(helpText);
        this.add(this.helpScroll);

        this.plus = new JButton("+");
        this.minus = new JButton("-");
        this.zero = new JButton("o");
        this.add(new GroupPanel(new Component[] { this.minus, this.zero, this.plus }, "row"));
    }

    private void addActionListeners() {
        // TODO
        this.helpText.setLineWrap(true);
        this.helpText.setWrapStyleWord(true);
        this.helpText.append("Informationen und Hilfestellungen\n\n");

        this.helpText.append("Dieses Programm soll die jährliche Vergabe der Internatszimmer der Landesschule Pforta unterstützen. Es benötigt die Umfrageergebnisse der dafür vorgesehenen Moodleumfragen sowie eine gültige Gurobi Optimizer Lizenz und eine funktionstüchtige Gurobi Optimizer Installation der Version 10.0.1. \n\nAlle einzulesenden Dateien müssen im CSV Dateiformat abgespeichert sein. Dabei werden Zellen mit Kommas und Zeilen mit Zeilenumbrüche voneinander getrennt. Eine CSV Datei können Sie z. B. mithilfe von Microsoft Excel erstellen. \nZuerst werden die nötigen Dateien eingelesen. Die erste Datei beinhaltet eine Liste aller Zimmer der Landesschule Pforta sowie ein paar weitere Informationen zu diesen Zimmern. Eine entsprechende Vorlage befindet sich unten auf dieser Seite. Die zweite Datei beinhaltet die Ergebnisse der Moodle Umfrage \"Personenbezogene Daten der Schueler:innen\". Bitte achten Sie beim Download darauf, bei \"Optionen für das Herunterladen (CSV)\" alle Haken zu entfernen: \"Auswahlcode einbeziehen\", \"Auswahltext einbeziehen\", \"unvollständige Durchlaeufe einbeziehen\", \"Durchschnittswerte fuer Rangfragen einbeziehen\" sollen nicht markiert sein. Die dritte Datei beinhaltet die Ergebnisse der Moodle Umfrage \"Zimmerpartner\". Zum Herunterladen klicken Sie auf \"Einen Downloadlink für eine Gruppendaten-Datei erzeugen\" und danach auf \"CSV-Datei herunterladen\". \nDie vierte Datei beinhaltet die Ergebnisse der Moodle Umfrage \"Zimmerwunsch\". Bitte achten Sie beim Herunterladen wieder darauf, bei \"Optionen fuer das Herunterladen (CSV)\" alle Haken zu entfernen. Wenn Sie alle Dateien ausgewählt haben, klicken Sie auf \"Dateien einlesen\". Sofern die Dateien das richtige Format und den erwarteten Aufbau aufweisen sowie alle Schueler:innen die Umfragen korrekt ausgefuellt haben, sollten sich alle Textfelder gruen faerben. Falls dies nicht der Fall ist, wird sich das zur fehlerhaften Datei zugehoerige Textfeld rot faerben und ein Fehlertext erscheinen, der das Problem erlaeutert. Ein Textfeld wird orange gefaeurbt, falls nicht alle Umfragen vollstaendig ausgefuellt wurden. Dies ist - im Gegensatz zur roten Faerbung - nicht als kritischen Fehler zu verstehen sondern als Warnung, wodurch die Ergebnisse des Programms sich auf die betroffenen Schueler:innen negativ auswirken kann. \n\n");

        this.plus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                Gui.changeFont(Gui.mainFrame, Gui.mainFrame.getFont().getSize() + 1);
            }
        });

        this.minus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                Gui.changeFont(Gui.mainFrame, Gui.mainFrame.getFont().getSize() - 1);
            }
        });

        this.zero.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                Gui.changeFont(Gui.mainFrame, 22);
            }
        });
    }
}
