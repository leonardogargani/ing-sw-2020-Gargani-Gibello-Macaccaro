package it.polimi.ingsw.PSP43.client;

import it.polimi.ingsw.PSP43.client.networkMessages.PingMessage;

import java.io.IOException;
import java.net.Socket;

public class ConnectionDetector implements Runnable {
    private Socket serverSocket;
    private static final int timeout = 10000;
    private ClientBG clientBG;



    public ConnectionDetector(Socket serverSocket, ClientBG clientBG) throws IOException {
        this.serverSocket = serverSocket;
        this.clientBG = clientBG;
        serverSocket.setSoTimeout(timeout);
    }

    @Override
    public void run() {
        long time=System.currentTimeMillis();
        while(true){
            if(System.currentTimeMillis()==time+timeout/2){
                time=System.currentTimeMillis();
                try {
                    do{
                        if(!clientBG.isLock()){
                                clientBG.setLock(true);
                                clientBG.sendMessage(new PingMessage());
                                clientBG.setLock(false);
                }}while (clientBG.isLock());
                }
                catch (IOException e) {
                    System.out.println("Server:problems with ping messages");
                }
            }
        }
    }
}
