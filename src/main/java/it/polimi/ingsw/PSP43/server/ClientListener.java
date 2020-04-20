package it.polimi.ingsw.PSP43.server;

import it.polimi.ingsw.PSP43.client.networkMessages.*;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.networkMessages.ServerMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class ClientListener implements Runnable{
    private static ArrayList<GameSession> gameSessions;
    private int idGame;
    private Socket clientSocket;
    private static final int TIMEOUT=20000;
    ObjectInputStream input;
    ObjectOutputStream output;

    ClientListener(Socket clientSocket)throws SocketException {
        this.clientSocket=clientSocket;
        clientSocket.setSoTimeout(TIMEOUT);
    }

    @Override
    public void run(){
        try{
            while (true){

                receiver();
            }}catch (IOException |ClassNotFoundException e){
            System.out.println("Invalid message received");}

    }


    public synchronized ClientMessage receiver(Object message) throws IOException,ClassNotFoundException{
        ClientMessage clientMessage = null;
        try {

            if(message instanceof RegistrationMessage)
                clientMessage = (RegistrationMessage)message;
                //handleMessage((RegistrationMessage) message);
            else if(message instanceof NumberPlayerResponse)
                clientMessage = (NumberPlayerResponse)message;
            else if(message instanceof ChoseCardMessage)
                clientMessage = (ChoseCardMessage)message;
            else if(message instanceof WorkerColorResponse)
                clientMessage = (WorkerColorResponse)message;
            else if(message instanceof ActionResponse)
                clientMessage = (ActionResponse)message;
            else if(message instanceof LeaveGameMessage)
                clientMessage = (LeaveGameMessage)message;
                //handleMessage((LeaveGameMessage)message);


        }catch (ClassCastException e){};

        return clientMessage;
    }


    public synchronized void handleMessage(RegistrationMessage message){


            int counter = 0;
            while(idGame==-1||gameSessions.size()>counter){
                    //gibi deve modificare il metodo facendo ritornare l id
                    idGame = gameSessions.get(counter).registerToTheGame(message,this);
                    counter++;
                }
                if(idGame==-1){
                    boolean a = gameSessions.add(new GameSession(gameSessions.size()+1));
                    idGame = gameSessions.get(gameSessions.size()).registerToTheGame(message,this);
                }


    }

    public synchronized void handleMessage(LeaveGameMessage message){
            gameSessions.get(idGame).unregisterFromGame(message,this);
        }




    public void sendMessage(EndGame message)throws IOException{

    }


    //send message senza attesa di ritorni
    public void sendMessage(ServerMessage message)throws IOException{
        output= new ObjectOutputStream(clientSocket.getOutputStream());
        output.writeObject(message);}
    }


    //send request generale con ritorno di messaggio
    public synchronized ClientMessage sendRequest(ServerMessage message)throws IOException,ClassNotFoundException{
        output = new ObjectOutputStream(clientSocket.getOutputStream());
        output.writeObject(message);
        input = new ObjectInputStream(clientSocket.getInputStream());
        while (input.readObject()==null)
            input.readObject();

        return receiver(input.readObject());
    }
}
