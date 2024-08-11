package main.java.mr.school1337.exceptions;

public class AlreadyAuthenticatedException extends RuntimeException {
    
    final static String ANSI_RESET = "\u001B[0m";
    final static String ANSI_RED = "\u001B[31m";
    
    public AlreadyAuthenticatedException() {
        super(ANSI_RED + "User already athenticated!\n" + ANSI_RESET);
    }
}
