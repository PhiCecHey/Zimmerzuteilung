package zimmerzuteilung.objects;

public class Student {
    private static int count = 0;

    private String name;
    private GENDER gender;
    private String userName;
    private final int id;
    private int grade;
    private SPECIALIZATION special;
    private String moodleDate;
    private String email;
    private String groupName;
    private Room lastYearsRoom;
    private Building lastYearsBuilding;
    private boolean[] registrationValid;

    public Student(final String n, final String u) {
        this.name = n;
        this.userName = u;
        this.registrationValid = new boolean[] { false, false, false, false, false, false };

        ++Student.count;
        this.id = Student.count;
    }

    public void validateGrade(boolean gradeValid) {
        this.registrationValid[0] = gradeValid;
    }

    public void validateLastYearsRoom(boolean roomValid) {
        this.registrationValid[1] = roomValid;
    }

    public void validateLastYearsBuilding(boolean buildingValid) {
        this.registrationValid[2] = buildingValid;
    }

    public void validateTeam(boolean teamValid) {
        this.registrationValid[3] = teamValid;
    }

    public void validateSpecial(boolean specialValid) {
        this.registrationValid[4] = specialValid;
    }

    public void validateGender(boolean genderValid) {
        this.registrationValid[5] = genderValid;
    }

    public boolean genderValid() {
        return this.registrationValid[5];
    }

    public boolean teamValid() {
        return this.registrationValid[3];
    }

    public boolean gradeValid() {
        return this.registrationValid[0];
    }

    public boolean lastYearsRoomValid() {
        return this.registrationValid[1];
    }

    public boolean lastYearsBuildingValid() {
        return this.registrationValid[2];
    }

    public boolean problems() {
        boolean problem = false;
        for (boolean v : this.registrationValid) {
            if (!v) {
                problem = true;
            }
        }
        return problem;
    }

    public String errormsg() {
        if (!this.problems()) {
            return "";
        }

        String errormsg = "Bei Schüler:in " + this.name + " gibt es folgende Probleme: ";
        if (!this.registrationValid[0]) {
            errormsg += " ~ Klassenstufe unbekannt ";
        }
        if (!this.registrationValid[1]) {
            errormsg += " ~ diesjähriges Zimmer unbekannt";
        }
        if (!this.registrationValid[2]) {
            errormsg += " ~ diesjähriges Internat unbekannt";
        }
        if (!this.registrationValid[3]) {
            errormsg += " ~ gehört keinem Team an";
        }
        if (!this.registrationValid[4]) {
            errormsg += " ~ Zweig unbekannt";
        }
        if (!this.registrationValid[5]) {
            errormsg += " ~ Geschlecht unbekannt";
        }
        return errormsg;
    }

    public String name() {
        return this.name;
    }

    public GENDER gender() {
        return this.gender;
    }

    public void gender(GENDER g) {
        this.gender = g;
    }

    public int grade() {
        return this.grade;
    }

    public void grade(int g) {
        this.grade = g;
    }

    public void special(SPECIALIZATION s) {
        this.special = s;
    }

    public String userName() {
        return this.userName;
    }

    public String moodleDate() {
        return this.moodleDate;
    }

    public void moodleDate(String mD) {
        this.moodleDate = mD;
    }

    public void email(String em) {
        this.email = em;
    }

    public void group(String g) {
        this.groupName = g;
    }

    public String group() {
        return this.groupName;
    }

    public void lastYearsRoom(Room r) {
        this.lastYearsRoom = r;
    }

    public Room lastYearsRoom() {
        return this.lastYearsRoom;
    }

    public void lastYearsBuilding(Building b) {
        this.lastYearsBuilding = b;
    }

    public Building lastYearsBuilding() {
        return this.lastYearsBuilding;
    }
}
