package zimmerzuteilung.objects;

import java.util.HashMap;
import java.util.Map;

public class Clas {
    private static int count = 0;

    private SPECIALIZATION specialization;
    private int id;
    private Map<Integer, Student> students = new HashMap<>();

    public Clas(final SPECIALIZATION special) {
        this.specialization = special;
        this.id = Clas.count;
        ++Clas.count;
    }

    public int getId() {
        return this.id;
    }

    public SPECIALIZATION getSpecialization() {
        return this.specialization;
    }

    Map<Integer, Student> getStudents() {
        return this.students;
    }

    @Override
    public String toString() {
        String string = "Class{id: " + this.id + ", specialization: "
                + this.specialization;

        for (Student student : this.students.values()) {
            string += student.toString() + ", ";
        }

        string += "}";
        return string;
    }

    Student findStudent(final Student student) {
        for (var entry : this.students.entrySet()) {
            if (entry.getKey() == student.id()) {
                return entry.getValue();
            }
        }
        return null;
    }

    boolean containsStudent(final Student student) {
        for (var entry : this.students.entrySet()) {
            if (entry.getKey() == student.id()) {
                return true;
            }
        }
        return false;
    }

    boolean addStudent(final Student student) {
        if (this.findStudent(student) == null) {
            this.students.put(student.id(), student);
            return true;
        }
        System.out.println("student already in class");
        return false;
    }
}
