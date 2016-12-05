package com.tom.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.tom.Account;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Tom on 01/12/2016.
 */
public class json {

    public static List<Account> getAccountsJson() {
        try {
            JsonReader reader = new JsonReader(new FileReader("src/accounts.json"));
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Account>>(){}.getType();
            return gson.fromJson(reader, listType);
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        }
        return null;
    }

}
