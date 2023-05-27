package zimmerzuteilung.importsExports;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import zimmerzuteilung.Config;
import zimmerzuteilung.Exceptions.*;
import zimmerzuteilung.Log;
import zimmerzuteilung.objects.Building;
import zimmerzuteilung.objects.GENDER;
import zimmerzuteilung.objects.Room;
import zimmerzuteilung.objects.SPECIALIZATION;
import zimmerzuteilung.objects.Student;
import zimmerzuteilung.objects.Team;

public class ImportFiles {
    private static ArrayList<Building> buildings = new ArrayList<>();
    private static ArrayList<Student> students = new ArrayList<>();
    private static ArrayList<Student> studentsWithoutGroup = new ArrayList<>();
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

    // tested, works
    /**
     * Compares two moodle time stamps.
     * 
     * @param date1 first moodle time stamp
     * @param date2 second moodle time stamp
     * @return returns true, if date1 is earlier or equal to date2
     */
    public static boolean compareDate(String date1, String date2) {
        int year1, year2, month1, month2, day1, day2, hour1, hour2, min1, min2, sec1, sec2;
        try {
            // date
            year1 = Integer.valueOf(date1.substring(6, 10));
            year2 = Integer.valueOf(date2.substring(6, 10));
            month1 = Integer.valueOf(date1.substring(3, 5));
            month2 = Integer.valueOf(date2.substring(3, 5));
            day1 = Integer.valueOf(date1.substring(0, 2));
            day2 = Integer.valueOf(date2.substring(0, 2));

            // time
            hour1 = Integer.valueOf(date1.substring(11, 13));
            hour2 = Integer.valueOf(date2.substring(11, 13));
            min1 = Integer.valueOf(date1.substring(14, 16));
            min2 = Integer.valueOf(date2.substring(14, 16));
            sec1 = Integer.valueOf(date1.substring(17, 19));
            sec2 = Integer.valueOf(date2.substring(17, 19));

        } catch (NumberFormatException e) {
            System.err.println(date1 + " und " + date2 + " sind keine validen Moodletimestamps.");
            Log.append(date1 + " und " + date2 + " sind keine validen Moodletimestamps.");
            throw e;
        }

        // compare date
        if (year1 < year2)
            return true;
        else if (year1 > year2)
            return false;

        if (month1 < month2)
            return true;
        else if (month1 > month2)
            return false;

        if (day1 < day2)
            return true;
        else if (day1 > day2)
            return false;

        // compare time
        if (hour1 < hour2)
            return true;
        else if (hour1 > hour2)
            return false;

        if (min1 < min2)
            return true;
        else if (min1 > min2)
            return false;

        if (sec1 < sec2)
            return true;
        else if (sec1 > sec2)
            return false;

        // if date and time equal
        return true;
    }

