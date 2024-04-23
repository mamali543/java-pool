package ex04;

public class IllegalTransactionException extends RuntimeException{

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";

    @Override
    public String toString(){
        return ANSI_RED+"Insufficient funds for transfer"+ANSI_RESET;
    }
    public IllegalTransactionException(){
        super(ANSI_RED+"Insufficient funds for transfer"+ANSI_RESET);
    }
}
