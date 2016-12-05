package com.tom;

/**
 * Created by Tom on 05/12/2016.
 */
public class State {

    static String name;

    public static void setAccountName(String name) {
        State.name = name;
    }

    public static String getAccountName() {
        return name;
    }


}
