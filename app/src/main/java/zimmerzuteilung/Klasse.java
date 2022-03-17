package zimmerzuteilung;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Klasse {
    int stufe = 0;  // 9-12
    Zweig zweig = null;  // MUSIK, SPRACHEN, NAWI
    List<Schueler> schuelerDieserKlasse = new ArrayList<>();  // alle Schueler dieser Klasse
    public static Map<StufeZweig, Klasse> alleKlassen = new HashMap<StufeZweig, Klasse>();  // Liste

    private Klasse(int stufe, Zweig zweig) throws IllegalArgumentException {
        if (stufe < 9 || stufe > 12) {
            throw new IllegalArgumentException();
        }

        // jede Klasse soll nur 1x erstellt werden
        if (findeKlasse(stufe, zweig) == null) {
            this.stufe = stufe;
            this.zweig = zweig;
            alleKlassen.put(new StufeZweig(stufe, zweig), this);
        }
    }

    public static @Nullable Klasse findeKlasse(int stufe, Zweig zweig) throws IllegalArgumentException {
        if (stufe < 9 || stufe > 12) {
            throw new IllegalArgumentException();
        }

        for (Map.Entry<StufeZweig, Klasse> eintrag : alleKlassen.entrySet()) {
            if (eintrag.getKey().stufe == stufe && eintrag.getKey().zweig == zweig) {
                return eintrag.getValue();
            }
        }
        return null;
    }

    public static @NotNull Klasse findeOderErstelleKlasse(int stufe, Zweig zweig) throws IllegalArgumentException{
        Klasse klasse = findeKlasse(stufe, zweig);
        if (klasse == null){
            klasse = new Klasse(stufe, zweig);
        }
        return klasse;
    }

    public boolean schuelerHinzufuegen(Schueler schueler){
        if(this.schuelerDieserKlasse.contains(schueler)){
            return false;
        }
        else{
            this.schuelerDieserKlasse.add(schueler);
            return true;
        }
    }

}
