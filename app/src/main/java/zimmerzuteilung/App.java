package zimmerzuteilung;

import java.io.File;

import com.formdev.flatlaf.FlatLightLaf;

import zimmerzuteilung.GUI.Gui;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        // theme
        FlatLightLaf.setup();
        com.formdev.flatlaf.intellijthemes.FlatCyanLightIJTheme.setup();
        Gui.init();
        //programm();

    }

    public static void programm() {
        File zimmer = new File("app/files/Internatszimmer.csv");
        File gruppen = new File("app/files/gruppen.csv");
        File zimmerwahl = new File("app/files/Zimmerwahl.csv");
        File persDaten = new File("app/files/persDaten.csv");
        
    }
}
