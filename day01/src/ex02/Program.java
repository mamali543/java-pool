package ex02;

public class Program {
    public static void main(String[] args) {
        User p1 = new User("Mike", 2600);
        User p2 = new User("John", 1000);
        User p3 = new User("Nikol",-200);

        p1.printConsole();
        p2.printConsole();
        p3.printConsole();

//        Transaction t1 = new Transaction(p1, p2, 1000);
//        t1.doTransaction();
//        t1.printConsole();

        UsersArrayList u1 = new UsersArrayList();
        u1.addUser(p1);
        u1.addUser(p3);
        u1.addUser(p2);
        System.out.println(u1.getUserById(1).getName());
        System.out.println(u1.getUserByIndex(4).getName());
        System.out.println(u1.getCountUsers());
    }
}
