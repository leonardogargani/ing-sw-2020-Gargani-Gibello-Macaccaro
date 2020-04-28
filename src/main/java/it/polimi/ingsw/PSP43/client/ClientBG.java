package it.polimi.ingsw.PSP43.client;

import it.polimi.ingsw.PSP43.server.networkMessages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientBG implements Runnable {
    private Socket serverSocket;
    private Client client;
    private boolean lock;


    /**
     * Not default constructor for the ClientBG class, that is the thread who is going to handle the net
     * @param serverSocket is the open socket for the connection with the server
     * @param client refers to his client launcher
     * @throws IOException
     */
    public ClientBG(Socket serverSocket,Client client) throws IOException {
        this.serverSocket = serverSocket;
        this.client = client;
    }

    /**
     * In that override of the run method we are going to create a connection detector thread
     * and after that it listens to the network messages
     */
    @Override
    public void run() {
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
                receiver();
            } catch (IOException|ClassNotFoundException e) {
                System.out.println("Invalid message from the server");
            }
        }
    }

    /**
     * Setter method for the boolean variable lock, that will be used for the
     * synchronization of method that are going to use the socket
     * @param lock boolean variable
     */
    public void setLock(boolean lock) {
        this.lock = lock;
    }

    public boolean isLock() {
        return lock;
    }

    //Modified input parameter from ClientMessage to Object

    /**
     * Method that allows client to send messages to the server
     * @param message is the message created from the client and that will be send
     * @throws IOException
     */
    public void sendMessage(Object message) throws IOException {
        ObjectOutputStream output = new ObjectOutputStream(serverSocket.getOutputStream());
        do {
            if(!this.lock){
                this.lock = true;
                output.writeObject(message);
                this.lock = false;
        }}while (lock);
    }

    /**
     * Method that receives messages and calls handleMessages method
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void receiver() throws IOException, ClassNotFoundException {
        ObjectInputStream input = new ObjectInputStream(serverSocket.getInputStream());
        Object message = input.readObject();
        if(message instanceof TextMessage)
            handleMessage((TextMessage)message);
        else if(message instanceof WorkerMessage)
            handleMessage((WorkerMessage)message);
        else if(message instanceof CellMessage)
            handleMessage((CellMessage)message);
    }

    /**
     * Method that handles WorkerMessages and calls the update method
     * @param message is a WorkerMessage
     */
    public void handleMessage(WorkerMessage message){
        client.getGraphics().updateBoardChange(message);
    }

    /**
     * Method that handles CellMessages and calls the update method
     * @param message is a CellMessage
     */
    public void handleMessage(CellMessage message){
        client.getGraphics().updateBoardChange(message);
    }

    /**
     * Method that handles TextMessages and their subclasses and then calls the update method
     * @param message is a TextMessage or a subclass of that
     * @throws IOException
     */
    public void handleMessage(TextMessage message) throws IOException {
        if(message instanceof ActionRequest)
            client.getGraphics().updateMenuChange((ActionRequest)message);
        else if(message instanceof CardRequest)
            client.getGraphics().updateMenuChange((CardRequest)message);
        else if(message instanceof ChangeNickRequest)
            client.getGraphics().updateMenuChange((ChangeNickRequest)message);
        else if(message instanceof EndGameMessage){
            client.getGraphics().updateMenuChange((EndGameMessage)message);
           //TODO close input stream, output stream and socket
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
        else if(message != null)
            client.getGraphics().updateMenuChange(message);
    }
}
