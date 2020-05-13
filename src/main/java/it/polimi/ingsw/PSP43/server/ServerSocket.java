package it.polimi.ingsw.PSP43.server;

import java.io.IOException;
import java.net.Socket;


public class ServerSocket {
    public final static int SERVER_PORT = 50000;

    /**
     * Main method of the server it listens for new players(client) who want to open connection with the server
     * @param args
     */
    public static void main(String[] args)
    {
        java.net.ServerSocket socket;
        try {
            socket=new java.net.ServerSocket(SERVER_PORT);
        }catch (IOException e) {
            System.out.println("Cannot open server socket");
            System.exit(1);
            return;
        }
        while (true){
            try {
                Socket client = socket.accept();
                ClientListener clientListener = new ClientListener(client);
                Thread thread= new Thread(clientListener);
                thread.start();
            }catch (IOException e){
                System.out.println("connection dropped");
            }
        }
    }
}
