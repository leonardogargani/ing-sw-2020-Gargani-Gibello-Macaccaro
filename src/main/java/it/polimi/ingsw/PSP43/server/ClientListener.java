package it.polimi.ingsw.PSP43.server;

import it.polimi.ingsw.PSP43.client.networkMessages.ClientMessage;
import it.polimi.ingsw.PSP43.client.networkMessages.LeaveGameMessage;
import it.polimi.ingsw.PSP43.client.networkMessages.PingMessage;
import it.polimi.ingsw.PSP43.client.networkMessages.RegistrationMessage;
import it.polimi.ingsw.PSP43.server.networkMessages.EndGameMessage;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

/**
 * ClientListener is the server network handler
 */
public class ClientListener implements Runnable {
    private Integer idGame = -1;
    private final Socket clientSocket;
    private final Object lockOut;
    private final ArrayList<ClientMessage> stackMessages;
    private boolean disconnected = false;
    ObjectInputStream input;
    ObjectOutputStream output;

    /**
     * Not default constructor for the ClientListener class
     *
     * @param clientSocket is the socket that is opened between a client and this ClientListener
     */
    ClientListener(Socket clientSocket) throws SocketException {
        this.clientSocket = clientSocket;
        this.lockOut = new Object();
        this.stackMessages = new ArrayList<>();
        clientSocket.setSoTimeout(20000);
    }

    /**
     * Override of the run method, here the ClientListener creates and starts the connection detector to keep the
     * connection active and after that it listens for messages form the client for all the match
     */
    @Override
    public void run() {

        //Start ping sender thread
        try {
            ConnectionDetector connectionDetector = new ConnectionDetector(clientSocket, this);
            Thread connectionThread = new Thread(connectionDetector);
            connectionThread.start();
        } catch (IOException e) {
            System.out.println("Problems starting connection detector");
        }

        ClientMessage message;

        while (!disconnected) {
            try {
                message = receive();
                if (message != null) {
                    handleMessage(message);
                }
            } catch (IOException | ClassNotFoundException | InterruptedException e) {
                //a player left the game
                try {
                    handleDisconnection();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }


    /**
     * Method that receives messages and if it is not a PingMessage, the receive method handles the message and returns
     * it or puts it in the stackMessages ArrayList
     *
     * @throws IOException signals that an I/O exception of some sort has occurred.
     * @throws ClassNotFoundException occurs when you try to load a class at run time using Class .forName() or
     * loadClass() methods and mentioned classes are not found in the classpath.
     */
    public synchronized ClientMessage receive() throws IOException, ClassNotFoundException, InterruptedException {
        Object objectArrived;
        do {
            input = new ObjectInputStream(clientSocket.getInputStream());
            objectArrived = input.readObject();
        } while (objectArrived instanceof PingMessage);

        if (objectArrived instanceof RegistrationMessage)
            return (ClientMessage) objectArrived;

        else if (objectArrived instanceof LeaveGameMessage) {
            handleDisconnection();
            return null;
        }

        else {
            stackMessages.add((ClientMessage) objectArrived);
            notifyAll();
            wait();
            return null;
        }
    }

    /**
     * Method used to send messages to the client
     * @param message that will be sent
     * @throws IOException signals that an I/O exception of some sort has occurred.
     */
    public void sendMessage(Object message) throws IOException {
        try{
            synchronized (lockOut) {
                output = new ObjectOutputStream(clientSocket.getOutputStream());
                output.writeObject(message);
            }}catch (IOException e){handleDisconnection();}
    }

    /**
     * Method used to send EndGameMessages, after the  delivery it calls the handleDisconnection method
     * @param message is the EndGameMessage that will be sent
     * @throws IOException signals that an I/O exception of some sort has occurred.
     */
    public void sendMessage(EndGameMessage message) throws IOException {
        synchronized (lockOut) {
            output = new ObjectOutputStream(clientSocket.getOutputStream());
            output.writeObject(message);
        }
        handleDisconnection();
    }

    /**
     * This method checks the kind of message and then if it is a RegistrationMessage it creates and starts a
     * RegisterClientListener, that is a thread for the registration in a match
     * @param message is received from the receive method and then handleMessage takes it
     * @throws IOException signals that an I/O exception of some sort has occurred.
     */
    public void handleMessage(ClientMessage message) throws IOException {
        if (message instanceof RegistrationMessage) {
            RegisterClientListener registrator = new RegisterClientListener(this, (RegistrationMessage) message);
            Thread newRegistratorThread = new Thread(registrator);
            newRegistratorThread.start();
        }
    }

    /**
     * Getter method for a single message in the stackMessages ArrayList
     * @return the first message in the stackMessages
     * @throws InterruptedException when a thread is waiting, sleeping, or otherwise occupied, and the thread is
     * interrupted, either before or during the activity.
     */
    public synchronized ClientMessage getMessage() throws InterruptedException {
        while (stackMessages.size() == 0) {
            wait();
        }
        return stackMessages.get(0);
    }

    /**
     * Remover method for messages in the stackMessages ArrayList
     * @param message that will be removed
     */
    public synchronized void popMessageFromStack(ClientMessage message) {
        stackMessages.remove(message);
        notifyAll();
    }

    /**
     * This method is called when for some reason(ex. connection problem) the match has to end, so it closes input
     * and output streams, the socket and then it creates a RegisterClientListener to call the unregister method on
     * this GameSession
     * @throws IOException signals that an I/O exception of some sort has occurred.
     */
    public synchronized void handleDisconnection() throws IOException {
        if (!this.disconnected) {
            this.disconnected = true;
            clientSocket.close();


            if(this.idGame != -1){
                output.close();
                input.close();
                stackMessages.add(new LeaveGameMessage());
                notifyAll();
                RegisterClientListener notifier = new RegisterClientListener();
                notifier.notifyDisconnection(this.idGame);
            }
        }
    }

    /**
     * Setter method for the integer variable idGame
     * @param idGame is the identifier of the match
     */
    public void setIdGame(Integer idGame) {
        this.idGame = idGame;
    }

    /**
     * Getter method for the boolean variable disconnected, when this variable becomes true this thread and the
     * ConnectionDetector thread are stopped
     * @return disconnected
     */
    public boolean isDisconnected() {
        return disconnected;
    }
}