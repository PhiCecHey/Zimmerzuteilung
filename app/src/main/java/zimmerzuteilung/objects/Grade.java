package zimmerzuteilung.objects;

import java.util.HashMap;
import java.util.Map;

class Grade {
    private int grade;
    private Map<Integer, Clas> classes = new HashMap<>();

    Grade(final int g) throws IllegalArgumentException {
        if (g < 9 || g > 12) {
            throw new IllegalArgumentException();
        }

        this.grade = g;
    }

    Map<Integer, Clas> getClasses() {
        return this.classes;
    }

    int getGrade() {
        return this.grade;
    }

    @Override
    public String toString() {
        String string = "Grade{grade: " + this.grade + ", classes: [";

        for (Clas clas : this.classes.values()) {
            string += clas.toString() + ", ";
        }

        string += "]}";
        return string;
    }

    boolean addClass(final Clas clas) {
        for (Map.Entry<Integer, Clas> entry : this.classes.entrySet()) {
            if (entry.getKey() == clas.getId()) {
                System.out.println("Class already in Grade!");
                return false;
            }
        }
        this.classes.put(clas.getId(), clas);
        return true;
    }

    Clas findClass(final SPECIALIZATION special) {
        for (var entry : this.classes.entrySet()) {
            if (entry.getValue().getSpecialization() == special) {
                return entry.getValue();
            }
        }
        return null;
    }
}
