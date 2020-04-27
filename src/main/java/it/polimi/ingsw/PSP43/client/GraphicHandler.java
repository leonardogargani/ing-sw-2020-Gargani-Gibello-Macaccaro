package it.polimi.ingsw.PSP43.client;

import it.polimi.ingsw.PSP43.server.networkMessages.*;


public abstract class GraphicHandler {

    private ClientBG clientBG;


    /**
     * Non default constructor that sets the clientBG attribute, which is used
     * to send messages to the server.
     * @param clientBG clientBG attribute, which is used to send messages to the server.
     */
    public GraphicHandler(ClientBG clientBG) {
        this.clientBG = clientBG;
    }


    /**
     * This method returns the clientBG attribute, which is used to send messages to the server.
     * @return clientBG attribute, which is used to send messages to the server.
     */
    public ClientBG getClientBG() {
        return clientBG;
    }


    /**
     * This method updates the cli or the gui changing the color of a cell, based
     * on the color of the worker that has been moved.
     * @param workerMessage message containing the worker that has been moved
     */
    public abstract void updateBoardChange(WorkerMessage workerMessage);


    /**
     * This method updates the cli or the gui changing the symbol of a cell, based
     * on the fact that a worker can build on a cell.
     * @param cellMessage message containing the cell that has changed (a worker has built on it)
     */
    public abstract void updateBoardChange(CellMessage cellMessage);


    // methods that send a response calling ClientBG.sendMessage(ClientMessage) as their last instruction
    public abstract void updateMenuChange(PlayersNumberRequest request);
    public abstract void updateMenuChange(InitialCardsRequest request);
    public abstract void updateMenuChange(CardRequest request);
    public abstract void updateMenuChange(WorkerColorRequest request);
    public abstract void updateMenuChange(ActionRequest request);
    public abstract void updateMenuChange(RequestMessage request);

    // methods that do not send a response
    public abstract void updateMenuChange(ChangeNickRequest request);
    public abstract void updateMenuChange(TextMessage message);
    public abstract void updateMenuChange(PlayersListMessage message);


    /**
     * This method
     * @param message
     */
    public abstract void updateMenuChange(EndGameMessage message);


    /**
     * This method renders all the graphic aspects of the cli or the gui.
     */
    public abstract void render();


}
