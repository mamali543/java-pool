package ex02;
public class User {
    private int Identifier;
    private String Name;

    private int Balance;

    public int getIdentifier(){
        return this.Identifier;
    }

    public String getName(){
        return this.Name;
    }
    public User(String name, int balance){
        this.Identifier = UserIdsGenerator.getInstance().generateId();
        this.Name = name;
        this.Balance = balance;
    }

    public void printConsole() {
        System.out.format("\nID: %d\nName: %s\nBalance: %d\n", Identifier, Name, Balance);
    }

}
