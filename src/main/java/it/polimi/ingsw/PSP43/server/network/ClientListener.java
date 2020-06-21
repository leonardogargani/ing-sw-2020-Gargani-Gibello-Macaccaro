package it.polimi.ingsw.PSP43.server.network;

import it.polimi.ingsw.PSP43.client.network.networkMessages.ClientMessage;
import it.polimi.ingsw.PSP43.client.network.networkMessages.LeaveGameMessage;
import it.polimi.ingsw.PSP43.client.network.networkMessages.PingMessage;
import it.polimi.ingsw.PSP43.client.network.networkMessages.RegistrationMessage;
import it.polimi.ingsw.PSP43.server.network.networkMessages.EndGameMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

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
     * Not-default constructor for the ClientListener class.
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
            } catch (IOException e) {
                /* This block is called only if the player has "killed" the program and so the socket has been closed.
                This means that the client won't be available anymore. For this reason the ClientListener is stopped and
                the socket closed.
                * */
                handleDisconnection(true, LeaveGameMessage.TypeDisconnectionHeader.IRREVERSIBLE_DISCONNECTION);

                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            } catch (InterruptedException | ClassNotFoundException ignored) {}
        }
    }

    /**
     * Method that receives messages and if it is not a PingMessage, the receive method handles the message and returns
     * it or puts it in the stackMessages ArrayList.
     * @throws IOException            signals that an I/O exception of some sort has occurred.
     * @throws ClassNotFoundException occurs when you try to load a class at run time using Class .forName() or
     *                                loadClass() methods and mentioned classes are not found in the classpath.
     */
    public ClientMessage receive() throws IOException, ClassNotFoundException, InterruptedException {
        Object objectArrived;

        input = new ObjectInputStream(clientSocket.getInputStream());
        objectArrived = input.readObject();

        if (objectArrived instanceof RegistrationMessage) {
            /* This is made to flush possible messages from the previous game ended (e.g. the LeaveGameMessage
            inserted in the stack to give to the GameSession the knowledge that the game has ended if it was expecting
            an answer from the player linked to that CLientListener.
             * */
            flushStack();
            return (ClientMessage) objectArrived;
        } else if (objectArrived instanceof LeaveGameMessage) {
            /* If this message arrives it means that the player has decided to quit the game, but he has not disconnected.
            For this reason the socket is not closed but the GameSession is informed (in this way the game is stopped).
            * */
            handleDisconnection(true, ((LeaveGameMessage) objectArrived).getTypeDisconnectionHeader());
            return null;
        } else if (!(objectArrived instanceof PingMessage)) {
            pushMessageOnStack((ClientMessage) objectArrived);
            return null;
        } else return null;
    }

    /**
     * Method used to send messages to the client.
     * @param message The message that will be sent.
     */
    public void sendMessage(Object message) {
        try {
            synchronized (lockOut) {
                output = new ObjectOutputStream(clientSocket.getOutputStream());
                output.writeObject(message);
            }
        } catch (IOException ignored) {}
    }

    /**
     * Method used to send EndGameMessages. After the  delivery it calls the handleDisconnection method.
     * @param message is the EndGameMessage that will be sent
     */
    public void sendMessage(EndGameMessage message) {
        try {
            synchronized (lockOut) {
                output = new ObjectOutputStream(clientSocket.getOutputStream());
                output.writeObject(message);
            }
        } catch (IOException ignored) {}

        //   The handleDisconnection() is called in this case only because it could be possible that the
        //   GameSession was expecting an answer from the Client linked to this ClientListener.
        handleDisconnection(false, LeaveGameMessage.TypeDisconnectionHeader.GAME_DISCONNECTION);
    }

    /**
     * This method checks the kind of message and then if it is a RegistrationMessage it creates and starts a
     * RegisterClientListener, that is a thread for the registration in a match.
     * @param message The message that is received from the "receive" method.
     */
    public void handleMessage(ClientMessage message) {
        if (message instanceof RegistrationMessage) {
            RegisterClientListener register = new RegisterClientListener(this, (RegistrationMessage) message);
            Thread newRegisterThread = new Thread(register);
            newRegisterThread.start();
        }
    }

    /**
     * This method is used to push a message on the stack of the ClientListener when it arrives. In that way I notify
     * the thread that was waiting for a response from the client.
     * @param clientMessage The message arrived from the client.
     */
    public synchronized void pushMessageOnStack(ClientMessage clientMessage) throws InterruptedException {
        stackMessages.add(clientMessage);
        notifyAll();
        wait();
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
     * This method is used to flush all the messages from the stack when it is necessary.
     */
    public void flushStack() {
        for (Iterator<ClientMessage> clientMessageIterator = stackMessages.iterator(); clientMessageIterator.hasNext(); ) {
            clientMessageIterator.next();
            clientMessageIterator.remove();
        }
    }

    /**
     * This method is used to handle a disconnection in different ways, regarding different scenarios.
     * 1)   The client has "killed" the program. So he won't be available anymore. In this case the method will
     * close the socket, put a LeaveGameMessage in the stack and start the process of disconnection through RegisterClientListener;
     * 2)   The client was a player in a game that ended. In this case the client will be still available. For this reason
     * the socket won't be closed. If the client has been the first to disconnect, he will start the process of
     * disconnection through the RegisterClientListener.
     */
    public synchronized void handleDisconnection(boolean isFirstToDisconnect, LeaveGameMessage.TypeDisconnectionHeader typeDisconnectionHeader) {
        try {
            if (!this.disconnected) {
                if (typeDisconnectionHeader == LeaveGameMessage.TypeDisconnectionHeader.IRREVERSIBLE_DISCONNECTION) {
                    this.disconnected = true;

                    clientSocket.close();
                    if (input != null)
                        input.close();
                    if (output != null)
                        output.close();
                }

                // This adds a message of type LeaveGameMessage in the stack to inform the GameSession
                // of the disconnection of the client, if it was expecting a response from him.
                // (In other cases this wil be a "useless" statement).
                stackMessages.add(new LeaveGameMessage());

                // I do not execute the following code if the method was called due to the disconnection of another
                // player. In this case I have not to worry about notify a disconnection to the GameSession.
                if (isFirstToDisconnect) {
                    if (this.idGame != -1) {
                        RegisterClientListener notifier = new RegisterClientListener(this, null);
                        notifier.notifyDisconnection(this.idGame);
                    }
                }

                notifyAll();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Setter method for the integer variable idGame, that will be the reference of the game for the ClientListener.
     * @param idGame is the identifier of the match.
     */
    public void setIdGame(Integer idGame) {
        this.idGame = idGame;
    }

    /**
     * Getter method for the boolean variable disconnected. When this variable becomes true this thread and the
     * ConnectionDetector thread are stopped.
     * @return The value of the boolean disconnected. It is true if the connection from the client was lost, true otherwise.
     */
    public boolean isDisconnected() {
        return disconnected;
    }
}