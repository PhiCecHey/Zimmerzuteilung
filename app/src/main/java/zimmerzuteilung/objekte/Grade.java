package zimmerzuteilung.objekte;

import java.util.Map;
import java.util.HashMap;

class Grade {
    private int grade;
    private Map<Integer, Class> classes = new HashMap<>();

    public Grade(final int g) throws IllegalArgumentException {
        if (g < 9 || g > 12) {
            throw new IllegalArgumentException();
        }

        this.grade = g;
    }

    public Map<Integer, Class> getClasses() {
        return this.classes;
    }

    boolean addClass(final Class clas) {
        for (Map.Entry<Integer, Class> entry : this.classes.entrySet()) {
            if (entry.getKey() == clas.getId()) {
                return false;
            }
        }

        this.classes.put(clas.getId(), clas);
        return true;
    }

    public Class getClass(Class.SPECIALIZATION special) {
        for (var entry : this.classes.entrySet()) {
            if (entry.getValue().getSpecialization().equals(special)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public int getGrade(){
        return this.grade;
    }
}
