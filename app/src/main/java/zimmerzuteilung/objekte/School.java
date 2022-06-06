package zimmerzuteilung.objekte;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

public class School {
    private Map<Integer, Grade> grades = new HashMap<>();
    private Map<Integer, Building> buildings = new HashMap<>();

    public School() {
        this.initGrades();
        this.initClasses();
    }

    public Student[] getStudents() {
        List<Student> lStudents = new LinkedList<>();
        for (Grade grade : this.grades.values()) {
            for (Class clas : grade.getClasses().values()) {
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

    public Room[] getRooms() {
        List<Room> lRooms = new LinkedList<>();
        for (Building building : this.buildings.values()) {
            for (Room room : building.getRooms().values()) {
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

        for(Grade grade : this.grades.values()){
            string += grade.toString() + ", ";
        }
        string += "], buildings: [";

        for(Building building : this.buildings.values()){
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
        Class m9 = new Class(Class.SPECIALIZATION.MUSIK);
        Class s9 = new Class(Class.SPECIALIZATION.SPRACHEN);
        Class n9 = new Class(Class.SPECIALIZATION.NAWI);

        // 10:
        Class m10 = new Class(Class.SPECIALIZATION.MUSIK);
        Class s10 = new Class(Class.SPECIALIZATION.SPRACHEN);
        Class n10 = new Class(Class.SPECIALIZATION.NAWI);

        // 11:
        Class m11 = new Class(Class.SPECIALIZATION.MUSIK);
        Class s11 = new Class(Class.SPECIALIZATION.SPRACHEN);
        Class n11 = new Class(Class.SPECIALIZATION.NAWI);

        // 12:
        Class m12 = new Class(Class.SPECIALIZATION.MUSIK);
        Class s12 = new Class(Class.SPECIALIZATION.SPRACHEN);
        Class n12 = new Class(Class.SPECIALIZATION.NAWI);

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

    Class findClass(Class.SPECIALIZATION special, int grade) {
        for (var entry : this.grades.entrySet()) {
            if (entry.getValue().getGrade() == grade) {
                return entry.getValue().findClass(special);
            }
        }
        return null;
    }

    private void initRandomBuildingsWithRandomRooms(int nBuildings,
            int minNRooms, int maxNRooms, int minCap, int maxCap) {
        for (int i = 0; i < nBuildings; ++i) {
            int nRoom = ThreadLocalRandom.current().nextInt(minNRooms, maxNRooms + 1);
            Building b = new Building();
            b.addRandomRooms(nRoom, minCap, maxCap);
            this.buildings.put(b.getId(), b);
        }
    }

    private Class findRandomClass() {
        Class clas;
        int randGrade = ThreadLocalRandom.current().nextInt(9, 13);
        int randSpecial = ThreadLocalRandom.current().nextInt(0, 3);
        Class.SPECIALIZATION special;

        switch (randSpecial) {
            case 0:
                special = Class.SPECIALIZATION.MUSIK;
                break;
            case 1:
                special = Class.SPECIALIZATION.SPRACHEN;
                break;
            case 2:
                special = Class.SPECIALIZATION.NAWI;
                break;
            default:
                special = Class.SPECIALIZATION.MUSIK;
                System.out.println("Something went wrong: School get Random Class randSpecial");
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
                System.out.println("Something went wrong: School get Random Class randGrade");
                break;
        }

        return clas;
    }

    public void createRandomSchool(int nStudents, int nBuildings,
            int minNRooms, int maxNRooms, int minCap, int maxCap) {
        for (int i = 0; i < nStudents; ++i) {
            Student student = new Student();
            Class clas = this.findRandomClass();
            clas.addStudent(student);
        }

        this.initRandomBuildingsWithRandomRooms(nBuildings,
                minNRooms, maxNRooms, minCap, maxCap);

        System.out.println(this.toString());
    }

    public int countStudents() {
        int count = 0;
        for (Grade grade : this.grades.values()) {
            for (Class clas : grade.getClasses().values()) {
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
            for (Room room : building.getRooms().values()) {
                ++count;
            }
        }
        return count;
    }
}
