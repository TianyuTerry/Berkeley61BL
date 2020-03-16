/**
 * This class represents a bank account whose current balance is a nonnegative
 * amount in US dollars.
 */
public class Account {

    public int balance;
    public Account parentAccount;

    /** Initialize an account with the given BALANCE. */
    public Account(int balance) {
        this.balance = balance;
        this.parentAccount = null
    }

    public Account (int balance, Account parentAccount) {
        this.balance = balance;
        this.parentAccount = parentAccount;
    }

    /** Deposits AMOUNT into the current account. */
    public void deposit(int amount) {
        if (amount < 0) {
            System.out.println("Cannot deposit negative amount.");
        } else {
            balance += amount;
        }
    }

    /**
     * Subtract AMOUNT from the account if possible. If subtracting AMOUNT
     * would leave a negative balance, print an error message and leave the
     * balance unchanged.
     */
    public boolean withdraw(int amount) {
        // TODO
        int all = this.balance;
        Account currentparent = this.parentAccount; 
        while (currentparent != null) {
            all += currentparent.banlance;
            currentparent = currentparent.parentAccount;
        }
        if (amount > all) {
            System.out.println("Cannot withdraw negative amount.");
            return false;
        } else if (balance < amount) {
            System.out.println("Insufficient funds");
            balance = 0;
            parentAccount.withdraw(amount-balance);
            return true;
        } else {
            balance -= amount;
            return true;
        }
    }

    /**
     * Merge account OTHER into this account by removing all money from OTHER
     * and depositing it into this account.
     */
    public void merge(Account other) {
        // TODO
        amount = other.balance;
        other.balance = 0;
        this.balance += amount;
    }
}
