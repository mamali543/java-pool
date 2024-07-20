package mr.school1337.numbers;

public class IllegalNumberException extends RuntimeException{

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";

    public IllegalNumberException(){
        super(ANSI_RED+"Illegal Number!"+ANSI_RESET);
    }
}