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
            if (entry.getKey() == room.id()) {
                return false;
            }
        }

        this.rooms.put(room.id(), room);
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

    Map<Integer, Room> getRooms() {
        return this.rooms;
    }

    @Override
    public String toString(){
        String string = "Building{id: " + this.id + ", name: " + this.name + ", rooms: [";
        for(Room room : this.rooms.values()){
            string += room.toString() + ", ";
        }        
        return  string += "]}";
    }

    public void initRooms() {
        // von config file lesen
        // zimmer neu erstellen und zur map hinzufügen
    }
}
