package it.polimi.ingsw.PSP43.server;

import it.polimi.ingsw.PSP43.client.networkMessages.ClientMessage;
import it.polimi.ingsw.PSP43.client.networkMessages.LeaveGameMessage;
import it.polimi.ingsw.PSP43.client.networkMessages.PingMessage;
import it.polimi.ingsw.PSP43.client.networkMessages.RegistrationMessage;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;
import it.polimi.ingsw.PSP43.server.networkMessages.EndGameMessage;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.*;

public class ClientListener implements Runnable {
    private Integer idGame = -1;
    private final Socket clientSocket;
    private final Object lockOut;
    private final ArrayList<ClientMessage> stackMessages;
    ObjectInputStream input;
    ObjectOutputStream output;

    /**
     * Not default constructor for the ClientListener class
     *
     * @param clientSocket is the socket that is connected to the client
     * @throws IOException
     */
    ClientListener(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.lockOut = new Object();
        this.stackMessages = new ArrayList<>();
    }


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

        while (true) {
            try {
                message = receive();
                if (message != null) {
                    handleMessage(message);
                }
            } catch (IOException | WinnerCaughtException | ParserConfigurationException | SAXException | ClassNotFoundException | InterruptedException | ExecutionException e) {
                //Player left the game
                System.out.println("A player lost the connection or left the game");
                // TODO : handleDisconnection();
            }
        }
    }


    /**
     * Method that receives messages and calls handleMessages method
     *
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws WinnerCaughtException
     */
    public synchronized ClientMessage receive() throws IOException, ClassNotFoundException, InterruptedException {
        Object objectArrived;
        do {
            input = new ObjectInputStream(clientSocket.getInputStream());
            objectArrived = input.readObject();
        } while (objectArrived instanceof PingMessage);

        if (objectArrived instanceof RegistrationMessage)
            return (ClientMessage) objectArrived;
        else {
            stackMessages.add((ClientMessage) objectArrived);
            notifyAll();
            wait();
            return null;
        }
    }


    public void sendMessage(Object message) throws IOException {
        synchronized (lockOut) {
            output = new ObjectOutputStream(clientSocket.getOutputStream());
            output.writeObject(message);
        }
    }


    public void sendMessage(EndGameMessage message) throws IOException {
        synchronized (lockOut) {
            output = new ObjectOutputStream(clientSocket.getOutputStream());
            output.writeObject(message);
        }
        input.close();
        output.close();
        clientSocket.close();
    }


    public void handleMessage(ClientMessage message) throws WinnerCaughtException, IOException, ClassNotFoundException, ParserConfigurationException, SAXException, InterruptedException, ExecutionException {
        if (message instanceof RegistrationMessage) {
            int counter = 0;

            RegisterClientListener registrator = new RegisterClientListener(this, (RegistrationMessage) message);
            Thread newRegistratorThread = new Thread(registrator);
            newRegistratorThread.start();
        } else if (message instanceof LeaveGameMessage) {
            // thread for unregistration and so on other "routine" operations
        }
    }


    public synchronized ClientMessage getMessage() throws InterruptedException {
        while (stackMessages.size() == 0) {
            wait();
        }
        return stackMessages.get(0);
    }

    public synchronized void popMessageFromStack(ClientMessage message) {
        stackMessages.remove(message);
        notifyAll();
    }

    // TODO : is it still necessary? Shouldn't we only handle that in the gameSession stopping the thread with
    //          the boolean "active"?
    /*public synchronized void removeGameSession(int idGame) {
        gameSessions.remove(idGame);
    }*/

    // TODO : now the handling should be restricted only to handle your socket no?
    /*public void handleDisconnection() throws IOException {
        gameSessions.get(idGame).unregisterFromGame(new LeaveGameMessage(), this);
    }*/

    public void setIdGame(Integer idGame) {
        this.idGame = idGame;
    }
}