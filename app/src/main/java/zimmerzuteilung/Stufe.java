package zimmerzuteilung;

import java.util.List;
import java.util.ArrayList;

public class Stufe {
    int stufe;
    List<Klasse> klassen = new ArrayList<>();

    Stufe(int stufe) throws IllegalArgumentException{
        if (stufe<9 || stufe>12){
            throw new IllegalArgumentException();
        }

        this.stufe = stufe;
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
