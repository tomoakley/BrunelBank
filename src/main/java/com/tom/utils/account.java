package com.tom.utils;

import com.tom.Account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Tom on 06/12/2016.
 */
public class account {

    static ArrayList<String> loggedInAccounts = new ArrayList<>();

    public static void addLoggedInAccount(String accountName) {
        account.loggedInAccounts.add(accountName);
    }

    public static ArrayList<String> getLoggedInAccounts() {
        return account.loggedInAccounts;
    }

}
