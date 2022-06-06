package zimmerzuteilung.objekte;

import java.util.Map;
import java.util.HashMap;

public class Zimmer {
    private static int anzahl = 0;

    private final int id;
    private String nummer;
    private int kapazitaet;
    private Map<Integer, Schueler> bewohner = new HashMap<>();

    Zimmer(final Internat i, final int k) throws IllegalArgumentException {
        ++Zimmer.anzahl;
        this.id = Zimmer.anzahl;
        this.kapazitaet = k;

        i.addZimmer(this);
    }

    Zimmer(final Internat internat, final String n, final int k)
            throws IllegalArgumentException {
        ++Zimmer.anzahl;
        this.id = Zimmer.anzahl;
        this.kapazitaet = k;

        internat.addZimmer(this);
        this.nummer = n;
    }

    public int getId() {
        return this.id;
    }

    boolean addSchueler(final Schueler schueler) {
        if (this.kapazitaet <= this.bewohner.size()) {
            return false;
        }

        for (Map.Entry<Integer, Schueler> eintrag : this.bewohner.entrySet()) {
            if (eintrag.getKey() == schueler.getId()) {
                return false;
            }
        }

        this.bewohner.put(schueler.getId(), schueler);
        return true;
    }
}
