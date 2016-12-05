package com.tom;

/**
 * Created by Tom on 14/11/2016.
 */
public class Account {
    String id;
    String accountName;
    int balance;

    Account(String accountName) {
        this.accountName = accountName;
        this.balance = 0;
    }

    public String getAccountName() {
        return accountName;
    }

    public int getBalance() { return balance; }

    public void setBalance(int balance) {
        this.balance = balance;
    }

}
