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
        Database.update(
                "INSERT INTO users (name, balance) VALUES ("
                + "'" + this.accountName + "', "
                + this.balance + ") "
        );
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
            return Database.getAccountBalance(getAccountName());
        } else {
            System.err.println("Lock not acquired for account " + getAccountName());
            return 0;
        }
    }

    public void setBalance(double balance) {
        if (LockState.tryLock(accountName)) {
            this.balance = balance;
            Database.update("UPDATE users SET 'balance'=" + this.balance + " WHERE name='" + this.accountName + "'");
        } else {
            System.err.println("Lock not acquired for account " + getAccountName());
        }
    }

    public synchronized void lock() {
        try {
            Thread thisThread = Thread.currentThread();
            while (!LockState.tryLock(getAccountName())) {
                System.out.println(thisThread.getId() + " is waiting for a lock on account " + getAccountName());
                wait();
            }
            if (LockState.tryLock(getAccountName())) {
                System.out.println(thisThread.getId() + " got a lock on account " + getAccountName());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void release() {
        Thread thisThread = Thread.currentThread();
        LockState.unlock(accountName);
        System.out.println(thisThread.getId() + " has released a lock on account " + getAccountName());
        notifyAll();
    }

}
