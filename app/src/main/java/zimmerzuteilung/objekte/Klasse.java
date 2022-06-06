package zimmerzuteilung.objekte;

import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

public class Klasse {
    /*
     * public enum BEZEICHNUNG {
     * neunM, neunS, neunN,
     * zehnM, zehnS, zehnN,
     * elfM, elfS, elfN,
     * zwoelfM, zwoelfS, zwoelfN;
     * }
     * BEZEICHNUNG bezeichnung;
     * Klasse(BEZEICHNUNG bezeichnung){
     * this.bezeichnung = bezeichnung;
     * }
     */

    private String name;
    private Map<UUID, Schueler> schuelerDerKlasse = new HashMap<>();

    Klasse(final String n) {
        this.name = n;
    }

    public String getName() {
        return this.name;
    }

}
