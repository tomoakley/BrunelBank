package com.tom.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.tom.Account;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import static java.lang.Integer.parseInt;

/**
 * Created by Tom on 01/12/2016.
 */
public class json {

    public static List<Account> getAccountsJson() {
        try {
            JsonReader reader = new JsonReader(new FileReader("src/accounts.json"));
            Gson gson = new Gson();
            Type accountMap = new TypeToken<List<Account>>(){}.getType();
            return gson.fromJson(reader, accountMap);
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        }
        return null;
    }

    public static void writeToJson(List<Account> accountsList) {
        Gson gson = new Gson();
        String accountsListGson = gson.toJson(accountsList);
        try {
            FileOutputStream outputStream = new FileOutputStream("src/accounts.json");
            outputStream.write(accountsListGson.getBytes());
            outputStream.close();
        } catch(Exception e) {
            System.out.println("File not found!");
        }
    }
}