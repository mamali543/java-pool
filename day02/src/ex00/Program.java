package ex00;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Program {
    private static final String SIGNATURES = "ex00/signatures.txt";

    /*Each method that interacts with file I/O includes throws IOException in its declaration. This declaration does not handle the exception within the method itself but instead passes the responsibility to handle the exception up to whatever method calls it.
    In the case of the main method, any uncaught IOException would terminate the program and could be caught by the JVM, which might print a stack trace to the standard error stream.*/
    public static void main(String[] args) {
        Map<String, String> signatures = null;
        try {
            signatures = loadSignatures();
        }
        catch(IOException e){
            System.err.println("exception caught: "+ e.getMessage());
        }
        Scanner scanner = new Scanner(System.in);
        try (PrintWriter writer = new PrintWriter("result.txt")){
            while (true){
                System.out.println("Enter file path:");
                String path = scanner.nextLine();
                if (path.equals("42"))
                    break;
                String result = identifyFileType(path, signatures);
                if (!result.equals("UNDEFINED"))
                {
                    writer.println(result);
                    System.out.println("PROCESSED");
                }
                else
                    System.out.println("UNDEFINED");
            }
        }
        catch (IOException e){
            System.err.println(e.getMessage());
        }
    }

    /*This method is designed to determine the file type of given file*/
    private static String identifyFileType(String path, Map<String, String> signatures) throws IOException {
        try(FileInputStream is = new FileInputStream(path)){
            byte[] bytes = new byte[30];
            if (is.read(bytes) != -1){
                //StringBuilder it's a mutable sequence of characters, it has to be converted into a String
                StringBuilder hexString = new StringBuilder();
                for (byte b: bytes){
                    hexString.append(String.format("%02X", b));
                }
                String s = hexString.toString();
//                System.out.println("byte signature sequence:>> "+s);
                //Map is an interface representing a collection that maps keys to values, and each key-value pair is handled as an Entry.
                for (Map.Entry<String, String> entry: signatures.entrySet())
                {
                    if (s.startsWith(entry.getKey()))
                        return entry.getValue();
                }
            }
        }
        return "UNDEFINED";
    }

    /*to call a method directly from a static context without instantiating a class object the method has to be static, or u have to instantiate a class object then call the methode object.method */
    private static Map<String, String> loadSignatures() throws IOException {
        Map<String, String> signatures = new HashMap<>();
        /*This method uses a try-with-resources statement to automatically manage the BufferedReader resource,
        ensuring it is closed after the try block, even if an exception is thrown.*/
        try (BufferedReader reader = new BufferedReader(new FileReader(SIGNATURES))){
            String line;
            while ((line = reader.readLine()) != null){
                String[] args = line.split(", ");
                signatures.put(args[1].replace(" ", ""), args[0]);
            }
        }
        return signatures;
    }
}

