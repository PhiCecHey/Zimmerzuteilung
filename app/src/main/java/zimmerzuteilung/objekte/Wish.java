package zimmerzuteilung.objekte;

public class Wish {
    private final Room firstRoom;
    private final Room secondRoom;
    private final Room thirdRoom;
    private final Student roomMate;

    Wish(Room firstR, Room secondR, Room thirdR, Student mate) {
        this.firstRoom = firstR;
        this.secondRoom = secondR;
        this.thirdRoom = thirdR;
        this.roomMate = mate;
    }

    public Room getFirstRoom() {
        return this.firstRoom;
    }

    public Room getSecondRoom() {
        return this.secondRoom;
    }

    public Room getThirdRoom() {
        return this.thirdRoom;
    }

    public Student getRoomMate() {
        return this.roomMate;
    }
}
