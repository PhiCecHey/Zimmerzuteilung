package zimmerzuteilung.objekte;

import gurobi.GRBVar;

public class Allocation {
    GRBVar grbVar;
    Student student;
    Room room;
    double score = 1;

    public Allocation(Room r, Student s, GRBVar g) {
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

    public void setScore(double s) {
        this.score = s;
    }

    public double getScore() {
        return this.score;
    }

}
