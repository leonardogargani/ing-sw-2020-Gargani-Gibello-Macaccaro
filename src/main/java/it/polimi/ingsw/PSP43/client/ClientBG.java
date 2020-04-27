package it.polimi.ingsw.PSP43.client;

import it.polimi.ingsw.PSP43.client.networkMessages.ClientMessage;
import it.polimi.ingsw.PSP43.server.networkMessages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientBG implements Runnable {
    private Socket serverSocket;
    private Client client;
    ObjectOutputStream output;
    ObjectInputStream input;

    public ClientBG(Socket serverSocket,Client client) throws IOException {
        this.serverSocket = serverSocket;
        this.client = client;
        output = new ObjectOutputStream(serverSocket.getOutputStream());
        input = new ObjectInputStream(serverSocket.getInputStream());
    }


    @Override
    public void run() {
        //TODO start ConnectionDetector

        while (true)
        {
            try {
                receiver(input.readObject());
            } catch (IOException|ClassNotFoundException e) {
                System.out.println("Invalid message from the server");
            }
        }
    }

    public void sendMessage(ClientMessage message) throws IOException {
        output.writeObject(message);
    }

    public void receiver(Object message) throws IOException {
        if(message instanceof TextMessage)
            handleMessage((TextMessage)message);
        else if(message instanceof WorkerMessage)
            handleMessage((WorkerMessage)message);
        else if(message instanceof CellMessage)
            handleMessage((CellMessage)message);
    }


    public void handleMessage(WorkerMessage message){
        client.getGraphics().updateBoardChange(message);
    }


    public void handleMessage(CellMessage message){
        client.getGraphics().updateBoardChange(message);
    }


    public void handleMessage(TextMessage message) throws IOException {
        if(message instanceof CardRequest)
            client.getGraphics().updateMenuChange((CardRequest)message);
        else if(message instanceof ChangeNickRequest)
            client.getGraphics().updateMenuChange((ChangeNickRequest)message);
        else if(message instanceof EndGameMessage){
            client.getGraphics().updateMenuChange((EndGameMessage)message);
            output.close();
            input.close();
        }
        else if(message instanceof InitialCardsRequest)
            client.getGraphics().updateMenuChange((InitialCardsRequest)message);
        else if(message instanceof PlayersNumberRequest)
            client.getGraphics().updateMenuChange((PlayersNumberRequest)message);
        else if(message instanceof PlayersListMessage)
            client.getGraphics().updateMenuChange((PlayersListMessage)message);
        else if(message instanceof ActionRequest)
            client.getGraphics().updateMenuChange((ActionRequest)message);
        else if(message instanceof RequestMessage)
            client.getGraphics().updateMenuChange((RequestMessage)message);
        else if(message instanceof WorkersColorRequest)
            client.getGraphics().updateMenuChange((WorkersColorRequest)message);
        else if(message != null)
            client.getGraphics().updateMenuChange(message);
    }
}
