package zimmerzuteilung.objekte;

public class Wish {
    private final Room[] rooms;
    private final Student[] mates;

    Wish(Room[] r, Student[] m) {
        this.rooms = r;
        this.mates = m;
    }

    public Room[] rooms() {
        return this.rooms;
    }

    public Student[] mates() {
        return this.mates;
    }
}
