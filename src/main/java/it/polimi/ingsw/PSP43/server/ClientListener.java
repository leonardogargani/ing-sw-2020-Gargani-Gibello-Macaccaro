package it.polimi.ingsw.PSP43.server;

import it.polimi.ingsw.PSP43.client.networkMessages.ClientMessage;
import it.polimi.ingsw.PSP43.client.networkMessages.LeaveGameMessage;
import it.polimi.ingsw.PSP43.client.networkMessages.RegistrationMessage;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.gameStates.GameSessionObservable;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;
import it.polimi.ingsw.PSP43.server.networkMessages.EndGameMessage;
import it.polimi.ingsw.PSP43.server.networkMessages.ServerMessage;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientListener implements Runnable{
    private static ArrayList<GameSessionObservable> gameSessions;
    private int idGame = -1;
    private Socket clientSocket;
    private boolean lock2;
    private boolean lock1;


    /**
     * Not default constructor for the ClientListener class
     * @param clientSocket is the socket that is connected to the client
     * @throws IOException
     */
    ClientListener(Socket clientSocket)throws IOException {
        this.clientSocket=clientSocket;
    }


    /**
     * In that override of the run method we are going to create a connection detector thread
     * and after that it listens to the network messages
     */
    @Override
    public void run(){
        try{
            ConnectionDetector connectionDetector = new ConnectionDetector(clientSocket,this);
            Thread connectionThread = new Thread(connectionDetector);
            connectionThread.start();
        }catch (IOException e){
            System.out.println("Problems starting connection detector");
        }

        try{
            while (true){
                if(!lock1)
                    receiver();
            }}catch (IOException | ClassNotFoundException | SAXException | ParserConfigurationException | WinnerCaughtException e){
            System.out.println("Invalid message received");
        }
    }


    /**
     * Setter method for the boolean variable lock, that will be used for the
     * synchronization of method that are going to use the socket
     * @param lock2 boolean variable
     */
    public void setLock2(boolean lock2) {
        this.lock2 = lock2;
    }

    public boolean isLock2() {
        return lock2;
    }

    /**
     *  Method that receives messages and calls handleMessages method
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws WinnerCaughtException
     */
    public synchronized void receiver() throws IOException, ClassNotFoundException, SAXException, ParserConfigurationException, WinnerCaughtException {
        ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
        Object message = input.readObject();
        if(message instanceof RegistrationMessage)
            handleMessage((RegistrationMessage) message);
        else if(message instanceof LeaveGameMessage)
            handleMessage((LeaveGameMessage)message);

    }


    /**
     * Method that handles RegistrationMessages
     * @param message is the RegistartionMessage that contains the player's nickname
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws WinnerCaughtException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public synchronized void handleMessage(RegistrationMessage message) throws IOException, ClassNotFoundException, WinnerCaughtException, ParserConfigurationException, SAXException {

        int counter = 0;

        while(idGame ==-1&gameSessions.size()>counter){
            //TODO resolve problem in registration
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


    /**
     * Method that handles LeaveGameMessages
     * @param message is the LeaveGameMessage
     * @throws IOException
     */
    public synchronized void handleMessage(LeaveGameMessage message) throws IOException {
        gameSessions.get(idGame).unregisterFromGame(message,this);
    }


    /**
     * Method to remove a GameSession
     * @param idGame is the id of the game that is going to be removed
     */
    public synchronized void removeGameSession(int idGame){
        gameSessions.remove(idGame);
    }


    /**
     *
     * @param message
     * @throws IOException
     */
    public void sendMessage(EndGameMessage message)throws IOException{
        ObjectOutputStream output = new ObjectOutputStream(clientSocket.getOutputStream());
        do{
            if(!this.lock2){
                this.lock2 = true;
                output.writeObject(message);
                this.lock2 = false;
            }}while (lock2);
        output.close();
        clientSocket.close();
    }


//Modified input parameter in object from ServerMessage
    /**
     * Method that allows server to sends messages to the client
     * @param message is the message that will be send
     * @throws IOException
     */
    public synchronized void sendMessage(Object message)throws IOException{
        ObjectOutputStream output = new ObjectOutputStream(clientSocket.getOutputStream());
        do{
            if(!this.lock2){
                this.lock2 = true;
                output.writeObject(message);
                this.lock2 = false;
            }}while (lock2);
    }


    /**
     * Method that sends a message and wait for a return from the client
     * @param message is the request message that will be send
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public synchronized ClientMessage sendRequest(ServerMessage message) throws IOException, ClassNotFoundException {
        ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
        ObjectOutputStream output = new ObjectOutputStream(clientSocket.getOutputStream());
        ClientMessage response = null;
        Object receivedObject;
        do{
            if(!this.lock2) {
                this.lock2 = true;
                output.writeObject(message);
                this.lock2 = false;}}
            while (this.lock2);

            this.lock1 = true;
                while (response == null) {
                    receivedObject = input.readObject();
                    if (receivedObject instanceof LeaveGameMessage) {
                        handleMessage((LeaveGameMessage) receivedObject);
                        break;
                    } else if (receivedObject instanceof ClientMessage)
                        response = (ClientMessage) receivedObject;
                }
            this.lock1 = false;

        return response;

}}
