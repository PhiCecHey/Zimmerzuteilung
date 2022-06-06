package zimmerzuteilung.objekte;

//import java.util.UUID;

public class Student {
    public enum Sex {
        m, f, d
    }

    /*
     * Die Schueler werden der Reihe nach durchgezaehlt und erhalten
     * somit ihre ID. Vorteil zur randomID: verbraucht weniger Platz
     * und ist effizienter im Vergleich.
     */
    private static int count = 0;

    private final String name;
    private final Sex sex;
    private final int id;

    public Student(final String n, final Sex g) {
        this.name = n;
        this.sex = g;

        ++Student.count; // der erste Schueler kriegt also Nr. 1
        this.id = Student.count;
    }

    public int getId() {
        return this.id;
    }

    public Sex getSex() {
        return this.sex;
    }

}
