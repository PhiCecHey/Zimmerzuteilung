package zimmerzuteilung;

public class Log {
    private static String log = "";

    public static void append(String s) {
        Log.log += "\n" + s;
    }

    public static String log() {
        return Log.log;
    }

    public static void clear(){
        Log.log = "";
    }
}
