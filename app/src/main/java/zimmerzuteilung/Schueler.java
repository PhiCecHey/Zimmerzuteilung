package zimmerzuteilung;

//import java.util.UUID;

public class Schueler {
    public enum Geschlecht {
        m, w, d
    }

    /* Die Schueler werden der Reihe nach durchgezaehlt und erhalten somit ihre ID. 
    Vorteil zur randomID: verbraucht weniger Platz und ist effizienter im Vergleich. */
    static int anzahl = 0; 

    String name;
    Geschlecht geschlecht;
    final int ID;

    public Schueler(String name, Geschlecht geschlecht){
        this.name = name;
        this.geschlecht = geschlecht;

        ++ Schueler.anzahl;  // der erste Schueler kriegt also Nr. 1
        this.ID = Schueler.anzahl;
    }

}