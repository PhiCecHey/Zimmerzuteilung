package zimmerzuteilung.GUI;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

public class Gui {    
    private static Dimension window = new Dimension(1920, 1080);
    public static Dimension row = new Dimension(1920, 40);

    public static JFrame mainFrame = new JFrame("Landesschule Pforte Zimmerzuteilung");
    public static JTabbedPane tabs = new JTabbedPane();
    public static HelpPanel help = new HelpPanel();
    public static ImportsPanel imports = new ImportsPanel();
    public static GurobiPanel gurobi = new GurobiPanel();
    public static ResultsPanel result = new ResultsPanel();

    public static void changeFont(Component component, int size) {
        Font font = Gui.mainFrame.getFont();
        font = new Font(font.getName(), font.getStyle(), size);
        component.setFont(font);
        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                changeFont(child, size);
            }
        }
    }

    public static void init() {
        Gui.mainFrame.setSize(window);
        Gui.mainFrame.setVisible(true);
        Gui.mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Gui.mainFrame.setContentPane(tabs);
        Gui.tabs.addTab("Informationen", Gui.help);
        Gui.tabs.addTab("Dateien einlesen", imports);
        Gui.tabs.addTab("Gurobi", gurobi);
        Gui.tabs.addTab("Ergebnis", result);

        // layouts
        Gui.help.setLayout(new BoxLayout(help, BoxLayout.PAGE_AXIS));
        gurobi.setLayout(new BoxLayout(gurobi, BoxLayout.PAGE_AXIS));
        imports.setLayout(new BoxLayout(imports, BoxLayout.PAGE_AXIS));
        result.setLayout(new BoxLayout(result, BoxLayout.PAGE_AXIS));

        Gui.changeFont(mainFrame, 20);
    }
}
