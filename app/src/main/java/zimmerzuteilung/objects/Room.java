package zimmerzuteilung.objects;

import java.util.Map;
import java.util.HashMap;

public class Room {
    private static int count = 0;

    private final int id;
    private String officialRoomNumber;
    private String unofficialName;
    private int capacity;
    private Map<Integer, Student> roomMates = new HashMap<>();
    private GENDER gender;

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
            final int c) throws IllegalArgumentException {
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

    @Override
    public String toString() {
        String string = "Room{id: " + this.id + ", number: " + this.officialRoomNumber
                + ", capacity: " + this.capacity + ", roomMates: [";

        for (Student student : this.roomMates.values()) {
            string += student.toString();
        }
        string += "]}";

        return string;
    }

    boolean addStudent(final Student student) {
        if (this.capacity <= this.roomMates.size()) {
            return false;
        }

        for (Map.Entry<Integer, Student> entry : this.roomMates.entrySet()) {
            if (entry.getKey() == student.id()) {
                return false;
            }
        }

        this.roomMates.put(student.id(), student);
        return true;
    }
}
