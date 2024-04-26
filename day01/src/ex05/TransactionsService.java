package ex05;


import java.util.UUID;

public class TransactionsService {
    private UsersList usersList;

    public UsersList getUsersList() {
        return usersList;
    }
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
    }

    public Transaction[] checkTransactionsValidity(){
        int unpairedTransactions = 0;
        TransactionsLinkedList unpairedTransactionsList = new TransactionsLinkedList();
        for (int count = 0; count < usersList.getCountUsers(); count++ )
        {
            TransactionsLinkedList unpairedUserTransactions = new TransactionsLinkedList();
            unpairedUserTransactions = countUnpairedTransactions(usersList.getUserByIndex(count));
            if (unpairedUserTransactions.getLength() > 0)
            {
                Transaction[] transactions = unpairedUserTransactions.toArray();
                for (int i = 0; i < transactions.length; i++)
                    unpairedTransactionsList.addTransaction(transactions[i]);
            }
        }
        if (unpairedTransactionsList.getLength() == 0)
            throw new TransactionListEmptyException();
        else
            return unpairedTransactionsList.toArray();
    }

    private TransactionsLinkedList countUnpairedTransactions(User user) {
        TransactionsLinkedList unpairedTransactionsLinkedList = new TransactionsLinkedList();
        Transaction[] userTransactions = user.getTransactionsLinkedList().toArray();
        for (int i = 0; i < userTransactions.length; i++){
            boolean found = checkUnpairedTransactions(user, userTransactions[i]);
            if (found){
                unpairedTransactionsLinkedList.addTransaction(userTransactions[i]);
            }
        }
        return unpairedTransactionsLinkedList;
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
