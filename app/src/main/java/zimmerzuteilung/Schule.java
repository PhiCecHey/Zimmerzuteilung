package zimmerzuteilung;

import java.util.List;
import java.util.ArrayList;

public class Schule {
    static List<Stufe> stufen = new ArrayList<>();
    static List<Zweig> zweige = new ArrayList<>();

    public Schule(){
        Schule.initZweige();
        Schule.initStufen();
    }

    private static void initZweige(){
        Schule.zweige.add(new Zweig(Zweig.BEZEICHNUNG.MUSIK));
        Schule.zweige.add(new Zweig(Zweig.BEZEICHNUNG.SPRACHEN));
        Schule.zweige.add(new Zweig(Zweig.BEZEICHNUNG.NAWI));
    }

    private static void initStufen(){
        Schule.stufen.add(new Stufe(9));
        Schule.stufen.add(new Stufe(10));
        Schule.stufen.add(new Stufe(11));
        Schule.stufen.add(new Stufe(12));
    }
}
