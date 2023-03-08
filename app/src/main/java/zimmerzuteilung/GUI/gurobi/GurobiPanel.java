package zimmerzuteilung.GUI.gurobi;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GurobiPanel extends JPanel {
    public class SubPanel extends JPanel {
        JLabel label;
        JCheckBox box;

        SubPanel(String labelText, boolean enabled) {
            this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
            this.setMaximumSize(new Dimension(2000, 50));
            this.box = new JCheckBox();
            this.box.setSelected(true);
            this.box.setEnabled(enabled);
            this.add(box);
            this.label = new JLabel(labelText);
            this.add(label);
        }
    }

    private ArrayList<SubPanel> gurobiComponents = new ArrayList<>();

    private void gurobi() {
        this.gurobiComponents.add(new SubPanel("Ein Zimmer pro Team", false));
        this.gurobiComponents.add(new SubPanel("Ein Team pro Zimmer", false));
        this.gurobiComponents.add(new SubPanel("maximale Anzahl an Schüler:innen pro Zimmer einhalten", false));
        this.gurobiComponents.add(new SubPanel("Zimmerwünsche respektieren", true));
        this.gurobiComponents.add(new SubPanel("Zimmerreservierungen respektieren", true));
        this.gurobiComponents.add(new SubPanel("12er, 11er, 10er Privileg respektieren", true));
        

        for (SubPanel g : this.gurobiComponents) {
            this.add(g);
        }
    }
}
