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
}
