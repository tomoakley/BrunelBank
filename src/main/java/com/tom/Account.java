package com.tom;

import com.google.common.collect.ImmutableMap;
import com.tom.utils.json;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Tom on 14/11/2016.
 */
public class Account {

    private int id;
    private String accountName;
    private int balance;
    private long locked;

    Account(String accountName) {
        this.accountName = accountName;
        this.balance = 0;
        this.locked = -1;
        List<Account> accounts = json.getAccountsJson();
        setId(accounts.size());
    }

    public void setId(int id) { this.id = id; }

    public int getId() {
        return id;
    }

    public String getAccountName() {
        return accountName;
    }

    public int getBalance() { return balance; }

    public synchronized void setBalance(int balance) throws InterruptedException {
        Thread thisThread = Thread.currentThread();
        while (this.locked != thisThread.getId()) {
            System.out.println(getAccountName() + " is locked by " + this.locked);
            wait();
        }
        this.balance = balance;
    }

    public synchronized void setLock(Thread thisThread) throws InterruptedException {
        wait(5000);
        while (this.locked != -1) {
            System.out.println(thisThread.getId() + " is waiting for a lock on account " + getAccountName() + " currently locked by " + this.locked);
            wait();
        }
        this.locked = thisThread.getId();
        System.out.println(this.locked + " got a lock on account " + getAccountName());
    }

    public synchronized void releaseLock() {
        System.out.println(this.locked + " has released a lock on account " + getAccountName());
        this.locked = -1;
        notifyAll();
    }

}
