package zimmerzuteilung.objekte;

public class Allocations {
    final Allocation[][] allocations;
    final int nRooms;
    final int nStudents;

    public Allocations(int r, int s) {
        this.allocations = new Allocation[r][s];
        this.nStudents = s;
        this.nRooms = r;
    }

    public Allocation get(int r, int s) {
        return this.allocations[r][s];
    }

    public void set(int r, int s, Allocation allocation) {
        this.allocations[r][s] = allocation;
    }

    public int nRooms(){
        return this.nRooms;
    }

    public int nStudents(){
        return this.nStudents;
    }
}