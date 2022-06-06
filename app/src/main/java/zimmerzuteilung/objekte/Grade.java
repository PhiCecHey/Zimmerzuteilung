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

    boolean addClass(final Class clas) {
        for (Map.Entry<Integer, Class> entry : this.classes.entrySet()) {
            if (entry.getKey() == clas.getId()) {
                return false;
            }
        }

        this.classes.put(clas.getId(), clas);
        return true;
    }

    /*
     * public void initKlassen() {
     * switch (this.stufe) {
     * case 9:
     * this.klassen.put(Klasse.BEZEICHNUNG.neunM, new
     * Klasse(Klasse.BEZEICHNUNG.neunM));
     * this.klassen.put(Klasse.BEZEICHNUNG.neunS, new
     * Klasse(Klasse.BEZEICHNUNG.neunS));
     * this.klassen.put(Klasse.BEZEICHNUNG.neunN, new
     * Klasse(Klasse.BEZEICHNUNG.neunN));
     * case 10:
     * this.klassen.put(Klasse.BEZEICHNUNG.zehnM, new
     * Klasse(Klasse.BEZEICHNUNG.zehnM));
     * this.klassen.put(Klasse.BEZEICHNUNG.zehnS, new
     * Klasse(Klasse.BEZEICHNUNG.zehnS));
     * this.klassen.put(Klasse.BEZEICHNUNG.zehnN, new
     * Klasse(Klasse.BEZEICHNUNG.zehnN));
     * case 11:
     * this.klassen.put(Klasse.BEZEICHNUNG.elfM, new
     * Klasse(Klasse.BEZEICHNUNG.elfM));
     * this.klassen.put(Klasse.BEZEICHNUNG.elfS, new
     * Klasse(Klasse.BEZEICHNUNG.elfS));
     * this.klassen.put(Klasse.BEZEICHNUNG.elfN, new
     * Klasse(Klasse.BEZEICHNUNG.elfN));
     * case 12:
     * this.klassen.put(Klasse.BEZEICHNUNG.zwoelfM, new
     * Klasse(Klasse.BEZEICHNUNG.zwoelfM));
     * this.klassen.put(Klasse.BEZEICHNUNG.zwoelfS, new
     * Klasse(Klasse.BEZEICHNUNG.zwoelfS));
     * this.klassen.put(Klasse.BEZEICHNUNG.zwoelfN, new
     * Klasse(Klasse.BEZEICHNUNG.zwoelfN));
     * default:
     * throw new IllegalArgumentException();
     * }
     * }
     */
}
