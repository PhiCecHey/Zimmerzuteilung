package zimmerzuteilung.importsExports;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import zimmerzuteilung.Exceptions.*;
import zimmerzuteilung.log.Log;
import zimmerzuteilung.objects.Building;
import zimmerzuteilung.objects.GENDER;
import zimmerzuteilung.objects.Room;
import zimmerzuteilung.objects.SPECIALIZATION;
import zimmerzuteilung.objects.Student;
import zimmerzuteilung.objects.Team;

public class ImportFiles {
    static ArrayList<Building> buildings = new ArrayList<>();
    static ArrayList<Student> students = new ArrayList<>();
    static ArrayList<Team> teams = new ArrayList<>();

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

    // tested, works
    /**
     * Compares two moodle time stamps.
     * 
     * @param date1 first moodle time stamp
     * @param date2 second moodle time stamp
     * @return returns true, if date1 is earlier or equal to date2
     */
    public static boolean compareDate(String date1, String date2) {
        int year1, year2, month1, month2, day1, day2;
        try {
            year1 = Integer.valueOf(date1.substring(6, 10));
            year2 = Integer.valueOf(date2.substring(6, 10));
            month1 = Integer.valueOf(date1.substring(3, 5));
            month2 = Integer.valueOf(date2.substring(3, 5));
            day1 = Integer.valueOf(date1.substring(0, 2));
            day2 = Integer.valueOf(date2.substring(0, 2));
        } catch (NumberFormatException e) {
            System.err.println(date1 + " und " + date2 + " sind keine validen Moodletimestamps.");
            Log.append(date1 + " und " + date2 + " sind keine validen Moodletimestamps.");
            throw e;
        }
        if (year1 < year2) {
            return true;
        } else if (year1 > year2) {
            return false;
        }

        if (month1 < month2) {
            return true;
        } else if (month1 > month2) {
            return false;
        }

        if (day1 < day2) {
            return true;
        } else if (day1 > day2) {
            return false;
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
                String[] entry = line.strip().toLowerCase().split(",");
                if (line.equals("") || entry.length == 0) {
                    continue; // skip empty lines
                }

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
                    Log.append("\"ja\" or \"nein\" expected" + " but got " + entry[5] + "\n"
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

    // tested, works
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
                // ----------------------------------------------get_wish-----------------------------------------------
                String nameBuilding1 = "";
                String nameRoom1 = "";
                String nameRoom2 = "";
                String nameBuilding2 = "";
                for (int i = 9; i < entry.length; i++) {
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

    // tested, works
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

                boolean duplicateTeam = false;
                for (Team t : ImportFiles.teams) {
                    if (team.name().equals(t.name())) {
                        duplicateTeam = true;
                        team = t; // team already added to list of teams
                        break;
                    }
                }
                // ----------------------------------------get_students_of_team-----------------------------------------
                for (int i = 8; i <= 7 + teamSize * 5; i += 5) {
                    String userName = entry[i];
                    String name = entry[i + 2] + " " + entry[i + 3]; // FirstName LastName

                    Student student = null;
                    for (Student s : ImportFiles.students) {
                        if (s.userName().equals(userName)) {
                            student = s;
                        }
                    }
                    if (student == null) {
                        student = new Student(name, userName);
                        System.err.println("Schüler:in " + name + " hat die Umfrage zu den persönlichen Daten nicht "
                                + "(vollständig) ausgefüllt!");
                        Log.append("Schüler:in " + name + " hat die Umfrage zu den persönlichen Daten nicht "
                                + "(vollständig) ausgefüllt!");
                    }

                    // check for duplicates:
                    for (Team t : ImportFiles.teams) {
                        Student duplicate = t.getStudent(userName);
                        if (duplicate != null) {
                            System.err.println("Schüler:in befindet sich in mehreren Moodlegruppen!\n"
                                    + duplicate.userName() + ": " + team.name() + ", " + t.name());
                            Log.append("Schüler:in befindet sich in mehreren Moodlegruppen!\n"
                                    + duplicate.userName() + ": " + team.name() + ", " + t.name());

                            // student = duplicate; // add student anyways
                            break;
                        }
                    }
                    team.addStudent(student); // create new student and add to team
                }
                if (!duplicateTeam) {
                    ImportFiles.teams.add(team);
                }
            }
            reader.close();
            return ImportFiles.teams;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    // tested, works
    public static ArrayList<Student> importStudents(File csv) throws FileNotFoundException, IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(csv))) {
            int lineNum = 1;
            String line = reader.readLine(); // skip heading
            while ((line = reader.readLine()) != null) {
                lineNum++;
                line = line.strip();
                line = line.replace("\"", "");
                if (line.equals("")) {
                    continue; // skip empty lines
                }
                String[] entry = line.strip().toLowerCase().split(",");
                if (entry.length == 0) {
                    continue;
                }
                // ----------------------------------------------get_name-----------------------------------------------
                String name = entry[7];
                String username = entry[8];
                Student student = new Student(name, username);
                student.moodleDate(entry[1]);
                // -------------------------------------------check_duplicate-------------------------------------------
                boolean duplicate = false;
                boolean skip = false;
                for (Student s : ImportFiles.students) {
                    if (s.userName().equals(username)) {
                        if (ImportFiles.compareDate(s.moodleDate(), student.moodleDate())) {
                            // overwrite instead of skip
                            student = s;
                            duplicate = true;
                        } else {
                            // ignore and skip instead of overwrite
                            skip = true;
                        }
                        break;
                    }
                }
                if (skip) {
                    continue; // read next line
                }
                // ----------------------------------------------get_grade----------------------------------------------
                if (entry[9].contains("9")) {
                    student.grade(9);
                } else if (entry[9].contains("10")) {
                    student.grade(10);
                } else if (entry[9].contains("11")) {
                    student.grade(11);
                } else if (entry[9].contains("12")) {
                    student.grade(12);
                } else {
                    System.err.println("Schüler:in " + name + " hat keine gültige Klassenstufe!");
                    Log.append("Schüler:in " + name + " hat keine gültige Klassenstufe!");
                }
                // -----------------------------------------get_specialization------------------------------------------
                if (entry[10].equals("naturwissenschaften")) {
                    student.special(SPECIALIZATION.NAWI);
                } else if (entry[10].equals("musik")) {
                    student.special(SPECIALIZATION.MUSIK);
                } else if (entry[10].equals("sprachen")) {
                    student.special(SPECIALIZATION.SPRACHEN);
                } else {
                    System.err.println("Schüler:in " + name + " hat keinen gültigen Zweig!");
                    Log.append("Schüler:in " + name + " hat keinen gültigen Zweig!");
                }
                // ---------------------------------------------get_gender----------------------------------------------
                if (entry[11].equals("weiblich")) {
                    student.gender(GENDER.f);
                } else if (entry[11].contains("m") && entry[11].contains("nnlich")) {
                    student.gender(GENDER.m);
                } else if (entry[11].equals("divers")) {
                    student.gender(GENDER.d);
                } else {
                    System.err.println("Schüler:in " + name + " hat ein ungültiges Geschlecht!");
                    Log.append("Schüler:in " + name + " hat ein ungültiges Geschlecht!");
                }

                if (!duplicate) {
                    ImportFiles.students.add(student);
                }
            }
            return ImportFiles.students;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<Building> buildings() {
        return ImportFiles.buildings;
    }

    public static ArrayList<Team> teams() {
        return ImportFiles.teams;
    }

    public static void clear() {
        ImportFiles.buildings.clear();
        ImportFiles.students.clear();
        ImportFiles.teams.clear();
    }

}