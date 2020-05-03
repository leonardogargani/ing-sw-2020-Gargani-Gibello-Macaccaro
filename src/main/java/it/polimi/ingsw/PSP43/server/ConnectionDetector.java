package it.polimi.ingsw.PSP43.server;

import it.polimi.ingsw.PSP43.client.networkMessages.PingMessage;

import java.io.IOException;
import java.net.Socket;

public class ConnectionDetector implements Runnable {
    private Socket clientSocket;
    private static final int timeout = 20000;
    private ClientListener clientListener;


    public ConnectionDetector(Socket clientSocket, ClientListener clientListener) throws IOException {
        this.clientSocket = clientSocket;
        this.clientListener = clientListener;
        clientSocket.setSoTimeout(timeout);
    }

    @Override
    public void run() {
        long time = System.currentTimeMillis();
        while (true) {
            if (System.currentTimeMillis() > time + timeout / 2) {
                time = System.currentTimeMillis();
                try {
                    clientListener.sendMessage(new PingMessage());
                } catch (IOException e) {
                    System.out.println("Server:problems with ping messages");
                }
            }
        }
    }

}
