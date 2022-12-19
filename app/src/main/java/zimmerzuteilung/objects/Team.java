package zimmerzuteilung.objects;

import java.util.ArrayList;
import java.util.ListIterator;
import zimmerzuteilung.objects.GENDER;

public class Team {
    private class DifferentgenderException extends Exception {
        private DifferentgenderException(String str) {
            super(str);
        }
    }

    public ArrayList<Student> members;
    private Wish wish;
    private int id;
    private GENDER gender;
    private String name; // for json
    private String time; // for json
    private static int count = 0;

    public Team() {
        Team.count++;
        this.id = Team.count;
    }

    public Team(ArrayList<Student> students) throws DifferentgenderException {
        // check for same gender
        ListIterator<Student> it = students.listIterator();
        while (it.hasNext()) {
            Student first = it.next();
            while (it.hasNext()) {
                Student second = it.next();
                if (first.gender() != second.gender()) {
                    throw new DifferentgenderException("Die Schüler eines " +
                            "Teams müssen vom gleichen Geschlecht sein (" +
                            first.name() + ", " + second.name() + ").");
                }
            }
        }
        this.members = students;
        Team.count++;
        this.id = Team.count;
    }

    public Team(ArrayList<Student> students, Wish w) throws DifferentgenderException {
        // check for same gender
        ListIterator<Student> it = students.listIterator();
        while (it.hasNext()) {
            Student first = it.next();
            while (it.hasNext()) {
                Student second = it.next();
                if (first.gender() != second.gender()) {
                    throw new DifferentgenderException("Die Schüler eines " +
                            "Teams müssen vom gleichen Geschlecht sein (" +
                            first.name() + ", " + second.name() + ").");
                }
            }
        }
        this.members = students;
        this.wish = w;
        Team.count++;
        this.id = Team.count;
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
