package it.polimi.ingsw.PSP43.server;

import it.polimi.ingsw.PSP43.client.networkMessages.ClientMessage;
import it.polimi.ingsw.PSP43.server.networkMessages.ServerMessage;

import java.io.*;
import java.net.Socket;

public class Sender implements Runnable {
    private Socket client;
    private ClientListener clientListener;
    private Object lockOut;
    ObjectOutputStream output;
    File temp;
    OutputStream outputStream;
    InputStream inputStream;

    public Sender(Socket client,ClientListener clientListener){
        this.client = client;
        this.clientListener = clientListener;
        this.lockOut = new Object();
    }

    @Override
    public void run() {

    }



    public ClientMessage sendRequest(ServerMessage message) throws IOException, InterruptedException, ClassNotFoundException {
        ClientMessage response;

        synchronized (lockOut) {
           clientListener.sendMessage(message);
        }
            ClientMessage messageArrived = null;
            do{
                messageArrived = clientListener.getMessage();
            }while (messageArrived == null);


            temp = File.createTempFile("tmp",null,null);
            outputStream = new FileOutputStream(temp);
            ObjectOutputStream out = new ObjectOutputStream(outputStream);
            out.writeObject(messageArrived);
            inputStream = new FileInputStream(temp);
            ObjectInputStream in = new ObjectInputStream(inputStream);
            response = (ClientMessage) in.readObject();
            clientListener.setMessage(null);


        return response;

    }
}
