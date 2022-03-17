package zimmerzuteilung;

import java.util.UUID;

public class Schueler {
    public enum Geschlecht {
        m, w, d
    }

    String name;
    Geschlecht geschlecht;
    final UUID ID = UUID.randomUUID();

    public Schueler(String name, Geschlecht geschlecht){
        this.name = name;
        this.geschlecht = geschlecht;
    }

}