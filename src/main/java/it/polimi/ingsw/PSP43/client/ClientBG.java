package it.polimi.ingsw.PSP43.client;

import it.polimi.ingsw.PSP43.client.networkMessages.ClientMessage;
import it.polimi.ingsw.PSP43.client.networkMessages.LeaveGameMessage;
import it.polimi.ingsw.PSP43.client.networkMessages.PingMessage;
import it.polimi.ingsw.PSP43.server.networkMessages.EndGameMessage;
import it.polimi.ingsw.PSP43.server.networkMessages.ServerMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * ClientBG(client background) is the client network handler
 */
public class ClientBG implements Runnable {
    private Socket serverSocket;
    private final ClientManager clientManager;
    private static final int SERVER_PORT = 50000;
    private String SERVER_IP = "127.0.0.1";
    public Object messageArrived;
    private boolean disconnect = false;
    ObjectInputStream input;
    ObjectOutputStream output;

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

        System.out.println("Insert the ip of the server or press enter for the default ip(127.0.0.1)");
        Scanner scanner = new Scanner(System.in);

        SERVER_IP = scanner.nextLine();
        if (SERVER_IP.toLowerCase().equals("quit"))
            System.exit(0);
        try {
            serverSocket = new Socket(SERVER_IP, SERVER_PORT);
        } catch (IOException e) {
            System.out.println("server unreachable");
            System.exit(0);
        }
        System.out.println("Connected");

        clientManager.execute();

        try {
            ConnectionDetector connectionDetector = new ConnectionDetector(this.serverSocket, this);
            Thread connectionThread = new Thread(connectionDetector);
            connectionThread.start();
        } catch (IOException e) {
            System.out.println("Problems starting connection detector");
        }


        while (true) {
            if (disconnect)
                break;
            try {
                receive();
            } catch (IOException | ClassNotFoundException e) {
                handleDisconnection();
            }
        }
    }


    /**
     * This method will be active for the entire duration of the game and when a message arrives (if it's not a ping) the receive
     * cast it to a ServerMessage and then puts the message in the messageBox, that is an ArrayList of message
     * placed in the ClientManager class
     *
     * @throws IOException signals that an I/O exception of some sort has occurred.
     * @throws ClassNotFoundException occurs when you try to load a class at run time using Class .forName() or
     * loadClass() methods and mentioned classes are not found in the classpath.
     */
    public void receive() throws IOException, ClassNotFoundException {
        do {
            input = new ObjectInputStream(serverSocket.getInputStream());
            messageArrived = input.readObject();
        } while (messageArrived instanceof PingMessage);

        if (messageArrived instanceof EndGameMessage) {
            closer();
        }

        clientManager.getMessageBox().add((ServerMessage) messageArrived);
    }

    /**
     * Method used to send messages to the server
     *
     * @param message that will be sent
     * @throws IOException exception thrown if for some reason the socket is closed and we are trying to send a message
     */
    public void sendMessage(ClientMessage message) throws IOException {
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
     * @throws IOException exception thrown if for some reason the socket is closed and we are trying to send an
     *                     LeaveGameMessage
     */
    public void sendMessage(LeaveGameMessage message) throws IOException {
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
     *
     * @throws IOException signals that an I/O exception of some sort has occurred.
     */
    public void closer() throws IOException {
        input.close();
        output.close();
        serverSocket.close();
    }

    /**
     * This method put an EndGameMessage in the ClientManager's messageBox when you quit the match
     */
    public void handleDisconnection() {
        clientManager.getMessageBox().add(0, new EndGameMessage("end game!"));
    }
}
