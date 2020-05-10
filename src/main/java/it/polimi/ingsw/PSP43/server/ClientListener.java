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
     * Method that receives messages and calls handleMessages method
     *
     * @throws IOException
     * @throws ClassNotFoundException
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


    public void sendMessage(Object message) throws IOException {
        try{
            synchronized (lockOut) {
                output = new ObjectOutputStream(clientSocket.getOutputStream());
                output.writeObject(message);
            }}catch (IOException e){handleDisconnection();}
    }


    public void sendMessage(EndGameMessage message) throws IOException {
        synchronized (lockOut) {
            output = new ObjectOutputStream(clientSocket.getOutputStream());
            output.writeObject(message);
        }
        handleDisconnection();
    }


    public void handleMessage(ClientMessage message) throws IOException {
        if (message instanceof RegistrationMessage) {
            RegisterClientListener registrator = new RegisterClientListener(this, (RegistrationMessage) message);
            Thread newRegistratorThread = new Thread(registrator);
            newRegistratorThread.start();
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
    public synchronized void handleDisconnection() throws IOException {
        if (!this.disconnected) {
            this.disconnected = true;
            input.close();
            output.close();
            clientSocket.close();
            stackMessages.add(new LeaveGameMessage());
            notifyAll();
            RegisterClientListener notifier = new RegisterClientListener();
            notifier.notifyDisconnection(this.idGame);
        }
    }

    public void setIdGame(Integer idGame) {
        this.idGame = idGame;
    }

    public boolean isDisconnected() {
        return disconnected;
    }
}