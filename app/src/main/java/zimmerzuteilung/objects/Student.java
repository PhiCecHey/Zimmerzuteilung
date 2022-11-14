package zimmerzuteilung.objects;

import java.util.concurrent.ThreadLocalRandom;

public class Student {
    public enum SEX {
        m, f, d
    }

    public enum SPECIALIZATION {
        NAWI, MUSIK, SPRACHEN;
    }

    /*
     * Die Schueler werden der Reihe nach durchgezaehlt und erhalten
     * somit ihre ID. Vorteil zur randomID: verbraucht weniger Platz
     * und ist effizienter im Vergleich.
     */
    private static int count = 0;

    private final String name;
    private final SEX sex;
    private final int id;
    private int grade;
    private SPECIALIZATION special;

    public Student(final String n, final SEX s, final int g,
            final SPECIALIZATION sp) {
        this.name = n;
        this.sex = s;
        this.grade = g;
        this.special = sp;

        ++Student.count;
        this.id = Student.count;
    }

    public Student(final String n, final SEX s) {
        this.name = n;
        this.sex = s;

        ++Student.count;
        this.id = Student.count;
    }

    public Student() {
        // int randomNum = ThreadLocalRandom.current().nextInt(0, 3);
        int randomNum = ThreadLocalRandom.current().nextInt(0, 2);
        if (randomNum == 0) {
            this.sex = Student.SEX.f;
        } else if (randomNum == 1) {
            this.sex = Student.SEX.m;
        } else {
            this.sex = Student.SEX.d;
        }

        ++Student.count; // der erste Schueler kriegt also Nr. 1
        this.id = Student.count;
        this.name = Integer.toString(this.id);
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }

    public SEX getSex() {
        return this.sex;
    }

    @Override
    public String toString() {
        return "Student{id: " + this.id + ", name: " + this.name + ", sex: "
                + this.sex + "}";
    }

}
