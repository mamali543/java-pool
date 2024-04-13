package ex01;

public class User {
    private int Identifier;
    private String Name;

    private int Balance;

    public User(String name, int balance){
        this.Identifier = UserIdsGenerator.getInstance().generateId();
        this.Name = name;
        this.Balance = balance;
    }

    public void printConsole() {
        System.out.format("\nID: %d\nName: %s\nBalance: %d\n", Identifier, Name, Balance);
    }

}
