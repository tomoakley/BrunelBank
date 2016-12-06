package com.tom.utils;

import com.tom.Account;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Tom on 06/12/2016.
 */
public class account {
    public static Account findAccount(String accountName) {
        List<Account> accountList = json.getAccountsJson();
        Account account;
        Account target = null;
        assert accountList != null;
        for (int i = 0; i < accountList.size(); i++) {
            account = accountList.get(i);
            if (account.getAccountName().equals(accountName)) {
                target = account;
                target.setId(i);
            }
        }
        return target;
    }
}
