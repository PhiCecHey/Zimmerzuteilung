package zimmerzuteilung.objects;

public class Student {
    private static int count = 0;

    private String name;
    private GENDER gender;
    private String userName;
    private final int id;
    private int grade;
    private SPECIALIZATION special;
    private String moodleDate;

    /*public Student() {
        ++Student.count;
        this.id = Student.count;
        this.name = Integer.toString(this.id);
    }*/

    public Student(final String n, final String u) {
        this.name = n;
        this.userName = u;

        ++Student.count;
        this.id = Student.count;
    }

    public String name() {
        return this.name;
    }

    public GENDER gender() {
        return this.gender;
    }

    public void gender(GENDER g) {
        this.gender = g;
    }

    public int grade() {
        return this.grade;
    }

    public void grade(int g) {
        this.grade = g;
    }

    public void special(SPECIALIZATION s) {
        this.special = s;
    }

    public String userName() {
        return this.userName;
    }

    public String moodleDate() {
        return this.moodleDate;
    }

    public void moodleDate(String mD) {
        this.moodleDate = mD;
    }
}
