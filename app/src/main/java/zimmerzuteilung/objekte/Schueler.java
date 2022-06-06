package zimmerzuteilung.objekte;

//import java.util.UUID;

public class Schueler {
    public enum Geschlecht {
        m, w, d
    }

    /*
     * Die Schueler werden der Reihe nach durchgezaehlt und erhalten
     * somit ihre ID. Vorteil zur randomID: verbraucht weniger Platz
     * und ist effizienter im Vergleich.
     */
    private static int anzahl = 0;

    private final String name;
    private final Geschlecht geschlecht;
    private final int id;

    public Schueler(final String n, final Geschlecht g) {
        this.name = n;
        this.geschlecht = g;

        ++Schueler.anzahl; // der erste Schueler kriegt also Nr. 1
        this.id = Schueler.anzahl;
    }

    public int getId() {
        return this.id;
    }

    public Geschlecht getGeschlecht() {
        return this.geschlecht;
    }

}
