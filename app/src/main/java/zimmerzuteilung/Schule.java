package zimmerzuteilung;

import java.util.List;
import java.util.ArrayList;
// import java.util.Map;
// import java.util.HashMap;

public class Schule {
    static List<Stufe> stufen = new ArrayList<>();
    static List<Zweig> zweige = new ArrayList<>();
    //static Map<Klasse.BEZEICHNUNG, Klasse> klassen = new HashMap<>();

    public static void initSchule() {
        Schule.initZweige();
        Schule.initStufen();
        Schule.initKlassen();
    }

    private static void initZweige() {
        Schule.zweige.add(new Zweig(Zweig.BEZEICHNUNG.MUSIK));
        Schule.zweige.add(new Zweig(Zweig.BEZEICHNUNG.SPRACHEN));
        Schule.zweige.add(new Zweig(Zweig.BEZEICHNUNG.NAWI));
    }

    private static void initStufen() {
        Schule.stufen.add(new Stufe(9));
        Schule.stufen.add(new Stufe(10));
        Schule.stufen.add(new Stufe(11));
        Schule.stufen.add(new Stufe(12));
    }

    private static void initKlassen() {
        // 9:
        Klasse m9 = new Klasse(Klasse.BEZEICHNUNG.neunM);
        Klasse s9 = new Klasse(Klasse.BEZEICHNUNG.neunS);
        Klasse n9 = new Klasse(Klasse.BEZEICHNUNG.neunN);

        // Schule.klassen.put(Klasse.BEZEICHNUNG.neunM, m9);
        // Schule.klassen.put(Klasse.BEZEICHNUNG.neunS, s9);
        // Schule.klassen.put(Klasse.BEZEICHNUNG.neunN, n9);

        // 10:
        Klasse m10 = new Klasse(Klasse.BEZEICHNUNG.zehnM);
        Klasse s10 = new Klasse(Klasse.BEZEICHNUNG.zehnS);
        Klasse n10 = new Klasse(Klasse.BEZEICHNUNG.neunN);

        // Schule.klassen.put(Klasse.BEZEICHNUNG.zehnM, m10);
        // Schule.klassen.put(Klasse.BEZEICHNUNG.zehnS, s10);
        // Schule.klassen.put(Klasse.BEZEICHNUNG.zehnN, n10);

        // 11:
        Klasse m11 = new Klasse(Klasse.BEZEICHNUNG.elfM);
        Klasse s11 = new Klasse(Klasse.BEZEICHNUNG.elfS);
        Klasse n11 = new Klasse(Klasse.BEZEICHNUNG.elfN);

        // Schule.klassen.put(Klasse.BEZEICHNUNG.elfM, m11);
        // Schule.klassen.put(Klasse.BEZEICHNUNG.elfS, s11);
        // Schule.klassen.put(Klasse.BEZEICHNUNG.elfN, n11);

        // 12:
        Klasse m12 = new Klasse(Klasse.BEZEICHNUNG.zwoelfM);
        Klasse s12 = new Klasse(Klasse.BEZEICHNUNG.zwoelfS);
        Klasse n12 = new Klasse(Klasse.BEZEICHNUNG.zwoelfN);

        // Schule.klassen.put(Klasse.BEZEICHNUNG.zwoelfM, m12);
        // Schule.klassen.put(Klasse.BEZEICHNUNG.zwoelfS, s12);
        // Schule.klassen.put(Klasse.BEZEICHNUNG.zwoelfN, n12);

        // stufen:
        for (Stufe s : Schule.stufen) {
            switch (s.stufe) {
                case 9:
                    s.addKlasse(m9);
                    s.addKlasse(s9);
                    s.addKlasse(n9);
                    break;
                case 10:
                    s.addKlasse(m10);
                    s.addKlasse(s10);
                    s.addKlasse(n10);
                    break;
                case 11:
                    s.addKlasse(m11);
                    s.addKlasse(s11);
                    s.addKlasse(n11);
                    break;
                case 12:
                    s.addKlasse(m12);
                    s.addKlasse(s12);
                    s.addKlasse(n12);
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }

        for (Zweig z : zweige) {
            switch (z.bezeichung) {
                case MUSIK:
                    z.addKlasse(m9);
                    z.addKlasse(m10);
                    z.addKlasse(m11);
                    z.addKlasse(m12);
                    break;
                case SPRACHEN:
                    z.addKlasse(s9);
                    z.addKlasse(s10);
                    z.addKlasse(s11);
                    z.addKlasse(s12);
                    break;
                case NAWI:
                    z.addKlasse(n9);
                    z.addKlasse(n10);
                    z.addKlasse(n11);
                    z.addKlasse(n12);
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }
}
