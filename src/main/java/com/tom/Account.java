package com.tom;

/**
 * Created by Tom on 14/11/2016.
 */
public class Account {
    String id;
    String accountName;

    Account(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountName() {
        return accountName;
    }
}
