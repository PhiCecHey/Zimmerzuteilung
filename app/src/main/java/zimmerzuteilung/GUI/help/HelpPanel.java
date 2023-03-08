package zimmerzuteilung.GUI.help;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import zimmerzuteilung.GUI.Gui;

public class HelpPanel extends JPanel{
    private JTextArea helpText = new JTextArea();
    private JScrollPane helpScroll = new JScrollPane(helpText);
    private JPanel zoom = new JPanel();
    private JButton plus = new JButton("+");
    private JButton minus = new JButton("-");

    public HelpPanel(){
        this.init();
        this.addActionListeners();
    }
    
    private void init(){
        this.helpText.setEditable(false);
        this.add(this.helpScroll);
        this.zoom.setLayout(new BoxLayout(this.zoom, BoxLayout.LINE_AXIS));
        this.add(this.zoom);
        this.zoom.add(this.minus);
        this.zoom.add(this.plus);
    }

    private void addActionListeners() {
        // TODO
        this.helpText.append("Informationen und Hilfestellungen über das Programm");
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
    }
}
