package mr.t1337school.printer.app;
import printer.logic.Logic;

public class Program {
    public static void main(String[] args){
        if (args.length != 3 || args[0].length() != 1 || args[1].length() != 1){
            System.err.println("Incorrect usage!\\nUsage: program_name <char for white pixel> <char for black pixel> <full path to BMP image>!");
            System.exit(-1);
        }
        new Logic(args[0].charAt(0), args[1].charAt(0), args[2]).printImage();
    }
}
