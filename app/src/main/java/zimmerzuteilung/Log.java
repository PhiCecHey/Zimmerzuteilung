package zimmerzuteilung;

public class Log {
    private static String log = "";

    public static void append(String s) {
        if(s != null && !s.equals("")){
            Log.log += "\n" + s + "\n";
        }
    }

    public static String log() {
        return Log.log;
    }

    public static void clear() {
        Log.log = "";
    }
}