    private static String[] splitOnString(String line, String splitOn) throws LineEmptyException {
        String quote = "\"";
        String testIfEmpty = line.replace(splitOn, "");
        if (line.equals("") || line == null || testIfEmpty.equals("")) {
            throw new LineEmptyException("");
        }
        String[] split = line.toLowerCase().split(splitOn);
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
            toReturn[i] = entry.strip();
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
            // System.out.println("linenum: " + lineNum);
            String[] entry;
            try {
                entry = ImportFiles.splitOnString(line, ",");
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
                String errormsg = "\"ja\" or \"nein\" expected but got " + entry[Config.impBuildRoomReserved]
                        + "\n" + csv.getAbsolutePath() + ":" + lineNum;
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

    public static boolean[] importWishesGirlsBoys(File csvGirls, File csvBoys)
            throws IllegalArgumentException, IOException,
            BuildingDoesNotExistException, RoomDoesNotExistException, TeamDoesNotExistException {
        boolean noWarningsGirls = importWishesWithoutLog(csvGirls);
        boolean noWarningsBoys = importWishesWithoutLog(csvBoys);

        for (Team team : ImportFiles.teams) {
            Log.append(team.errorMsg());
        }

        return new boolean[] { noWarningsGirls, noWarningsBoys };
    }

    private static boolean importWishesWithoutLog(File csv)
            throws IOException, IllegalArgumentException, BuildingDoesNotExistException, RoomDoesNotExistException,
            TeamDoesNotExistException {
        boolean noWarnings = true;
        BufferedReader reader = new BufferedReader(new FileReader(csv));
        // int lineNum = 1;
        String line = reader.readLine();
        while ((line = reader.readLine()) != null) {
            // lineNum++;
            String[] entry;
            try {
                entry = ImportFiles.splitOnString(line, ",");
            } catch (LineEmptyException e) {
                continue;
            }
            // ----------------------------------------------get_team-----------------------------------------------
            String date = entry[Config.importWishDate];

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
                team.validateB1(false);
                team.validateB2(false);
                team.validateR1(false);
                team.validateR2(false);
                // throw new TeamDoesNotExistException(errormsg);
                noWarnings = false;
                continue;
            }

            // if this wish is younger than the wish stored, overwrite the earlier wish with
            // the later wish.
            // team needs to have valid wish so that earlier votes can be skipped and latest
            // vote is not overwritten.
            // check if team has a valid wish:
            if ((team.date() != null) && (!team.date().equals("")) && (!team.problems())) {
                // if team has already voted and team wish is younger than this one:
                if (ImportFiles.compareDate(date, team.date())) {
                    // skip this vote since it is older and should not overwrite already stored wish
                    continue;
                }
            }

            // ----------------------------------------------get_wish-----------------------------------------------
            Building b1 = null;
            Room r1 = null;
            Room r2 = null;
            Building b2 = null;

            for (int i = Config.impWishB1; i < entry.length; i++) {
                if (entry[i] != "") {
                    if (b1 == null) {
                        b1 = ImportFiles.findBuilding(entry[i]);
                    } else if (r1 == null) {
                        r1 = b1.getRoom(entry[i]);
                    } else if (r2 == null) {
                        r2 = b1.getRoom(entry[i]);
                    } else if (b2 == null) {
                        b2 = ImportFiles.findBuilding(entry[i]);
                    }
                }
            }

            if ((b1 != null) && (b2 != null) && (r1 != null) && (r2 != null)) {
                team.wish().building1(b1);
                team.validateB1(true);
                team.wish().room1(r1);
                team.validateR1(true);
                team.wish().room2(r2);
                team.validateR2(true);
                team.wish().building2(b2);
                team.validateB2(true);
                team.date(date);
            } else {
                team.date("");
                noWarnings = false;
            }

            if (b1 == null) {
                String errormsg = "Das Erstwunschinternat von Team " + team.name() + " konnte nicht gefunden werden!";
                b1 = ImportFiles.getRandomBuilding(b2);
                team.validateB1(false);
                // errormsg += " Es wurde das Internat " + b1.name() + " zufaellig als
                // Erstwunschinternat ausgewaehlt.";
                // Log.append(errormsg);
                System.out.println(errormsg);
                // reader.close();
                // throw new BuildingDoesNotExistException(errormsg);
            } else {
                team.validateB1(true);
            }
            if (b2 == null) {
                String errormsg = "Das Zweitwunschinternat von Team " + team.name() + " konnte nicht gefunden werden!";
                b2 = ImportFiles.getRandomBuilding(b1);
                team.validateB2(false);
                errormsg += " Es wurde das Internat " + b2.name() + " zufaellig als Zweitwunschinternat ausgewaehlt.";
                System.out.println(errormsg);
                // Log.append(errormsg);
                // reader.close();
                // throw new BuildingDoesNotExistException(errormsg);
            } else {
                team.validateB2(true);
            }
            if (r1 == null) {
                String errormsg = "Das Erstwunschzimmer von Team " + team.name() + " konnte nicht gefunden werden!";
                r1 = ImportFiles.getRandomRoom(b1, r2);
                team.validateR1(false);
                errormsg += " Es wurde das Zimmer " + r1.officialRoomNumber()
                        + " zufaellig als Erstwunschzimmer ausgewaehlt.";
                // Log.append(errormsg);
                System.out.println(errormsg);
                // reader.close();
                // throw new RoomDoesNotExistException(errormsg);
            } else {
                team.validateR1(true);
            }
            if (r2 == null) {
                String errormsg = "Das Zweitwunschzimmer von Team " + team.name() + " konnte nicht gefunden werden!";
                r2 = ImportFiles.getRandomRoom(b1, r1);
                team.validateR2(false);
                errormsg += " Es wurde das Zimmer " + r2.officialRoomNumber()
                        + " zufaellig als Erstwunschzimmer ausgewaehlt.";
                // Log.append(errormsg);
                System.out.println(errormsg);
                // reader.close();
                // throw new RoomDoesNotExistException(errormsg);
            } else {
                team.validateR2(true);
            }
        }
        reader.close();

        return noWarnings;
    }

    public static boolean[] importGirlBoyTeams(File txtGirls, File txtBoys) throws IllegalArgumentException,
            IOException, StudentInSeveralMoodleGroupsException, StudentDoesNotExistException {
        boolean workedGirls = ImportFiles.importTeams(txtGirls, true);
        boolean workedBoys = ImportFiles.importTeams(txtBoys, false);

        // print one errormsg per faulty student
        for (Student student : ImportFiles.students) {
            if (student.problems()) {
                String errormsg = student.errormsg();
                Log.append(student.errormsg());
            }
            if (!student.teamValid()) {
                ImportFiles.studentsWithoutGroup.add(student);
            } else {
                ImportFiles.studentsWithoutGroup.remove(student);
            }
        }

        return new boolean[] { workedGirls, workedBoys };
    }

    private static boolean importTeams(File txt, boolean girl)
            throws IOException, IllegalArgumentException, StudentInSeveralMoodleGroupsException,
            StudentDoesNotExistException {
        boolean noWarnings = true;
        BufferedReader reader = new BufferedReader(new FileReader(txt));
        // int lineNum = 2;
        String line = reader.readLine();
        // line = reader.readLine(); // skip heading

        while ((line = reader.readLine()) != null) {
            // lineNum++;
            String[] entry;
            try {
                entry = ImportFiles.splitOnString(line, "\t");
            } catch (LineEmptyException e) {
                continue;
            }

            String firstName = entry[Config.impTeamFirstName];
            String lastName = entry[Config.impTeamLastName];
            String email = entry[Config.impTeamEmail];
            String teamName = "";
            boolean studentHasNoGroup = false;
            try {
                teamName = entry[Config.impTeamTeamName];
            } catch (ArrayIndexOutOfBoundsException e) {
                studentHasNoGroup = true;
                // this doesnt matter though because student may be imported second time and has
                // a group then
            }

            if (firstName.contains("joel")) {
                int debug = 3;
            }

            Student student = ImportFiles.findStudentByName(firstName, lastName);

            if (student == null) {
                String errormsg = "Schueler:in " + firstName + " " + lastName
                        + " hat nicht die Persoenliche Daten Umfrage ausgefuellt! Damit sind die "
                        + "Klassenstufe und der Zweig unbekannt.";
                // throw new StudentDoesNotExistException(errormsg);
                // Log.append(errormsg);
                noWarnings = false;
                student = new Student(firstName + " " + lastName, email);
                student.email(email);
                ImportFiles.students.add(student);
            }

            // not necessary and causes bugs. TODO: determine students without group at the
            // end and not here
            /*
             * if (studentHasNoGroup && (!student.teamValid())) {
             * String errormsg = "Schueler:in " + firstName + " " + lastName
             * + " hat sich in keine Moodlegruppe eingetragen und kann demnach keinem "
             * + "Zimmer zugeteilt werden!";
             * // Log.append(errormsg);
             * System.out.println(errormsg);
             * student.validateTeam(false);
             * noWarnings = false;
             * }
             */

            if (!teamName.equals("")) {
                student.gender(teamName.toLowerCase().contains("jung") ? GENDER.m : GENDER.f);
                student.validateGender(true);
                if ((student.gender() == GENDER.f) != girl) {
                    char debug = 3; // maybe swapped input files for girls and boys?
                }
            } /*
               * else { // causes bugs and not necessary either
               * student.validateGender(false);
               * }
               */

            student.email(email);

            if (teamName.equals("")) {
                ImportFiles.studentsWithoutGroup.add(student);
                continue;
            }
            Team team = ImportFiles.findTeamByName(teamName);
            if (team == null) {
                team = new Team();
                team.name(teamName);
            }
            if (student.gender() != null) {
                team.gender(student.gender());
                team.validateGen(true);
            }

            Team teamInWhichStudentWasFound = ImportFiles.findStudentInTeam(student);
            if ((teamInWhichStudentWasFound != null) && (!teamInWhichStudentWasFound.name().equals(team.name()))) {
                reader.close();
                String errormsg = "Schueler:in " + student.name() + " hat sich in mehrere "
                        + "Moodlegruppen eingetragen!";
                Log.append(errormsg); // yes, append to log
                student.validateTeam(false);
                throw new StudentInSeveralMoodleGroupsException("errormsg");
            }

            team.addStudent(student);
            student.validateTeam(true);

            // add team
            if (!ImportFiles.teams.contains(team)) {
                ImportFiles.teams.add(team);
            }

        }
        reader.close();
        return noWarnings;
    }

    private static Student findStudentByName(String firstName, String lastName) {
        for (Student student : ImportFiles.students) {
            if (student.name().contains(firstName.toLowerCase()) && student.name().contains(lastName.toLowerCase())) {
                return student;
            }
        }
        return null;
    }

    private static Team findStudentInTeam(Student student) {
        for (Team team : ImportFiles.teams) {
            Student s = team.getStudent(student.userName());
            if (s != null) {
                return team;
            }
        }
        return null;
    }

    private static Team findTeamByName(String teamName) {
        for (Team team : ImportFiles.teams) {
            if (team.name().equals(teamName.toLowerCase())) {
                return team;
            }
        }
        return null;
    }

    // tested, works
    public static boolean importStudents(File csv) throws FileNotFoundException, IOException {
        boolean noWarnings = true;
        BufferedReader reader = new BufferedReader(new FileReader(csv));
        // int lineNum = 1;
        String line = reader.readLine(); // skip heading
        while ((line = reader.readLine()) != null) {
            // lineNum++;
            String[] entry;
            try {
                entry = ImportFiles.splitOnString(line, ",");
            } catch (LineEmptyException e) {
                continue;
            }
            // ----------------------------------------------get_name-----------------------------------------------
            String name = entry[Config.impStudName];
            String username = entry[Config.impStudUsername];
            Student student = new Student(name, username);
            student.moodleDate(entry[Config.impStudDate]);
            // -------------------------------------------check_duplicate-------------------------------------------
            if (name.contains("joel")) {
                int debug = 3;
            }

            boolean duplicate = false;
            boolean skip = false;
            Student overwrite = null;
            for (Student s : ImportFiles.students) {
                if (s.userName().equals(username)) {
                    duplicate = true;
                    if (ImportFiles.compareDate(student.moodleDate(), s.moodleDate())) {
                        if (!s.problems()) {
                            student = s;
                            skip = true;
                        }
                    } else { // s.moodleDate() <= student.moodleDate()
                        // only overwrite if data correct: s = student;
                        overwrite = s;
                        // planned: overwrite = student;
                    }
                    break;
                }
            }
            if (skip) {
                continue; // read next line
            }
            // ----------------------------------------------get_grade----------------------------------------------
            student.validateGrade(true);
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
                // Log.append(errormsg);
                student.validateGrade(false);
                noWarnings = false;
            }
            // -----------------------------------------get_specialization------------------------------------------
            student.validateSpecial(true);
            if (entry[Config.impStudSpecial].equals("naturwissenschaften")) {
                student.special(SPECIALIZATION.NAWI);
            } else if (entry[Config.impStudSpecial].equals("musik")) {
                student.special(SPECIALIZATION.MUSIK);
            } else if (entry[Config.impStudSpecial].equals("sprachen")) {
                student.special(SPECIALIZATION.SPRACHEN);
            } else {
                String errormsg = "Schueler:in " + name + " hat keinen gueltigen Zweig!";
                System.err.println(errormsg);
                // Log.append(errormsg);
                student.validateSpecial(false);
                noWarnings = false;
            }
            // ---------------------------------------------get_group----------------------------------------------
            if (entry[Config.impStudTeamName] != null) { // TODO: doesnt do anything yet
                student.group(entry[Config.impStudTeamName]);
            }
            // -------------------------------------------get_lastRoom---------------------------------------------
            try {
                if (entry[Config.impStudLastBuild].equals("")) {
                    String errormsg = "Schueler:in " + student.name() + " hat nicht angegeben, in welchem Internat "
                            + "er/sie dieses Jahr gewohnt hat!";
                    // Log.append(errormsg);
                    System.out.println(errormsg);
                    student.validateLastYearsBuilding(false);
                } else {
                    Building lastYearsBuilding = ImportFiles.getLastYearsBuilding(entry[Config.impStudLastBuild]);
                    student.lastYearsBuilding(lastYearsBuilding);
                    student.validateLastYearsBuilding(true);
                    String lastYearsRoomsName = "";
                    for (int i = Config.impStudLastBuild + 1; i < Config.impStudGrade; i++) {
                        if (!entry[i].equals("")) {
                            lastYearsRoomsName = entry[i];
                            break;
                        }
                    }
                    if (lastYearsRoomsName.equals("")) {
                        String errormsg = "Schueler:in " + student.name() + " hat nicht angegeben, in welchem Zimmer " +
                                "er/sie dieses Jahr gewohnt hat!";
                        // Log.append(errormsg);
                        student.validateLastYearsRoom(false);
                        System.out.println(errormsg);
                    } else {
                        Room lastYearsRoom = ImportFiles.getLastYearsRoom(student.lastYearsBuilding(),
                                lastYearsRoomsName);
                        student.lastYearsRoom(lastYearsRoom);
                        student.validateLastYearsRoom(true);
                    }
                }
            } catch (NoRoomWithThatNameException | NoBuildingWithThatNameException e) {
                Log.append(e.getMessage());
                System.err.println(e.getMessage());
            }

            // only overwrite, if survey has been filled out correctly
            if ((overwrite != null) && overwrite.gradeValid()) {
                if (overwrite.lastYearsBuildingValid() && overwrite.lastYearsRoomValid()) {
                    overwrite = student;
                } else if (!student.lastYearsRoomValid()) {
                    overwrite = student;
                }
            } else {
                int debug = 3;
            }
            if (!duplicate) {
                ImportFiles.students.add(student);
            }
        }
        reader.close();
        return noWarnings;
    }

