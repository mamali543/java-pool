package ex03;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Program {
    private static final String FILE_URLS_NAME = "ex03/file_urls.txt";
    private static final HashMap<String, Integer> mapUrls = new HashMap<>();
    public static void main(String[] args){
        if (args.length != 1){
            System.err.println("wrong arguments number");
            System.exit(-1);
        }
        String[] s = args[0].split("\\W+");
        if (!s[1].equals("threadsCount")){
            System.err.println("Invalid arguments");
            System.exit(-1);
        }
        int count = Integer.parseInt(s[2]);
        File file = new File(FILE_URLS_NAME);
        ExecutorService executorService = Executors.newFixedThreadPool(count);

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String stringBuff;
            int index = 0;
            while ((stringBuff = bufferedReader.readLine()) != null) {
                mapUrls.put(stringBuff, ++index);
            }
            ArrayList<String> keys = new ArrayList<>(mapUrls.keySet());
            for (int i = 0; i < mapUrls.size(); i++) {
                Runnable task = new threadDownload(keys.get(i), i, i % count);
                executorService.execute(task);
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            executorService.shutdown();
        }
    }
}
