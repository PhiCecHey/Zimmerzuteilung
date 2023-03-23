package zimmerzuteilung.GUI;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class HelpPanel extends JPanel{
    private JTextArea helpText;
    private JScrollPane helpScroll;
    private JButton plus;
    private JButton minus;

    public HelpPanel(){
        this.init();
        this.addActionListeners();
    }
    
    private void init(){
        this.helpText = new JTextArea();
        this.helpText.setEditable(false);

        this.helpScroll = new JScrollPane(helpText);
        this.add(this.helpScroll);

        this.plus = new JButton("+");
        this.minus = new JButton("-");
        this.add(new GroupPanel(new Component[]{this.plus, this.minus}, "row"));
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
