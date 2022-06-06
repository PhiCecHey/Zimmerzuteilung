package zimmerzuteilung.objekte;

import java.lang.management.ThreadInfo;

class ThreeWishes {
    final Room firstRoom;
    final Room secondRoom;
    final Room thirdRoom;
    final Student roomMate;

    ThreeWishes(Room firstR, Room secondR, Room thirdR, Student mate){
        this.firstRoom = firstR;
        this.secondRoom = secondR;
        this.thirdRoom = thirdR;
        this.roomMate = mate;
    }
}
