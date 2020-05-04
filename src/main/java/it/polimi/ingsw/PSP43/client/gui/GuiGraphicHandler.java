package it.polimi.ingsw.PSP43.client.gui;

import it.polimi.ingsw.PSP43.client.ClientBG;
import it.polimi.ingsw.PSP43.client.GraphicHandler;
import it.polimi.ingsw.PSP43.server.networkMessages.*;


public class GuiGraphicHandler extends GraphicHandler {


    public GuiGraphicHandler(ClientBG clientBG) {
        super(clientBG);
    }

    /**
     * This method updates the cli or the gui changing the color of a cell, based
     * on the color of the worker that has been moved.
     *
     * @param workerMessage message containing the worker that has been moved
     */
    @Override
    public void updateBoardChange(WorkerMessage workerMessage) {
        // TODO implementation with JavaFX
    }


    /**
     * This method updates the cli board changing the symbol of a cell, based
     * on the fact that a worker can build on a cell.
     *
     * @param cellMessage message containing the cell that has changed (a worker has built on it)
     */
    @Override
    public void updateBoardChange(CellMessage cellMessage) {
        // TODO implementation with JavaFX
    }


    /**
     * This method updates the graphics of the client displaying the message of the players number
     * request, waiting and sending the response through the ClientBG object.
     *
     * @param request message containing the request for the number of the players
     */
    @Override
    public void updateMenuChange(PlayersNumberRequest request) {
        // TODO implementation with JavaFX
    }


    /**
     * This method updates the graphics of the client displaying the message of the initial cards
     * request, waiting and sending the response through the ClientBG object.
     *
     * @param request message containing the request for the cards chosen for this game
     */
    @Override
    public void updateMenuChange(InitialCardsRequest request) {
        // TODO implementation with JavaFX
    }


    /**
     * This method updates the graphics of the client displaying the message of the single card
     * request, waiting and sending the response through the ClientBG object.
     *
     * @param request message containing the request for the card chosen by a player
     */
    @Override
    public void updateMenuChange(CardRequest request) {
        // TODO implementation with JavaFX
    }


    /**
     * This method updates the graphics of the client displaying the message of the workers color
     * request, waiting and sending the response through the ClientBG object.
     *
     * @param request message containing the request for the color of player's workers
     */
    @Override
    public void updateMenuChange(WorkersColorRequest request) {
        // TODO implementation with JavaFX
    }


    /**
     * This method updates the graphics of the client displaying the message of the action
     * request, waiting and sending the response through the ClientBG object.
     *
     * @param request message containing the request for the action a player wants to make
     */
    @Override
    public void updateMenuChange(ActionRequest request) {
        // TODO implementation with JavaFX
    }


    /**
     * This method updates the graphics of the client displaying the message of the generic request
     * that needs a boolean as a response, waiting and sending the response through the ClientBG object.
     *
     * @param request message containing the generic boolean request
     */
    @Override
    public void updateMenuChange(RequestMessage request) {
        // TODO implementation with JavaFX
    }


    /**
     * This method updates the graphics of the client displaying the message of the end of the game.
     *
     * @param message message that notifies that the client the game has ended
     */
    @Override
    public void updateMenuChange(EndGameMessage message) {
        // TODO implementation with JavaFX
    }


    /**
     * This method updates the graphics of the client displaying the message of the request
     * for a change of the nick, since the chosen one is already in use.
     *
     * @param request message that notifies the client that the nick he has just chosen
     *                is already taken
     */
    @Override
    public void updateMenuChange(ChangeNickRequest request) {
        // TODO implementation with JavaFX
    }


    /**
     * This method updates the graphics of the client displaying the message at the top
     * of the screen (used to write that it's someone else's turn).
     *
     * @param message message to be displayed at the top of the screen
     */
    @Override
    public void updateMenuChange(TextMessage message) {
        // TODO implementation with JavaFX
    }


    /**
     * This method updates the graphics of the client displaying players' nicknames,
     * the Gods they've chosen and their workers' color.
     *
     * @param message message containing workers, their colors and the chosen gods
     */
    @Override
    public void updateMenuChange(PlayersListMessage message) {
        // TODO implementation with JavaFX
    }

    /**
     * This method updates the graphics of the client displaying, at the beginning of
     * the game, some useful information about the state of the game preparation.
     *
     * @param message message to be displayed
     */
    @Override
    public void updateMenuChange(StartGameMessage message) {
        // TODO implementation with JavaFX
    }


    /**
     * This method renders all the graphic aspects of the cli or the gui.
     */
    @Override
    public void render() {
        // TODO implementation with JavaFX
    }


}
