package zimmerzuteilung.objekte;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

public class School {
    private Map<Integer, Grade> grades = new HashMap<>();
    private Map<Integer, Building> buildings = new HashMap<>();
    private Map<Integer, Team> teams = new HashMap<>();

    public School() {
        this.initGrades();
        this.initClasses();
    }

    public Student[] getStudents() {
        List<Student> lStudents = new LinkedList<>();
        for (Grade grade : this.grades.values()) {
            for (Clas clas : grade.getClasses().values()) {
                for (Student student : clas.getStudents().values()) {
                    lStudents.add(student);
                }
            }
        }
        Student[] aStudents = new Student[lStudents.size()];
        int i = 0;
        for (Student student : lStudents) {
            aStudents[i] = student;
            ++i;
        }
        return aStudents;
    }

    public Room[] rooms() {
        List<Room> lRooms = new LinkedList<>();
        for (Building building : this.buildings.values()) {
            for (Room room : building.rooms().values()) {
                lRooms.add(room);
            }
        }
        Room[] aRooms = new Room[lRooms.size()];
        int i = 0;
        for (Room room : lRooms) {
            aRooms[i] = room;
            ++i;
        }
        return aRooms;
    }

    @Override
    public String toString() {
        String string = "School{grades: [";

        for (Grade grade : this.grades.values()) {
            string += grade.toString() + ", ";
        }
        string += "], buildings: [";

        for (Building building : this.buildings.values()) {
            string += building.toString() + ", ";
        }
        string += "]}";
        return string;
    }

    private void initGrades() {
        this.grades.put(9, new Grade(9));
        this.grades.put(10, new Grade(10));
        this.grades.put(11, new Grade(11));
        this.grades.put(12, new Grade(12));
    }

    private void initClasses() {
        // 9:
        Clas m9 = new Clas(Clas.SPECIALIZATION.MUSIK);
        Clas s9 = new Clas(Clas.SPECIALIZATION.SPRACHEN);
        Clas n9 = new Clas(Clas.SPECIALIZATION.NAWI);

        // 10:
        Clas m10 = new Clas(Clas.SPECIALIZATION.MUSIK);
        Clas s10 = new Clas(Clas.SPECIALIZATION.SPRACHEN);
        Clas n10 = new Clas(Clas.SPECIALIZATION.NAWI);

        // 11:
        Clas m11 = new Clas(Clas.SPECIALIZATION.MUSIK);
        Clas s11 = new Clas(Clas.SPECIALIZATION.SPRACHEN);
        Clas n11 = new Clas(Clas.SPECIALIZATION.NAWI);

        // 12:
        Clas m12 = new Clas(Clas.SPECIALIZATION.MUSIK);
        Clas s12 = new Clas(Clas.SPECIALIZATION.SPRACHEN);
        Clas n12 = new Clas(Clas.SPECIALIZATION.NAWI);

        // stufen:
        for (var entry : this.grades.entrySet()) {
            switch (entry.getKey()) {
                case 9:
                    entry.getValue().addClass(m9);
                    entry.getValue().addClass(s9);
                    entry.getValue().addClass(n9);
                    break;
                case 10:
                    entry.getValue().addClass(m10);
                    entry.getValue().addClass(s10);
                    entry.getValue().addClass(n10);
                    break;
                case 11:
                    entry.getValue().addClass(m11);
                    entry.getValue().addClass(s11);
                    entry.getValue().addClass(n11);
                    break;
                case 12:
                    entry.getValue().addClass(m12);
                    entry.getValue().addClass(s12);
                    entry.getValue().addClass(n12);
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    Clas findClass(final Clas.SPECIALIZATION special, final int grade) {
        for (var entry : this.grades.entrySet()) {
            if (entry.getValue().getGrade() == grade) {
                return entry.getValue().findClass(special);
            }
        }
        return null;
    }

    private void initRandomBuildingsWithRandomRooms(final int nBuildings,
            final int minNRooms, final int maxNRooms, final int minCap,
            final int maxCap) {
        for (int i = 0; i < nBuildings; ++i) {
            int nRoom = ThreadLocalRandom.current().nextInt(minNRooms,
                    maxNRooms + 1);
            Building b = new Building();
            b.addRandomRooms(nRoom, minCap, maxCap);
            this.buildings.put(b.id(), b);
        }
    }

    private Clas findRandomClass() {
        Clas clas;
        int randGrade = ThreadLocalRandom.current().nextInt(9, 13);
        int randSpecial = ThreadLocalRandom.current().nextInt(0, 3);
        Clas.SPECIALIZATION special;

        switch (randSpecial) {
            case 0:
                special = Clas.SPECIALIZATION.MUSIK;
                break;
            case 1:
                special = Clas.SPECIALIZATION.SPRACHEN;
                break;
            case 2:
                special = Clas.SPECIALIZATION.NAWI;
                break;
            default:
                special = Clas.SPECIALIZATION.MUSIK;
                System.out.println("Something went wrong: "
                        + "School get Random Class randSpecial");
                break;
        }

        switch (randGrade) {
            case 9:
                clas = this.findClass(special, 9);
                break;
            case 10:
                clas = this.findClass(special, 10);
                break;
            case 11:
                clas = this.findClass(special, 11);
                break;
            case 12:
                clas = this.findClass(special, 12);
                break;
            default:
                clas = this.findClass(special, 9);
                System.out.println("Something went wrong: "
                        + "School get Random Class randGrade");
                break;
        }

        return clas;
    }

    public void createRandomSchool(final int nStudents, final int nBuildings,
            final int minNRooms, final int maxNRooms, final int minCap,
            final int maxCap) {
        for (int i = 0; i < nStudents; ++i) {
            Team team;
            Student student = new Student();
            Clas clas = this.findRandomClass();
            clas.addStudent(student);
        }

        this.initRandomBuildingsWithRandomRooms(nBuildings,
                minNRooms, maxNRooms, minCap, maxCap);

        System.out.println(this.toString());
    }

    public int countStudents() {
        int count = 0;
        for (Grade grade : this.grades.values()) {
            for (Clas clas : grade.getClasses().values()) {
                for (Student student : clas.getStudents().values()) {
                    ++count;
                }
            }
        }
        return count;
    }

    public int countRooms() {
        int count = 0;
        for (Building building : this.buildings.values()) {
            for (Room room : building.rooms().values()) {
                ++count;
            }
        }
        return count;
    }
}
