package zimmerzuteilung;

import java.util.Map;
import java.util.HashMap;

public class Internat {

    public enum BEZEICHNUNG{
        Klausur, II, III, Fueha, V, VI, VII;
    }

    BEZEICHNUNG bezeichnung;
    Map<Integer, Zimmer> zimmer = new HashMap<>();

    Internat(BEZEICHNUNG bezeichnung){
        this.bezeichnung = bezeichnung;
    }

    boolean addZimmer(Zimmer zimmer){
        for(Map.Entry<Integer, Zimmer> eintrag : this.zimmer.entrySet()){
            if(eintrag.getKey() == zimmer.nummer){
                return false;
            }
        }

        this.zimmer.put(zimmer.nummer, zimmer);
        return true;
    }

    public void initZimmer(){
        // von config file lesen
        // zimmer neu erstellen und zur map hinzufügen
    }

}
