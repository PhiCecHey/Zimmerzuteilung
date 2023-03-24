package zimmerzuteilung.objects;

import java.util.ArrayList;

public class Team {
    private ArrayList<Student> members;
    private Wish wish;
    private int id;
    private GENDER gender;
    private String name; // for json
    private String time; // for json
    private Room allocatedRoom;
    private float score;
    private static int count = 0;

    public Team() {
        this.members = new ArrayList<>();
        this.wish = new Wish();
        Team.count++;
        this.id = Team.count;
    }

    public boolean allocateRoom(Room r) {
        if (this.allocatedRoom != null) {
            return false;
        }
        this.allocatedRoom = r;
        return true;
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

    public String membersToCsv() {
        String str = "";
        for (Student s : this.members) {
            str += s.userName() + " ";
        }
        return str;
    }
}
