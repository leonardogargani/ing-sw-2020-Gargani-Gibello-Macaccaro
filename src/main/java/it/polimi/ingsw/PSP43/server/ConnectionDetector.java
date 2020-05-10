package it.polimi.ingsw.PSP43.server;

import it.polimi.ingsw.PSP43.client.networkMessages.PingMessage;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

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
        while (true) {
            try {
                TimeUnit.SECONDS.sleep(10);
                if(clientListener.isDisconnected())
                    break;
                try {
                    clientListener.sendMessage(new PingMessage());
                } catch (IOException e) {
                    clientListener.handleDisconnection();
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}
