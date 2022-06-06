package zimmerzuteilung.objekte;

import java.util.Map;
import java.util.HashMap;

class Room {
    private static int count = 0;

    private final int id;
    private String number;
    private int capacity;
    private Map<Integer, Student> roomMates = new HashMap<>();

    Room(final Building i, final int k) throws IllegalArgumentException {
        ++Room.count;
        this.id = Room.count;
        this.capacity = k;

        i.addRoom(this);
    }

    Room(final Building internat, final String n, final int k)
            throws IllegalArgumentException {
        ++Room.count;
        this.id = Room.count;
        this.capacity = k;

        internat.addRoom(this);
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
