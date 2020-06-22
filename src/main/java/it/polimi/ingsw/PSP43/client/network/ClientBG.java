package it.polimi.ingsw.PSP43.client.network;

import it.polimi.ingsw.PSP43.client.network.networkMessages.ClientMessage;
import it.polimi.ingsw.PSP43.client.network.networkMessages.LeaveGameMessage;
import it.polimi.ingsw.PSP43.client.network.networkMessages.PingMessage;
import it.polimi.ingsw.PSP43.client.network.networkMessages.RegistrationMessage;
import it.polimi.ingsw.PSP43.server.network.networkMessages.EndGameMessage;
import it.polimi.ingsw.PSP43.server.network.networkMessages.ServerMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * ClientBG(client background) is the client network handler
 */
public class ClientBG implements Runnable {
    private Socket serverSocket;
    private final ClientManager clientManager;
    private static final int SERVER_PORT = 50000;
    private String serverIP = null;
    private Object messageArrived;
    private boolean connected = false;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    /**
     * Not default constructor for the client background.
     *
     * @param clientManager This is the reference to the client manager thread
     */
    public ClientBG(ClientManager clientManager) {
        this.clientManager = clientManager;
        this.messageArrived = new Object();
    }

    /**
     * Override of run method. Here the connection with the server starts (after the player has given the
     * IP address that he wants to reach). After that clientBG starts the ping connection in ConnectionDetector
     * thread. Finally it exchanges messages from and to the server.
     */
    @Override
    public void run() {
        do {
            while (this.serverIP == null) {
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            try {
                serverSocket = new Socket(this.serverIP, SERVER_PORT);
                setConnected(true);
            } catch (IOException e) {
                System.out.println("Server Unreachable");
                serverIP = null;
                setConnected(false);
            }
        } while (!this.connected);

        try {
            ConnectionDetector connectionDetector = new ConnectionDetector(this.serverSocket, this);
            Thread connectionThread = new Thread(connectionDetector);
            connectionThread.start();
        } catch (IOException e) {
            System.out.println("Problems starting connection detector");
        }

        while (connected) {
            try {
                receive();
            } catch (IOException e) {
                // If this exception is thrown the Server in not reachable anymore. For this reason the socket will be closed
                // and the player will be informed of the problem. The application will end the execution.
                handleDisconnection();

                EndGameMessage messageServerDown = new EndGameMessage("", EndGameMessage.EndGameHeader.SERVER_CRASHED);
                clientManager.pushMessageInBox(messageServerDown);
            } catch (ClassNotFoundException ignored) {
            }
        }
    }

    /**
     * Setter method for serverIP String.
     *
     * @param serverIP It is the address of the server
     */
    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    /**
     * This method will be active for the entire duration of the game and when a message arrives (if it's not a ping)
     * the receive cast it to a ServerMessage and then puts the message in the messageBox, that is an ArrayList of
     * message placed in the ClientManager class.
     *
     * @throws IOException            signals that an I/O exception of some sort has occurred
     * @throws ClassNotFoundException occurs when you try to load a class at run time using Class .forName() or
     *                                loadClass() methods and mentioned classes are not found in the classpath
     */
    public void receive() throws IOException, ClassNotFoundException {
        input = new ObjectInputStream(serverSocket.getInputStream());

        messageArrived = input.readObject();

        if (messageArrived instanceof EndGameMessage) {
            /* In this case a message of end is arrived for 3 possible reasons :
                    - the player won the game;
                    - the player lose the game;
                    - another player of the game left;
               In all the three circumstances, the player won't be disconnected from the server. The application
               will continue to run and will ask if he wants to quit the game or if he wants to join another game.
            */

            clientManager.pushMessageInBox((ServerMessage) messageArrived);

        } else if (!(messageArrived instanceof PingMessage))
            clientManager.pushMessageInBox((ServerMessage) messageArrived);
    }

    /**
     * Method used to send messages to the server.
     *
     * @param message The message that will be sent to the server
     */
    public void sendMessage(ClientMessage message) {
        try {
            if (!(clientManager.containsEndGameMessage()) || message instanceof PingMessage) {
                output = new ObjectOutputStream(serverSocket.getOutputStream());
                output.writeObject(message);
            }
        } catch (IOException ignored) {
        }
    }

    /**
     * Method used to send a message of registration to the server.
     *
     * @param message The message that will be sent to the server
     */
    public void sendMessage(RegistrationMessage message) {
        try {
            clientManager.flushStack();
            output = new ObjectOutputStream(serverSocket.getOutputStream());
            output.writeObject(message);
        } catch (IOException ignored) {
        }
    }

    /**
     * Method used to send only LeaveGameMessages. After sending, it calls the handleDisconnection method.
     *
     * @param message The message that will be sent to the server
     */
    public void sendMessage(LeaveGameMessage message) {
        try {
            if (!(clientManager.containsEndGameMessage())) {
                output = new ObjectOutputStream(serverSocket.getOutputStream());
                output.writeObject(message);
            }
        } catch (IOException ignored) {
        }
    }


    /**
     * Getter method for the boolean variable disconnected, that controls the exit from the while in the run method
     * to get down the connection to the server.
     *
     * @return the value of the boolean variable disconnected
     */
    public boolean isConnected() {
        return connected;
    }

    public synchronized void waitForChangeConnected() {
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Setter method for the boolean variable connected.
     * @param connected is true if the connection to the server is established
     */
    public synchronized void setConnected(boolean connected) {
        this.connected = connected;
        notifyAll();
    }

    /**
     * This method handles the end of the connection of the client to the server.
     */
    public void handleDisconnection() {
        if (connected) {
            clientManager.flushStack();
            connected = false;
            clientManager.setActive(false);

            try {
                if (input != null)
                    input.close();
                if (output != null)
                    output.close();
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
