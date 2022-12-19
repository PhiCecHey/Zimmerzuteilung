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
        try {

            ImportFileBak.importBuildings("app/files");
            ImportFileBak.importRooms(",", true);

            ArrayList<Gurobi.RULES> constraints = new ArrayList<>();
            constraints.add(Gurobi.RULES.maxStudentsPerRoom);
            constraints.add(Gurobi.RULES.oneRoomPerTeam);
            constraints.add(Gurobi.RULES.oneTeamPerRoom);
            constraints.add(Gurobi.RULES.respectWish);

            ArrayList<Team> teams = new ArrayList<>();

            Student s1 = new Student("Anna T1", GENDER.f);
            Student s2 = new Student("Berta T1", GENDER.f);
            Student s3 = new Student("Clara T1", GENDER.f);
            ArrayList<Student> a1 = new ArrayList<>();
            a1.add(s1);
            a1.add(s2);
            a1.add(s3);
            Team t1 = new Team(a1);

            Student s4 = new Student("David T2", GENDER.m);
            Student s5 = new Student("Emanual T2", GENDER.m);
            Student s6 = new Student("Ferdinand T2", GENDER.m);
            ArrayList<Student> a2 = new ArrayList<>();
            a2.add(s4);
            a2.add(s5);
            a2.add(s6);
            Team t2 = new Team(a2);

            teams.add(t1);
            teams.add(t2);

            ArrayList<Room> rooms = new ArrayList<>();
            Building b1 = new Building();
            Building b2 = new Building();
            Room r1 = new Room(b1, 4);
            Room r2 = new Room(b1, 4);
            Room r3 = new Room(b2, 4);
            Room r4 = new Room(b2, 4);
            rooms.add(r1);
            rooms.add(r2);
            rooms.add(r3);
            rooms.add(r4);

            t1.wish(new Wish(b1, r2, r1, b2));
            t2.wish(new Wish(b1, r2, r1, b2));

            // System.out.println(t1.toString());
            // System.out.println(t2.toString());
            // System.out.println("\n######################################################################################################\n");

            Gurobi.calculate(constraints, rooms, teams);

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