    private static Building getLastYearsBuilding(String nameOfBuilding) throws NoBuildingWithThatNameException {
        for (Building building : ImportFiles.buildings) {
            if (building.name().equals(nameOfBuilding)) {
                return building;
            }
        }
        throw new NoBuildingWithThatNameException("Das Internat " + nameOfBuilding + " konnte nicht gefunden werden!");
    }

    private static Room getLastYearsRoom(Building lastYearsBuilding, String nameOfRoom)
            throws NoRoomWithThatNameException {
        Room lastYearsRoom = lastYearsBuilding.getRoom(nameOfRoom);
        if (lastYearsRoom == null) {
            return ImportFiles.getLastYearsRoomAlternative(lastYearsBuilding, nameOfRoom);
        }
        return lastYearsRoom;
    }

    private static Room getLastYearsRoomAlternative(Building lastYearsBuilding, String nameOfRoom)
            throws NoRoomWithThatNameException {
        Room lastYearsRoom = lastYearsBuilding.getRoomAlternative(nameOfRoom);
        if (lastYearsRoom == null) {
            throw new NoRoomWithThatNameException("Das Zimmer " + nameOfRoom + " im Internat "
                    + lastYearsBuilding.name() + " konnte nicht gefunden werden!");
        }
        String errormsg = "Das Zimmer " + nameOfRoom + " im Internat " + lastYearsBuilding.name()
                + " konnte nicht gefunden werden. Es wurde ein Zimmer mit aehnlichem Namen gefunden: "
                + lastYearsRoom.officialRoomNumber();
        Log.append(errormsg);
        System.out.println(errormsg);
        return lastYearsRoom;
    }

    private static Building findBuilding(String nameBuilding) {
        for (Building building : ImportFiles.buildings) {
            if (building.name().equals(nameBuilding)) {
                return building;
            }
        }
        return null;
    }

    private static Building getRandomBuilding(Building building) {
        if (building == null) {
            building = new Building("ThereShallBeNoBuildingWithThisName");
        }
        Building random = new Building("ThereShallBeNoBuildingWithThisName");
        boolean doWhile = true;
        while (doWhile || random.name().equals(building.name())) {
            int n = new Random().nextInt(ImportFiles.buildings.size());
            random = ImportFiles.buildings.get(n);
            doWhile = false;
        }
        return random;
    }

    private static Room getRandomRoom(Building building, Room room) {
        if (room == null) {
            room = new Room("ThereShallBeNoRoomWithThisName");
        }
        Room random = new Room("ThereShallBeNoRoomWithThisName");
        boolean doWhile = true;
        while (doWhile || random.officialRoomNumber().equals(room.officialRoomNumber())) {
            int n = new Random().nextInt(building.rooms().size());
            System.out.println(n);
            random = building.rooms().get(n);
            doWhile = false;
        }
        return random;
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