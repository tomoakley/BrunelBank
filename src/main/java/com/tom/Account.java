package com.tom;

/**
 * Created by Tom on 14/11/2016.
 */
public class Account {

    private int id; // not actually needed but probably useful to keep here to remind me it exists
    private String accountName;
    private double balance;

    Account(String accountName) {
        this.accountName = accountName;
        this.balance = 0;
        Database.createAccount(this.accountName, this.balance);
        Server.log(accountName + " created");
    }

    Account(int id, String accountName, int balance) {
        this.id = id;
        this.accountName = accountName;
        this.balance = balance;
    }

    public String getAccountName() {
        return accountName;
    }

    public synchronized double getBalance() {
        if (LockState.tryLock(accountName)) {
            return Database.getAccountBalance(accountName);
        } else {
            Server.log("Lock not acquired for account " + accountName + " before getting balance");
            return 0;
        }
    }

    public void setBalance(double balance) {
        if (LockState.tryLock(accountName)) {
            this.balance = balance;
            Database.setAccountBalance(accountName, this.balance);
            Server.log(getAccountName() + " balance updated");
        } else {
            Server.log("Lock not acquired for " + this.accountName + " before setting balance");
        }
    }

    public synchronized void lock() {
        try {
            while (!LockState.tryLock(accountName)) {
                Server.log("waiting for a lock on account " + accountName);
                wait();
            }
            Server.log(getAccountName() + " locked");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void release() {
        LockState.unlock(accountName);
        Server.log(accountName + " lock released");
        notifyAll();
    }

    public void logout() {
        this.balance = 0;
        this.accountName = null;
        this.id = 0;
    }

}
