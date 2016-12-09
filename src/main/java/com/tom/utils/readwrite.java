package com.tom.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.Buffer;

/**
 * Created by Tom on 07/12/2016.
 */
public class readwrite {

    private static Socket socket;
    private static BufferedReader in;
    private static PrintWriter out;

    public static void setSocket(Socket socket) {
        readwrite.socket = socket;
        try {
            readwrite.out = new PrintWriter(readwrite.socket.getOutputStream(), true);
            readwrite.in = new BufferedReader(new InputStreamReader(readwrite.socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PrintWriter getWriter() {
       return out;
    }

    public static BufferedReader getReader() {
        return in;
    }

}
