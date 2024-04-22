package ex00;

import java.util.UUID;

public class Transaction {


    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    private UUID Identifier;
    private User Sender;
    private User Recipient;
    private Long Transfer_amount;

    public Transaction() {
        Identifier = UUID.randomUUID();
    }
    public UUID getIdentifier() {
        return this.Identifier;
    }

    public User getSender() {
        return this.Sender;
    }

    public User getRecipient() {
        return this.Recipient;
    }


    public Long getTransfer_amount() {
        return this.Transfer_amount;
    }

    public void setSender(User sender){
        this.Sender = sender;
    }

    public void setRecipient(User recipient){
        this.Recipient = recipient;
    }


    public void setTransfer_amount(long transferAmount){
        /*check if amount greater than 0*/
        if (transferAmount >= 0)
        {
            /*check if sender has enough balance*/
            if (Sender.getBalance() >= transferAmount){
                /*get Recipient and sender Balances and update them*/
                long recipientBalance = Recipient.getBalance();
                long senderBalance = Sender.getBalance();
                Recipient.setBalance(recipientBalance+transferAmount);
                Sender.setBalance(senderBalance-transferAmount);
                Recipient.setTransfer_category("INCOME");
                Sender.setTransfer_category("OUTCOME");
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
                "\nSender: " + "ID: " + Sender.getIdentifier() + " ,Name: "  +Sender.getName() + " ,TransferAmount: "  +getTransfer_amount() + " ,TransferCategorie: " + Sender.getTransfer_category() +
                "\nRecipient: " +  "ID: " + Recipient.getIdentifier() +" ,Name: " +Recipient.getName() +" ,TransferAmount: "  + getTransfer_amount() + " ,TransferCategorie: " + Recipient.getTransfer_category() +
                "\n\n||SENDER|| "  + " Balance: " + Sender.getBalance() +
                "\n\n||RECIPIENT|| " + " Balance: " + Recipient.getBalance() + "\n");
    }
}
