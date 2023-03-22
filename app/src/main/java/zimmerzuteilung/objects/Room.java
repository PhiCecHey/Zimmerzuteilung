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

    public Room(final Building building, final int c)
            throws IllegalArgumentException {
        ++Room.count;
        this.id = Room.count;
        this.officialRoomNumber = Integer.toString(this.id);
        if (c < 0) {
            throw new IllegalArgumentException("Die maximale " +
                    "Schülerkapazität eines Zimmers darf nicht negativ sein.");
        }
        this.capacity = c;
        building.addRoom(this);
    }

    public Room(final int c) throws IllegalArgumentException {
        this.id = Room.count;
        this.officialRoomNumber = Integer.toString(this.id);
        if (c < 0) {
            throw new IllegalArgumentException("Die maximale " +
                    "Schülerkapazität eines Zimmers darf nicht negativ sein.");
        }
        this.capacity = c;
        ++Room.count;
    }

    public Room(final Building building, final String n, final int c)
            throws IllegalArgumentException {
        ++Room.count;
        this.id = Room.count;
        if (c < 0) {
            throw new IllegalArgumentException("Die maximale " +
                    "Schülerkapazität eines Zimmers darf nicht negativ sein.");
        }
        this.capacity = c;

        building.addRoom(this);
        this.officialRoomNumber = n;
    }

    public Room(final String unoffName, final String offNum, final GENDER s,
            final int c, boolean res) throws IllegalArgumentException {
        ++Room.count;
        this.id = Room.count;
        if (c < 0) {
            throw new IllegalArgumentException("Die maximale " +
                    "Schülerkapazität eines Zimmers darf nicht negativ sein.");
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

    public int getCapacity() {
        return this.capacity;
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

    @Override
    public String toString() {
        String string = "Room{id: " + this.id + ", number: " + this.officialRoomNumber
                + ", capacity: " + this.capacity + "}";

        return string;
    }
}
