package ex04;
public class TransactionListEmptyException extends RuntimeException{
    @Override
    public String toString() {
        return "Transaction list is empty";
    }

}
