package zimmerzuteilung.objekte;

import gurobi.GRBVar;

public class Allocation {
    private GRBVar grbVar;
    private Student suspect;
    private Student roomMate;
    private Room room;
    // How important is it that suspect is in room with roomMate?
    private double score = 0;

    public Allocation(final Room r, final Student s, final GRBVar g) {
        this.grbVar = g;
        this.room = r;
        this.suspect = s;
    }

    public GRBVar grbVar() {
        return this.grbVar;
    }

    public Student suspect() {
        return this.suspect;
    }

    public Student roomMate() {
        return this.roomMate;
    }

    public Room room() {
        return this.room;
    }

    public void setScore(final double s) {
        this.score = s;
    }

    public double getScore() {
        return this.score;
    }

}
