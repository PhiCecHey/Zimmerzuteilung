package zimmerzuteilung.objects;

import java.util.ArrayList;

public class Building {
    private static int count = 0;

    private int id;
    private String name;
    private ArrayList<Room> rooms = new ArrayList<>();

    public Building(final String n) {
        this.name = n;
        ++Building.count;
        this.id = Building.count;
    }

    public boolean addRoom(final Room room) {
        if (this.containsRoom(room)) {
            return false;
        }
        this.rooms.add(room);
        return true;
    }

    public int id() {
        return this.id;
    }

    public String name() {
        return this.name;
    }

    public ArrayList<Room> rooms() {
        return this.rooms;
    }

    public boolean containsRoom(Room room) {
        for (Room r : this.rooms) {
            if (r.id() == room.id()) {
                return true;
            }
        }
        return false;
    }

    public Room getRoom(String name) {
        for (Room room : this.rooms) {
            if (room.officialRoomNumber().equals(name)) {
                return room;
            }
        }
        return null;
    }

    public Room getRoomAlternative(String name) {
        for (Room room : this.rooms) {
            if (room.officialRoomNumber().contains(name) || name.contains(room.officialRoomNumber())) {
                return room;
            }
        }
        return null;
    }
}
