package zimmerzuteilung;

import java.util.Map;
import java.util.HashMap;

public class Zweig{
    public enum enumZweig{
        MUSIK, SPRACHEN, NAWI;
    }    

    enumZweig bezeichung;
    Map<Klasse.enumKlasse, Klasse> klassen = new HashMap<>();

    Zweig(enumZweig bezeichnung){
        this.bezeichung = bezeichnung;
    }

    boolean klasseHinzufuegen(Klasse klasse){
        for(Map.Entry<Klasse.enumKlasse, Klasse> eintrag : this.klassen.entrySet()){
            if(eintrag.getKey() == klasse.bezeichnung){
                return false;
            }
        }

        this.klassen.put(klasse.bezeichnung, klasse);
        return true;
    }
}
