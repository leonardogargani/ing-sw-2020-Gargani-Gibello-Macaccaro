package it.polimi.ingsw.PSP43.server;

import it.polimi.ingsw.PSP43.client.networkMessages.ClientMessage;
import it.polimi.ingsw.PSP43.client.networkMessages.LeaveGameMessage;
import it.polimi.ingsw.PSP43.client.networkMessages.PingMessage;
import it.polimi.ingsw.PSP43.client.networkMessages.RegistrationMessage;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.gameStates.GameSessionObservable;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;
import it.polimi.ingsw.PSP43.server.networkMessages.EndGameMessage;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.*;

public class ClientListener implements Runnable {
    private static ArrayList<GameSessionObservable> gameSessions;
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
        gameSessions = new ArrayList<>();
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
                try {
                    handleDisconnection();
                } catch (IOException ex) {
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
            ExecutorService exe = Executors.newSingleThreadExecutor();

            while (idGame == -1 && gameSessions.size() > counter) {
                GameSessionObservable gameSession = gameSessions.get(counter);
                while (gameSession.getMaxNumPlayers() == 1) {}
                if (gameSession.getMaxNumPlayers() >= gameSession.getNumOfPlayers()) {
                    RegisterClientListener registrator = new RegisterClientListener(gameSessions.get(counter), this, message);
                    Thread newRegistratorThread = new Thread(registrator);
                    newRegistratorThread.start();
                }

                counter++;
            }

            if (idGame == -1) {
                GameSession game = new GameSession(gameSessions.size());
                gameSessions.add(game);
                Thread gameThread = new Thread(game);
                gameThread.start();

                RegisterClientListener registrator = new RegisterClientListener(game, this, message);
                Thread newRegistratorThread = new Thread(registrator);
                newRegistratorThread.start();
            }
        } else if (message instanceof LeaveGameMessage)
            gameSessions.get(idGame).unregisterFromGame((LeaveGameMessage) message, this);
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


    public synchronized void removeGameSession(int idGame) {
        gameSessions.remove(idGame);
    }


    public void handleDisconnection() throws IOException {
        gameSessions.get(idGame).unregisterFromGame(new LeaveGameMessage(), this);
    }

    public void setIdGame(Integer idGame) {
        this.idGame = idGame;
    }
}