package zimmerzuteilung;

import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

import org.checkerframework.checker.units.qual.s;

public class Zimmer {
    int nummer;
    Map<UUID, Schueler> bewohner = new HashMap<>();

    Zimmer(Internat internat, int nummer) throws IllegalArgumentException{
        if(nummer < 0){
            throw new IllegalArgumentException();
        }
        this.nummer = nummer;
        internat.addZimmer(this);
    }

    boolean addSchueler(Schueler schueler){
        for(Map.Entry<UUID, Schueler> eintrag : this.bewohner.entrySet()){
            if(eintrag.getKey() == schueler.ID){
                return false;
            }
        }

        this.bewohner.put(schueler.ID, schueler);
        return true;
    }
}
