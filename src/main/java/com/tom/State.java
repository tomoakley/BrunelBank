package com.tom;

/**
 * Created by Tom on 05/12/2016.
 */
public class State {

    static Account account;
    static int accNumber;

    public static void setAccount(int accNumber, Account account) {
        State.account = account;
        State.accNumber = accNumber;
    }

    public static Account getAccount() {
        return account;
    }

    public static int getAccountNumber() {
        return accNumber;
    }


}
