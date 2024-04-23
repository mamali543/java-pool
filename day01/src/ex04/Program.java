package ex04;

import ex03.TransactionListEmptyException;

public class Program {
    public static void main(String[] args) {
        User p1 = new User("Angle", 3600);
        User p2 = new User("Bob", 1000);
        User p3 = new User("Franke", -200);

        TransactionsService service = new TransactionsService();
        service.addUser(p1);
        service.addUser(p2);
        service.addUser(p3);

        System.out.println("\nUsers before transaction");
//        System.out.println(p1);
//        System.out.println(p2);
//        System.out.println(p3);
        p1.printConsole();
        p2.printConsole();
        p3.printConsole();
        System.out.println("\nTransfer transaction\n");
        try{
            service.transferTransaction(p1.getIdentifier(), p2.getIdentifier(), 3700);
        }
        catch(IllegalTransactionException e){
            System.out.println("exception Occurred: "+ e.getMessage());
        }

        try{
            service.transferTransaction(p1.getIdentifier(), p2.getIdentifier(), 1000);
        }
        catch(IllegalTransactionException e){
            System.out.println("exception Occurred: "+ e.getMessage());
        }

        try{
            service.transferTransaction(p1.getIdentifier(), p3.getIdentifier(), 300);
        }
        catch(IllegalTransactionException e){
            System.out.println("exception Occurred: "+ e.getMessage());
        }


        System.out.println("\nUser balance after one transaction");
        System.out.println(service.retrieveUserBalance(p1));
        System.out.println(service.retrieveUserBalance(p2));
        System.out.println(service.retrieveUserBalance(p3));
        System.out.println("\nUser translations");
        service.retrieveUserTransactions(p1).print();
        service.retrieveUserTransactions(p2).print();
        service.retrieveUserTransactions(p3).print();

        System.out.println("\nDeleting a transaction...");
        int transactionIndex = 1;
        service.removeUserTransaction(p1.getIdentifier(), service.retrieveUserTransactions(p1).toArray()[transactionIndex].getIdentifier());
//        transactionIndex = 0;
//        service.removeUserTransaction(p3.getIdentifier(), service.retrieveUserTransactions(p3).toArray()[transactionIndex].getIdentifier());

        System.out.println("\nUser transfers after deleting a transaction by ID");
        service.retrieveUserTransactions(p1).print();
        service.retrieveUserTransactions(p2).print();
        service.retrieveUserTransactions(p3).print();

        System.out.println("\nUnpaired transactions");
        printTransactionArray(service.checkTransactionsValidity());
    }
    public static void printTransactionArray(Transaction[] transactionArray) {
        if (transactionArray.length == 0)
            System.out.println("No transactions\n");
        System.out.println("length of unpairedTransactionArray: "+transactionArray.length);
        for (int count = 0; count < transactionArray.length; count++)
            transactionArray[count].printConsole();
    }
}
