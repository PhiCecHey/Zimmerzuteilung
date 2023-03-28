package zimmerzuteilung.GUI;

import java.awt.Dimension;

import javax.swing.JPanel;

class Filler extends JPanel {
    Filler(int x, int y) {
        this.setSize(new Dimension(x, y));
        //this.setPreferredSize(new Dimension((int) x, (int) y));
        this.setMinimumSize(new Dimension(x, y));
        this.setMaximumSize(new Dimension(x, y));
    }

    Filler(float x, float y) {
        this.setSize(new Dimension((int) x, (int) y));
        //this.setPreferredSize(new Dimension((int) x, (int) y));
        this.setMinimumSize(new Dimension((int) x, (int) y));
        this.setMaximumSize(new Dimension((int) x, (int) y));
    }

    Filler(double x, double y) {
        this.setSize(new Dimension((int) x, (int) y));
        //this.setPreferredSize(new Dimension((int) x, (int) y));
        this.setMinimumSize(new Dimension((int) x, (int) y));
        this.setMaximumSize(new Dimension((int) x, (int) y));
    }
}
