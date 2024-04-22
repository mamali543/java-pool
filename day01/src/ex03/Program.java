package ex03;

public class Program {
    public static void main(String[] args) {
        User p1 = new User("Mike", 2600);
        User p2 = new User("John", 1000);
        User p3 = new User("Nikol", -200);

        p1.printConsole();
        p2.printConsole();
        p3.printConsole();

        System.out.println("|||||||||||||||||||");
        Transaction t1 = new Transaction(p1, p2);
        t1.setTransfer_amount(1000);
        t1.printConsole();

        Transaction t2 = new Transaction(p2, p1);
        t2.setTransfer_amount(50);
        t2.printConsole();

        Transaction t3 = new Transaction(p2, p3);
        t3.setTransfer_amount(200);
        t3.printConsole();

        Transaction t4 = new Transaction(p2, p3);
        t4.setTransfer_amount(400);
        t4.printConsole();

        System.out.println("|||||||||||||||||||");

        TransactionsLinkedList tl = new TransactionsLinkedList();
        tl.addTransaction(t1);
        tl.addTransaction(t2);
        tl.addTransaction(t3);
        tl.addTransaction(t4);
        tl.print();
        System.out.println("\nTransaction ID: " + t2.getIdentifier());
        tl.removeTransactionById(t2.getIdentifier());

        System.out.println("\n||Transaction List to Array||");
        tl.print();

        p1.setTransactionsLinkedList(tl);
        System.out.println("\n"+p1.getTransactionsLinkedList().getLength());
    }
}