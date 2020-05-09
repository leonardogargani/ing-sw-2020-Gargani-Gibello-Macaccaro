package it.polimi.ingsw.PSP43.client;

import it.polimi.ingsw.PSP43.client.networkMessages.PingMessage;

import java.io.IOException;
import java.net.Socket;

public class ConnectionDetector implements Runnable {
    //private Socket serverSocket;
    private static final int timeout = 20000;
    private ClientBG clientBG;


    public ConnectionDetector(Socket serverSocket, ClientBG clientBG) throws IOException {
        //this.serverSocket = serverSocket;
        this.clientBG = clientBG;
        serverSocket.setSoTimeout(timeout);
    }

    @Override
    public void run() {
        long time = System.currentTimeMillis();
        while (true) {

            if (System.currentTimeMillis() > time + timeout / 2) {
                time = System.currentTimeMillis();
                try {
                    if(clientBG.isDisconnect())
                        break;
                    clientBG.sendMessage(new PingMessage());
                } catch (IOException e) {
                    //System.out.println("Server:problems with ping messages");
                }
            }
        }
    }
}
