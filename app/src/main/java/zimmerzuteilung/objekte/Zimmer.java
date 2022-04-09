package zimmerzuteilung.objekte;

import java.util.Map;
import java.util.HashMap;


public class Zimmer {
    static int anzahl = 0;

    final int ID;
    String nummer;
    int kapazitaet;
    Map<Integer, Schueler> bewohner = new HashMap<>();

    Zimmer(Internat internat, int kapazitaet) throws IllegalArgumentException{
        ++Zimmer.anzahl;
        this.ID = Zimmer.anzahl;
        this.kapazitaet = kapazitaet;

        internat.addZimmer(this);
    }

    Zimmer(Internat internat, String nummer, int kapazitaet) throws IllegalArgumentException{
        ++Zimmer.anzahl;
        this.ID = Zimmer.anzahl;
        this.kapazitaet = kapazitaet;

        internat.addZimmer(this);
        this.nummer = nummer;
    }

    boolean addSchueler(Schueler schueler){
        if (this.kapazitaet <= this.bewohner.size()){
            return false;
        }

        for(Map.Entry<Integer, Schueler> eintrag : this.bewohner.entrySet()){
            if(eintrag.getKey() == schueler.ID){
                return false;
            }
        }

        this.bewohner.put(schueler.ID, schueler);
        return true;
    }
}
