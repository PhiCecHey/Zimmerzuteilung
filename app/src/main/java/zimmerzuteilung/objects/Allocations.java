package zimmerzuteilung.objects;

public class Allocations {
    private final Allocation[][] allocations;
    private final int nRooms;
    private final int nTeams;

    public Allocations(final int r, final int t) {
        this.allocations = new Allocation[r][t];
        this.nTeams = t;
        this.nRooms = r;
    }

    public Allocation get(final int r, final int t) {
        return this.allocations[r][t];
    }

    public void set(final int r, final int t, final Allocation allocation) {
        this.allocations[r][t] = allocation;
    }

    public int nRooms() {
        return this.nRooms;
    }

    public int nTeams() {
        return this.nTeams;
    }
}
