package ex01;

public class Hen extends Thread{
    private int count;
    private Store store;
    public Hen(int count, Store store){
        this.count = count;
        this.store = store;
    }
    @Override
    public void run(){
        for (int i = 0; i < count ; i++) {
            this.store.print("Hen");
        }
    }
}
