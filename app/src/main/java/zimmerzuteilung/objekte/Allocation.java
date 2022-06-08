package zimmerzuteilung.objekte;

import gurobi.GRBVar;

public class Allocation {
    private GRBVar grbVar;
    private Student student;
    private Room room;
    private double score = 0;

    public Allocation(final Room r, final Student s, final GRBVar g) {
        this.grbVar = g;
        this.room = r;
        this.student = s;
    }

    public GRBVar grbVar() {
        return this.grbVar;
    }

    public Student student() {
        return this.student;
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
