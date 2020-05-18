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

import java.io.IOException;
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
            }
        }
    }

    /**
     * This method checks if there are messages in the messageBox and if there are some of these it calls the update
     * on the graphic handler
     * @throws QuitPlayerException if a player in the input writes quit to leave the game
     * @throws IOException signals that an I/O exception of some sort has occurred.
     */
    public void handleEvent() throws QuitPlayerException {
        if (messageBox.size() >= 1) {
            if (messageBox.get(0) instanceof CellMessage) {
                graphicHandler.updateBoardChange((CellMessage) messageBox.get(0));
                messageBox.remove(0);
            } else if (messageBox.get(0) instanceof ActionRequest) {
                graphicHandler.updateMenuChange((ActionRequest) messageBox.get(0));
                messageBox.remove(0);
            } else if (messageBox.get(0) instanceof CardRequest) {
                graphicHandler.updateMenuChange((CardRequest) messageBox.get(0));
                messageBox.remove(0);
            } else if (messageBox.get(0) instanceof ChangeNickRequest) {
                graphicHandler.updateMenuChange((ChangeNickRequest) messageBox.get(0));
                messageBox.remove(0);
            } else if (messageBox.get(0) instanceof EndGameMessage) {
                graphicHandler.updateMenuChange((EndGameMessage) messageBox.get(0));
                clientBG.setDisconnect(true);
                clientBG.closer();
                this.isActive = false;
            } else if (messageBox.get(0) instanceof InitialCardsRequest) {
                graphicHandler.updateMenuChange((InitialCardsRequest) messageBox.get(0));
                messageBox.remove(0);
            } else if (messageBox.get(0) instanceof PlayersNumberRequest) {
                graphicHandler.updateMenuChange((PlayersNumberRequest) messageBox.get(0));
                messageBox.remove(0);
            } else if (messageBox.get(0) instanceof PlayersListMessage) {
                graphicHandler.updateMenuChange((PlayersListMessage) messageBox.get(0));
                messageBox.remove(0);
            } else if (messageBox.get(0) instanceof RequestMessage) {
                graphicHandler.updateMenuChange((RequestMessage) messageBox.get(0));
                messageBox.remove(0);
            } else if (messageBox.get(0) instanceof StartGameMessage) {
                graphicHandler.updateMenuChange((StartGameMessage) messageBox.get(0));
                messageBox.remove(0);
            } else if (messageBox.get(0) instanceof WorkersColorRequest) {
                graphicHandler.updateMenuChange((WorkersColorRequest) messageBox.get(0));
                messageBox.remove(0);
            } else if (messageBox.get(0) instanceof TextMessage) {
                graphicHandler.updateMenuChange((TextMessage) messageBox.get(0));
                messageBox.remove(0);
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

    /**
     * Getter method for the messageBox, that is an ArrayList where messages are put when they are received
     * @return messageBox
     */
    public ArrayList<ServerMessage> getMessageBox() {
        return messageBox;
    }

    /**
     * Getter method for the ClientBG, that is the network handler
     * @return clientBG
     */
    public ClientBG getClientBG() {
        return clientBG;
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


}
