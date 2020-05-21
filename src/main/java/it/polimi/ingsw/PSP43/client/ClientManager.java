package it.polimi.ingsw.PSP43.client;

import it.polimi.ingsw.PSP43.client.cli.CliGraphicHandler;
import it.polimi.ingsw.PSP43.client.cli.CliInputHandler;
import it.polimi.ingsw.PSP43.client.cli.QuitPlayerException;
import it.polimi.ingsw.PSP43.client.cli.Screens;
import it.polimi.ingsw.PSP43.client.gui.GuiGraphicHandler;
import it.polimi.ingsw.PSP43.client.gui.GuiStarter;
import it.polimi.ingsw.PSP43.client.networkMessages.LeaveGameMessage;
import it.polimi.ingsw.PSP43.client.networkMessages.RegistrationMessage;
import it.polimi.ingsw.PSP43.server.networkMessages.*;
import javafx.application.Application;

import java.util.ArrayList;

/**
 * ClientManager class that starts the connection thread(ClientBG), and then handles updates on the CLI or the GUI
 */
public class ClientManager implements Runnable {
    private final int chosenInterface;
    private GraphicHandler graphicHandler;
    private ClientBG clientBG;
    private boolean isActive;
    private final ArrayList<ServerMessage> messageBox;

    /**
     * Not default constructor for ClientManager class that initializes the game on your chosen interface CLI or GUI
     * @param chosenInterface can be 1 for the CLI or 2 for the GUI
     */
    public ClientManager(int chosenInterface) {
        this.chosenInterface = chosenInterface;
        this.isActive = true;
        this.messageBox = new ArrayList<>();
    }

    /**
     * Override of the run method, here the client manager creates the network handler(ClientBG), initializes the chosen
     * interface and then handles events for the entire duration of the game, that events are the arrivals of messages from the server
     */
    @Override
    public void run() {

        clientBG = new ClientBG(this);
        Thread clientBGThread = new Thread(clientBG);

        if (chosenInterface == 1) {
            graphicHandler = new CliGraphicHandler(clientBG);
            clientBGThread.start();
        } else {
            clientBGThread.start();
            graphicHandler = new GuiGraphicHandler(clientBG);
            Application.launch(GuiStarter.class);
        }


        while (isActive) {
            try {
                handleEvent();
            } catch (QuitPlayerException e) {
                clientBG.sendMessage(new LeaveGameMessage());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method checks if there are messages in the messageBox and if there are some of these it calls the update
     * on the graphic handler
     * @throws QuitPlayerException if a player in the input writes quit to leave the game
     */
    public synchronized void handleEvent() throws QuitPlayerException, InterruptedException {

        ServerMessage message = popMessageFromBox();
        {
            if (message instanceof CellMessage) {
                graphicHandler.updateBoardChange((CellMessage) message);
            } else if (message instanceof ActionRequest) {
                graphicHandler.updateMenuChange((ActionRequest) message);
            } else if (message instanceof CardRequest) {
                graphicHandler.updateMenuChange((CardRequest) message);
            } else if (message instanceof ChangeNickRequest) {
                graphicHandler.updateMenuChange((ChangeNickRequest) message);
            } else if (message instanceof EndGameMessage) {
                graphicHandler.updateMenuChange((EndGameMessage) message);
                clientBG.setDisconnect(true);
                clientBG.closer();
                this.isActive = false;
            } else if (message instanceof InitialCardsRequest) {
                graphicHandler.updateMenuChange((InitialCardsRequest) message);
            } else if (message instanceof PlayersNumberRequest) {
                graphicHandler.updateMenuChange((PlayersNumberRequest) message);
            } else if (message instanceof PlayersListMessage) {
                graphicHandler.updateMenuChange((PlayersListMessage) message);
            } else if (message instanceof RequestMessage) {
                graphicHandler.updateMenuChange((RequestMessage) message);
            } else if (message instanceof StartGameMessage) {
                graphicHandler.updateMenuChange((StartGameMessage) message);
            } else if (message instanceof WorkersColorRequest) {
                graphicHandler.updateMenuChange((WorkersColorRequest) message);
            } else if (message instanceof TextMessage) {
                graphicHandler.updateMenuChange((TextMessage) message);
            }
        }
    }



    /**
     * Getter method for the graphic handler
     * @return graphic handler that can be a CliGraphicHandler or a GuiGraphicHandler
     */
    public GraphicHandler getGraphicHandler() {
        return graphicHandler;
    }

    //TODO delete this method
    /**
     * Getter method for the messageBox, that is an ArrayList where messages are put when they are received
     * @return messageBox
     */
    public ArrayList<ServerMessage> getMessageBox() {
        return messageBox;
    }


    /**
     * Initial method called by the clientBG after the start of the connection, it asks a nick from the player and
     * send it to the server
     */
    public void execute() {
        System.out.println(Screens.WELCOME);

        try {
            System.out.print("Choose a nickname:\n");
            CliInputHandler inputHandler = new CliInputHandler();
            String nickname = inputHandler.requestInputString();
            RegistrationMessage message = new RegistrationMessage(nickname);
            clientBG.sendMessage(message);
        } catch (QuitPlayerException e) {
            clientBG.sendMessage(new LeaveGameMessage());
        }

    }


    /**
     * Synchronized method to add message in the message box
     * @param message is the added message
     */
    public synchronized void pushMessageInBox(ServerMessage message){
        messageBox.add( message);
        notifyMessageArrived();
    }


    /**
     * Synchronized method to remove a message from the message box
     * @return the removed message
     */
    public synchronized ServerMessage popMessageFromBox() throws InterruptedException {
        while(messageBox.size() == 0) {
            wait();
        }

        ServerMessage message = messageBox.get(0);
        messageBox.remove(message);
        return message;
    }


    /**
     * Synchronized method to notify the arrivals of a message
     */
    public synchronized void notifyMessageArrived(){
        notifyAll();
    }
}
