package zimmerzuteilung;

import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

public class Klasse {
    /*
    public enum BEZEICHNUNG {
        neunM, neunS, neunN,
        zehnM, zehnS, zehnN,
        elfM, elfS, elfN,
        zwoelfM, zwoelfS, zwoelfN;
    }
    BEZEICHNUNG bezeichnung;
    Klasse(BEZEICHNUNG bezeichnung){
        this.bezeichnung = bezeichnung;
    }
    */

    String name;
    Map<UUID, Schueler> schuelerDerKlasse = new HashMap<>();

    Klasse(String name){
        this.name = name;
    }
    
}
