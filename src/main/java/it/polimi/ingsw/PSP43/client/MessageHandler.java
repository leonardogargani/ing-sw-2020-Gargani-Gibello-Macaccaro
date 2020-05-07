package it.polimi.ingsw.PSP43.client;

import it.polimi.ingsw.PSP43.client.cli.QuitPlayerException;
import it.polimi.ingsw.PSP43.server.networkMessages.*;

public class MessageHandler implements Runnable {
private Client client;
private ServerMessage message;

public MessageHandler(Client client, ServerMessage message){
        this.client = client;
        this.message = message;
    }


    @Override
    public void run() {
        handleMessage();
    }

    /*public void messageInBox(ServerMessage messageArrived){
        this.message = messageArrived;
        notifyAll();
    }*/

    public void handleMessage(){
        try {
            if (message instanceof WorkerMessage)
                client.getGraphics().updateBoardChange((WorkerMessage) message);
            else if (message instanceof CellMessage)
                client.getGraphics().updateBoardChange((CellMessage) message);
            else if (message instanceof ActionRequest)
                client.getGraphics().updateMenuChange((ActionRequest) message);
            else if (message instanceof CardRequest)
                client.getGraphics().updateMenuChange((CardRequest) message);
            else if (message instanceof ChangeNickRequest)
                client.getGraphics().updateMenuChange((ChangeNickRequest) message);
            else if (message instanceof InitialCardsRequest)
                client.getGraphics().updateMenuChange((InitialCardsRequest) message);
            else if (message instanceof PlayersNumberRequest)
                client.getGraphics().updateMenuChange((PlayersNumberRequest) message);
            else if (message instanceof PlayersListMessage)
                client.getGraphics().updateMenuChange((PlayersListMessage) message);
            else if (message instanceof RequestMessage)
                client.getGraphics().updateMenuChange((RequestMessage) message);
            else if (message instanceof StartGameMessage)
                client.getGraphics().updateMenuChange((StartGameMessage) message);
            else if (message instanceof WorkersColorRequest)
                client.getGraphics().updateMenuChange((WorkersColorRequest) message);
            else if (message instanceof TextMessage)
                client.getGraphics().updateMenuChange((TextMessage) message);
        } catch (QuitPlayerException e) {
            // TODO implement the handling of a QuitPlayerException when a player writes "quit" in the cli
        }
    }
}
