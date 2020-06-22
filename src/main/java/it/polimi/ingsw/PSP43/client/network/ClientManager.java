package it.polimi.ingsw.PSP43.client.network;

import it.polimi.ingsw.PSP43.client.graphic.GraphicHandler;
import it.polimi.ingsw.PSP43.client.graphic.cli.CliGraphicHandler;
import it.polimi.ingsw.PSP43.client.graphic.cli.QuitPlayerException;
import it.polimi.ingsw.PSP43.client.graphic.gui.GuiExecutor;
import it.polimi.ingsw.PSP43.client.graphic.gui.GuiGraphicHandler;
import it.polimi.ingsw.PSP43.client.network.networkMessages.LeaveGameMessage;
import it.polimi.ingsw.PSP43.server.network.networkMessages.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * ClientManager class that starts the connection thread(ClientBG), and then handles updates on the CLI or the GUI
 */
public class ClientManager implements Runnable {
    private final int chosenInterface;
    private GraphicHandler graphicHandler;
    private boolean isActive;
    private final ArrayList<ServerMessage> messageBox;
    private static Thread guiExecutorThread;
    private static GuiExecutor guiExecutor;

    /**
     * Not default constructor for ClientManager class that initializes the game on your chosen interface CLI or GUI.
     *
     * @param chosenInterface can be 1 for the CLI or 2 for the GUI
     */
    public ClientManager(int chosenInterface) {
        this.chosenInterface = chosenInterface;
        this.isActive = true;
        this.messageBox = new ArrayList<>();
    }

    /**
     * Override of the run method, here the client manager creates the network handler(ClientBG), initializes
     * the chosen interface and then handles events for the entire duration of the game, that events are the
     * arrivals of messages from the server.
     */
    @Override
    public void run() {
        ClientBG clientBG = new ClientBG(this);
        Thread clientBGThread = new Thread(clientBG);

        // Here the chosen interface is started
        if (chosenInterface == 1) {
            clientBGThread.start();
            graphicHandler = new CliGraphicHandler(clientBG);
            Thread cliGraphicHandlerThread = new Thread((CliGraphicHandler) graphicHandler);
            cliGraphicHandlerThread.start();
        } else {
            clientBGThread.start();
            graphicHandler = new GuiGraphicHandler(clientBG);
            guiExecutor = new GuiExecutor();
            guiExecutorThread = new Thread(guiExecutor);
            guiExecutorThread.start();
        }

        // Here begins a infinite loop where the ClientManager waits for messages to handle from the server.
        // This loop ends only if the player decides to shut down the application by setting isActive to false.
        while (isActive /*|| messageBox.size() != 0*/) {
            try {
                if (guiExecutor != null) {
                    if (guiExecutorThread.isAlive()) {
                        handleEvent();
                    } else {
                        throw new QuitPlayerException("Gui closed");
                    }
                } else {
                    handleEvent();
                }

            } catch (QuitPlayerException e) {
                // This block is only called if the player clicks on the "home button" in the GUI or if he writes
                // "quit" in the cli. Here is given the possibility to re-join another game by not closing the socket.
                // Here a message of DISCONNECTION is sent to make the Server conscious of the disconnection and to
                // inform all other players of the fact.
                LeaveGameMessage leaveGameMessage = new LeaveGameMessage();
                leaveGameMessage.setTypeDisconnectionHeader(LeaveGameMessage.TypeDisconnectionHeader.GAME_DISCONNECTION);
                clientBG.sendMessage(leaveGameMessage);
                clientBG.handleDisconnection();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                TimeUnit.MILLISECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method checks if there are messages in the messageBox and if there are some of these it calls the update
     * on the graphic handler.
     *
     * @throws QuitPlayerException if a player in the input writes quit to leave the game
     */
    public void handleEvent() throws QuitPlayerException, InterruptedException {
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
            } else if (message instanceof ChooseStarterMessage) {
                graphicHandler.updateMenuChange((ChooseStarterMessage) message);
            } else if (message instanceof TextMessage) {
                graphicHandler.updateMenuChange((TextMessage) message);
            }
        }
    }

    /**
     * Setter method for the boolean variable isActive.
     *
     * @param active false to stop the run of the thread
     */
    public void setActive(boolean active) {
        isActive = active;
    }

    /**
     * Synchronized method to add message in the message box.
     *
     * @param message is the message that will be pushed on the stack of the ClientManager
     */
    public synchronized void pushMessageInBox(ServerMessage message) {
        if (!containsEndGameMessage()) {
            messageBox.add(message);
            notifyAll();
        }
    }

    /**
     * This method is used to check if there already EndGameMessages on the stack.
     *
     * @return true if there are already EndGameMessages on the stack, false otherwise
     */
    public synchronized boolean containsEndGameMessage() {
        for (ServerMessage s : messageBox) {
            if (s instanceof EndGameMessage) return true;
        }
        return false;
    }

    /**
     * This method is used to eliminate all the messages from the stack. This is done in case it could be
     * necessary to eliminate all previous and useless messages arrived.
     */
    public synchronized void flushStack() {
        for (Iterator<ServerMessage> messageIterator = messageBox.iterator(); messageIterator.hasNext(); ) {
            messageIterator.next();
            messageIterator.remove();
        }
    }

    /**
     * Synchronized method to remove a message from the message box.
     *
     * @return the removed message
     */
    public synchronized ServerMessage popMessageFromBox() throws InterruptedException {
        while (messageBox.size() == 0) {
            wait();
        }

        ServerMessage message = messageBox.get(0);
        /*if (!(message instanceof EndGameMessage)) {
            messageBox.remove(message);
        }*/

        messageBox.remove(message);
        return message;
    }
}