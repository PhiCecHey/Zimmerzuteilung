package zimmerzuteilung;

public class Config {
    // -----------------------SCORES-----------------------
    // score for wishes
    public static float scoreBuilding1 = 30;
    public static float scoreBuilding2 = 20;
    public static float scoreRoom1 = 20;
    public static float scoreRoom2 = 10;

    // score for grade priv
    public static float scoreTwelve = 10;
    public static float scoreEleven = 7;
    public static float scoreTen = 4;

    // score for room reservation
    public static float scoreReservation = -100;

    public static float scoreRandom = 0;
    
    public static float oneRoomPerTeam = -100;
    public static float oneTeamPerRoom = -100;
    public static float maxStudentsPerRoom = -100;
    public static float scoreGender = -100;

    public static float scoreStayInRoom = 10;
    public static float scoreStayInBuilding = 10;

    // -----------------------MOODLE-----------------------
    // importBuildings:
    public static int impBuildBuildingName = 0;
    public static int impBuildUnofficialRoomNum = 1;
    public static int impBuildOfficialRoomNum = 2;
    public static int impBuildRoomGender = 3;
    public static int impBuildRoomCapacity = 4;
    public static int impBuildRoomReserved = 5;

    // importWishes:
    public static int importWishDate = 1;
    public static int impWishTeamName = 5;
    public static int impWishB1 = 9;

    // importTeams:
    /*public static int impTeamTeamName = 1;
    public static int impTeamTeamSize = 2;
    public static int impTeamFirstMember = 8;
    public static int impTeamCycleLengthTilNextMember = 5;*/

    public static int impTeamLastName = 0;
    public static int impTeamFirstName = 1;
    public static int impTeamEmail = 3;
    public static int impTeamTeamName = 4;

    // importStudents:
    public static int impStudDate = 1;
    public static int impStudTeamName = 5;
    public static int impStudName = 7;
    public static int impStudUsername = 8;
    public static int impStudLastBuild = 9;
    //public static int impStudLastRoom = 10; // til 16
    public static int impStudGrade = 17;
    public static int impStudSpecial = 18;
}
