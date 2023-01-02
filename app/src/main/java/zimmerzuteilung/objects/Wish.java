package zimmerzuteilung.objects;

public class Wish {
    private class RoomNotInBuildingException extends Exception {
        private RoomNotInBuildingException(String str) {
            super(str);
        }
    }

    private class SameLocationException extends Exception {
        private SameLocationException(String str) {
            super(str);
        }
    }

    private Building building1, building2;
    private Room room1, room2;

    private static int count = 0;
    private int id;

    public Wish() {
        Wish.count++;
        this.id = Wish.count;
    }

    public Wish(final Building fB, final Room fR, final Room sR, final Building sB)
            throws RoomNotInBuildingException, SameLocationException {
        if (fB.id() == sB.id()) { // fB != sB?
            throw new SameLocationException("Das Erstwunschinternat " +
                    "nicht dem Zweitwunschinternat entsprechen.");
        }
        if (fR.id() == sR.id()) { // fR != sR?
            throw new SameLocationException("Das Erstwunschzimmer " +
                    "darf nicht dem Zweitwunschzimmer entsprechen.");
        }
        if (!fB.containsRoom(fR)) { // fR in fB?
            throw new RoomNotInBuildingException("Das Erstwunschzimmer " +
                    "muss sich im Erstwunschinternat befinden.");
        }
        if (!fB.containsRoom(sR)) { // sR in fB?
            throw new RoomNotInBuildingException("Das Zweitwunschzimmer " +
                    "muss sich im Erstwunschinternat befinden.");
        }

        this.building1 = fB;
        this.room1 = fR;
        this.room2 = sR;
        this.building2 = sB;

        Wish.count++;
        this.id = Wish.count;
    }

    public int id() {
        return this.id;
    }

    public void building1(Building b) {
        this.building1 = b;
    }

    public Building building1() {
        return this.building1;
    }

    public Room room1() {
        return this.room1;
    }

    public void room1(Room r) {
        this.room1 = r;
    }

    public Room room2() {
        return this.room2;
    }

    public void room2(Room r) {
        this.room2 = r;
    }

    public Building building2() {
        return this.building2;
    }

    public void building2(Building b) {
        this.building2 = b;
    }

    @Override
    public String toString() {
        String res = "Wish{id: " + this.id + ", building1: "
                + this.building1.toString() + ", room1: " +
                this.room1.toString() + ", room2: " + this.room2.toString() +
                ", building2: " + this.building2.toString() + "}";

        return res;
    }
}
