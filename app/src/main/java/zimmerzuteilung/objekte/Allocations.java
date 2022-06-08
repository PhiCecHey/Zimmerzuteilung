package zimmerzuteilung.objekte;

public class Allocations {
    private final Allocation[][] allocations;
    private final int nRooms;
    private final int nStudents;

    public Allocations(final int r, final int s) {
        this.allocations = new Allocation[r][s];
        this.nStudents = s;
        this.nRooms = r;
    }

    public Allocation get(final int r, final int s) {
        return this.allocations[r][s];
    }

    public void set(final int r, final int s, final Allocation allocation) {
        this.allocations[r][s] = allocation;
    }

    public int nRooms() {
        return this.nRooms;
    }

    public int nStudents() {
        return this.nStudents;
    }
}
