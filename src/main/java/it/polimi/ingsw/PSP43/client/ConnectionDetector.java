package it.polimi.ingsw.PSP43.client;

import it.polimi.ingsw.PSP43.client.networkMessages.PingMessage;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class ConnectionDetector implements Runnable {
    private static final int timeout = 20000;
    private final ClientBG clientBG;


    public ConnectionDetector(Socket serverSocket, ClientBG clientBG) throws IOException {
        this.clientBG = clientBG;
        serverSocket.setSoTimeout(timeout);
    }

    @Override
    public void run() {
        while (true) {
            try {
                TimeUnit.SECONDS.sleep(10);
                if(clientBG.isDisconnect())
                    break;
                try {
                    clientBG.sendMessage(new PingMessage());
                } catch (IOException e) {
                    clientBG.handleDisconnection();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
