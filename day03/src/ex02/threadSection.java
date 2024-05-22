package ex02;
public class threadSection extends Thread {
    private int threadIndex;
    private int from;
    private int to;
    private int sum;

    public  threadSection(int threadIndex, int start, int end, int sum){
        this.threadIndex = threadIndex+1;
        this.from = start;
        this.to = end;
        this.sum = sum + 1;
    }

    @Override
    public void run() {
        System.out.printf("Thread %d: from %d to %d sum is %d\n", this.threadIndex, this.from, this.to, this.sum);
    }
}
