package zimmerzuteilung.objekte;

import java.util.ArrayList;
import java.util.ListIterator;

public class Team {
    private class DifferentSexException extends Exception {
        private DifferentSexException(String str) {
            super(str);
        }
    }

    public ArrayList<Student> members;
    private Wish wish;
    private int id;
    private static int count = 0;

    public Team() {
        Team.count++;
        this.id = Team.count;
    }

    public Team(ArrayList<Student> students) throws DifferentSexException {
        // check for same sex
        ListIterator<Student> it = students.listIterator();
        while (it.hasNext()) {
            Student first = it.next();
            while (it.hasNext()) {
                Student second = it.next();
                if (first.getSex() != second.getSex()) {
                    throw new DifferentSexException("Die Schüler eines " +
                            "Teams müssen vom gleichen Geschlecht sein (" +
                            first.getName() + ", " + second.getName() + ").");
                }
            }
        }
        this.members = students;
        Team.count++;
        this.id = Team.count;
    }

    public Team(ArrayList<Student> students, Wish w) throws DifferentSexException {
        // check for same sex
        ListIterator<Student> it = students.listIterator();
        while (it.hasNext()) {
            Student first = it.next();
            while (it.hasNext()) {
                Student second = it.next();
                if (first.getSex() != second.getSex()) {
                    throw new DifferentSexException("Die Schüler eines " +
                            "Teams müssen vom gleichen Geschlecht sein (" +
                            first.getName() + ", " + second.getName() + ").");
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

    public void setWish(Wish w) {
        this.wish = w;
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
