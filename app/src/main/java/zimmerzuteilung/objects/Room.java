package zimmerzuteilung.objects;

public class Room {
    private static int count = 0;

    private final int id;
    private String officialRoomNumber;
    private String unofficialName;
    private int capacity;
    private GENDER gender;
    private boolean reserved = false;
    private Team allocatedTeam;

    public Room() {
        Room.count++;
        this.id = Room.count;
    }

    public Room(final String unoffName, final String offNum, final GENDER s,
            final int c, boolean res) throws IllegalArgumentException {
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

    public boolean allocateTeam(Team t) {
        if (this.allocatedTeam != null) {
            return false;
        }
        this.allocatedTeam = t;
        return true;
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
