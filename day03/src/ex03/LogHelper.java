package ex03;

public class LogHelper {
    public static synchronized void log(String message) {
        System.out.println(message);
    }
}
