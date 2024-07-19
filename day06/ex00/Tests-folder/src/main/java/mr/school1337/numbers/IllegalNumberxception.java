public class IllegalNumberxception extends RuntimeException{

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";

    public IllegalNumberxception(){
        super(ANSI_RED+"Illegal Number!"+ANSI_RESET);
    }
}