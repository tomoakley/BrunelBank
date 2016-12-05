package com.tom;

/**
 * Created by Tom on 05/12/2016.
 */
public class State {

    static Account account;

    public static void setAccount(Account account) {
        State.account = account;
    }

    public static Account getAccount() {
        return account;
    }


}
