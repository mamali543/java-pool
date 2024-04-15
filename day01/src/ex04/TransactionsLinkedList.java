package ex04;
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
    public Transaction[] toArray() {
        Transaction[] transactionsArray = new Transaction[length];
        TransactionNode node = head;
        if(node.getData() == null)
            throw new TransactionListEmptyException();
        for (int i = length-1; i>=0; i--) {
            transactionsArray[i] = node.getData();
            node = node.getPrevious();
        }
        return transactionsArray;
    }
    public void print() {
        Transaction[] arr = toArray();
        for (Transaction el : arr ) {
            System.out.println("Transaction: "+el.getIdentifier());
        }
    }
}
