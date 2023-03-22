package zimmerzuteilung.importsExports;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import zimmerzuteilung.objects.Building;
import zimmerzuteilung.objects.Room;
import zimmerzuteilung.objects.Student;
import zimmerzuteilung.objects.Team;
import zimmerzuteilung.objects.Wish;

public class ExportFiles {

    private static ArrayList<Building> buildings = ImportFiles.buildings;
    private static ArrayList<Student> students = ImportFiles.students;
    private static ArrayList<Team> teams = ImportFiles.teams;

    public static boolean eportToCsv(File file) {
        // one row represents one team
        String toWrite = "Gruppenname, Zimmermitbewohner, Zugeteiltes Zimmer, Score (höher ist besser), Erstwunschinternat, Erstwunschzimmer, Zweitwunschzimmer, Zweitwunschinternat, Kommentar\n";

        for (Team team : ExportFiles.teams) {
            String comment = "";

            Building b1 = null;
            Building b2 = null;
            Room r1 = null;
            Room r2 = null;
            Room allocatedRoom = team.allocatedRoom();
            Wish wish = team.wish();

            toWrite += team.name() + ", " + team.membersToCsv() + ", ";

            if (allocatedRoom == null) {
                System.out.println("Team " + team.name() + " wurde kein Zimmer zugewiesen!");
                comment += "Team " + team.name() + " wurde kein Zimmer zugewiesen! ";
                toWrite += "-, -,";
            } else {
                toWrite += team.allocatedRoom().officialRoomNumber()
                        + ", " + team.score() + ", ";
            }
            if (wish == null) {
                System.out.println("Team " + team.name() + " hat keinen Zimmerwunsch abgegeben!");
                comment += "Team " + team.name() + " hat keinen Zimmerwunsch abgegeben!";
                toWrite += "-, ";
            } else {
                b1 = wish.building1();
                b2 = wish.building2();
                r1 = wish.room1();
                r2 = wish.room2();
            }
            if (b1 == null) {
                System.out.println("Team " + team.name() + " hat kein Erstwunschinternat angegeben!");
                comment += "Team " + team.name() + " hat kein Erstwunschinternat angegeben!";
                toWrite += "-, ";
            } else {
                toWrite += b1.name() + ", ";
            }
            if (r1 == null) {
                System.out.println("Team " + team.name() + " hat kein Erstwunschzimmer angegeben!");
                comment += "Team " + team.name() + " hat kein Erstwunschzimmer angegeben!";
                toWrite += "-, ";
            } else {
                toWrite += r1.officialRoomNumber() + ", ";
            }
            if (r2 == null) {
                System.out.println("Team " + team.name() + " hat kein Zweitwunschzimmer angegeben!");
                comment += "Team " + team.name() + " hat kein Zweitwunschzimmer angegeben!";
                toWrite += "-, ";
            } else {
                toWrite += r2.officialRoomNumber() + ", ";
            }
            if (b2 == null) {
                System.out.println("Team " + team.name() + " hat kein Zweitwunschinternat angegeben!");
                comment += "Team " + team.name() + " hat kein Zweitwunschinternat angegeben!";
                toWrite += "-, ";
            } else {
                toWrite += b2.name() + ", ";
            }
            toWrite += comment + "\n";
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write(toWrite);
        } catch (IOException e) {
            System.out.println("Problem in function exportToCsv()");
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
