package zimmerzuteilung.objekte;

import java.util.Map;
import java.util.HashMap;

public class Room {
    private static int count = 0;

    private final int id;
    private String number;
    private int capacity;
    private Map<Integer, Student> roomMates = new HashMap<>();

    Room(final Building building, final int c) throws IllegalArgumentException {
        this.id = Room.count;
        this.number = Integer.toString(this.id);
        this.capacity = c;

        building.addRoom(this);
        ++Room.count;
    }

    Room(final int c) throws IllegalArgumentException {
        this.id = Room.count;
        this.number = Integer.toString(this.id);
        this.capacity = c;

        ++Room.count;
    }

    Room(final Building building, final String n, final int c)
            throws IllegalArgumentException {
        ++Room.count;
        this.id = Room.count;
        this.capacity = c;

        building.addRoom(this);
        this.number = n;
    }

    Room(final String n, final int c)
            throws IllegalArgumentException {
        ++Room.count;
        this.id = Room.count;
        this.capacity = c;

        this.number = n;
    }

    public int getId() {
        return this.id;
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
