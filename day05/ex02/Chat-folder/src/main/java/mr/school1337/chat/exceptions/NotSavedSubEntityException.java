package mr.school1337.chat.exceptions;

public class NotSavedSubEntityException extends RuntimeException{
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";

    public NotSavedSubEntityException(){
        super(ANSI_RED+"Subentity Id not found!"+ANSI_RESET);
    }
}