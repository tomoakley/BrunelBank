package com.tom;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tom on 13/12/2016.
 */
public class LockState {
    static private Map<String, Long> locked = new HashMap<>();

    public static boolean tryLock(String accountName) {
        Thread thisThread = Thread.currentThread();
        if (locked.containsKey(accountName)) { // can't have the same account locked by two threads now can we?
            return locked.get(accountName) == thisThread.getId(); // either, the account has been locked by a different thread, or the current thread
        } else {
            locked.put(accountName, thisThread.getId()); // woohoo got the lock!
            return true;
        }
    }

    public static void unlock(String accountName) {
        locked.remove(accountName);
    }

}