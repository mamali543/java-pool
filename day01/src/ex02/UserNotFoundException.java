package ex02;

public class UserNotFoundException extends RuntimeException {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    @Override
    public String toString() {
        return (ANSI_RED+"There is no user with this Identifier"+ANSI_RESET);
    }

    public UserNotFoundException(){
        super(ANSI_RED+"There is no user with this Identifier"+ANSI_RESET);
    }
}
