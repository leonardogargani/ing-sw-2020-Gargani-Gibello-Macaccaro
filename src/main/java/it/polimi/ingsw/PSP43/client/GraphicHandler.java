package it.polimi.ingsw.PSP43.client;

import it.polimi.ingsw.PSP43.server.networkMessages.*;


public abstract class GraphicHandler {

    private ClientBG clientBG;

    public GraphicHandler(ClientBG clientBG) {
        this.clientBG = clientBG;
    }

    /**
     * This method sets the clientBG attribute, which is used to send messages to the server.
     * @param clientBG clientBG attribute, which is used to send messages to the server.
     */
    public void setClientBG(ClientBG clientBG) {
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
     * This method updates the graphics of the client changing the color of a cell, based
     * on the color of the worker that has been moved.
     * @param workerMessage message containing the worker that has been moved
     */
    public abstract void updateBoardChange(WorkerMessage workerMessage);


    /**
     * This method updates the graphics of the client changing the symbol of a cell, based
     * on the fact that a worker can build on a cell.
     * @param cellMessage message containing the cell that has changed (a worker has built on it)
     */
    public abstract void updateBoardChange(CellMessage cellMessage);


    // following update methods send a response calling ClientBG.sendMessage(ClientMessage) as last instruction


    /**
     * This method updates the graphics of the client displaying the message of the players number
     * request, waiting and sending the response through the ClientBG object.
     * @param request message containing the request for the number of the players
     */
    public abstract void updateMenuChange(PlayersNumberRequest request);


    /**
     * This method updates the graphics of the client displaying the message of the initial cards
     * request, waiting and sending the response through the ClientBG object.
     * @param request message containing the request for the cards chosen for this game
     */
    public abstract void updateMenuChange(InitialCardsRequest request);


    /**
     * This method updates the graphics of the client displaying the message of the single card
     * request, waiting and sending the response through the ClientBG object.
     * @param request message containing the request for the card chosen by a player
     */
    public abstract void updateMenuChange(CardRequest request);


    /**
     * This method updates the graphics of the client displaying the message of the workers color
     * request, waiting and sending the response through the ClientBG object.
     * @param request message containing the request for the color of player's workers
     */
    public abstract void updateMenuChange(WorkersColorRequest request);


    /**
     * This method updates the graphics of the client displaying the message of the action
     * request, waiting and sending the response through the ClientBG object.
     * @param request message containing the request for the action a player wants to make
     */
    public abstract void updateMenuChange(ActionRequest request);


    /**
     * This method updates the graphics of the client displaying the message of the generic request
     * that needs a boolean as a response, waiting and sending the response through the ClientBG object.
     * @param request message containing the generic boolean request
     */
    public abstract void updateMenuChange(RequestMessage request);


    // following update methods do not send a response


    /**
     * This method updates the graphics of the client displaying the message of the end of the game.
     * @param message message that notifies that the client the game has ended
     */
    public abstract void updateMenuChange(EndGameMessage message);


    /**
     * This method updates the graphics of the client displaying the message of the request
     * for a change of the nick, since the chosen one is already in use.
     * @param request message that notifies the client that the nick he has just chosen
     *                is already taken
     */
    public abstract void updateMenuChange(ChangeNickRequest request);


    /**
     * This method updates the graphics of the client displaying the message at the top
     * of the screen (used to write that it's someone else's turn).
     * @param message message to be displayed at the top of the screen
     */
    public abstract void updateMenuChange(TextMessage message);


    /**
     * This method updates the graphics of the client displaying players' nicknames,
     * the Gods they've chosen and their workers' color.
     * @param message message containing workers, their colors and the chosen gods
     */
    public abstract void updateMenuChange(PlayersListMessage message);


    /**
     * This method renders all the graphic aspects of the cli or the gui.
     */
    public abstract void render();


}
