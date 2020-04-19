package it.polimi.ingsw.PSP43.server;

import it.polimi.ingsw.PSP43.client.networkMessages.*;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.networkMessages.ResMessage;

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


    public synchronized void receiver() throws IOException,ClassNotFoundException{
        input = new ObjectInputStream(clientSocket.getInputStream());
        try {
            Object message = input.readObject();

            if(message instanceof RegistrationMessage)
                handleMessage((RegistrationMessage) message);
            else if(message instanceof NumberPlayer)
                gameSessions.get(idGame).handleGameCommand((NumberPlayer)message);
            else if(message instanceof ChoseCardMessage)
                gameSessions.get(idGame).handleGameCommand((ChoseCardMessage)message);
            else if(message instanceof ChoseWorkerColor)
                gameSessions.get(idGame).handleGameCommand((ChoseWorkerColor)message);
            else if(message instanceof ActionRequest)
                gameSessions.get(idGame).handleGameCommand((ActionRequest)message);
            else if(message instanceof LeaveGameMessage)
                handleMessage((LeaveGameMessage)message);
            //else if(message instanceof PingMessage)
            //  handleMessage((PingMessage)message);

        }catch (ClassNotFoundException|ClassCastException e){};
    }


    public synchronized void handleMessage(RegistrationMessage message){
        int counter = 0;
        while(idGame==-1||gameSessions.size()>counter){
            //gibi deve modificare il metodo facendo ritornare l id
            idGame=gameSessions.get(counter).registerToTheGame(message);
            counter++;
        }
        if(idGame==-1){
            boolean a=gameSessions.add(new GameSession(gameSessions.size()+1));
            idGame=gameSessions.get(gameSessions.size()).registerToTheGame(message);
        }
        gameSessions.get(idGame).handleGameCommand(message);
    }

    public synchronized void handleMessage(LeaveGameMessage message){
        gameSessions.get(idGame).eliminatePlayer(message.getNick());
    }


    public synchronized void handleMessage(PingMessage message){
        ;
    }


    //TODO metodo per la gestione dei ping e dei controlli di caduta di rete

    public synchronized void sendMessage(ResMessage message)throws IOException{
        output= new ObjectOutputStream(clientSocket.getOutputStream());
        output.writeObject(message);
    }
}
