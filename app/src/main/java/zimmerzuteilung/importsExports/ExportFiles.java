package zimmerzuteilung.importsExports;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import zimmerzuteilung.algorithms.Gurobi;
import zimmerzuteilung.objects.Building;
import zimmerzuteilung.objects.Room;
import zimmerzuteilung.objects.Team;
import zimmerzuteilung.objects.Wish;

public class ExportFiles {
    // private static ArrayList<Building> buildings = ImportFiles.buildings();

    public static boolean eportToCsv(File file, String deliminator) {
        // one row represents one team
        String toWrite = "Gruppenname" + deliminator + " " + "Zimmermitbewohner" + deliminator + " "
                + "Zugeteiltes Zimmer" + deliminator + " " + "Score (hoeher ist besser)" + deliminator + " "
                + "Erstwunschinternat" + deliminator + " " + "Erstwunschzimmer" + deliminator + " "
                + "Zweitwunschzimmer" + deliminator + " " + "Zweitwunschinternat" + deliminator + " " + "Kommentar\n";

        for (Team team : ImportFiles.teams()) {
            String comment = "";

            Building b1 = null;
            Building b2 = null;
            Room r1 = null;
            Room r2 = null;
            ArrayList<Room> allocatedRooms = team.allocatedRooms();
            Wish wish = team.wish();

            toWrite += team.name() + deliminator + " " + team.membersToCsv() + deliminator + " ";

            if (allocatedRooms == null) {
                System.out.println("Team " + team.name() + " wurde kein Zimmer zugewiesen!");
                comment += "Team " + team.name() + " wurde kein Zimmer zugewiesen! ";
                toWrite += "-" + deliminator + " " + "-,";
            } else {
                for (Room room : team.allocatedRooms()) {
                    toWrite += room.officialRoomNumber() + "&";
                }
                toWrite = toWrite.substring(0, toWrite.length() - 1);
                toWrite += deliminator + " " + team.score() + deliminator + " ";
            }
            if (wish == null) {
                System.out.println("Team " + team.name() + " hat keinen Zimmerwunsch abgegeben!");
                comment += "Team " + team.name() + " hat keinen Zimmerwunsch abgegeben! ";
                toWrite += "-" + deliminator + " " + "";
            } else {
                b1 = wish.building1();
                b2 = wish.building2();
                r1 = wish.room1();
                r2 = wish.room2();
            }
            if (b1 == null) {
                System.out.println("Team " + team.name() + " hat kein Erstwunschinternat angegeben!");
                comment += "Team " + team.name() + " hat kein Erstwunschinternat angegeben! ";
                toWrite += "-" + deliminator + " " + "";
            } else {
                toWrite += b1.name() + deliminator + " ";
            }
            if (r1 == null) {
                System.out.println("Team " + team.name() + " hat kein Erstwunschzimmer angegeben!");
                comment += "Team " + team.name() + " hat kein Erstwunschzimmer angegeben! ";
                toWrite += "-" + deliminator + " " + "";
            } else {
                toWrite += r1.officialRoomNumber() + deliminator + " ";
            }
            if (r2 == null) {
                System.out.println("Team " + team.name() + " hat kein Zweitwunschzimmer angegeben!");
                comment += "Team " + team.name() + " hat kein Zweitwunschzimmer angegeben! ";
                toWrite += "-" + deliminator + " " + "";
            } else {
                toWrite += r2.officialRoomNumber() + deliminator + " ";
            }
            if (b2 == null) {
                System.out.println("Team " + team.name() + " hat kein Zweitwunschinternat angegeben!");
                comment += "Team " + team.name() + " hat kein Zweitwunschinternat angegeben! ";
                toWrite += "-" + deliminator + " " + "";
            } else {
                toWrite += b2.name() + deliminator + " ";
            }
            toWrite += comment + "\n";
        }

        toWrite += "\n\nTeams ohne Zimmer:" + deliminator;
        for (Team team : Gurobi.unallocatedTeams) {
            toWrite += "~ " + team.name();
        }
        toWrite += "\nFreie Zimmer:" + deliminator;
        for (Room room : Gurobi.unoccupiedRooms) {
            toWrite += "~ " + room.officialRoomNumber();
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
