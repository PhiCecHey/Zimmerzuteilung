package zimmerzuteilung.objekte;

import gurobi.GRBVar;

public class Allocation {
    private GRBVar grbVar;
    private Room room;
    private Team team;
    private float score = 1; // importance of this team getting this room

    public Allocation(final Room r, final Team t, final GRBVar g) {
        this.grbVar = g;
        this.room = r;
        this.team = t;
    }

    public GRBVar grbVar() {
        return this.grbVar;
    }

    public Team team() {
        return this.team;
    }

    public Room room() {
        return this.room;
    }

    public void setScore(final float s) {
        this.score = s;
    }

    public void addToScore(final float s) {
        this.score += s;
    }

    public double getScore() {
        return this.score;
    }

}
