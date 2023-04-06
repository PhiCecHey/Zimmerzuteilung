package zimmerzuteilung.importsExports;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import zimmerzuteilung.Config;
import zimmerzuteilung.Log;
import zimmerzuteilung.Exceptions.*;
import zimmerzuteilung.objects.Building;
import zimmerzuteilung.objects.GENDER;
import zimmerzuteilung.objects.Room;
import zimmerzuteilung.objects.SPECIALIZATION;
import zimmerzuteilung.objects.Student;
import zimmerzuteilung.objects.Team;

public class ImportFiles {
    private static ArrayList<Building> buildings = new ArrayList<>();
    private static ArrayList<Student> students = new ArrayList<>();
    private static ArrayList<Team> teams = new ArrayList<>();

    /*
     * public static File[] getFilesFromFolder(String pathToFolder) {
     * File[] fileList = new File[0];
     * try {
     * File folder = new File(pathToFolder);
     * System.out.println("Ordner: " + folder.getAbsolutePath());
     * fileList = folder.listFiles();
     * if (fileList.length == 0) {
     * throw new FileNotFoundException();
     * }
     * return fileList;
     * } catch (FileNotFoundException e) {
     * System.out.println("Es wurden keine Dateien in dem Ordner " + pathToFolder +
     * "gefunden.");
     * e.printStackTrace();
     * }
     * return fileList;
     * }
     */

    /*
     * public static String editLine(String line) {
     * // remove double quotes and comma
     * if (line != null) {
     * if (line.length() > 2) {
     * if (line.equals("null,") || line.equals("\"\",")) {
     * return ""; // no information in survey
     * } else {
     * if (line.endsWith(",")) {
     * line = line.substring(0, line.length() - 1);
     * }
     * if (line.startsWith("\"") && line.endsWith("\"")) {
     * line = line.substring(1, line.length() - 1);
     * }
     * return line.toLowerCase();
     * }
     * }
     * }
     * return null;
     * }
     */

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

