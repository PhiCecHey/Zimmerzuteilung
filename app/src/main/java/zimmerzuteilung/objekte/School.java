package zimmerzuteilung.objekte;

import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class School {
    private Map<Integer, Grade> grades = new HashMap<>();

    public School() {
        this.initGrades();
        this.initClasses();
    }

    private void initGrades() {
        this.grades.put(9, new Grade(9));
        this.grades.put(10, new Grade(10));
        this.grades.put(11, new Grade(11));
        this.grades.put(12, new Grade(12));
    }

    private void initClasses() {
        // 9:
        Class m9 = new Class(Class.SPECIALIZATION.MUSIK);
        Class s9 = new Class(Class.SPECIALIZATION.SPRACHEN);
        Class n9 = new Class(Class.SPECIALIZATION.NAWI);

        // 10:
        Class m10 = new Class(Class.SPECIALIZATION.MUSIK);
        Class s10 = new Class(Class.SPECIALIZATION.SPRACHEN);
        Class n10 = new Class(Class.SPECIALIZATION.NAWI);

        // 11:
        Class m11 = new Class(Class.SPECIALIZATION.MUSIK);
        Class s11 = new Class(Class.SPECIALIZATION.SPRACHEN);
        Class n11 = new Class(Class.SPECIALIZATION.NAWI);

        // 12:
        Class m12 = new Class(Class.SPECIALIZATION.MUSIK);
        Class s12 = new Class(Class.SPECIALIZATION.SPRACHEN);
        Class n12 = new Class(Class.SPECIALIZATION.NAWI);

        // stufen:
        for (var entry : this.grades.entrySet()) {
            switch (entry.getKey()) {
                case 9:
                    entry.getValue().addClass(m9);
                    entry.getValue().addClass(s9);
                    entry.getValue().addClass(n9);
                    break;
                case 10:
                    entry.getValue().addClass(m10);
                    entry.getValue().addClass(s10);
                    entry.getValue().addClass(n10);
                    break;
                case 11:
                    entry.getValue().addClass(m11);
                    entry.getValue().addClass(s11);
                    entry.getValue().addClass(n11);
                    break;
                case 12:
                    entry.getValue().addClass(m12);
                    entry.getValue().addClass(s12);
                    entry.getValue().addClass(n12);
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    boolean addStudent(final Student student, final Class clas) {
        if (clas.containsStudent(student)) {
            return false;
        }
        clas.addStudent(student);
        return true;
    }

    Class findClass(Class.SPECIALIZATION special, int grade) {
        for (var entry : this.grades.entrySet()) {
            if (entry.getValue().getGrade() == grade) {
                return entry.getValue().getClass(special);
            }
        }
        return null;
    }

    private Class findRandomClass() {
        Class clas;
        int randGrade = ThreadLocalRandom.current().nextInt(9, 13);
        int randSpecial = ThreadLocalRandom.current().nextInt(0, 3);
        Class.SPECIALIZATION special;

        switch (randSpecial) {
            case 0:
                special = Class.SPECIALIZATION.MUSIK;
                break;
            case 1:
                special = Class.SPECIALIZATION.SPRACHEN;
                break;
            case 2:
                special = Class.SPECIALIZATION.NAWI;
                break;
            default:
                special = Class.SPECIALIZATION.MUSIK;
                System.out.println("Something went wrong: School get Random Class randSpecial");
                break;
        }

        switch (randGrade) {
            case 9:
                clas = this.findClass(special, 9);
                break;
            case 10:
                clas = this.findClass(special, 10);
                break;
            case 11:
                clas = this.findClass(special, 11);
                break;
            case 12:
                clas = this.findClass(special, 12);
                break;
            default:
                clas = this.findClass(special, 9);
                System.out.println("Something went wrong: School get Random Class randGrade");
                break;
        }

        return clas;
    }

    public void createRandomSchool(int nStudents) {
        for (int i = 0; i < nStudents; ++i) {
            this.addStudent(new Student(), this.findRandomClass());
        }
    }

    

}
