package zimmerzuteilung.objekte;

import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

class Building {

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
    private Map<Integer, Room> rooms = new HashMap<>();

    Building(final String n) {
        this.name = n;

        this.id = Building.count;
        ++Building.count;
    }

    Building() {
        this.id = Building.count;
        this.name = Integer.toString(this.id);
        ++Building.count;
    }

    boolean addRoom(Room room) {
        for (Map.Entry<Integer, Room> entry : this.rooms.entrySet()) {
            if (entry.getKey() == room.getId()) {
                return false;
            }
        }

        this.rooms.put(room.getId(), room);
        return true;
    }

    public void addRandomRooms(int nRooms, int minCapacity, int maxCapacity) {
        for (int i = 0; i < nRooms; ++i) {
            int randCap = ThreadLocalRandom.current().nextInt(minCapacity, maxCapacity + 1);
            this.addRoom(new Room(randCap));
        }
    }

    public int getId() {
        return this.id;
    }

    public void initRooms() {
        // von config file lesen
        // zimmer neu erstellen und zur map hinzufügen
    }

    Map<Integer, Room> getRooms() {
        return this.rooms;
    }

}
