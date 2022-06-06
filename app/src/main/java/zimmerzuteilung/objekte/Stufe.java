package zimmerzuteilung.objekte;

import java.util.Map;
import java.util.HashMap;

public class Stufe {
    private int stufe;
    private Map<String, Klasse> klassen = new HashMap<>();

    public Stufe(final int s) throws IllegalArgumentException {
        if (s < 9 || s > 12) {
            throw new IllegalArgumentException();
        }

        this.stufe = s;
    }

    boolean addKlasse(final Klasse klasse) {
        for (Map.Entry<String, Klasse> eintrag : this.klassen.entrySet()) {
            if (eintrag.getKey() == klasse.getName()) {
                return false;
            }
        }

        this.klassen.put(klasse.getName(), klasse);
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
