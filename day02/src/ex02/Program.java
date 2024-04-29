package ex02;

import java.io.File;
import java.util.Scanner;
// 1 kb = 1024 byte
// 1 mb = 1024 * 1024 byte = 1024KB
public class Program {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static void main(String[] args){
        if (args.length != 1){
            System.err.println(ANSI_RED+"Wrong Arguments Number!."+ANSI_RESET);
            System.exit(-1);
        }
        String[] s = args[0].split("=");
        if (s.length != 2) {
            System.err.println(ANSI_RED + "Invalid argument format!." + ANSI_RESET);
            System.exit(-1);
        }
        System.out.println(s[1]);
        String currentPosition = s[1];
        Scanner scanner = new Scanner(System.in);
        String str;
        while ((str = scanner.nextLine()) != "exit"){
            s = str.split("//s+");
            if (s.length == 1 && s[0].equals("ls")){
                listFiles(currentPosition);
            }
        }
    }

    private static void listFiles(String currentPosition) {
        // List files and directories inside the current position
        File dir = new File(currentPosition);
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    System.out.println(file.getName() + " " + ((file.length()/1024) == 0 ? String.format("%.2f KB",file.length()/1024.0) : (file.length()/1024 + " KB")));
                }
            } else {
                System.out.println("The directory is empty.");
            }
        } else {
            System.err.println(ANSI_RED + "The provided path does not exist or is not a directory." + ANSI_RESET);
        }
    }
}
