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

        this.helpText.append("Dieses Programm soll die jährliche Vergabe der Internatszimmer der Landesschule Pforta unterstützen. Es benötigt die Umfrageergebnisse der dafür vorgesehenen Moodleumfragen sowie eine gültige Gurobi Optimizer Lizenz und eine funktionstüchtige Gurobi Optimizer Installation der Version 10.0.1. \n\nAlle einzulesenden Dateien müssen im CSV Dateiformat abgespeichert sein. Dabei werden Zellen mit Kommas und Zeilen mit Zeilenumbrüche voneinander getrennt. Eine CSV Datei können Sie z. B. mithilfe von Microsoft Excel erstellen. \nZuerst werden die nötigen Dateien eingelesen. Die erste Datei beinhaltet eine Liste aller Zimmer der Landesschule Pforta sowie ein paar weitere Informationen zu diesen Zimmern. Eine entsprechende Vorlage befindet sich im Moodleraum. \nDie zweite Datei beinhaltet die Ergebnisse der Moodle Umfrage \"Personenbezogene Daten der Schueler:innen\". Bitte achten Sie beim Download darauf, bei \"Optionen für das Herunterladen (CSV)\" alle Haken zu entfernen: \"Auswahlcode einbeziehen\", \"Auswahltext einbeziehen\", \"unvollständige Durchlaeufe einbeziehen\", \"Durchschnittswerte fuer Rangfragen einbeziehen\" sollen nicht markiert sein. \nDie dritte Datei beinhaltet die Ergebnisse der Moodle Umfrage \"Zimmerpartner\". Zum Herunterladen klicken Sie auf \"Einen Downloadlink für eine Gruppendaten-Datei erzeugen\" und danach auf \"CSV-Datei herunterladen\". \nDie vierte Datei beinhaltet die Ergebnisse der Moodle Umfrage \"Zimmerwunsch\". Bitte achten Sie beim Herunterladen wieder darauf, bei \"Optionen fuer das Herunterladen (CSV)\" alle Haken zu entfernen. \nWenn Sie alle Dateien ausgewählt haben, klicken Sie auf \"Dateien einlesen\". Sofern die Dateien das richtige Format und den erwarteten Aufbau aufweisen und es zu keinen unerwarteten Fehlern kommt, faerben sich die Textfelder gruen. Falls dies nicht der Fall ist, z. B. weil die Datei nicht gefunden werden konnte, wird sich das zur fehlerhaften Datei zugehoerige Textfeld rot faerben und ein Fehlertext erscheinen, der das Problem erlaeutert. Ein Textfeld wird orange gefaerbt, falls Schueler:innen Umfragen nicht richtig oder vollstaendig ausgefuellt haben. Dies ist - im Gegensatz zur roten Faerbung - nicht als kritischen Fehler zu verstehen sondern als Warnung. Nicht vernuenftig ausgefuellte Umfragen fuehren zu einer suboptimalen Berechnung fuer das entsprechende Team, da durch falsches oder unvollstaendiges Ausfuellen manche Boni nicht beruecksichtigt werden koennen. \n\n"
        + "Nach dem Einlesen der Dateien haben Sie die Möglichkeit, Parameter anzupassen, die zu einem anderen Ergebnis fuehren koennen. Um diese Parameter genauer verstehen zu koennen, wird nun die Funktionsweise des Programms erlaeutert. \nZwei wichtige, intern verwendete Datenstrukturen, die stark mit der Berechnung der Ergebnisse zusammenhaengen, koennen als Tabellen symbolisiert werden. Beide Tabellen gehen in x-Richtung ueber alle Teams und in y-Richtung ueber alle Zimmer. Die erste Tabelle beinhaltet ausschliesslich Nullen und Einsen. Im Gegensatz zu einer 0 bedeutet eine 1, dass das Team dieser Spalte dem Zimmer dieser Zeile zugewiesen wird. Demnach wird eine Zuweisung als ein Team verstanden, das einem bestimmten Zimmer zugeteilt wird. Die zweite Tabelle, die wie die erste ueber alle Teams und Zimmer geht, beinhaltet sowohl positive als auch negative Gleitkommawerte. Ein Wert symbolisiert hier den Score einer moeglichen Zuweisung. Je hoeher dieser Score ist, desto wahrscheinlicher wird es, dass das Team genau diesem Zimmer zugewiesen wird und nicht einem anderen. Wird das Team diesem Zimmer zugewiesen, so steht in der Spalte des Teams und der Zeile des Zimmers in der ersten Tabelle eine 1 und in der zweiten Tabelle ein hoher Score. \nDas Problem der Zuweisung der Schueler:innen an die Zimmer wird hier als Optimierungsproblem betrachtet. Das Ziel ist es, eine moeglichst zufriedenstellende Zuweisung zu finden - die Zufriedenheit aller zu optimieren. Dies geschieht durch sogenannte Constraints - Bedingungen, die erfuellt werden muessen - und Optimierungen, die durch eine intern berechnete Punktzahl bewertet werden. Diese intern berechnete Punktzahl - welche hier als Score bezeichnet wird - gibt an, wie optimal eine Zuweisung ist. Im Tab \"Gurobi\" kann der Score einer Zuweisung durch die Anpassung der Werte fuer die Boni beeinflusst sowie Constraints de-/aktiviert werden. Die Berechnung haengt demnach stark von den Einstellungen ab, die im Tab \"Gurobi\" getroffen werden. \nNun wird die Funktionsweise der Constraints und der Einfluss der Boni auf den Score erlaeutert. Folgende Constraints werden zur Verfuegung gestellt. \"Genau ein Zimmer pro Team\" garantiert, dass ein Team an Schueler:innen, die zusammen in einem Zimmer wohnen werden, genau einem Zimmer zugewiesen werden. Die Alternative waere, dass diesem Schueler:innenteam gar kein oder sogar mehr als ein Zimmer zur Verfuegung gestellt wird anstatt genau eins. \n\"Maximal ein Team pro Zimmer\" hingegen sorgt dafuer, dass nie mehr als ein Team in einem Zimmer wohnt, sondern, insofern dieses Zimmer belegt wird, es mit genau einem Team belegt wird, demnach mit maximal einem. Falls es zu viele Einerteams und dadurch zu wenig Zimmer gibt, koennte es sinnvoll sein, das Constraint \"Maximal ein Team pro Zimmer\" zu deaktivieren, um zu ermoeglichen, dass manche (Einer-)Teams gemeinsam in ein Zimmer ziehen. Es wird empfohlen, diese beiden Constraints stets aktiviert zu lassen und sie nur zu deaktivieren, falls die Konsequenzen klar sind. \nDas Constraint \"Zimmer reservieren - unbedingt reservierte Zimmer freihalten\" garantiert, dass reservierte Zimmer nicht belegt werden. Zimmer koennen in der Datei, die im Tab \"Dateien einlesen\" zuerst eingelesen wird und die Zimmer Schulpfortas definiert, reserviert werden, indem in der Spalte \"Zimmer reservieren\" fuer das entsprechende Zimmer \"Ja\" eingetragen wird. Dies ist z. B. fuer die Zimmer nuetzlich, die fuer die Neuntklaessler freigehalten werden sollen. Die alternative Einstellung \"Zimmer reservieren - Falls moeglich reservierte Zimmer freihalten\" hingegen beeinflusst den Score einer Zuweisung. Der Wert, der in das Textfeld eingetragen wird, wird auf den Score fuer eine Zuweisung addiert. Es wird stark empfohlen, hier einen negativen Wert einzutragen, da dieser negative Bonus - dieser Malus - dafuer sorgt, dass alle moeglichen Zuweisungen, die bedeuten, dass dieses Zimmer nicht freigehalten wird, negativ beeinflusst werden und damit einen niedrigeren Score erhalten. Dies erhoeht die Wahrscheinlichkeit, dass die reservierten Zimmer freigehalten werden, insofern das moeglich ist. Wird das Constraint \"Unbedingt reservierte Zimmer freihalten\" aktiviert und es gibt zu wenig Zimmer bzw. zu viele Teams fuer die Anzahl der Zimmer, so kann der Algorithmus kein Ergebnis berechnen. Falls dem so ist, wird entweder empfohlen, Teams zusammenzulegen, sodass es weniger Teams gibt, bzw. weniger Zimmer zu reservieren, damit mehr Zimmer zur Verfuegung stehen oder das Constraint zu deaktivieren und stattdessen Score zu beeinflussen. \nDas Constraint \"Maximale Anzahl aller Schueler:innen pro Zimmer - Immer maximale Belegung einhalten\" sorgt dafuer, dass nur Teams mit maximal so vielen Schueler:innen, die die Kapazitaet des Zimmers nicht ueberschreiten, in diesem Zimmer wohnen duerfen. Die Alternative deaktiviert dieses Constraint und beeinflusst den Score aller Zuweisungen durch den negativen Bonus negativ, welche diese Regel nicht einhalten. Es wird empfohlen, das Constraint aktiviert zu lassen, ausser es ist geplant, nach der Berechnung Schueler:innen aus einem uebervollen Teams zu entfernen oder weitere Betten in ein Zimmer zu stellen. \nDas Constraint \"Maedchen-/Jungenteams in Maedchen-/Jungenzimmer - Immer einhalten\" sorgt dafuer, dass ausschliesslich ein reines Maedchen- bzw. Jungenteam einem Maedchen- bzw. Jungenzimmer zugeteilt wird. Die Alternative \"Maedchen-/Jungenteams in Maedchen-/Jungenzimmer - Moeglichst einhalten\" hingegen laesst es zu, dass Maedchenteams in Jungenzimmern und Jungenteams in Maedchenzimmern wohnen duerfen. Dies koennte nuetzlich sein, falls die Anzahl der Maedchen-/Jungenteams groesser ist als die Anzahl der Maedchen-/Jungenzimmer und es demnach zu einer Umverteilung kommen muss, damit eine Loesung gefunden werden kann. Ebenfalls sorgt diese Option dafuer, dass Maedchen mit Jungen in einem Zimmer wohnen. Dies ist notwendig, falls z. B. eine Schuelerin die Umfrage zu den Personenbezogenen Daten falsch ausgefuellt und \"männlich\" statt \"weiblich\" angekreuzt hat, sich allerdings in ein Maedchenteam eingetragen hat und es somit \"gemischte\" Teams gibt. \nDie weiteren Einstellungen stellen keine Constraints dar sondern beeinflussen lediglich den Score. Die Option \"Zimmerwuensche respektieren\" sorgt dafuer, dass Zuweisungen, bei welchen ein Team einem seiner Wunschzimmer/-internate zugewiesen wird, einen hoeheren Score erhalten und somit gegenueber anderer moeglicher Zuweisungen bevorzugt wird. Es wird empfohlen, den \"Erstwunschinternat Bonus\" hoeher zu waehlen als den \"Zweitwunschinternat Bonus\" und den \"Erstwunschzimmer Bonus\" hoeher zu waehlen als den \"Zweitwunschzimmer Bonus\", damit die Erstwuensche gegenueber den Zweitwuenschen bevorzugt werden.\n\"12er, 11er, 10er Bonus\" stellt die Bevorzugung dieser Klassenstufen dar. Bspw. wird der 12er Bonus auf jede moegliche Zuweisung addiert, bei der ein Team aus ausschliesslich Zwoelftklaesslern einem seiner Wunschzimmer/-internate zugewiesen wird. Es wird empfohlen, den 12er Bonus am groessten und den 10er Bonus am niedrigsten zu waehlen. \nUm diese Optionen besser verstehen zu koennen, werden sie nun an einem Beispiel erlaeutert.");

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