    private static String[] splitOnComma(String line) throws LineEmptyException {
        String quote = "\"";
        String testIfEmpty = line.replace(",", "");
        if (line.equals("") || line == null || testIfEmpty.equals("")) {
            throw new LineEmptyException("");
        }
        String[] split = line.toLowerCase().split(",");
        ArrayList<String> list = new ArrayList<>();

        // , could be contained in string that should not be split
        // => merge strings so that string starts and ends with quote
        boolean merge = false;
        String newString = "";

        for (int i = 0; i < split.length; i++) {
            String entry = split[i].toLowerCase();
            if (!merge) {
                if (entry.startsWith(quote)) {
                    if (entry.endsWith(quote)) {
                        list.add(entry);
                        merge = false;
                        newString = "";
                    } else {
                        merge = true;
                        newString = entry;
                    }
                } else {
                    if (!entry.endsWith(quote)) {
                        merge = false;
                        newString = "";
                        list.add(entry);
                    }
                }
            } else {
                newString += entry;
                if (entry.endsWith(quote)) {
                    list.add(newString);
                    merge = false;
                    newString = "";
                } else {
                    merge = true;
                }
            }
        }

        // list to array
        String[] toReturn = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            String entry = list.get(i).replace(quote, "");
            toReturn[i] = entry;
        }
        return toReturn;
    }

    // tested, works
    public static boolean importBuildings(File csv) throws IOException, IllegalArgumentException {
        boolean noWarnings = true;
        BufferedReader reader = new BufferedReader(new FileReader(csv));
        int lineNum = 1;
        String line = reader.readLine(); // skip heading
        while ((line = reader.readLine()) != null) {
            lineNum++;
            String[] entry;
            try {
                entry = ImportFiles.splitOnComma(line);
            } catch (LineEmptyException e) {
                continue;
            }
            // --------------------------------------------get_building---------------------------------------------
            Building building = new Building(entry[Config.impBuildBuildingName]);
            if (ImportFiles.buildings.isEmpty()) {
                // add first building to list
                ImportFiles.buildings.add(building);
            } else {
                boolean found = false;
                for (Building b : ImportFiles.buildings) {
                    if (b.name().toLowerCase().equals(entry[Config.impBuildBuildingName])) {
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
                if (r.officialRoomNumber().equals(entry[Config.impBuildOfficialRoomNum])) {
                    // room already exists
                    String errmsg = "Raum " + r.officialRoomNumber() + " in " + building.name()
                            + " existiert bereits "
                            + "und wird nicht erneut eingelesen! " + "Siehe " + csv.getAbsolutePath()
                            + " in Zeile " + lineNum;
                    System.err.println(errmsg);
                    Log.append(errmsg);
                    continue; // ignore duplicate room
                }
            }
            // ---------------------------------------------get_gender----------------------------------------------
            GENDER gender;
            if (entry[Config.impBuildRoomGender].contains("w")) {
                gender = GENDER.f;
            } else if (entry[Config.impBuildRoomGender].contains("m")) {
                gender = GENDER.m;
            } else {
                String errormsg = "Zimmer " + room.officialRoomNumber() + " hat ein ungueltiges Geschlecht!";
                System.err.println(errormsg);
                Log.append(errormsg);
                noWarnings = false;
                gender = GENDER.d;
            }
            // --------------------------------------------get_capacity---------------------------------------------
            int capacity = 0;
            try {
                capacity = Integer.valueOf(entry[Config.impBuildRoomCapacity]);
            } catch (NumberFormatException e) {
                String errormsg = "Fehler: In " + csv.getAbsolutePath() + " Zeile " + lineNum
                        + " muss fuer die Kapazitaet "
                        + "eine Zahl angegeben werden! Statt dessen " + "gefunden: "
                        + entry[Config.impBuildRoomCapacity];
                System.err.println(errormsg);
                Log.append(errormsg);
            }
            // -------------------------------------------check_reserved--------------------------------------------
            boolean reserved = false;
            if (entry[Config.impBuildRoomReserved].equals("ja")) {
                reserved = true;
            } else if (!(entry[Config.impBuildRoomReserved].equals("nein")
                    || entry[Config.impBuildRoomReserved].equals(""))) {
                // if invalid argument
                String errormsg = "\"ja\" or \"nein\" expected" + " but got " + entry[Config.impBuildRoomReserved]
                        + "\n"
                        + csv.getAbsolutePath() + ":" + lineNum;
                System.err.println(errormsg);
                Log.append(errormsg);
                noWarnings = false;
            }
            // ----------------------------------------------add_room-----------------------------------------------
            room = new Room(entry[Config.impBuildUnofficialRoomNum], entry[Config.impBuildOfficialRoomNum], gender,
                    capacity, reserved);
            building.addRoom(room);
        }
        reader.close();
        return noWarnings;
    }

    // tested, works
    public static boolean importWishes(File csv)
            throws IOException, IllegalArgumentException, BuildingDoesNotExist, RoomDoesNotExist, TeamDoesNotExist {
        boolean noWarnings = true;
        BufferedReader reader = new BufferedReader(new FileReader(csv));
        int lineNum = 1;
        String line = reader.readLine();
        while ((line = reader.readLine()) != null) {
            lineNum++;
            String[] entry;
            try {
                entry = ImportFiles.splitOnComma(line);
            } catch (LineEmptyException e) {
                continue;
            }
            // ----------------------------------------------get_team-----------------------------------------------
            Team team = new Team();
            for (Team t : ImportFiles.teams) {
                if (t.name().equals(entry[Config.impWishTeamName])) {
                    team = t;
                    break;
                }
            }
            if (team.name() == null) {
                String errormsg = "Die Zimmerwahl von Team \"" + entry[Config.impWishTeamName]
                        + "\" kann nicht angenommen "
                        + "werden, da das Team noch nicht angelegt wurde. Haben sich die entsprechenden "
                        + "Schueler:innen in eine Gruppe im Moodleraum eingetragen?";
                Log.append(errormsg);
                reader.close();
                throw new TeamDoesNotExist(errormsg);
            }
            // ----------------------------------------------get_wish-----------------------------------------------
            String nameBuilding1 = "";
            String nameRoom1 = "";
            String nameRoom2 = "";
            String nameBuilding2 = "";
            for (int i = Config.impWishCycleLengthTilNextMember; i < entry.length; i++) {
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
                String errormsg = "Das Internat " + nameBuilding1
                        + " existiert nicht! Wurde es vorher korrekt eingelesen?";
                Log.append(errormsg);
                reader.close();
                throw new BuildingDoesNotExist(errormsg);
            }
            if (!building2) {
                String errormsg = "Das Internat " + nameBuilding2
                        + " existiert nicht! Wurde es vorher korrekt eingelesen?";
                Log.append(errormsg);
                reader.close();
                throw new BuildingDoesNotExist(errormsg);
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
                String errormsg = "Das Zimmer " + nameRoom1
                        + " existiert nicht! Wurde es vorher korrekt eingelesen?";
                Log.append(errormsg);
                reader.close();
                throw new RoomDoesNotExist(errormsg);
            }
            if (!room2) {
                String errormsg = "Das Zimmer " + nameRoom2
                        + " existiert nicht! Wurde es vorher korrekt eingelesen?";
                Log.append(errormsg);
                reader.close();
                throw new RoomDoesNotExist(errormsg);
            }
        }
        reader.close();
        return noWarnings;
    }

    // tested, works
    public static boolean importTeams(File csv)
            throws IOException, IllegalArgumentException, StudentInSeveralMoodleGroups {
        boolean noWarnings = true;
        BufferedReader reader = new BufferedReader(new FileReader(csv));
        int lineNum = 2;
        String line = reader.readLine();
        line = reader.readLine(); // skip heading

        while ((line = reader.readLine()) != null) {
            lineNum++;
            String[] entry;
            try {
                entry = ImportFiles.splitOnComma(line);
            } catch (LineEmptyException e) {
                continue;
            }
            // --------------------------------------------get_team_size--------------------------------------------
            int teamSize = Integer.valueOf(entry[Config.impTeamTeamSize]);
            if (teamSize == 0) {
                continue; // team is empty
            }
            // --------------------------------------------get_teamname---------------------------------------------
            Team team = new Team();
            team.name(entry[Config.impTeamTeamName]); // moodle team name

            boolean duplicateTeam = false;
            for (Team t : ImportFiles.teams) {
                if (team.name().equals(t.name())) {
                    duplicateTeam = true;
                    team = t; // team already added to list of teams
                    break;
                }
            }
            // ----------------------------------------get_students_of_team-----------------------------------------
            for (int i = Config.impTeamFirstMember; i <= Config.impTeamCycleLengthTilNextMember - 1 + teamSize
                    * Config.impTeamCycleLengthTilNextMember; i += Config.impTeamCycleLengthTilNextMember) {
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
                    System.err.println("Schueler:in " + name + " hat die Umfrage zu den persoenlichen Daten nicht "
                            + "(vollstaendig) ausgefuellt!");
                    Log.append("Schueler:in " + name + " hat die Umfrage zu den persoenlichen Daten nicht "
                            + "(vollstaendig) ausgefuellt!");
                    noWarnings = false;
                }

                // check for duplicates:
                for (Team t : ImportFiles.teams) {
                    Student duplicate = t.getStudent(userName);
                    if (duplicate != null) {
                        String errormsg = "Schueler:in befindet sich in mehreren Moodlegruppen! "
                                + duplicate.userName() + ": " + team.name() + ", " + t.name();
                        Log.append(errormsg);
                        reader.close();
                        throw new StudentInSeveralMoodleGroups(errormsg);
                    }
                }
                team.addStudent(student); // create new student and add to team
            }
            if (!duplicateTeam) {
                // check gender of students
                GENDER gender = ImportFiles.determineGender(team);
                if (gender == null) {
                    team.gender(GENDER.d);
                    String errormsg = "Das Team " + team.name() + " ist nicht gleichgeschlechtlich!";
                    System.err.println(errormsg);
                    Log.append(errormsg);
                    noWarnings = false;
                } else {
                    team.gender(gender);
                }

                // add team
                ImportFiles.teams.add(team);
            }
        }
        reader.close();
        return noWarnings;
    }

    // tested, works
    public static boolean importStudents(File csv) throws FileNotFoundException, IOException {
        boolean noWarnings = true;
        BufferedReader reader = new BufferedReader(new FileReader(csv));
        int lineNum = 1;
        String line = reader.readLine(); // skip heading
        while ((line = reader.readLine()) != null) {
            lineNum++;
            String[] entry;
            try {
                entry = ImportFiles.splitOnComma(line);
            } catch (LineEmptyException e) {
                continue;
            }
            // ----------------------------------------------get_name-----------------------------------------------
            String name = entry[Config.impStudName];
            String username = entry[Config.impStudUsername];
            Student student = new Student(name, username);
            student.moodleDate(entry[Config.impStudDate]);
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
            if (entry[Config.impStudGrade].contains("9")) {
                student.grade(9);
            } else if (entry[Config.impStudGrade].contains("10")) {
                student.grade(10);
            } else if (entry[Config.impStudGrade].contains("11")) {
                student.grade(11);
            } else if (entry[Config.impStudGrade].contains("12")) {
                student.grade(12);
            } else {
                String errormsg = "Schueler:in " + name + " hat keine gueltige Klassenstufe!";
                System.err.println(errormsg);
                Log.append(errormsg);
                noWarnings = false;
            }
            // -----------------------------------------get_specialization------------------------------------------
            if (entry[Config.impStudSpecial].equals("naturwissenschaften")) {
                student.special(SPECIALIZATION.NAWI);
            } else if (entry[Config.impStudSpecial].equals("musik")) {
                student.special(SPECIALIZATION.MUSIK);
            } else if (entry[Config.impStudSpecial].equals("sprachen")) {
                student.special(SPECIALIZATION.SPRACHEN);
            } else {
                String errormsg = "Schueler:in " + name + " hat keinen gueltigen Zweig!";
                System.err.println(errormsg);
                Log.append(errormsg);
                noWarnings = false;
            }
            // ---------------------------------------------get_gender----------------------------------------------
            if (entry[Config.impStudGender].equals("weiblich")) {
                student.gender(GENDER.f);
            } else if (entry[Config.impStudGender].contains("m") && entry[Config.impStudGender].contains("nnlich")) {
                student.gender(GENDER.m);
            } else if (entry[Config.impStudGender].equals("divers")) {
                student.gender(GENDER.d);
            } else {
                String errormsg = "Schueler:in " + name + " hat ein ungueltiges Geschlecht!";
                System.err.println(errormsg);
                Log.append(errormsg);
                noWarnings = false;
            }

            if (!duplicate) {
                ImportFiles.students.add(student);
            }
        }
        reader.close();
        return noWarnings;
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

    private static GENDER determineGender(Team team) {
        short countM = 0;
        short countF = 0;
        short countD = 0;
        for (Student s : team.members()) {
            if (s.gender().equals(GENDER.f)) {
                countF++;
            } else if (s.gender().equals(GENDER.m)) {
                countM++;
            } else if (s.gender().equals(GENDER.d)) {
                countD++;
            } else {
                s.gender(GENDER.d);
                String errmsg = "Schueler " + s.userName() + " hat kein gueltiges Geschlecht!";
                System.err.println(errmsg);
                Log.append(errmsg);
            }
        }
        if (countM > 0 && countF > 0 || countM > 0 && countD > 0 || countF > 0 && countD > 0) {
            return null;
        }

        if (countM > countF) {
            if (countM >= countD) {
                return GENDER.m;
            } else {
                return GENDER.d;
            }
        } else {
            if (countF >= countD) {
                return GENDER.f;
            } else {
                return GENDER.d;
            }
        }
    }
}