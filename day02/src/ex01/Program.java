package ex01;

import java.io.*;
import java.util.*;
/*HashMap and Map are different interfaces HashMap has more methods than Map interface*/

public class Program {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";

    public static String fileA = "fileA.txt";
    public static String fileB = "fileB.txt";
    static Map<String, Integer[]> occurrencesMap = new HashMap<>();
    public static void main(String[] args) {
        if (args.length != 2){
            System.err.println("Wrong Arguments Number");
            System.exit(-1);
        }
        File a = new File(fileA);
        File b = new File(fileB);
        long sizeLimit = 10 * 1024 * 1024;
        if (a.length()+b.length() > sizeLimit){
            System.err.println(ANSI_RED+"10MB size exceeded!"+ANSI_RESET);
            System.exit(-1);
        }
        try {
            FileOccurences(args[0], 0);
            FileOccurences(args[1], 1);
        }
        catch (IOException e){
            System.err.println(ANSI_RED+"exception caught: "+e.getMessage()+ANSI_RESET);
        }
        TreeMap<String, Integer[]> sortedMap = new TreeMap<>(occurrencesMap);
        for (Map.Entry<String, Integer[]> entry: sortedMap.entrySet()){
            System.out.println("key: "+entry.getKey()+" fileAvalue: "+entry.getValue()[0]+" fileBvalue: "+entry.getValue()[1]);
        }
        double numerator = getNumerator(sortedMap);
        double denominator = getDenominator(sortedMap);
        System.out.println("Similarity = "+String.format("%.3f", numerator/denominator));
        try(FileWriter writer = new FileWriter("dictionary.txt")){
            for (Map.Entry<String, Integer[]> entry: sortedMap.entrySet())
                writer.write(entry.getKey()+ " ");
        }
        catch (IOException e){
            System.err.println("exception caught: "+ e.getMessage());
        }
    }

    private static double getDenominator(TreeMap<String, Integer[]> sortedMap) {
        double Denomiator = 0;
        double DenomiatorA = 0;
        double DenomiatorB = 0;
        for (Map.Entry<String, Integer[]> entry: sortedMap.entrySet()){
            DenomiatorA += Math.pow(entry.getValue()[0], 2);
            DenomiatorB += Math.pow(entry.getValue()[1], 2);
        }
        Denomiator = Math.sqrt(DenomiatorA)*Math.sqrt(DenomiatorB);
        return Denomiator;
    }

    private static long getNumerator(TreeMap<String, Integer[]> sortedMap) {
        long Numerator = 0;
        for (Map.Entry<String, Integer[]> entry: sortedMap.entrySet()){
            Numerator += entry.getValue()[0]*entry.getValue()[1];
        }
        return Numerator;
    }

    private static void FileOccurences(String arg, int fileIndex) throws IOException {
        try(BufferedReader reader = new BufferedReader(new FileReader(arg))){
            String line;
            while ((line = reader.readLine()) != null){
                /*When splitting lines into words, the regex "\\s+" handles any amount of whitespace,
                 ensuring words are correctly separated.*/
                String[] str = line.split(" ");
                for (String s: str){
                    s.trim();
                    occurrencesMap.putIfAbsent(s, new Integer[]{0, 0});
                    occurrencesMap.get(s)[fileIndex]++;
                }
            }
        }
    }
}
