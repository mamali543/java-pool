package ex05;

import ex02.UserNotFoundException;

import java.util.Scanner;
import java.util.UUID;

/*the Scanner class is used to parse primitive types and strings */
public class Menu {
    private TransactionsService service;
    private int userCommand;
    private int menuCount;
    private boolean devMenu;
    private Scanner scanner;
    public Menu() {
        this.scanner = new Scanner(System.in); /* Initialize the scanner to read from standard input */
        this.service = new TransactionsService();
    }
    public void setDevMenu(boolean devMenu) {
        this.devMenu = devMenu;
    }

    public boolean getDevMenu(){
        return this.devMenu;
    }
    public void startMenu(){
        while (true)
        {
            showMenu();
            enterOptionNumber(scanner.nextInt());
            System.out.println("---------------------------------------------------------");
        }
    }
    private void enterOptionNumber(int optionNumber) {
        System.out.print("menuCount equal: "+menuCount+"\n");
        if (optionNumber < 1 || optionNumber > menuCount)
        {
            System.err.print("enter a number between 1 and "+ (menuCount-1) +" \n");
            startMenu();
        }
        switch (optionNumber){
            case 1:
                addNewUser();
                break;
            case 2:
                viewUserBalance();
                break;
            case 3:
                performTransfer();
                break;
            case 4:
                viewUserTransactions();
                break;
            case 5:
                if (!devMenu)
                    System.exit(0);
                removeTransactionById();
                break;
            case 6:
                checkTransferValidity();
                break;
            case 7:
                System.exit(0);
        }
    }

    private void checkTransferValidity() {
    }

    private void removeTransactionById() {
        scanner.nextLine();
        int userId = 0;
        UUID transactionId = UUID.fromString("");
        while(true){
            System.out.println("Enter a user ID and a transfer ID\n-> ");
            String input = scanner.nextLine();
            String[] args = input.split(" ");
            if (args.length != 2){
                System.out.println("Wrong Arguments Number!");
                continue;
            }
            try{
                userId = Integer.parseInt(args[0]);
            }
            catch(NumberFormatException e){
                System.err.println("Id Must Be A Number!");
                continue;
            }
            try {
                transactionId = UUID.fromString( args[1]);
            }
            catch(IllegalArgumentException e){
                System.err.println("Transfer ID in bad format");
                continue;
            }
            try{
                Transaction transaction = service.getUsersList().getUserById(userId).getTransactionsLinkedList().findTransactionById(transactionId);
                service.removeUserTransaction(userId, transactionId);
                User recipient = transaction.getRecipient();
                System.out.println("Transfer To "+ recipient.getName() +"(id = "+ recipient.getIdentifier()+") "+transaction.getTransfer_amount()+" removed");

            }
            catch(UserNotFoundException e){
                System.err.println("exception caught: "+ e.getMessage());
            }
            catch(TransactionListEmptyException e){
                System.err.println("exception caught: "+ e.getMessage());
            }
            catch(TransactionNotFoundException e){
                System.err.println("exception caught: "+ e.getMessage());
            }
            return ;
        }
    }

    private void viewUserTransactions() {
        scanner.nextLine();
        int Id = 0;
        while(true){
            System.out.println("Enter a user ID\n-> ");
            String userId = scanner.nextLine();
            String[] args = userId.split(" ");
            if (args.length != 1){
                System.out.println("Wrong number of Arguments");
                continue;
            }
            try {
                 Id = Integer.parseInt(userId);
            }
            catch(NumberFormatException e){
                System.err.println("exception caught: "+e.getMessage());
            }
            try{
                User user = service.getUsersList().getUserById(Id);
                Transaction[] transactions = service.retrieveUserTransactions(user).toArray();
                for (int i =0; i < transactions.length; i++){
                    User recipient = transactions[i].getRecipient();
                    int transferAmount = transactions[i].getTransfer_amount();
                    UUID transactionId = transactions[i].getIdentifier();
                    System.out.println("To "+recipient.getName()+"(id = "+recipient.getIdentifier()+") -"+transferAmount+" with id = "+transactionId);
                }
            }
            catch(UserNotFoundException e){
                System.err.println("exception caught: "+ e.getMessage());
            }
            catch(TransactionListEmptyException e){
                System.err.println("exception caught: "+ e.getMessage());
            }
            return ;
        }
    }

    private void viewUserBalance() {
        scanner.nextLine();
        int Id = 0;
        while (true){
            System.out.print("Enter a User ID\n-> ");
            String userId = scanner.nextLine();
            String[] args = userId.split(" ");
            if (args.length != 1){
                System.out.println("Wrong Number Of Arguments");
                continue;
            }
            try{
                Id = Integer.parseInt(userId);
            }
            catch (NumberFormatException e){
                System.err.println("Id must be a Number!");
            }
            try{
                System.out.println((service.getUsersList().getUserById(Id)).getName()+" - "+(service.retrieveUserBalance(service.getUsersList().getUserById(Id))));
            }
            catch(UserNotFoundException e){
                System.err.println("exception caught: "+e.getMessage());
            }
            return ;
        }
    }

    private void performTransfer() {
        int senderId;
        int recipientId;
        int transferAmount;
        scanner.nextLine();
        while (true){
            System.out.print("Enter a sender ID, a recipient ID, and a transfer amount\n-> ");
            String transferDeatial = scanner.nextLine();
            String[] args = transferDeatial.split(" ");
            if (args.length != 3){
                System.out.println("Wrong number of arguments");
                continue;
            }
            try {
                senderId = Integer.parseInt(args[0]);
                recipientId = Integer.parseInt(args[1]);
                transferAmount = Integer.parseInt(args[2]);
            }
            catch (NumberFormatException e){
                System.err.println("All arguments must be numbers");
                continue;
            }
            if (senderId == recipientId) {
                System.out.println("User can't made transaction to himself");
                continue;
            }
            try{
                service.transferTransaction(senderId, recipientId, transferAmount);
            }
            catch (UserNotFoundException e){
                System.err.print("Exception caught: "+ e.getMessage());
                return;
            }
            catch (IllegalTransactionException e){
                System.err.print("Exception caught: "+ e.getMessage());
                return;
            }
            System.out.println("The transfer is completed");
            return;
        }
    }

    private void addNewUser() {
        scanner.nextLine();
        int balance = 0;
        String userName = "";
        while (true){
            System.out.println("Enter a user name and a balance");
            System.out.print("-> ");
            String userAndBalance = scanner.nextLine();
            String[] args = userAndBalance.split(" ");
            if (args.length != 2){
                System.out.println("Wrong number of arguments");
                continue;
            }
            try {
                balance = Integer.parseInt(args[1]);
            }
            catch (NumberFormatException e){
                System.err.println("Balance is not numeric");
                continue;
            }
            userName = args[0];
            break ;
        }
        User user = new User(userName, balance);
        service.addUser(user);
        System.out.format("User with id = %d is added", user.getIdentifier());
    }

    private void showMenu() {
        int menuCount = 1;
        System.out.print(menuCount++ + ". Add a user\n"+menuCount++ + "2. View user balances\n"
        +menuCount++ + ". Perform a transfer\n" + menuCount++ + ". View all transactions for a specific user\n");
        if (devMenu){
            System.out.print(menuCount++ + ". DEV - remove a transfer by ID\n" + menuCount++ + ". DEV - check transfer validity\n");
        }
        System.out.print(menuCount++ + ". Finish execution\n" + "-> ");
    }
}
