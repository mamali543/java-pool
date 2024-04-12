package ex00;

public class Program {
    public static void main(String[] args){

        User sender = new User();
        sender.setIdentifier(0);
        sender.setName("Rayan");
        sender.setBalance(20000);
        sender.printConsole();

        User receiver = new User();
        receiver.setIdentifier(1);
        receiver.setName("Reda");
        receiver.setBalance(10000);
        receiver.printConsole();

        Transaction t = new Transaction();
        t.setSender(sender);
        t.setRecipient(receiver);
        t.setTransfer_amount(300);
        t.printConsole();

    }
}
