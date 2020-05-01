package it.polimi.ingsw.PSP43.server;

import it.polimi.ingsw.PSP43.client.networkMessages.ClientMessage;
import it.polimi.ingsw.PSP43.server.networkMessages.EndGameMessage;
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
        //Start ping sender thread
        try{
            ConnectionDetector connectionDetector = new ConnectionDetector(client,this);
            Thread connectionThread = new Thread(connectionDetector);
            connectionThread.start();
        }catch (IOException e){
            System.out.println("Problems starting connection detector");
        }
    }

    public void sendMessage(Object message) throws IOException {
        synchronized (lockOut){
            output = new ObjectOutputStream(client.getOutputStream());
            output.writeObject(message);
        }
    }

    public void sendMessage(EndGameMessage message) throws IOException {
        synchronized (lockOut){
            output = new ObjectOutputStream(client.getOutputStream());
            output.writeObject(message);
        }
        clientListener.getInput().close();
        output.close();
        client.close();
    }
//cambiato sendRequest da sendAndReceive
    public ClientMessage sendRequest(ServerMessage message) throws IOException, InterruptedException, ClassNotFoundException {
        ClientMessage response;

        synchronized (lockOut) {
           sendMessage(message);
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
