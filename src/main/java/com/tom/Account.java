package com.tom;

/**
 * Created by Tom on 14/11/2016.
 */
public class Account {

    private int id;
    private String accountName;
    private double balance;
    private long locked = 0;

    Account(String accountName) {
        this.accountName = accountName;
        this.balance = 0;
        this.locked = 0;
        Database.update(
                "INSERT INTO users (name, balance, locked) VALUES ("
                + "'" + this.accountName + "', "
                + this.balance + ", "
                + this.locked + ")"
        );
    }

    Account(int id, String accountName, int balance, long locked) { // this is just for setting the Account details. Overloading ftw!
        this.id = id;
        this.accountName = accountName;
        this.balance = balance;
    }

    public String getAccountName() {
        return accountName;
    }

    public double getBalance() { return Database.getAccountBalance(getAccountName()); }

    public synchronized void setBalance(double balance) throws InterruptedException {
        Thread thisThread = Thread.currentThread();
        while (this.locked != thisThread.getId() && this.locked != 0) {
            System.out.println(thisThread.getId() + " is waiting for a lock on " + getAccountName());
            wait();
        }
        this.balance = balance;
        Database.update("UPDATE users SET 'balance'=" + this.balance + " WHERE name='" + this.accountName + "'");
    }

    public long getLockStatus() {
        return Database.getLockStatus(getAccountName());
    }

    public synchronized void setLock(Thread thisThread) throws InterruptedException {
        wait(5000);
        this.locked = getLockStatus();
        while (this.locked != 0) {
            System.out.println(thisThread.getId() + " is waiting for a lock on account " + getAccountName() + " currently locked by " + this.locked);
            wait();
        }
        this.locked = thisThread.getId();
        Database.update("UPDATE users SET 'locked'=" + this.locked + " WHERE name='" + this.accountName + "'");
        System.out.println(this.locked + " got a lock on account " + getAccountName());
    }

    public synchronized void releaseLock() {
        System.out.println(this.locked + " has released a lock on account " + getAccountName());
        this.locked = 0;
        Database.update("UPDATE users SET 'locked'=" + this.locked + " WHERE name='" + this.accountName + "'");
        notifyAll();
    }

}
