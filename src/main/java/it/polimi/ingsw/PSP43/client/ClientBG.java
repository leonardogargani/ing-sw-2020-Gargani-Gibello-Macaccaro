package it.polimi.ingsw.PSP43.client;

import it.polimi.ingsw.PSP43.client.networkMessages.ClientMessage;
import it.polimi.ingsw.PSP43.client.networkMessages.LeaveGameMessage;
import it.polimi.ingsw.PSP43.client.networkMessages.PingMessage;
import it.polimi.ingsw.PSP43.server.networkMessages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class ClientBG implements Runnable {
    private Socket serverSocket;
    private final ClientManager clientManager;
    private static final int SERVER_PORT = 50000;
    public Object messageArrived;
    private boolean disconnect = false;
    ObjectInputStream input;
    ObjectOutputStream output;


    public ClientBG(ClientManager clientManager) {
        this.clientManager = clientManager;
        this.messageArrived = new Object();
    }


    @Override
    public void run() {

        try {
            String SERVER_IP = "127.0.0.1";
            serverSocket = new Socket(SERVER_IP, SERVER_PORT);
        } catch (IOException e) {
            System.out.println("server unreachable");
            return;
        }
        System.out.println("Connected");

        clientManager.execute();

        try {
            ConnectionDetector connectionDetector = new ConnectionDetector(this.serverSocket, this);
            Thread connectionThread = new Thread(connectionDetector);
            connectionThread.start();
        } catch (IOException e) {
            System.out.println("Problems starting connection detector");
        }


        while (true) {
            if (disconnect)
                break;
            try {
                receive();
            } catch (IOException | ClassNotFoundException e) {
                handleDisconnection();
            }
        }
    }



    public void receive() throws IOException, ClassNotFoundException {
        do {
            input = new ObjectInputStream(serverSocket.getInputStream());
            messageArrived = input.readObject();
        }while (messageArrived instanceof PingMessage);

        if(messageArrived instanceof EndGameMessage){
            closer();}

        clientManager.getMessageBox().add((ServerMessage)messageArrived);
    }


    public void sendMessage(ClientMessage message) throws IOException {
        try{
            output = new ObjectOutputStream(serverSocket.getOutputStream());
            output.writeObject(message);}catch (IOException e){handleDisconnection();
        }
    }


    public void sendMessage(LeaveGameMessage message) throws IOException {
        try {
            output = new ObjectOutputStream(serverSocket.getOutputStream());
            output.writeObject(message);
        }catch (IOException e){handleDisconnection();}
        handleDisconnection();
    }


    public boolean isDisconnect() {
        return disconnect;
    }


    public void setDisconnect(boolean disconnect) {
        this.disconnect = disconnect;
    }

    public void closer() throws IOException {
        input.close();
        output.close();
        serverSocket.close();}

    public void handleDisconnection() {
        clientManager.getMessageBox().add(0,new EndGameMessage("end game!"));
    }
}
