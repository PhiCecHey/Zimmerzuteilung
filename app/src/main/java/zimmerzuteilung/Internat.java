package zimmerzuteilung;

import java.util.Map;
import java.util.HashMap;

public class Internat {

    /*
    public enum BEZEICHNUNG{
        Klausur, II, III, Fueha, V, VI, VII;
    }
    BEZEICHNUNG bezeichnung;
    Internat(BEZEICHNUNG bezeichnung){
        this.bezeichnung = bezeichnung;
    }
    */
    
    String name;
    Map<Integer, Zimmer> zimmer = new HashMap<>();

    Internat(String name){
        this.name = name;
    }

    boolean addZimmer(Zimmer zimmer){
        for(Map.Entry<Integer, Zimmer> eintrag : this.zimmer.entrySet()){
            if(eintrag.getKey() == zimmer.ID){
                return false;
            }
        }

        this.zimmer.put(zimmer.ID, zimmer);
        return true;
    }

    public void initZimmer(){
        // von config file lesen
        // zimmer neu erstellen und zur map hinzufügen
    }

}
