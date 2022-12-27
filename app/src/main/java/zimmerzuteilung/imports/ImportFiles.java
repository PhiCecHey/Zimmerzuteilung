package zimmerzuteilung.imports;

import zimmerzuteilung.objects.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class ImportFiles {

    private static ArrayList<Building> buildings = new ArrayList<>();
    private static ArrayList<Student> students = new ArrayList<>();
    private static ArrayList<Team> teams = new ArrayList<>();

    public static File[] getFilesFromFolder(String pathToFolder) {
        File[] fileList = new File[0];
        try {
            File folder = new File(pathToFolder);
            System.out.println("Ordner: " + folder.getAbsolutePath());
            fileList = folder.listFiles();
            if (fileList.length == 0) {
                throw new FileNotFoundException();
            }
            return fileList;
        } catch (FileNotFoundException e) {
            System.out.println("Es wurden keine Dateien in dem Ordner "
                    + pathToFolder + "gefunden.");
            e.printStackTrace();
        }
        return fileList;
    }

    public static String editLine(String line) {
        // remove double quotes and comma
        if (line != null) {
            if (line.length() > 2) {
                if (line.equals("null,") || line.equals("\"\",")) {
                    // no information in survey
                    return "";
                } else {
                    if (line.endsWith(",")) {
                        line = line.substring(0, line.length() - 1);
                    }
                    if (line.startsWith("\"") && line.endsWith("\"")) {
                        line = line.substring(1, line.length() - 1);
                    }
                    line = line.toLowerCase();
                    return line;
                }
            }
        }
        return null;
    }

    /**
     * Compares two moodle time stamps.
     * 
     * @param str1 first moodle time stamp
     * @param str2 second moodle time stamp
     * @return true, if str1 is <= str2
     */
    public static boolean compareTime(String str1, String str2) {
        String[] dateTime1 = new String[6]; // y m d h m s
        String[] dateTime2 = new String[6]; // y m d h m s
        String date1 = str1.split(" ")[0]; // d m y
        String date2 = str2.split(" ")[0]; // d m y
        String time1 = str1.split(" ")[1]; // h m s
        String time2 = str2.split(" ")[1]; // h m s
        dateTime1[0] = date1.split(".")[2]; // y
        dateTime2[0] = date2.split(".")[2]; // y
        dateTime1[1] = date1.split(".")[1]; // m
        dateTime2[1] = date2.split(".")[1]; // m
        dateTime1[2] = date1.split(".")[0]; // d
        dateTime2[2] = date2.split(".")[0]; // d
        dateTime1[3] = time1.split(":")[0]; // h
        dateTime2[3] = time2.split(":")[0]; // h
        dateTime1[4] = time1.split(":")[1]; // m
        dateTime2[4] = time2.split(":")[1]; // m
        dateTime1[5] = time1.split(":")[2]; // s
        dateTime2[5] = time2.split(":")[2]; // s

        for (int i = 0; i < 6; i++) {
            if (Integer.valueOf(dateTime1[i]) != Integer.valueOf(dateTime2[i])) {
                if (Integer.valueOf(dateTime1[i]) > Integer.valueOf(dateTime2[i])) {
                    return false;
                }
            }
        }
        return true;
    }

    // not finished
    public static void importWishes(File json) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(json));
        String line;

        ArrayList<Team> teams = new ArrayList<>();
        Team team = new Team();
        int counter = -1;

        while ((line = reader.readLine().strip()) != null) {
            counter++;
            if (line.startsWith("]")) {
                // end of wish
                ImportFiles.teams.add(team);
                counter = -1;
                continue;
            }
            if (line.length() > 2 && line.charAt(2) == '[') {
                // start of wish
                counter = 0;
                continue;
            } else if (counter > 0) {
                line = ImportFiles.editLine(line);
                if (line == null)
                    continue;
            }
            // ---------------------- get time of survey -----------------------
            if (counter == 2) {
                team.time(line);
            }
            // ------------------------- get team name -------------------------
            else if (counter == 6) {
                for (Team t : teams) {
                    if (team.name().equals(t.name())) {
                        if (ImportFiles.compareTime(t.time(), team.time())) {
                            // team has already filled out survey
                            // remove earlier survey
                            teams.remove(t);
                        }
                    } else {
                        team = new Team();
                    }
                }

            }
            // ------------------------ get team gender ------------------------
            else if (counter == 11) {
                if (line.equals("mädchenzimmer")) {
                    team.gender(GENDER.f);
                } else if (line.contains("jung") && line.contains("zimmer")) {
                    team.gender(GENDER.m);
                } else {
                    team.gender(GENDER.d);
                }
            }
            // --------------------------- get grade ---------------------------
            else if (counter == 12) {
                // TODO is this a good idea? do individual surveys instead?
                if (line.contains("9.")) {

                } else if (line.contains("10.")) {

                } else if (line.contains("11.")) {

                } else if (line.contains("12.")) {

                }
            }
            // ---------------------- get specialization -----------------------
            else if (counter == 13) {
                // TODO is this a good idea? do individual surveys instead?
                if (line.contains("musik")) {

                } else if (line.contains("sprachen")) {

                } else if (line.contains("naturwissenschaften")
                        || line.contains("nawi")) {

                }
            }
            // ------------------------- get building1 -------------------------
            else if (counter == 14) {
                for (Building b : buildings) {
                    if (b.name().toLowerCase().contains(line)) {
                        team.wish().building1(b);
                        break;
                    }
                }
            }
            // --------------------------- get room1 ---------------------------
            else if (counter == 19) {
                String roomNumber = ""; // TODO
                for (Building b : buildings) {
                    for (Room r : b.rooms()) {
                        if (r.officialRoomNumber().equals(roomNumber)) {
                            team.wish().room1(r);
                        }
                    }
                }
            }
            // --------------------------- get room2 ---------------------------
            else if (counter == 26) {
                String roomNumber = ""; // TODO
                for (Building b : buildings) {
                    for (Room r : b.rooms()) {
                        if (r.officialRoomNumber().equals(roomNumber)) {
                            team.wish().room2(r);
                        }
                    }
                }
            }
            // ------------------------- get building2 -------------------------
            else if (counter == 33) {
                for (Building b : buildings) {
                    if (b.name().toLowerCase().contains(line)) {
                        team.wish().building2(b);
                        break;
                    }
                }
            }
            reader.close();
        }

    }

    // tested, works
    public static ArrayList<Building> importBuildings(File csv)
            throws IOException, NumberFormatException {
        BufferedReader reader = new BufferedReader(new FileReader(csv));
        String line = reader.readLine(); // skip heading
        while ((line = reader.readLine()) != null) {
            line = line.strip();
            if (line.equals(""))
                continue; // skip empty lines

            String[] entry = line.strip().toLowerCase().split(",");
            // ------------------------- get building --------------------------
            Building building = new Building(entry[0]);
            if (ImportFiles.buildings.isEmpty()) {
                // add first building to list
                ImportFiles.buildings.add(building);
            } else {
                boolean found = false;
                for (Building b : ImportFiles.buildings) {
                    if (b.name().toLowerCase().equals(entry[0])) {
                        // avoid duplicates
                        building = b;
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    // no building with same name found
                    ImportFiles.buildings.add(building);
                }
            }
            // --------------------------- get room ----------------------------
            Room room = new Room();
            for (Room r : building.rooms()) {
                if (r.officialRoomNumber().equals(entry[2])) {
                    // room already exists
                    System.out.println("Raum " + r.officialRoomNumber() +
                            " in " + building.name() + " existiert bereits!");
                    continue;
                }
            }
            // -------------------------- get gender ---------------------------
            GENDER gender;
            if (entry[4].contains("w")) {
                gender = GENDER.f;
            } else if (entry[3].contains("m")) {
                gender = GENDER.m;
            } else {
                gender = GENDER.d;
            }
            // ------------------------- get capacity --------------------------
            int capacity = Integer.valueOf(entry[5]);
            // ------------------------- compare room --------------------------
            room = new Room(entry[1], entry[2], gender, capacity);
            building.addRoom(room);
        }
        reader.close();
        return ImportFiles.buildings;
    }
}