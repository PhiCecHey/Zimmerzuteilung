package zimmerzuteilung.objects;

import java.util.ArrayList;

public class Room {
    private static int count = 0;

    private final int id;
    private String officialRoomNumber;
    private String unofficialName;
    private int capacity;
    private GENDER gender;
    private boolean reserved = false;
    private ArrayList<Team> allocatedTeams;

    public Room() {
        this.allocatedTeams = new ArrayList<>();
        Room.count++;
        this.id = Room.count;
    }

    public Room(String offRoomNum) {
        this.allocatedTeams = new ArrayList<>();
        this.officialRoomNumber = offRoomNum;
        Room.count++;
        this.id = Room.count;
    }

    public Room(final String unoffName, final String offNum, final GENDER s,
            final int c, boolean res) throws IllegalArgumentException {
        this.allocatedTeams = new ArrayList<>();
        ++Room.count;
        this.id = Room.count;
        if (c < 0) {
            throw new IllegalArgumentException("Die maximale " +
                    "Schuelerkapazitaet eines Zimmers darf nicht negativ sein.");
        }
        this.capacity = c;
        this.officialRoomNumber = offNum;
        this.unofficialName = unoffName;
        this.gender = s;
        this.reserved = res;
    }

    public ArrayList<Team> unallocateTeams() {
        ArrayList<Team> teams = this.allocatedTeams;
        this.allocatedTeams.clear();
        return teams;
    }

    public boolean allocateTeam(Team t) {
        if (this.allocatedTeams.size() > 1) {
            return false;
        }
        this.allocatedTeams.add(t);
        return true;
    }

    public ArrayList<Team> allocatedTeams() {
        return this.allocatedTeams;
    }

    public int id() {
        return this.id;
    }

    public int capacity() {
        return this.capacity;
    }

    public void capacity(final int c) {
        this.capacity = c;
    }

    public String officialRoomNumber() {
        return this.officialRoomNumber;
    }

    public void officialRoomNumber(String n) {
        this.officialRoomNumber = n;
    }

    public boolean reserved() {
        return this.reserved;
    }

    public void reserved(boolean r) {
        this.reserved = r;
    }

    public GENDER gender() {
        return this.gender;
    }
}
