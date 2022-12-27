package zimmerzuteilung;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import zimmerzuteilung.algorithms.*;
import zimmerzuteilung.imports.*;
import zimmerzuteilung.objects.*;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        File file = new File("app/files/alle.csv");
        try {
            ArrayList<Building> b = ImportFiles.importBuildings(file);
            int a = 3;
        } catch (Exception e) {
            System.out.println("Working Directory = " + System.getProperty("user.dir"));
            System.out.println(file.getAbsolutePath());
            e.printStackTrace();
        }
    }
}
