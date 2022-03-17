package zimmerzuteilung;

import java.util.Map;
import java.util.HashMap;

public class Zweig {
    public enum BEZEICHNUNG {
        MUSIK, SPRACHEN, NAWI;
    }

    BEZEICHNUNG bezeichung;
    Map<Klasse.BEZEICHNUNG, Klasse> klassen = new HashMap<>();

    Zweig(BEZEICHNUNG bezeichnung) {
        this.bezeichung = bezeichnung;
    }

    boolean addKlasse(Klasse klasse){
        for(Map.Entry<Klasse.BEZEICHNUNG, Klasse> eintrag : this.klassen.entrySet()){
            if(eintrag.getKey() == klasse.bezeichnung){
                return false;
            }
        }

        this.klassen.put(klasse.bezeichnung, klasse);
        return true;
    }

    public void initKlassen() {
        switch (this.bezeichung) {
            case MUSIK:
                this.klassen.put(Klasse.BEZEICHNUNG.neunM, new Klasse(Klasse.BEZEICHNUNG.neunM));
                this.klassen.put(Klasse.BEZEICHNUNG.zehnM, new Klasse(Klasse.BEZEICHNUNG.zehnM));
                this.klassen.put(Klasse.BEZEICHNUNG.elfM, new Klasse(Klasse.BEZEICHNUNG.elfM));
                this.klassen.put(Klasse.BEZEICHNUNG.zwoelfM, new Klasse(Klasse.BEZEICHNUNG.zwoelfM));
            case SPRACHEN:
                this.klassen.put(Klasse.BEZEICHNUNG.neunS, new Klasse(Klasse.BEZEICHNUNG.neunS));
                this.klassen.put(Klasse.BEZEICHNUNG.zehnS, new Klasse(Klasse.BEZEICHNUNG.zehnS));
                this.klassen.put(Klasse.BEZEICHNUNG.elfS, new Klasse(Klasse.BEZEICHNUNG.elfS));
                this.klassen.put(Klasse.BEZEICHNUNG.zwoelfS, new Klasse(Klasse.BEZEICHNUNG.zwoelfS));
            case NAWI:
                this.klassen.put(Klasse.BEZEICHNUNG.neunN, new Klasse(Klasse.BEZEICHNUNG.neunN));
                this.klassen.put(Klasse.BEZEICHNUNG.zehnN, new Klasse(Klasse.BEZEICHNUNG.zehnN));
                this.klassen.put(Klasse.BEZEICHNUNG.elfN, new Klasse(Klasse.BEZEICHNUNG.elfN));
                this.klassen.put(Klasse.BEZEICHNUNG.zwoelfN, new Klasse(Klasse.BEZEICHNUNG.zwoelfN));
            default:
                throw new IllegalArgumentException();
        }
    }

}
