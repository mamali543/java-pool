package ex01;
public class Store {
    private  boolean eggTurn = true;
    public synchronized void print(String msg) {
        try {
            while ((eggTurn && !msg.equals("Egg")) || (!eggTurn && !msg.equals("Hen")))
                wait();
            eggTurn = !eggTurn;
            System.out.println(msg);
            notifyAll();
        }
        catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }
}
