package it.polimi.ingsw.PSP43.server;

import it.polimi.ingsw.PSP43.client.networkMessages.ClientMessage;
import it.polimi.ingsw.PSP43.client.networkMessages.LeaveGameMessage;
import it.polimi.ingsw.PSP43.client.networkMessages.PingMessage;
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
    private Object lockIn;
    private Object lockOut;
    private ClientMessage message;
    private Object messageArrived;
    private Sender sender;
    ObjectInputStream input;
    ObjectOutputStream output;

    /**
     * Not default constructor for the ClientListener class
     * @param clientSocket is the socket that is connected to the client
     * @throws IOException
     */
    ClientListener(Socket clientSocket)throws IOException {
        this.clientSocket=clientSocket;
        this.lockIn = new Object();
        this.lockOut = new Object();
        this.messageArrived = new Object();
    }



    @Override
    public void run(){

        //Start ping sender thread
        try{
            ConnectionDetector connectionDetector = new ConnectionDetector(clientSocket,this);
            Thread connectionThread = new Thread(connectionDetector);
            connectionThread.start();
        }catch (IOException e){
            System.out.println("Problems starting connection detector");
        }

        sender = new Sender(this.clientSocket,this);
        Thread senderThread = new Thread(sender);
        senderThread.start();


        while(true){
            try {
                message = receive();
                handleMessage(message);
            } catch (IOException | WinnerCaughtException | ParserConfigurationException | SAXException | ClassNotFoundException | InterruptedException e) {
                //Player left the game
                System.out.println("A player lost the connection or left the game");
                try {
                    handleDisconnection();
                } catch (IOException ex) {}
            }}

    }



    /**
     *  Method that receives messages and calls handleMessages method
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws WinnerCaughtException
     */
    public synchronized ClientMessage receive() throws IOException, ClassNotFoundException, InterruptedException {
       do {
           input = new ObjectInputStream(clientSocket.getInputStream());
           messageArrived = input.readObject();
       }while (messageArrived instanceof PingMessage);

       if(messageArrived instanceof RegistrationMessage)
           return (ClientMessage) messageArrived;
       else{
           message = (ClientMessage)messageArrived;
           notifyAll();
           wait();
           return null;
       }
    }


    public void sendMessage(Object message) throws IOException {
        synchronized (lockOut){
            output = new ObjectOutputStream(clientSocket.getOutputStream());
            output.writeObject(message);
        }
    }


    public void sendMessage(EndGameMessage message) throws IOException {
        synchronized (lockOut){
            output = new ObjectOutputStream(clientSocket.getOutputStream());
            output.writeObject(message);
        }
        input.close();
        output.close();
        clientSocket.close();
    }


    public synchronized void handleMessage(ClientMessage message) throws WinnerCaughtException, IOException, ClassNotFoundException, ParserConfigurationException, SAXException {
        if(message instanceof RegistrationMessage){

            int counter = 0;

            while(idGame ==-1&gameSessions.size()>counter){
                //TODO resolve problem in registration
                //if(gameSessions.get(counter-1).getListenersHashMap().size()=1)
                //  wait();//possible solution time in the wait or notifyAll in received NumberPlayerResponse
                idGame = gameSessions.get(counter).registerToTheGame((RegistrationMessage) message,this);
                counter++;
            }

            if(idGame==-1){
                gameSessions.add(new GameSession(gameSessions.size()+1));
                idGame = gameSessions.get(gameSessions.size()-1).registerToTheGame((RegistrationMessage) message,this);
            }
        }
        else if(message instanceof LeaveGameMessage)
            gameSessions.get(idGame).unregisterFromGame((LeaveGameMessage)message,this);
    }


    public synchronized ClientMessage getMessage() throws InterruptedException {
        while (message == null){
            wait();
        }
        return message;
    }


    public synchronized void setMessage(ClientMessage message) {
        this.message = message;
        notifyAll();
    }


    public synchronized void removeGameSession(int idGame){
        gameSessions.remove(idGame);
    }


    public void handleDisconnection() throws IOException {
        gameSessions.get(idGame).unregisterFromGame(new LeaveGameMessage(),this);
    }


    public ClientMessage sendRequest(ServerMessage message) throws InterruptedException, IOException, ClassNotFoundException {
        return getSender().sendRequest(message);
    }


    public Sender getSender() {
        return sender;
    }
}
