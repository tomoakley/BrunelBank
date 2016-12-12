package com.tom;

import java.sql.*;

/**
 * Created by Tom on 12/12/2016.
 */
public class Database {
    private static Connection connection = null;
    private static Statement statement = null;
    Database() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:brunelbank.db");
            statement = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS `users` (\n" +
                    "\t`id`\tINTEGER NOT NULL,\n" +
                    "\t`name`\tTEXT NOT NULL,\n" +
                    "\t`balance`\tNUMERIC NOT NULL,\n" +
                    "\t`locked`\t,\n" +
                    "\tPRIMARY KEY(id)\n" +
                    ")";
            statement.executeUpdate(sql);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void update(String sql) {
        try {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static long getLockStatus(String accountName) {
        try {
            ResultSet rs = statement.executeQuery("SELECT locked FROM users WHERE name='" + accountName + "'");
            return rs.getLong("locked");
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static double getAccountBalance(String accountName) {
        try {
            ResultSet rs = statement.executeQuery("SELECT balance FROM users WHERE name='" + accountName + "'");
            return rs.getDouble("balance");
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static Account getAccount(String accountName) {
        try {
            ResultSet rs = statement.executeQuery("SELECT * FROM users WHERE name='" + accountName + "'");
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int balance = rs.getInt("balance");
                long locked = rs.getLong("locked");
                return new Account(id, name, balance, locked);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
