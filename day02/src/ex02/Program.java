package ex02;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
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
            s = str.split("\\s+");
            if (s.length == 1 && s[0].equals("ls")){
                listFiles(currentPosition);
            } else if (s.length == 2 && s[0].equals("cd")) {
                currentPosition = changeDirector(currentPosition, s);
                System.out.println(currentPosition);
            } else if (s.length == 3 && s[0].equals("mv")) {
                moveFile(currentPosition, s);
            }
        }
    }

    private static String changeDirector(String currentPosition, String[] s) {
        String newPathSegment = s[1];
        Path currentPath = Paths.get(currentPosition);
        Path newPath = currentPath.resolve(newPathSegment).normalize();

        String normalizedPath = newPath.toString();

        // Check if the resulting path is valid and exists
        if (new File(normalizedPath).exists())
            return normalizedPath;
        return null;
    }

    private static void moveFile(String currentPosition, String[] s) {
        String source = s[1];
        String destination = s[2];
//        System.out.println("destination: "+ destination);
        File srcFile = new File(currentPosition,source);
        File destFile = new File(currentPosition,destination);
        if (destFile.exists()){
                //if u need to move a file to a directory without specifying its new name renameTo() needs to know the destination with the name of the sourceFile
                File newDestFile = new File(destFile, srcFile.getName());
                srcFile.renameTo(newDestFile);
        }
        else
            srcFile.renameTo(destFile);
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
