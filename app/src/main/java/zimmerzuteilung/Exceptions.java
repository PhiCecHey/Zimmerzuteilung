package zimmerzuteilung;

public class Exceptions {
    public static class WarningException extends Exception {
        public WarningException(String str) {
            super(str);
        }
    }

    public static class BuildingDoesNotExistException extends Exception {
        public BuildingDoesNotExistException(String str) {
            super(str);
        }
    }

    public static class RoomDoesNotExistException extends Exception {
        public RoomDoesNotExistException(String str) {
            super(str);
        }
    }

    public static class TeamDoesNotExistException extends Exception {
        public TeamDoesNotExistException(String str) {
            super(str);
        }
    }

    public static class StudentDoesNotExistException extends Exception {
        public StudentDoesNotExistException(String str) {
            super(str);
        }
    }

    public static class DifferentGenderException extends Exception {
        public DifferentGenderException(String str) {
            super(str);
        }
    }

    public static class RoomNotInBuildingException extends Exception {
        public RoomNotInBuildingException(String str) {
            super(str);
        }
    }

    public static class SameLocationException extends Exception {
        public SameLocationException(String str) {
            super(str);
        }
    }

    public static class RoomOccupiedException extends Exception {
        public RoomOccupiedException(String str) {
            super(str);
        }
    }

    public static class StudentInSeveralMoodleGroupsException extends Exception {
        public StudentInSeveralMoodleGroupsException(String str) {
            super(str);
        }
    }

    public static class LineEmptyException extends Exception {
        public LineEmptyException(String str) {
            super(str);
        }
    }
}
