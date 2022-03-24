package zimmerzuteilung.objekte;

import java.util.Map;
import java.util.HashMap;


public class Zimmer {
    static int anzahl = 0;

    final int ID;
    String nummer;
    Map<Integer, Schueler> bewohner = new HashMap<>();

    Zimmer(Internat internat) throws IllegalArgumentException{
        ++Zimmer.anzahl;
        this.ID = Zimmer.anzahl;

        internat.addZimmer(this);
    }

    Zimmer(Internat internat, String nummer) throws IllegalArgumentException{
        ++Zimmer.anzahl;
        this.ID = Zimmer.anzahl;

        internat.addZimmer(this);
        this.nummer = nummer;
    }

    boolean addSchueler(Schueler schueler){
        for(Map.Entry<Integer, Schueler> eintrag : this.bewohner.entrySet()){
            if(eintrag.getKey() == schueler.ID){
                return false;
            }
        }

        this.bewohner.put(schueler.ID, schueler);
        return true;
    }
}
