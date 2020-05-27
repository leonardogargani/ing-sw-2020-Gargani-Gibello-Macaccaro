package it.polimi.ingsw.PSP43.server;

import it.polimi.ingsw.PSP43.client.networkMessages.ClientMessage;
import it.polimi.ingsw.PSP43.client.networkMessages.LeaveGameMessage;
import it.polimi.ingsw.PSP43.client.networkMessages.PingMessage;
import it.polimi.ingsw.PSP43.client.networkMessages.RegistrationMessage;
import it.polimi.ingsw.PSP43.server.networkMessages.EndGameMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
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
     * Not default constructor for the ClientListener class.
     * @param clientSocket is the socket that is opened between a client and this ClientListener
     */
    public ClientListener(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.lockOut = new Object();
        this.stackMessages = new ArrayList<>();
    }

    /**
     * Override of the run method, here the ClientListener creates and starts the connection detector to keep the
     * connection active and after that it listens for messages form the client for all the match
     */
    @Override
    public void run() {

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
            } catch (SocketTimeoutException e) {
                handleDisconnection(true);
            } catch (InterruptedException | IOException | ClassNotFoundException ignored) {}
        }
    }

    /**
     * Method that receives messages and if it is not a PingMessage, the receive method handles the message and returns
     * it or puts it in the stackMessages ArrayList.
     * @throws IOException            signals that an I/O exception of some sort has occurred.
     * @throws ClassNotFoundException occurs when you try to load a class at run time using Class .forName() or
     *                                loadClass() methods and mentioned classes are not found in the classpath.
     */
    public synchronized ClientMessage receive() throws IOException, ClassNotFoundException, InterruptedException {
        Object objectArrived;

        input = new ObjectInputStream(clientSocket.getInputStream());
        objectArrived = input.readObject();

        if (objectArrived instanceof RegistrationMessage)
            return (ClientMessage) objectArrived;

        else if (objectArrived instanceof LeaveGameMessage) {
            handleDisconnection(true);
            return null;
        } else if (!(objectArrived instanceof PingMessage)) {
            stackMessages.add((ClientMessage) objectArrived);
            notifyAll();
            wait();
            return null;
        }
        else return null;
    }

    /**
     * Method used to send messages to the client.
     * @param message that will be sent
     */
    public void sendMessage(Object message) {
        try {
            synchronized (lockOut) {
                output = new ObjectOutputStream(clientSocket.getOutputStream());
                output.writeObject(message);
            }
        } catch (IOException e) {
            handleDisconnection(true);
        }
    }

    /**
     * Method used to send EndGameMessages, after the  delivery it calls the handleDisconnection method.
     * @param message is the EndGameMessage that will be sent
     */
    public void sendMessage(EndGameMessage message) {
        try {
            synchronized (lockOut) {
                output = new ObjectOutputStream(clientSocket.getOutputStream());
                output.writeObject(message);
            }
        } catch (IOException ignored) {
            handleDisconnection(false);
            return;
        }
        handleDisconnection(false);
    }

    /**
     * This method checks the kind of message and then if it is a RegistrationMessage it creates and starts a
     * RegisterClientListener, that is a thread for the registration in a match.
     * @param message is received from the receive method and then handleMessage takes it
     */
    public void handleMessage(ClientMessage message) {
        if (message instanceof RegistrationMessage) {
            RegisterClientListener register = new RegisterClientListener(this, (RegistrationMessage) message);
            Thread newRegisterThread = new Thread(register);
            newRegisterThread.start();
        }
    }

    /**
     * Getter method for a single message in the stackMessages ArrayList.
     * @return the first message in the stackMessages
     */
    public synchronized ClientMessage popMessageFromStack() {
        while (stackMessages.size() == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        ClientMessage messageArrived = stackMessages.get(0);
        stackMessages.remove(messageArrived);
        notifyAll();

        return messageArrived;
    }

    /**
     * This method is called when for some reason(ex. connection problem) the match has to end, so it closes input
     * and output streams, the socket and then it creates a RegisterClientListener to call the unregister method on
     * this GameSession.
     */
    public synchronized void handleDisconnection(boolean mode) {
        try {
            if (!this.disconnected) {
                this.disconnected = true;

                clientSocket.close();
                if (input != null)
                    input.close();
                if (output != null)
                    output.close();

                // This adds a message of type LeaveGameMessage in the stack to inform the GameSession
                // of the disconnection of the client, if it was expecting a response from him.
                // (In other cases this wil be a "useless" statement).
                stackMessages.add(new LeaveGameMessage());
                notifyAll();

                // I do not execute the following code if the method was called due to the disconnection of another
                // player. In this case I have not to worry about notify a disconnection to the GameSession.
                if (mode) {
                    if (this.idGame != -1) {
                        RegisterClientListener notifier = new RegisterClientListener(this, null);
                        notifier.notifyDisconnection(this.idGame);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Setter method for the integer variable idGame.
     * @param idGame is the identifier of the match
     */
    public void setIdGame(Integer idGame) {
        this.idGame = idGame;
    }

    /**
     * Getter method for the boolean variable disconnected, when this variable becomes true this thread and the
     * ConnectionDetector thread are stopped.
     * @return disconnected
     */
    public boolean isDisconnected() {
        return disconnected;
    }

}