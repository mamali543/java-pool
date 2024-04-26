package ex04;

public class TransactionListEmptyException extends RuntimeException{

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";

    @Override
    public String toString() {
        return ANSI_RED+"Transaction list is empty"+ANSI_RESET;
    }
    public TransactionListEmptyException(){
        super(ANSI_RED+"Transaction list is empty"+ANSI_RESET);
    }
}
