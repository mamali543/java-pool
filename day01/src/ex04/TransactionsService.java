package ex04;


import java.util.UUID;

public class TransactionsService {
    private UsersList usersList;
    public void addUser(User user){ usersList.addUser(user); }

    public int retrieveUserBalance(User user){
        int balance = usersList.getUserById(user.getIdentifier()).getBalance();
        return balance;
    }

    public Transaction[] retrieveUserTransactions(User user){
        User u = usersList.getUserById(user.getIdentifier());
        Transaction[] t =  u.getTransactionsLinkedList().toArray();
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
    }

//    private int countUnpairedTransactions(User user) {
//        int count = 0;
//        Transaction[] userTransactionArray = user.getTransactionsLinkedList().toArray();
//        for (int userTransactionsCounter = 0; userTransactionsCounter < userTransactionArray.length; ++userTransactionsCounter) {
//            boolean notFound = checkUnpairedTransactionArray(user, userTransactionArray, userTransactionsCounter);
//            if (notFound) {
//                count++;
//            }
//        }
//        return count;
//    }
//
//    public Transaction[] checkValidityTransactions() {
//        int unpairedTransactionsCount = 0;
//        for (int count = 0; count < userDataBase.getAmountUser(); count++) {
//            unpairedTransactionsCount += countUnpairedTransactions(userDataBase.findUserIndex(count));
//        }
//        Transaction[] unpairedTransactionsArray = new Transaction[unpairedTransactionsCount];
//        int position = 0;
//
//        return unpairedTransactionsArray;
//    }
//
//    private boolean checkUnpairedTransactionArray(User user, Transaction[] userTransactionArray, int userTransactionsCounter){
//        Transaction[] partnerTransactionArray;
//        if (userTransactionArray[userTransactionsCounter].getRecipient().getID() == user.getID()) {
//            partnerTransactionArray = userDataBase.findUserID(userTransactionArray[userTransactionsCounter].getSender().getID())
//                    .getTransactionsLinkedList().toArray();
//        } else {
//            partnerTransactionArray = userDataBase.findUserID(userTransactionArray[userTransactionsCounter].getRecipient().getID())
//                    .getTransactionsLinkedList().toArray();
//        }
//        boolean notFound = true;
//        for (int partnerCounter = 0; partnerCounter < partnerTransactionArray.length; ++partnerCounter) {
//            if (userTransactionArray[userTransactionsCounter].getUID()
//                    == partnerTransactionArray[partnerCounter].getUID()) {
//                notFound = false;
//                break;
//            }
//        }
//        return notFound;
//    }

}
