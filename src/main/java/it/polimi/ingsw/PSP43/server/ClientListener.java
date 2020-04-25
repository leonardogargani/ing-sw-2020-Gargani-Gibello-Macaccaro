package it.polimi.ingsw.PSP43.server;

import it.polimi.ingsw.PSP43.client.networkMessages.ClientMessage;
import it.polimi.ingsw.PSP43.client.networkMessages.LeaveGameMessage;
import it.polimi.ingsw.PSP43.client.networkMessages.RegistrationMessage;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.gameStates.GameSessionObservable;
import it.polimi.ingsw.PSP43.server.networkMessages.EndGameMessage;
import it.polimi.ingsw.PSP43.server.networkMessages.ServerMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientListener implements Runnable{
    private static ArrayList<GameSessionObservable> gameSessions;
    private int idGame = -1;
    private Socket clientSocket;
    //private static final int TIMEOUT=20000;
    ObjectInputStream input;
    ObjectOutputStream output;
    private boolean lock;


    ClientListener(Socket clientSocket)throws IOException {
        this.clientSocket=clientSocket;
        //clientSocket.setSoTimeout(TIMEOUT);

        input = new ObjectInputStream(clientSocket.getInputStream());
        output = new ObjectOutputStream(clientSocket.getOutputStream());
    }


    @Override
    public void run(){
        /*try{
            Thread connection = new Thread(new ConnectionDetector(clientSocket));
            connection.start();
        }catch (IOException e){
            System.out.println("Problems starting connection detector");
        }*/

        try{
            while (true){
                if(!lock)
                    receiver(input.readObject());
            }}catch (IOException | ClassNotFoundException e){
            System.out.println("Invalid message received");
        }
    }


    public synchronized void receiver(Object message) throws IOException, ClassNotFoundException {

        if(message instanceof RegistrationMessage)
            handleMessage((RegistrationMessage) message);
        else if(message instanceof LeaveGameMessage)
            handleMessage((LeaveGameMessage)message);

    }


    public synchronized void handleMessage(RegistrationMessage message) throws IOException, ClassNotFoundException {

        int counter = 0;

        while(idGame ==-1|gameSessions.size()>counter){
            //if(gameSessions.get(counter-1).getListenersHashMap().size()=1)
            //  wait();//possible solution time in the wait or notifyAll in received NumberPlayerResponse
            idGame = gameSessions.get(counter).registerToTheGame(message,this);
            counter++;
        }

        if(idGame==-1){
            gameSessions.add(new GameSession(gameSessions.size()+1));
            idGame = gameSessions.get(gameSessions.size()-1).registerToTheGame(message,this);
        }

    }


    public synchronized void handleMessage(LeaveGameMessage message) throws IOException {
        gameSessions.get(idGame).unregisterFromGame(message,this);
    }

    public synchronized void removeGameSession(int idGame){
        gameSessions.remove(idGame);
    }

    public void sendMessage(EndGameMessage message)throws IOException{
        input.close();
        output.writeObject(message);
        output.close();
        clientSocket.close();
    }


    public synchronized void sendMessage(ServerMessage message)throws IOException{
        output.writeObject(message);
    }

    public synchronized ClientMessage sendRequest(ServerMessage message) throws IOException, ClassNotFoundException {
        output.writeObject(message);
        this.lock = true;
        Object receivedObject;
        ClientMessage response = null;
        while(response == null)
        {
            receivedObject = input.readObject();
            if(receivedObject instanceof LeaveGameMessage){
                handleMessage((LeaveGameMessage) receivedObject);
                break;}
            else if(receivedObject instanceof ClientMessage)
                response = (ClientMessage)receivedObject;
        }
        this.lock = false;
        return response;
    }
}
