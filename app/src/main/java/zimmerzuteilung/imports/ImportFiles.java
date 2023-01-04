package zimmerzuteilung.imports;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import zimmerzuteilung.objects.Building;
import zimmerzuteilung.objects.GENDER;
import zimmerzuteilung.objects.Room;
import zimmerzuteilung.objects.Student;
import zimmerzuteilung.objects.Team;

public class ImportFiles {

    private static class BuildingDoesNotExist extends Exception {
        private BuildingDoesNotExist(String str) {
            super(str);
        }
    }

    private static class RoomDoesNotExist extends Exception {
        private RoomDoesNotExist(String str) {
            super(str);
        }
    }

    private static class TeamDoesNotExist extends Exception {
        private TeamDoesNotExist(String str) {
            super(str);
        }
    }

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
            System.out.println("Es wurden keine Dateien in dem Ordner " + pathToFolder + "gefunden.");
            e.printStackTrace();
        }
        return fileList;
    }

    public static String editLine(String line) {
        // remove double quotes and comma
        if (line != null) {
            if (line.length() > 2) {
                if (line.equals("null,") || line.equals("\"\",")) {
                    return ""; // no information in survey
                } else {
                    if (line.endsWith(",")) {
                        line = line.substring(0, line.length() - 1);
                    }
                    if (line.startsWith("\"") && line.endsWith("\"")) {
                        line = line.substring(1, line.length() - 1);
                    }
                    return line.toLowerCase();
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

    // tested, works
    public static ArrayList<Building> importBuildings(File csv) throws IOException, IllegalArgumentException {
        try (BufferedReader reader = new BufferedReader(new FileReader(csv))) {
            int lineNum = 1;
            String line = reader.readLine(); // skip heading
            while ((line = reader.readLine()) != null) {
                lineNum++;
                line = line.strip();
                if (line.equals(""))
                    continue; // skip empty lines

                String[] entry = line.strip().toLowerCase().split(",");
                // --------------------------------------------get_building---------------------------------------------
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
                // ----------------------------------------------get_room-----------------------------------------------
                Room room = new Room();
                for (Room r : building.rooms()) {
                    if (r.officialRoomNumber().equals(entry[2])) {
                        // room already exists
                        System.out.println(
                                "Raum " + r.officialRoomNumber() + " in " + building.name() + " existiert bereits "
                                        + "und wird nicht erneut eingelesen! " + "Siehe " + csv.getAbsolutePath()
                                        + " in Zeile " + lineNum);
                        continue; // ignore duplicate room
                    }
                }
                // ---------------------------------------------get_gender----------------------------------------------
                GENDER gender;
                if (entry[3].contains("w")) {
                    gender = GENDER.f;
                } else if (entry[3].contains("m")) {
                    gender = GENDER.m;
                } else {
                    gender = GENDER.d;
                }
                // --------------------------------------------get_capacity---------------------------------------------
                int capacity = 0;
                try {
                    capacity = Integer.valueOf(entry[4]);
                } catch (NumberFormatException e) {
                    System.out.println(
                            "Fehler: In " + csv.getAbsolutePath() + " Zeile " + lineNum + " muss für die Kapazität "
                                    + "eine Zahl angegeben werden! Statt dessen " + "gefunden: " + entry[4]);
                }
                // -------------------------------------------check_reserved--------------------------------------------
                boolean reserved = false;
                if (entry[5].equals("ja")) {
                    reserved = true;
                } else if (!(entry[5].equals("nein") || entry[5].equals(""))) {
                    // if invalid argument
                    System.err.println("\"ja\" or \"nein\" expected" + " but got " + entry[5] + "\n"
                            + csv.getAbsolutePath() + ":" + lineNum);
                }
                // ----------------------------------------------add_room-----------------------------------------------
                room = new Room(entry[1], entry[2], gender, capacity, reserved);
                building.addRoom(room);
            }
            reader.close();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return ImportFiles.buildings;
    }

    // TODO: test
    public static ArrayList<Team> importWishes(File csv)
            throws IOException, IllegalArgumentException, BuildingDoesNotExist, RoomDoesNotExist, TeamDoesNotExist {
        try (BufferedReader reader = new BufferedReader(new FileReader(csv))) {
            ArrayList<Team> list = ImportFiles.teams;
            int lineNum = 1;
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                lineNum++;
                line = line.strip();
                line = line.replace("\"", "");
                if (line.equals("")) {
                    continue; // skip empty lines
                }
                String[] entry = line.strip().toLowerCase().split(",");
                // ----------------------------------------------get_team-----------------------------------------------
                Team team = new Team();
                for (Team t : ImportFiles.teams) {
                    if (t.name().equals(entry[5])) {
                        team = t;
                        break;
                    }
                }
                if (team.name() == null) {
                    throw new TeamDoesNotExist("Die Zimmerwahl von Team \"" + entry[5] + "\" kann nicht angenommen "
                            + "werden, da das Team noch nicht angelegt wurde. Haben sich die entsprechenden "
                            + "Schüler:innen in eine Gruppe im Moodleraum eingetragen?");
                }
                // -----------------------------------------get_specialization------------------------------------------
                /*
                 * SPECIALIZATION specialization;
                 * if (entry[3].equals("Naturwissenschaften")) {
                 * specialization = SPECIALIZATION.NAWI;
                 * } else if (entry[3].equals("Sprachen")) {
                 * specialization = SPECIALIZATION.SPRACHEN;
                 * } else if (entry[3].equals("Musik")) {
                 * specialization = SPECIALIZATION.MUSIK;
                 * }
                 */
                // ---------------------------------------------get_gender----------------------------------------------
                team.gender(GENDER.d);
                if (entry[10].contains("zimmer")) {
                    if (entry[10].contains("Jung")) {
                        team.gender(GENDER.m);
                    } else if (entry[10].contains("Mädchen")) {
                        team.gender(GENDER.f);
                    }
                }
                // ----------------------------------------------get_grade----------------------------------------------
                /*
                 * int grade;
                 * if (entry[11].contains("Klasse")) {
                 * if (entry[11].contains("10")) {
                 * grade = 10;
                 * } else if (entry[11].contains("11")) {
                 * grade = 11;
                 * } else if (entry[11].contains("12")) {
                 * grade = 12;
                 * }
                 * }
                 */
                // ----------------------------------------------get_wish-----------------------------------------------
                String nameBuilding1 = "";
                String nameRoom1 = "";
                String nameRoom2 = "";
                String nameBuilding2 = "";
                for (int i = 13; i < entry.length; i++) {
                    if (entry[i] != "") {
                        if (nameBuilding1.equals("")) {
                            nameBuilding1 = entry[i];
                        } else if (nameRoom1.equals("")) {
                            nameRoom1 = entry[i];
                        } else if (nameRoom2.equals("")) {
                            nameRoom2 = entry[i];
                        } else if (nameBuilding2.equals("")) {
                            nameBuilding2 = entry[i];
                        }
                    }
                }

                Boolean building1 = false, building2 = false, room1 = false, room2 = false;
                for (Building building : ImportFiles.buildings) {
                    if (building.name().equals(nameBuilding1)) {
                        building1 = true;
                        team.wish().building1(building);
                    }
                    if (building.name().equals(nameBuilding2)) {
                        building2 = true;
                        team.wish().building2(building);
                    }

                    if (building1 && building2) {
                        break;
                    }
                }
                if (!building1) {
                    throw new BuildingDoesNotExist("Das Internat " + nameBuilding1
                            + " existiert nicht! Wurde es vorher korrekt eingelesen?");
                }
                if (!building2) {
                    throw new BuildingDoesNotExist("Das Internat " + nameBuilding2
                            + " existiert nicht! Wurde es vorher korrekt eingelesen?");
                }

                for (Room room : team.wish().building1().rooms()) {
                    if (room.officialRoomNumber().equals(nameRoom1)) {
                        room1 = true;
                        team.wish().room1(room);
                    } else if (nameRoom1.contains(room.officialRoomNumber())) {
                        room1 = true;
                        team.wish().room1(room);
                    }

                    if (room.officialRoomNumber().equals(nameRoom2)) {
                        room2 = true;
                        team.wish().room2(room);
                    } else if (nameRoom2.contains(room.officialRoomNumber())) {
                        room2 = true;
                        team.wish().room2(room);
                    }

                    if (room1 && room2) {
                        break;
                    }
                }
                if (!room1) {
                    throw new RoomDoesNotExist("Das Zimmer " + nameRoom1
                            + " existiert nicht! Wurde es vorher korrekt eingelesen?");
                }
                if (!room2) {
                    throw new RoomDoesNotExist("Das Zimmer " + nameRoom2
                            + " existiert nicht! Wurde es vorher korrekt eingelesen?");
                }
            }
            reader.close();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return ImportFiles.teams;
    }

    // TODO: test
    public static ArrayList<Team> importTeams(File csv) throws IOException, IllegalArgumentException {
        try (BufferedReader reader = new BufferedReader(new FileReader(csv))) {
            int lineNum = 2;
            String line = reader.readLine();
            String sep = ",";
            if (line.contains("sep=")) {
                sep = Character.toString(line.charAt(4));
            }
            line = reader.readLine(); // skip heading

            while ((line = reader.readLine()) != null) {
                lineNum++;
                line = line.strip();
                if (line.equals("")) {
                    continue; // skip empty lines
                }
                line = line.replace("\"", "");
                String[] entry = line.strip().toLowerCase().split(",");
                // --------------------------------------------get_team_size--------------------------------------------
                int teamSize = Integer.valueOf(entry[2]);
                if (teamSize == 0) {
                    continue; // team is empty
                }
                // --------------------------------------------get_teamname---------------------------------------------
                Team team = new Team();
                team.name(entry[1]); // moodle team name
                // ----------------------------------------get_students_of_team-----------------------------------------
                for (int i = 8; i <= 7 + teamSize * 5; i += 5) {
                    String userName = entry[i];
                    String name = entry[i + 2] + " " + entry[i + 3]; // FirstName LastName

                    Student student = new Student(name, userName);
                    // check for duplicates:
                    for (Team t : ImportFiles.teams) {
                        Student duplicate = t.getStudent(userName);
                        if (duplicate != null) {
                            System.err.println("Schüler:in befindet sich in " + "meheren Moodlegruppen!\n"
                                    + duplicate.userName() + ": " + team.name() + ", " + t.name());

                            student = duplicate; // add student anyways
                            break;
                        }
                    }
                    team.addStudent(student); // create new student and add to team
                }
                ImportFiles.teams.add(team);
            }
            reader.close();
            return ImportFiles.teams;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }
}