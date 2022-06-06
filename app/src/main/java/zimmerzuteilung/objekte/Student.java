package zimmerzuteilung.objekte;

import java.util.concurrent.ThreadLocalRandom;

public class Student {
    public enum SEX {
        m, f, d
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

    public Student(final String n, final SEX g) {
        this.name = n;
        this.sex = g;

        ++Student.count; // der erste Schueler kriegt also Nr. 1
        this.id = Student.count;
    }

    public Student() {
        int randomNum = ThreadLocalRandom.current().nextInt(0, 3);
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

    public int getId() {
        return this.id;
    }

    public SEX getSex() {
        return this.sex;
    }

    @Override
    public String toString(){
        return "Student{id: " + this.id + ", name: " + this.name + ", sex: " + this.sex + "}";
    }

}
