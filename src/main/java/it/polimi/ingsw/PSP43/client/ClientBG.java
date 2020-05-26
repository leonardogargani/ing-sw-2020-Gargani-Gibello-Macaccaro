package it.polimi.ingsw.PSP43.client;

import it.polimi.ingsw.PSP43.client.cli.CliGraphicHandler;
import it.polimi.ingsw.PSP43.client.cli.CliInputHandler;
import it.polimi.ingsw.PSP43.client.cli.QuitPlayerException;
import it.polimi.ingsw.PSP43.client.networkMessages.ClientMessage;
import it.polimi.ingsw.PSP43.client.networkMessages.LeaveGameMessage;
import it.polimi.ingsw.PSP43.client.networkMessages.PingMessage;
import it.polimi.ingsw.PSP43.server.networkMessages.ServerMessage;

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
    private boolean disconnect = true;
    private ObjectInputStream input;
    private ObjectOutputStream output;


    /**
     * Not default constructor for the client background
     *
     * @param clientManager is the reference to the client manager thread
     */
    public ClientBG(ClientManager clientManager) {
        this.clientManager = clientManager;
        this.messageArrived = new Object();
    }


    /**
     * Override of run method, here the connection with the server starts, after that clientBG starts the connection
     * detector thread and then communicates with messages from and to the server
     */
    @Override
    public void run() {
        do {
            if (clientManager.getGraphicHandler() instanceof CliGraphicHandler) {
                CliInputHandler ip = new CliInputHandler();
                try {
                    this.serverIP = ip.requestServerIP();
                } catch (QuitPlayerException e) {
                    System.exit(0);
                }
            } else {
                while (this.serverIP == null) {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                this.disconnect = false;
                serverSocket = new Socket(this.serverIP, SERVER_PORT);
            } catch (IOException e) {
                System.out.println("server unreachable");
                this.disconnect = true;
            }
        } while (this.disconnect);
        System.out.println("Connected");

        try {
            ConnectionDetector connectionDetector = new ConnectionDetector(this.serverSocket, this);
            Thread connectionThread = new Thread(connectionDetector);
            connectionThread.start();
        } catch (IOException e) {
            System.out.println("Problems starting connection detector");
        }

        if (clientManager.getGraphicHandler() instanceof CliGraphicHandler) {
            clientManager.execute();
        }

        while (!disconnect) {
            try {
                receive();
            } catch (IOException e) {
                handleDisconnection();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * This method will be active for the entire duration of the game and when a message arrives (if it's not a ping)
     * the receive cast it to a ServerMessage and then puts the message in the messageBox, that is an ArrayList of
     * message placed in the ClientManager class
     *
     * @throws IOException            signals that an I/O exception of some sort has occurred.
     * @throws ClassNotFoundException occurs when you try to load a class at run time using Class .forName() or
     *                                loadClass() methods and mentioned classes are not found in the classpath.
     */
    public void receive() throws IOException, ClassNotFoundException {
        do {
            input = new ObjectInputStream(serverSocket.getInputStream());
            messageArrived = input.readObject();
        } while (messageArrived instanceof PingMessage);


        clientManager.pushMessageInBox((ServerMessage) messageArrived);
    }


    /**
     * Method used to send messages to the server
     *
     * @param message that will be sent
     */
    public void sendMessage(ClientMessage message) {
        try {
            output = new ObjectOutputStream(serverSocket.getOutputStream());
            output.writeObject(message);
        } catch (IOException e) {
            handleDisconnection();
        }
    }


    /**
     * Method used to send only LeaveGameMessages, after the sending it calls the handleDisconnection method
     *
     * @param message that will be sent
     */
    public void sendMessage(LeaveGameMessage message) {
        try {
            output = new ObjectOutputStream(serverSocket.getOutputStream());
            output.writeObject(message);
        } catch (IOException e) {
            handleDisconnection();
        }
        handleDisconnection();
    }


    /**
     * Getter method for the boolean variable disconnected, that controls the exit from the while in the run at the end
     * of the match
     *
     * @return the value of the boolean variable disconnected
     */
    public boolean isDisconnect() {
        return disconnect;
    }


    /**
     * Setter method for the boolean variable disconnected
     *
     * @param disconnect is a boolean variable that controls the exit from the while in the run at the end of the match
     */
    public void setDisconnect(boolean disconnect) {
        this.disconnect = disconnect;
    }


    /**
     * Method that close input and output streams and the socket at the end of the match
     */
    public void closer() {
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


    /**
     * This method put an EndGameMessage in the ClientManager's messageBox when you quit the match
     */
    public void handleDisconnection() {
        if (clientManager.getMessageBox().size() == 0){
            clientManager.setActive(false);
            this.disconnect = true;
            closer();
        }
        else
            clientManager.notifyMessageArrived();
    }


    /**
     * Setter method for serverIP String
     *
     * @param serverIP is the address of the server
     */
    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }
}
