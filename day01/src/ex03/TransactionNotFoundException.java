package ex03;

public class TransactionNotFoundException extends RuntimeException{
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    @Override
    public String toString() {
        return (ANSI_RED+"Transaction with current ID does not exist"+ANSI_RESET);
    }
}
