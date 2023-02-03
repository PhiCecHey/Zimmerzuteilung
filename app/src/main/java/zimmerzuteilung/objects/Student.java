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

    public Student() {
        ++Student.count;
        this.id = Student.count;
        this.name = Integer.toString(this.id);
    }

    public Student(final String n, final String u) {
        this.name = n;
        this.userName = u;

        ++Student.count;
        this.id = Student.count;
    }

    public Student(final String n, final GENDER s, final int g,
            final SPECIALIZATION sp) {
        this.name = n;
        this.gender = s;
        this.grade = g;
        this.special = sp;

        ++Student.count;
        this.id = Student.count;
    }

    public Student(final String n, final GENDER s) {
        this.name = n;
        this.gender = s;

        ++Student.count;
        this.id = Student.count;
    }

    public String name() {
        return this.name;
    }

    public void name(String n) {
        this.name = n;
    }

    public int id() {
        return this.id;
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

    public SPECIALIZATION special() {
        return this.special;
    }

    public void special(SPECIALIZATION s) {
        this.special = s;
    }

    public String userName() {
        return this.userName;
    }

    public void userName(String un) {
        this.userName = un;
    }

    public String moodleDate() {
        return this.moodleDate;
    }

    public void moodleDate(String mD) {
        this.moodleDate = mD;
    }

    @Override
    public String toString() {
        return "Student{id: " + this.id + ", name: " + this.name + ", gender: "
                + this.gender + "}";
    }
}
