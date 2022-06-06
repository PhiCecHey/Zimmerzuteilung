package zimmerzuteilung.objekte;

import java.util.Map;
import java.util.HashMap;

public class Class {
    public enum SPECIALIZATION {
        NAWI, MUSIK, SPRACHEN;
    }

    private static int count = 0;

    private SPECIALIZATION specialization;
    private int id;
    private Map<Integer, Student> studentsOfClass = new HashMap<>();

    public Class(final SPECIALIZATION special) {
        this.specialization = special;
        this.id = Class.count;
        ++Class.count;
    }

    public int getId() {
        return this.id;
    }

    public SPECIALIZATION getSpecialization() {
        return this.specialization;
    }

    Student findStudent(final Student student) {
        for (var entry : this.studentsOfClass.entrySet()) {
            if (entry.getKey() == student.getId()) {
                return entry.getValue();
            }
        }
        return null;
    }

    boolean containsStudent(final Student student) {
        for (var entry : this.studentsOfClass.entrySet()) {
            if (entry.getKey() == student.getId()) {
                return true;
            }
        }
        return false;
    }

    boolean addStudent(final Student student) {
        if (this.findStudent(student) == null) {
            this.studentsOfClass.put(student.getId(), student);
            return true;
        }
        System.out.println("student already in class");
        return false;
    }

    Map<Integer, Student> getStudents(){
        return this.studentsOfClass;
    }
}
