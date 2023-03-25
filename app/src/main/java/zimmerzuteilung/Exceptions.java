package zimmerzuteilung;

public class Exceptions {
    public static class WarningException extends Exception {
        public WarningException(String str) {
            super(str);
        }
    }

    public static class BuildingDoesNotExist extends Exception {
        public BuildingDoesNotExist(String str) {
            super(str);
        }
    }

    public static class RoomDoesNotExist extends Exception {
        public RoomDoesNotExist(String str) {
            super(str);
        }
    }

    public static class TeamDoesNotExist extends Exception {
        public TeamDoesNotExist(String str) {
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

    public static class StudentInSeveralMoodleGroups extends Exception {
        public StudentInSeveralMoodleGroups(String str) {
            super(str);
        }
    }

    public static class LineEmptyException extends Exception {
        public LineEmptyException(String str) {
            super(str);
        }
    }
}
