package com.tom;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tom on 07/12/2016.
 */
public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        int port = 4444;

        try {
            socket = new Socket("localhost", port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String fromServer, fromUser;
        List<String> output = new ArrayList<>();

        while ((fromServer = in.readLine()) != null) {
            if (fromServer.isEmpty()) {
                for (String anOutput : output) {
                    System.out.println(anOutput);
                }
                output.clear();
            }
            if (!in.ready()) {
                System.out.print(fromServer);
                fromUser = stdIn.readLine();
                if (!fromUser.equals("")) {
                    out.println(fromUser);
                }
            } else {
                output.add(fromServer);
            }
        }

        out.close();
        in.close();
        stdIn.close();
        socket.close();
    }
}
