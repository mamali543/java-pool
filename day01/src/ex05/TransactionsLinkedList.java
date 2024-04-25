package ex05;
import java.util.UUID;
public class TransactionsLinkedList implements TransactionsList {
    private int length = 0;
    private TransactionNode head;

    public int getLength() {
        return length;
    }
    @Override
    public void addTransaction(Transaction transaction) {
        TransactionNode newNode = new TransactionNode(transaction);
        if (head != null){
            head.setNext(newNode);
            newNode.setPrevious(head);
        }
        head = newNode;
        this.length++;
    }
    @Override
    public void removeTransactionById(UUID uuid) {
        if (head == null) {
            throw new TransactionListEmptyException();
        }
        TransactionNode node = head;
        while(node != null){
            if (node.getData().getIdentifier().equals(uuid)){
                if (node.getNext() != null){ node.getNext().setPrevious(node.getPrevious()); }
                if (node.getPrevious() != null){
                    if (node.getNext() == null){
                        head = node.getPrevious();
                    }
                    (node.getPrevious()).setNext(node.getNext());
                }
                this.length--;
                return;
            }
            node = node.getPrevious();
        }
        throw new TransactionNotFoundException();
    }

    @Override
    public Transaction findTransactionById(UUID uuid) {
        if (head == null) {
            throw new TransactionListEmptyException();
        }
        TransactionNode node = head;
        while (node != null){
            Transaction transaction = node.getData();
            if (transaction.getIdentifier().equals(uuid))
                return transaction;
            node = node.getPrevious();
        }
        throw new TransactionNotFoundException();
    }

    @Override
    public Transaction[] toArray() {
        if (head == null) {
            throw new TransactionListEmptyException();
        }
        Transaction[] transactionsArray = new Transaction[length];
        TransactionNode node = head;
        for (int i = length-1; i>=0; i--) {
            transactionsArray[i] = node.getData();
            node = node.getPrevious();
        }
        return transactionsArray;
    }

    public void print() {
        Transaction[] arr = toArray();
        for (Transaction el : arr ) {
            System.out.println("TransactionId: "+el.getIdentifier()+"\nTransactionSender: "+ el.getSender().getName()+"\nTransactionRecipient: "+ el.getRecipient().getName()+"\nTransactionTransferAmount: " +el.getTransfer_amount());
        }
        System.out.println("--------------------------");
    }
}
