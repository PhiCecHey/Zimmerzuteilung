package zimmerzuteilung;

public class Config {
    // -----------------------SCORES-----------------------
    // score for wishes
    public static float scoreBuilding1 = 5;
    public static float scoreBuilding2 = 10;
    public static float scoreRoom1 = 8;
    public static float scoreRoom2 = 5;

    // score for grade priv
    public static float scoreTwelve = 7;
    public static float scoreEleven = 5;
    public static float scoreTen = 3;

    // score for room reservation
    public static float scoreReservation = -50;

    public static float scoreRandom = 0;
    
    public static float oneRoomPerTeam = -100;
    public static float oneTeamPerRoom = -100;
    public static float maxStudentsPerRoom = -100;
    public static float scoreGender = -50;

    // -----------------------MOODLE-----------------------
    // importBuildings:
    public static int impBuildBuildingName = 0;
    public static int impBuildUnofficialRoomNum = 1;
    public static int impBuildOfficialRoomNum = 2;
    public static int impBuildRoomGender = 3;
    public static int impBuildRoomCapacity = 4;
    public static int impBuildRoomReserved = 5;

    // importWishes:
    public static int impWishTeamName = 5;
    public static int impWishCycleLengthTilNextMember = 9;

    // importTeams:
    public static int impTeamTeamName = 1;
    public static int impTeamTeamSize = 2;
    public static int impTeamFirstMember = 8;
    public static int impTeamCycleLengthTilNextMember = 5;

    // importStudents:
    public static int impStudDate = 1;
    public static int impStudName = 7;
    public static int impStudUsername = 8;
    public static int impStudGrade = 9;
    public static int impStudSpecial = 10;
    public static int impStudGender = 11;
}
