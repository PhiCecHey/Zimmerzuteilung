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
        this.helpText.append("Informationen und Hilfestellungen\n\n");

        this.helpText.append(""
                + "Dieses Programm soll die jährliche Vergabe der Internatszimmer der Landesschule Pforta unterstützen.\n"
                + "Es benötigt die Umfrageergebnisse der dafür vorgesehenen Moodleumfragen sowie eine gültige Gurobi\n"
                + "Optimizer Lizenz und eine funktionstüchtige Gurobi Optimizer Installation der Version 10.0.1.\n\n"
                + "Alle einzulesenden Dateien müssen im CSV Dateiformat abgespeichert sein. Dabei werden Zellen mit\n"
                + "Kommas und Zeilen mit Zeilenumbrüche voneinander getrennt. Eine CSV Datei können Sie z. B. mithilfe\n"
                + "von Microsoft Excel erstellen.\n"
                + "Zuerst werden die nötigen Dateien eingelesen. Die erste Datei beinhaltet eine Liste aller Zimmer der\n"
                + "Landesschule Pforta sowie ein paar weitere Informationen zu diesen Zimmern. Eine entsprechende\n"
                + "Vorlage befindet sich unten auf dieser Seite.\n"
                + "Die zweite Datei beinhaltet die Ergebnisse der Moodle Umfrage \"Personenbezogene Daten der\n"
                + "Schueler:innen\". Bitte achten Sie beim Download darauf, bei \"Optionen für das Herunterladen (CSV)\"\n"
                + "alle Haken zu entfernen: \"Auswahlcode einbeziehen\", \"Auswahltext einbeziehen\", \"unvollständige\n"
                + "Durchlaeufe einbeziehen\", \"Durchschnittswerte fuer Rangfragen einbeziehen\" sollen nicht markiert sein.\n"
                + "Die dritte Datei beinhaltet die Ergebnisse der Moodle Umfrage \"Zimmerpartner\". Zum Herunterladen\n"
                + "klicken Sie auf \"Einen Downloadlink für eine Gruppendaten-Datei erzeugen\" und danach auf \"CSV-Datei\n"
                + "herunterladen\".\n"
                + "Die vierte Datei beinhaltet die Ergebnisse der Moodle Umfrage \"Zimmerwunsch\". Bitte achten Sie beim\n"
                + "Herunterladen wieder darauf, bei \"Optionen fuer das Herunterladen (CSV)\" alle Haken zu entfernen.\n"
                + "Wenn Sie alle Dateien ausgewählt haben, klicken Sie auf \"Dateien einlesen\". Sofern die Dateien das\n"
                + "richtige Format und den erwarteten Aufbau aufweisen sowie alle Schueler:innen die Umfragen korrekt\n"
                + "ausgefuellt haben, sollten sich alle Textfelder gruen faerben. Falls dies nicht der Fall ist, wird sich\n"
                + "das zur fehlerhaften Datei zugehoerige Textfeld rot faerben und ein Fehlertext erscheinen, der\n"
                + "das Problem erlaeutert. Ein Textfeld wird orange gefaeurbt, falls nicht alle Umfragen vollstaendig\n"
                + "ausgefuellt wurden. Dies ist - im Gegensatz zur roten Faerbung - nicht als kritischen Fehler zu verstehen\n"
                + "sondern als Warnung, wodurch die Ergebnisse des Programms sich auf die betroffenen Schueler:innen\n"
                + "negativ auswirken kann.\n\n"
                + "");

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
