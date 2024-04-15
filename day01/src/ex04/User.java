package ex04;

public class User {
    private int Identifier;
    private String Name;
    private int Balance;
    private String transferCategorie;
    private TransactionsLinkedList transactionsLinkedList;

    public User(String name, int balance){
        this.Identifier = UserIdsGenerator.getInstance().generateId();
        this.Name = name;
        this.Balance = balance;
        this.transactionsLinkedList = new TransactionsLinkedList();

    }

    public int getIdentifier(){
        return this.Identifier;
    }

    public String getName(){
        return this.Name;
    }
    public int getBalance(){
        return this.Balance;
    }
    public String getTransferCategorie() {
        return this.transferCategorie;
    }

    public TransactionsLinkedList getTransactionsLinkedList() { return this.transactionsLinkedList;}

    public void setBalance(int balance) {
        if (balance >= 0) {
            this.Balance = balance;
        }
    }
    public void setTransferCategorie(String transferCategorie) {
        this.transferCategorie = transferCategorie;
    }
    public void setName(String name){ this.Name = name;}
    public void setTransactionsLinkedList(TransactionsLinkedList transactionsLinkedList){ this.transactionsLinkedList = transactionsLinkedList;}



    public void printConsole() {
        System.out.format("\nID: %d\nName: %s\nBalance: %d\n", Identifier, Name, Balance);
    }

}
