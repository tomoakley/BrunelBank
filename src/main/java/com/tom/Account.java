package com.tom;

import com.google.common.collect.ImmutableMap;
import com.tom.utils.json;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Tom on 14/11/2016.
 */
public class Account {
    int id;
    String accountName;
    int balance;

    Account(String accountName) {
        this.accountName = accountName;
        this.balance = 0;
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

    public void setBalance(int balance) {
        this.balance = balance;
    }

}
