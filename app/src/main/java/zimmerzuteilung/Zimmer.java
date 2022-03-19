package zimmerzuteilung;

import java.util.Map;
import java.util.HashMap;


public class Zimmer {
    int ID;
    String nummer;
    Map<Integer, Schueler> bewohner = new HashMap<>();

    Zimmer(Internat internat, int ID) throws IllegalArgumentException{
        if(ID < 0){
            throw new IllegalArgumentException();
        }
        this.ID = ID;
        internat.addZimmer(this);
        this.nummer = Integer.toString(this.ID);
    }

    Zimmer(Internat internat, int ID, String nummer) throws IllegalArgumentException{
        if(ID < 0){
            throw new IllegalArgumentException();
        }
        this.ID = ID;
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
