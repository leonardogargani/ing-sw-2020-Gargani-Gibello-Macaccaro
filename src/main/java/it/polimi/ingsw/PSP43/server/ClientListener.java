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
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.*;

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
     * @param clientSocket is the socket that is connected to the client
     * @throws IOException
     */
    ClientListener(Socket clientSocket) throws SocketException {
        this.clientSocket = clientSocket;
        this.lockOut = new Object();
        this.stackMessages = new ArrayList<>();
        clientSocket.setSoTimeout(20000);
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
            if(disconnected)
                break;
            try {
                message = receive();
                if (message != null) {
                    handleMessage(message);
                }
            } catch (IOException | WinnerCaughtException | ParserConfigurationException | SAXException | ClassNotFoundException | InterruptedException | ExecutionException e) {
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
        try{
            synchronized (lockOut) {
                output = new ObjectOutputStream(clientSocket.getOutputStream());
                output.writeObject(message);
            }}catch (IOException e){handleDisconnection();}
    }


    public void sendMessage(EndGameMessage message) throws IOException, ClassNotFoundException {
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

            RegisterClientListener registrator = new RegisterClientListener(this, (RegistrationMessage) message);
            Thread newRegistratorThread = new Thread(registrator);
            newRegistratorThread.start();
        } else if (message instanceof LeaveGameMessage) {
            handleDisconnection();
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

    //TODO fix problems with this method
    public void handleDisconnection() throws IOException {
        this.disconnected = true;
        input.close();
        output.close();
        clientSocket.close();
        RegisterClientListener unregister = new RegisterClientListener(this,null);
        unregister.notifyDisconnection(this.idGame);
    }

    public void setIdGame(Integer idGame) {
        this.idGame = idGame;
    }

    public boolean isDisconnected() {
        return disconnected;
    }
}