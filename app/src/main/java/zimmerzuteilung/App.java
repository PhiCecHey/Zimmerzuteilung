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

        String hallo = "h,a,ll,,o";
        String[] split = hallo.split(",");

        for (String s : split) {
            System.out.println(">" + s + "<");
        }
        try {
            // Gurobi.calculate(constraints, rooms, teams);

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
