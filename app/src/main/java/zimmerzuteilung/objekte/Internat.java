package zimmerzuteilung.objekte;

import java.util.Map;
import java.util.HashMap;

public class Internat {

    /*
     * public enum BEZEICHNUNG{
     * Klausur, II, III, Fueha, V, VI, VII;
     * }
     * BEZEICHNUNG bezeichnung;
     * Internat(BEZEICHNUNG bezeichnung){
     * this.bezeichnung = bezeichnung;
     * }
     */

    private String name;
    private Map<Integer, Zimmer> zimmer = new HashMap<>();

    Internat(final String n) {
        this.name = n;
    }

    boolean addZimmer(Zimmer zimmer) {
        for (Map.Entry<Integer, Zimmer> eintrag : this.zimmer.entrySet()) {
            if (eintrag.getKey() == zimmer.getId()) {
                return false;
            }
        }

        this.zimmer.put(zimmer.getId(), zimmer);
        return true;
    }

    public void initZimmer() {
        // von config file lesen
        // zimmer neu erstellen und zur map hinzufügen
    }

}
