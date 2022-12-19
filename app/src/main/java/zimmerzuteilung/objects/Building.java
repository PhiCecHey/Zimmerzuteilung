package zimmerzuteilung.objects;

import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class Building {

    /*
     * public enum BEZEICHNUNG{
     * Klausur, II, III, Fueha, V, VI, VII;
     * }
     * BEZEICHNUNG bezeichnung;
     * Internat(BEZEICHNUNG bezeichnung){
     * this.bezeichnung = bezeichnung;
     * }
     */

    private static int count = 0;

    private int id;
    private String name;
    // private Map<Integer, Room> rooms = new HashMap<>();
    private ArrayList<Room> rooms = new ArrayList<>();

    public Building(final String n) {
        this.name = n;
        ++Building.count;
        this.id = Building.count;
    }

    public Building() {
        ++Building.count;
        this.id = Building.count;
        this.name = Integer.toString(this.id);
    }

    public boolean addRoom(final Room room) {
        if (this.containsRoom(room)) {
            return false;
        }
        this.rooms.add(room);
        return true;
    }

    public void addRandomRooms(final int nRooms, final int minCapacity,
            final int maxCapacity) {
        for (int i = 0; i < nRooms; ++i) {
            int randCap = ThreadLocalRandom.current().nextInt(
                    minCapacity, maxCapacity + 1);
            this.addRoom(new Room(randCap));
        }
    }

    public int id() {
        return this.id;
    }

    public String name() {
        return this.name;
    }

    public void name(String n) {
        this.name = n;
    }

    public ArrayList<Room> rooms() {
        return this.rooms;
    }

    @Override
    public String toString() {
        String string = "Building{id: " + this.id + ", name: " + this.name
                + ", rooms: [";
        for (Room room : this.rooms) {
            string += room.toString() + ", ";
        }
        return string += "]}";
    }

    // TODO
    public void initRooms() {
        // von config file lesen
        // zimmer neu erstellen und zur map hinzufügen
    }

    public boolean containsRoom(Room room) {
        for (Room r : this.rooms) {
            if (r.id() == room.id()) {
                return true;
            }
        }
        return false;
    }
}
