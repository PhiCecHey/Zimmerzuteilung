package zimmerzuteilung.objects;

import java.util.ArrayList;

public class Team {
    private ArrayList<Student> members;
    private Wish wish;
    private int id;
    private GENDER gender;
    private String name;
    private String date;
    private Room allocatedRoom;
    private float score;
    private boolean[] wishValid;

    private static int count = 0;

    public Team() {
        this.members = new ArrayList<>();
        this.wish = new Wish();
        this.wishValid = new boolean[] { false, false, false, false, false };
        Team.count++;
        this.id = Team.count;
    }

    /**
     * Check if team wants to stay in the room they were in previously and then if
     * so check if the bonus applies.
     * 
     * @return true if bonus applies, else false
     */
    public boolean canStayInRoom() {
        Room room = this.members.get(0).lastYearsRoom();
        for (Student student : this.members) {
            Room r = student.lastYearsRoom();
            if (r == null || (!r.officialRoomNumber().equals(room.officialRoomNumber()))) {
                return false;
            }
        }

        // only return true if team wants to stay in room
        if (this.wish.room1().id() == room.id()) {
            return true;
        }
        return false;
    }

    /**
     * Check if team wants to stay in the building they were in previously and then
     * if
     * so check if the bonus applies.
     * 
     * @return true if bonus applies, else false
     */
    public boolean canStayInBuilding() {
        Building building = this.members.get(0).lastYearsBuilding();
        for (Student student : this.members) {
            Building b = student.lastYearsBuilding();
            if (b == null || (!building.name().equals(b.name()))) {
                return false;
            }
        }

        // only return true if team wants to stay in building
        if (this.wish.building1().id() == building.id()) {
            return true;
        }
        return false;
    }

    public boolean allocateRoom(Room r) {
        if (this.allocatedRoom != null) {
            return false;
        }
        this.allocatedRoom = r;
        return true;
    }

    public void validateB1(boolean wishValid) {
        this.wishValid[0] = wishValid;
    }

    public void validateR1(boolean wishValid) {
        this.wishValid[1] = wishValid;
    }

    public void validateR2(boolean wishValid) {
        this.wishValid[2] = wishValid;
    }

    public void validateB2(boolean wishValid) {
        this.wishValid[3] = wishValid;
    }

    public void validateGen(boolean wishValid) {
        this.wishValid[4] = wishValid;
    }

    public boolean problems() {
        boolean problems = false;
        for (boolean wv : this.wishValid) {
            if (!wv) {
                problems = true;
            }
        }
        return problems;
    }

    public String errorMsg() {
        boolean problem = false;
        for (boolean wv : this.wishValid) {
            if (!wv) {
                problem = true;
                break;
            }
        }
        if (!problem) {
            return "";
        }

        String errormsg = "Team " + this.name + " weist folgende Probleme auf: ";
        if (!this.wishValid[4]) {
            errormsg += " ~ Geschlecht unbekannt ";
        }
        if (!this.wishValid[0]) {
            errormsg += " ~ Erstwunschinternat unbekannt ";
        }
        if (!this.wishValid[1]) {
            errormsg += " ~ Erstwunschzimmer unbekannt ";
        }
        if (!this.wishValid[2]) {
            errormsg += " ~ Zweitwunschzimmer unbekannt";
        }
        if (!this.wishValid[3]) {
            errormsg += " ~ Zweitwunschinternat unbekannt";
        }
        return errormsg;
    }

    public Room allocatedRoom() {
        return this.allocatedRoom;
    }

    public void score(float s) {
        this.score = s;
    }

    public float score() {
        return this.score;
    }

    public int id() {
        return this.id;
    }

    public Wish wish() {
        return this.wish;
    }

    public String name() {
        return this.name;
    }

    public void name(String n) {
        this.name = n;
    }

    public GENDER gender() {
        return this.gender;
    }

    public void gender(GENDER s) {
        this.gender = s;
    }

    public void addStudent(Student student) {
        this.members.add(student);
    }

    public ArrayList<Student> members() {
        return this.members;
    }

    /**
     * Returns the student with the specified username in case they are part of the
     * team.
     * 
     * @param userName
     * @return the student if found, else null
     */
    public Student getStudent(String userName) {
        for (Student student : this.members) {
            if (student.userName().equals(userName)) {
                return student;
            }
        }
        return null;
    }

    public String date() {
        return this.date;
    }

    public void date(String date) {
        this.date = date;
    }

    public String membersToCsv() {
        String str = "";
        for (Student s : this.members) {
            str += s.userName() + " ";
        }
        return str;
    }
}
