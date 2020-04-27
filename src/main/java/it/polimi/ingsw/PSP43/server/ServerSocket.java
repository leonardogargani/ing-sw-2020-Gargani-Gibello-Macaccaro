package it.polimi.ingsw.PSP43.server;

import java.io.IOException;
import java.net.Socket;

public class ServerSocket {
    public final static int SOCKET_PORT = 50000;

    public static void main(String[] args)
    {
        java.net.ServerSocket socket;
        try {
            socket=new java.net.ServerSocket(SOCKET_PORT);
        }catch (IOException e) {
            System.out.println("Cannot open server socket");
            System.exit(1);
            return;
        }
        while (true){
            try {
                Socket client = socket.accept();
                ClientListener clientListener = new ClientListener(client);
                Thread thread= new Thread(clientListener,"server_"+client.getInetAddress());
                thread.start();
            }catch (IOException e){
                System.out.println("connection dropped");
            }
        }
    }
}
