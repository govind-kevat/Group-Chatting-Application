package group.chatting.application;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server implements Runnable {

    Socket socket;
    public static Vector<BufferedWriter> client = new Vector<>();

    public Server(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        BufferedWriter writer = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            client.add(writer);

            while (true) {
                String data = reader.readLine().trim();
                System.out.println("Received " + data);

                for (int i = 0; i < client.size(); i++) {
                    try {
                        BufferedWriter bw = client.get(i);
                        bw.write(data);
                        bw.write("\r\n");
                        bw.flush();
                    } catch (Exception e) {
                        // this client is gone, skip it
                    }
                }
            }
        } catch (Exception e) {
            client.remove(writer);
        }
    }

    public static void main(String[] args) throws Exception {
        ServerSocket s = new ServerSocket(2003);
        while (true) {
            Socket socket = s.accept();
            Server server = new Server(socket);
            Thread thread = new Thread(server);
            thread.start();
        }
    }
}