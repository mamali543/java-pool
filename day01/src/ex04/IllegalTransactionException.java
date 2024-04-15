package ex04;

public class IllegalTransactionException extends RuntimeException{

    @Override
    public String toString(){
        return "Insufficient funds for transfer";
    }
}
