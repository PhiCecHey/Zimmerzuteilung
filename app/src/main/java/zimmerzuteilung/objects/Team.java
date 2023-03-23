package zimmerzuteilung.objects;

import java.util.ArrayList;
import java.util.ListIterator;
import zimmerzuteilung.Exceptions.*;

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

    public Team(ArrayList<Student> students) throws DifferentGenderException {
        // check for same gender
        ListIterator<Student> it = students.listIterator();
        while (it.hasNext()) {
            Student first = it.next();
            while (it.hasNext()) {
                Student second = it.next();
                if (first.gender() != second.gender()) {
                    throw new DifferentGenderException(
                            "Die Schüler eines " + "Teams müssen vom gleichen Geschlecht sein (" + first.name() + ", "
                                    + second.name() + ").");
                }
            }
        }
        this.members = students;
        Team.count++;
        this.id = Team.count;
    }

    public Team(ArrayList<Student> students, Wish w) throws DifferentGenderException {
        // check for same gender
        ListIterator<Student> it = students.listIterator();
        while (it.hasNext()) {
            Student first = it.next();
            while (it.hasNext()) {
                Student second = it.next();
                if (first.gender() != second.gender()) {
                    throw new DifferentGenderException(
                            "Die Schüler eines " + "Teams müssen vom gleichen eschlecht sein (" + first.name() + ", "
                                    + second.name() + ").");
                }
            }
        }
        this.members = students;
        this.wish = w;
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

    public void wish(Wish w) {
        this.wish = w;
    }

    public String name() {
        return this.name;
    }

    public void name(String n) {
        this.name = n;
    }

    public String time() {
        return this.time;
    }

    public void time(String t) {
        this.time = t;
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

    public boolean hasStudent(String userName) {
        for (Student student : this.members) {
            if (student.userName().equals(userName)) {
                return true;
            }
        }
        return false;
    }

    public String membersToCsv() {
        String str = "";
        for (Student s : this.members) {
            str += s.userName() + " ";
        }
        return str;
    }

    @Override
    public String toString() {
        String res = "Team{id: " + this.id + ", members: ";
        for (Student member : this.members) {
            res += member.toString() + ", ";
        }
        res += this.wish.toString() + "}";
        return res;
    }
}
