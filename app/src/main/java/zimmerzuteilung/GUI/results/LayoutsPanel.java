package zimmerzuteilung.GUI.results;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class LayoutsPanel extends JPanel{
    public JTextArea resultArea = new JTextArea();
    private JScrollPane resultScroll = new JScrollPane(resultArea);
    private JButton resultButton = new JButton("Ergebnis berechnen");


}
