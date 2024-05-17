package ex02;

import java.util.ArrayList;

public class Program {
    public static void main(String[] args) throws InterruptedException {
        if (args.length != 2) {
            System.err.println("wrong number of arguments");
            System.exit(-1);
        }
        String[] arraySize = args[0].split("\\W+");
        String[] threadsCount = args[1].split("\\W+");
        if (!arraySize[1].equals("arraySize") || !threadsCount[1].equals("threadsCount")) {
            System.err.println("Invalid arguments");
            System.exit(-1);
        }
        ArrayList<Thread> threadsArray = new ArrayList<>();
        int size = Integer.parseInt(arraySize[2]);
        int count = Integer.parseInt(threadsCount[2]);
        if (size > 2000000 || count > size) {
            System.err.println("Error: big size");
            System.exit(-1);
        }
        System.out.println("Sum: " + size);
        int start = 0;
        int sum = size / count;
        int end = sum;
        for (int i = 0; i < count - 1; i++) {
            threadsArray.add(new threadSection(i, start, end, sum));
            start = end + 1;
            if (i != count - 2)
                end += sum + 1;
        }
        end = size - (end + 1);
        threadsArray.add(new threadSection(count - 1, start, start + end - 1, end - 1));
        for (Thread t : threadsArray) {
            t.start();
            t.join();
        }
        System.out.println("Sum by threads: " + size);
    }
}
