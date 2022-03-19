package zimmerzuteilung;

import java.util.Map;
import java.util.HashMap;

public class Schule {
    static Map<Integer, Stufe> stufen = new HashMap<>();
    static Map<String, Zweig> zweige = new HashMap<>();
    // static Map<String, Klasse> klassen = new HashMap<>();

    public static void initSchule() {
        Schule.initZweige();
        Schule.initStufen();
        Schule.initKlassen();
    }

    private static void initZweige() {
        Schule.zweige.put("MUSIK", new Zweig("MUSIK"));
        Schule.zweige.put("SPRACHEN", new Zweig("SPRACHEN"));
        Schule.zweige.put("NAWI", new Zweig("NAWI"));
    }

    private static void initStufen() {
        Schule.stufen.put(9, new Stufe(9));
        Schule.stufen.put(10, new Stufe(10));
        Schule.stufen.put(11, new Stufe(11));
        Schule.stufen.put(12, new Stufe(12));
    }

    private static void initKlassen() {
        // 9:
        Klasse m9 = new Klasse9("9m");
        Klasse s9 = new Klasse9("9s");
        Klasse n9 = new Klasse9("9n");

        // Schule.klassen.put(Klasse.name.neunM, m9);
        // Schule.klassen.put(Klasse.name.neunS, s9);
        // Schule.klassen.put(Klasse.name.neunN, n9);

        // 10:
        Klasse m10 = new Klasse10("10m");
        Klasse s10 = new Klasse10("10s");
        Klasse n10 = new Klasse10("10n");

        // Schule.klassen.put(Klasse.name.zehnM, m10);
        // Schule.klassen.put(Klasse.name.zehnS, s10);
        // Schule.klassen.put(Klasse.name.zehnN, n10);

        // 11:
        Klasse m11 = new Klasse11("11m");
        Klasse s11 = new Klasse11("11s");
        Klasse n11 = new Klasse11("11n");

        // Schule.klassen.put(Klasse.name.elfM, m11);
        // Schule.klassen.put(Klasse.name.elfS, s11);
        // Schule.klassen.put(Klasse.name.elfN, n11);

        // 12:
        Klasse m12 = new Klasse12("12m");
        Klasse s12 = new Klasse12("12s");
        Klasse n12 = new Klasse12("12n");

        // Schule.klassen.put(Klasse.name.zwoelfM, m12);
        // Schule.klassen.put(Klasse.name.zwoelfS, s12);
        // Schule.klassen.put(Klasse.name.zwoelfN, n12);

        // stufen:
        for (Map.Entry<Integer, Stufe> eintrag : Schule.stufen.entrySet()) {
            switch (eintrag.getKey()) {
                case 9:
                    eintrag.getValue().addKlasse(m9);
                    eintrag.getValue().addKlasse(s9);
                    eintrag.getValue().addKlasse(n9);
                    break;
                case 10:
                    eintrag.getValue().addKlasse(m10);
                    eintrag.getValue().addKlasse(s10);
                    eintrag.getValue().addKlasse(n10);
                    break;
                case 11:
                    eintrag.getValue().addKlasse(m11);
                    eintrag.getValue().addKlasse(s11);
                    eintrag.getValue().addKlasse(n11);
                    break;
                case 12:
                    eintrag.getValue().addKlasse(m12);
                    eintrag.getValue().addKlasse(s12);
                    eintrag.getValue().addKlasse(n12);
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }

        for (Map.Entry<String, Zweig> eintrag : Schule.zweige.entrySet()) {
            switch (eintrag.getKey()) {
                case "MUSIK":
                    eintrag.getValue().addKlasse(m9);
                    eintrag.getValue().addKlasse(m10);
                    eintrag.getValue().addKlasse(m11);
                    eintrag.getValue().addKlasse(m12);
                    break;
                case "SPRACHEN":
                    eintrag.getValue().addKlasse(s9);
                    eintrag.getValue().addKlasse(s10);
                    eintrag.getValue().addKlasse(s11);
                    eintrag.getValue().addKlasse(s12);
                    break;
                case "NAWI":
                    eintrag.getValue().addKlasse(n9);
                    eintrag.getValue().addKlasse(n10);
                    eintrag.getValue().addKlasse(n11);
                    eintrag.getValue().addKlasse(n12);
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }
}
