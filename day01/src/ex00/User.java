package ex00;

import java.util.UUID;

public class User {
    private int Identifier;
    private String Name;
    private long Balance;
    private String Transfer_category;


    public int getIdentifier(){
        return this.Identifier;
    }

    public void setIdentifier(int id){
        this.Identifier = id;
    }
    public long getBalance(){
        return this.Balance;
    }

    public void setBalance(long balance){
        this.Balance = balance;
    }

    public void setName(String name){
        this.Name = name;
    }

    public String getName(){
        return this.Name;
    }
    public String getTransfer_category() {
        return  this.Transfer_category;
    }

    public void setTransfer_category(String transferCategory){
        this.Transfer_category = transferCategory;
    }

    public void printConsole() {
        System.out.format("\nID: %d\nName: %s\nBalance: %d\n", Identifier, Name, Balance);
    }

}
