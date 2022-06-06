package zimmerzuteilung.objekte;

import java.util.Map;
import java.util.HashMap;

public class School {
    private static Map<Integer, Grade> grades = new HashMap<>();

    public static void initSchule() {
        School.initGrades();
        School.initClasses();
    }

    private static void initGrades() {
        School.grades.put(9, new Grade(9));
        School.grades.put(10, new Grade(10));
        School.grades.put(11, new Grade(11));
        School.grades.put(12, new Grade(12));
    }

    private static void initClasses() {
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
        for (Map.Entry<Integer, Grade> entry : School.grades.entrySet()) {
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

    public static boolean addStudent(final Student student, final Class clas) {
        if (clas.containsStudent(student)) {
            return false;
        }
        clas.addStudent(student);
        return true;
    }
}
