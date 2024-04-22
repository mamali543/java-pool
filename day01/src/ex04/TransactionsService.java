package ex04;


import java.util.UUID;

public class TransactionsService {
    private UsersList usersList;

    TransactionsService() {
        usersList = new UsersArrayList();
    }

    public void addUser(User user){ usersList.addUser(user); }

    public int retrieveUserBalance(User user){
        int balance = usersList.getUserById(user.getIdentifier()).getBalance();
        return balance;
    }

    public TransactionsLinkedList retrieveUserTransactions(User user){
        User u = usersList.getUserById(user.getIdentifier());
        TransactionsLinkedList t =  u.getTransactionsLinkedList();
        return t;
    }

    public void removeUserTransaction(int userId, UUID transactionId){
        User u = usersList.getUserById(userId);
        u.getTransactionsLinkedList().removeTransactionById(transactionId);
    }

    public void transferTransaction(int senderId, int recipientId, int transferAmount){
        User s = usersList.getUserById(senderId);
        User r = usersList.getUserById(recipientId);
        if (s.getBalance() < transferAmount)
            throw new IllegalTransactionException();
        Transaction t =  new Transaction(s, r);
        t.setTransfer_amount(transferAmount);
        s.getTransactionsLinkedList().addTransaction(t);
        r.getTransactionsLinkedList().addTransaction(t);
//        System.out.println("senderName: "+t.getSender().getName()+"\n"+"recipientName: "+t.getRecipient().getName()+"\n");
    }

    public Transaction[] checkTransactionsValidity(){
        int unpairedTransactions = 0;
        System.out.println(usersList.getCountUsers());
        for (int count = 0; count < usersList.getCountUsers(); count++ )
            unpairedTransactions += countUnpairedTransactions(usersList.getUserByIndex(count));
        Transaction[] unpairedTransactionArray = new Transaction[unpairedTransactions];
        System.out.println("unpairedTransactionArray length:  "+unpairedTransactionArray.length);

        return unpairedTransactionArray;
    }

    private int countUnpairedTransactions(User user) {
        int count = 0;
        TransactionsLinkedList unpairedTransactionsLinkedList = new TransactionsLinkedList();
        Transaction[] userTransactions = user.getTransactionsLinkedList().toArray();
        for (int i = 0; i < userTransactions.length; i++){
            boolean found = checkUnpairedTransactions(user, userTransactions[i]);
            if (found){
                unpairedTransactionsLinkedList.addTransaction(userTransactions[i]);
                count++;
            }
        }
        return count;
    }

    private boolean checkUnpairedTransactions(User user, Transaction userTransaction) {
        Transaction[] partnerTransactionArray;
        if (userTransaction.getSender().getIdentifier() == user.getIdentifier())
            partnerTransactionArray = usersList.getUserById(userTransaction.getRecipient().getIdentifier()).getTransactionsLinkedList().toArray();
        else
            partnerTransactionArray = usersList.getUserById(userTransaction.getSender().getIdentifier()).getTransactionsLinkedList().toArray();
        boolean unpaired = true;
        for (int i = 0; i < partnerTransactionArray.length; i++){
            if (userTransaction.getIdentifier() == partnerTransactionArray[i].getIdentifier()) {
                unpaired = false;
                break;
            }
        }
        return unpaired;
    }
}
