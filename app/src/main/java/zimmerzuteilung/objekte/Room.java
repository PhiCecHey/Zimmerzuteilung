package zimmerzuteilung.objekte;

import java.util.Map;
import java.util.HashMap;

public class Room {
    private static int count = 0;

    private final int id;
    private String number;
    private int capacity;
    private Map<Integer, Student> roomMates = new HashMap<>();

    public Room(final Building building, final int c) throws IllegalArgumentException {
        ++Room.count;
        this.id = Room.count;
        this.number = Integer.toString(this.id);
        if (c < 0) {
            throw new IllegalArgumentException("Die maximale " +
                    "Schülerkapazität eines Zimmers darf nicht negativ sein.");
        }
        this.capacity = c;
        building.addRoom(this);
    }

    public Room(final int c) throws IllegalArgumentException {
        this.id = Room.count;
        this.number = Integer.toString(this.id);
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
        this.number = n;
    }

    public Room(final String n, final int c)
            throws IllegalArgumentException {
        ++Room.count;
        this.id = Room.count;
        if (c < 0) {
            throw new IllegalArgumentException("Die maximale " +
                    "Schülerkapazität eines Zimmers darf nicht negativ sein.");
        }
        this.capacity = c;
        this.number = n;
    }

    public int id() {
        return this.id;
    }

    public int getCapacity() {
        return this.capacity;
    }

    @Override
    public String toString() {
        String string = "Room{id: " + this.id + ", number: " + this.number
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
            if (entry.getKey() == student.getId()) {
                return false;
            }
        }

        this.roomMates.put(student.getId(), student);
        return true;
    }
}
