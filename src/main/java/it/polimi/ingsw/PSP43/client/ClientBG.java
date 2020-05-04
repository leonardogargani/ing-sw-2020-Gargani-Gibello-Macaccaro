package it.polimi.ingsw.PSP43.client;

import it.polimi.ingsw.PSP43.client.networkMessages.ClientMessage;
import it.polimi.ingsw.PSP43.client.networkMessages.PingMessage;
import it.polimi.ingsw.PSP43.server.networkMessages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;


public class ClientBG implements Runnable {
    private Socket serverSocket;
    private Client client;
    private final Object lockIn;
    private final Object lockOut;
    private Object messageArrived;
    private String SERVER_IP ;
    private static final int SERVER_PORT = 50000;
    private boolean started = false;
    ObjectInputStream input;
    ObjectOutputStream output;



    public ClientBG(String serverIp) {
        this.lockIn = new Object();
        this.lockOut = new Object();
        this.messageArrived = new Object();
        this.SERVER_IP = serverIp;
    }

    /**
     * In that override of the run method we are going to create a connection detector thread
     * and after that it listens to the network messages
     */
    @Override
    public void run() {

        try {
            serverSocket = new Socket(SERVER_IP, SERVER_PORT);
        } catch (IOException e) {
            System.out.println("server unreachable");
            return;
        }
        System.out.println("Connected");

        this.started = true;

        try {
            ConnectionDetector connectionDetector = new ConnectionDetector(this.serverSocket,this);
            Thread connectionThread = new Thread(connectionDetector);
            connectionThread.start();
        } catch (IOException e) {
            System.out.println("Problems starting connection detector");
        }

        while (true)
        {
            try {
                ServerMessage message = receive();
                handleMessage(message);
            } catch (IOException|ClassNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void setClient(Client client) {
        this.client = client;
    }


    public boolean isStarted() {
        return started;
    }


    public void sendMessage(ClientMessage message) throws IOException {
        synchronized (lockOut) {
            output = new ObjectOutputStream(serverSocket.getOutputStream());
            output.writeObject(message);
        }
    }


    public ServerMessage receive() throws IOException, ClassNotFoundException {
        synchronized (lockIn) {
            do{
            input = new ObjectInputStream(serverSocket.getInputStream());
            messageArrived = input.readObject();
            }while (messageArrived instanceof PingMessage);
            ServerMessage message = (ServerMessage) messageArrived;
            return message;
        }
    }


    public void handleMessage(ServerMessage message) throws IOException {
        if(message instanceof EndGameMessage){
            client.getGraphics().updateMenuChange((EndGameMessage)message);
            output.close();
            input.close();
            serverSocket.close();
        }
        MessageHandler messageHandler = new MessageHandler(client,message);
        Thread messageHandlerThread = new Thread(messageHandler);
        messageHandlerThread.start();
        /*if(message instanceof WorkerMessage)
            client.getGraphics().updateBoardChange((WorkerMessage)message);
        else if(message instanceof CellMessage)
            client.getGraphics().updateBoardChange((CellMessage)message);
        else if(message instanceof ActionRequest)
            client.getGraphics().updateMenuChange((ActionRequest)message);
        else if(message instanceof CardRequest)
            client.getGraphics().updateMenuChange((CardRequest)message);
        else if(message instanceof ChangeNickRequest)
            client.getGraphics().updateMenuChange((ChangeNickRequest)message);
        else if(message instanceof EndGameMessage){
            client.getGraphics().updateMenuChange((EndGameMessage)message);
            output.close();
            input.close();
            serverSocket.close();
        }
        else if(message instanceof InitialCardsRequest)
            client.getGraphics().updateMenuChange((InitialCardsRequest)message);
        else if(message instanceof PlayersNumberRequest)
            client.getGraphics().updateMenuChange((PlayersNumberRequest)message);
        else if(message instanceof PlayersListMessage)
            client.getGraphics().updateMenuChange((PlayersListMessage)message);
        else if(message instanceof RequestMessage)
            client.getGraphics().updateMenuChange((RequestMessage)message);
        else if(message instanceof StartGameMessage)
            client.getGraphics().updateMenuChange((StartGameMessage)message);
        else if(message instanceof WorkersColorRequest)
            client.getGraphics().updateMenuChange((WorkersColorRequest)message);
        else if(message instanceof TextMessage)
            client.getGraphics().updateMenuChange((TextMessage) message);*/
    }

}
