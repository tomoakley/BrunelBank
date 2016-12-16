package com.tom;

import java.net.Socket;

/**
 * Created by Tom on 14/11/2016.
 */

public class BrunelBank extends Thread {

    private Socket socket = null;

    public BrunelBank(Socket socket) {
        super("BrunelBank");
        this.socket = socket;
    }

    public void run() {
        Server.log("new client connection");
        new Login(socket, "Welcome to the Brunel Bank! Type your account name below to get started: ");
    }

}
