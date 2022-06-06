package zimmerzuteilung.objekte;

import java.util.Map;
import java.util.HashMap;

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

    private String name;
    private Map<Integer, Room> rooms = new HashMap<>();

    Building(final String n) {
        this.name = n;
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

    public void initRooms() {
        // von config file lesen
        // zimmer neu erstellen und zur map hinzufügen
    }

}
