package ex04;

import java.util.UUID;

public class Transaction {


    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    private UUID Identifier;
    private User Sender;
    private User Recipient;
    private int Transfer_amount;

    public Transaction(User sender, User recipient) {
        Identifier = UUID.randomUUID();
        this.Sender = sender;
        this.Recipient = recipient;
    }
    public UUID getIdentifier() {
        return this.Identifier;
    }
    public User getSender() {
        return this.Sender;
    }
    public User getRecipient() {
        return this.Sender;
    }
    public int getTransfer_amount() {
        return this.Transfer_amount;
    }
    public void setSender(User sender){
        this.Sender = sender;
    }
    public void setRecipient(User recipient){
        this.Recipient = recipient;
    }
    public void setTransfer_amount(int transferAmount){
        /*check if amount greater than 0*/
        if (transferAmount >= 0)
        {
            /*check if sender has enough balance*/
            if (Sender.getBalance() >= transferAmount){
                /*get Recipient and sender Balances and update them*/
                int recipientBalance = Recipient.getBalance();
                int senderBalance = Sender.getBalance();
                Recipient.setBalance(recipientBalance+transferAmount);
                Sender.setBalance(senderBalance-transferAmount);
                Recipient.setTransferCategorie("INCOME");
                Sender.setTransferCategorie("OUTCOME");
                this.Transfer_amount = transferAmount;
            }
            else {
                System.err.print(ANSI_RED+"Operation Failed!, Sender: "+Sender.getName()+" doesn't have enough balance"+ANSI_RESET);
                System.exit(-1);
            }
        }
    }
    public void printConsole() {
        System.out.print("\nID: " + getIdentifier() +
                "\nSender: " + "ID: " + Sender.getIdentifier() + " ,Name: "  +Sender.getName() + " ,TransferAmount: "  +getTransfer_amount() + " ,TransferCategorie: " + Sender.getTransferCategorie() +
                "\nRecipient: " +  "ID: " + Recipient.getIdentifier() +" ,Name: " +Recipient.getName() +" ,TransferAmount: "  + getTransfer_amount() + " ,TransferCategorie: " + Recipient.getTransferCategorie() +
                "\n\n||SENDER|| "  + " Balance: " + Sender.getBalance() +
                "\n\n||RECIPIENT|| " + " Balance: " + Recipient.getBalance() + "\n");
    }
}
