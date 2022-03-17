package zimmerzuteilung;

import java.util.UUID;
public class Schueler {
    String name;  // Max Mustermann
    Geschlecht geschlecht;  // m, w, d
    Klasse klasse;  // 9-12
    final UUID id = UUID.randomUUID();

    public Schueler(String name, Geschlecht geschlecht, int stufe, Zweig zweig){
        this.name = name;
        this.geschlecht = geschlecht;
        this.klasse = Klasse.findeOderErstelleKlasse(stufe, zweig);
        this.klasse.schuelerHinzufuegen(this);
    }

}