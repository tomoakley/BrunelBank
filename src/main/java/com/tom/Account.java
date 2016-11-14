package com.tom;

/**
 * Created by Tom on 14/11/2016.
 */
public class Account {
    String id;
    String accountName;
    int amount;

    Account(String accountName) {
        this.accountName = accountName;
        this.amount = 0;
    }

    public String getAccountName() {
        return accountName;
    }

    public int getAmount() {
        return amount;
    }

}
