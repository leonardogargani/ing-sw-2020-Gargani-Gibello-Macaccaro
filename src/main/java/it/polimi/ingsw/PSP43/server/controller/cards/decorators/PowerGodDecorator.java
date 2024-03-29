package it.polimi.ingsw.PSP43.server.controller.cards.decorators;

import it.polimi.ingsw.PSP43.client.network.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.server.model.DataToBuild;
import it.polimi.ingsw.PSP43.server.model.DataToMove;
import it.polimi.ingsw.PSP43.server.controller.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.controller.cards.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException.GameLostException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This decoration is made to add dinamically features to the cards, in order to give powers to the players' Gods and
 * to limit their possibilities because of the opponents' Gods
 */
public abstract class PowerGodDecorator extends AbstractGodCard {
    private static final long serialVersionUID = -1361536654497716050L;
    private final AbstractGodCard godComponent;

    public PowerGodDecorator(AbstractGodCard godComponent) {
        this.godComponent = godComponent;
    }

    /**
     * This method is used to get the component stored into the current instance of the card.
     * @return The component stored into the current instance of the card.
     */
    public AbstractGodCard getGodComponent() {
        return godComponent;
    }

    /**
     * This method changes the model calling methods on model handlers and performing a move of a worker.
     * @param dataToMove The data used to change the model and obtained by the interaction with the client.
     */
    public void move(DataToMove dataToMove) {
        godComponent.move(dataToMove);
    }

    /**
     * This method finds all the available positions where a player can move his workers.
     * @param gameSession This is a reference to the main access to the game database.
     * @return All the available positions where a player can move his workers.
     */
    public HashMap<Coord, ArrayList<Coord>> findAvailablePositionsToMove(GameSession gameSession) {
        return godComponent.findAvailablePositionsToMove(gameSession);
    }

    /**
     * This method finds all the available positions where a player can build a block.
     * @param gameSession This is a reference to the main access to the game database.
     * @return All the available positions where a player can build a block.
     */
    public HashMap<Coord, ArrayList<Coord>> findAvailablePositionsToBuildBlock(GameSession gameSession) {
        return godComponent.findAvailablePositionsToBuildBlock(gameSession);
    }

    /**
     * This method returns the name of the god.
     * @return The name of the god.
     */
    public String getGodName() {
        return godComponent.getGodName();
    }

    /**
     * This method returns the description of the god.
     * @return The decription of the god.
     */
    public String getDescription() {
        return godComponent.getDescription();
    }

    /**
     * This method returns the string that represents the power of the god.
     * @return The string that represents the power of the god.
     */
    public String getPower() {
        return godComponent.getPower();
    }

    /**
     * This method handles the move during the turn, according to the card powers.
     * @param gameSession This is a reference to the main access to the game database.
     * @throws GameEndedException if the player decides to leave the game during his turn.
     * @throws GameLostException if the player can't do any move.
     */
    public void initMove(GameSession gameSession) throws GameEndedException, GameLostException {
        godComponent.initMove(gameSession);
    }

    /**
     * This method handles the build of a block or a dome.
     * @param gameSession This is a reference to the main access to the game database.
     * @throws GameEndedException if the player decides to leave the game during his turn.
     */
    public void initBuild(GameSession gameSession) throws GameEndedException {
        godComponent.initBuild(gameSession);
    }

    /**
     * This method performs a build of a block or a dome calling methods on model handlers that change the model.
     * @param dataToBuild The data used to update the model.
     */
    public void build(DataToBuild dataToBuild) {
        godComponent.build(dataToBuild);
    }

    /**
     * This method finds all the available positions where a player can build a dome.
     * @param gameSession This is a reference to the main access to the game database.
     * @return All the available positions where a player can build a dome.
     */
    public HashMap<Coord, ArrayList<Coord>> findAvailablePositionsToBuildDome(GameSession gameSession) {
        return godComponent.findAvailablePositionsToBuildDome(gameSession);
    }

    /**
     * This method is used to ask to the player where he wants to move his worker.
     * @param gameSession This is a reference to the main access to the game database.
     * @param availablePositions The already selected positions where a player can move his worker.
     * @return The response arrived from the client.
     * @throws GameEndedException if the player decides to leave the game during his turn.
     */
    public ActionResponse askForMove(GameSession gameSession, HashMap<Coord, ArrayList<Coord>> availablePositions) throws GameEndedException {
        return godComponent.askForMove(gameSession, availablePositions);
    }

    /**
     * This method asks to the player where he wants to build.
     * @param gameSession This is a reference to the main access to the game database.
     * @return The data used to perform the build changing the model.
     * @throws GameEndedException if the player decides to leave the game during his turn.
     */
    public DataToBuild genericAskForBuild(GameSession gameSession) throws GameEndedException {
        return godComponent.genericAskForBuild(gameSession);
    }

    /**
     * This method asks to the client to move a worker.
     * @param gameSession This is a reference to the main access to the game database.
     * @return The response containing the choice of the player.
     * @throws GameEndedException if the player decides to leave the game during his turn.
     * @throws GameLostException if the player can't do any move.
     */
    public ActionResponse askForMove(GameSession gameSession) throws GameEndedException, GameLostException {
        return godComponent.askForMove(gameSession);
    }

    /**
     * This method is used to ask to the player where he wants to build with his worker.
     * @param gameSession This is a reference to the main access to the game database.
     * @param availablePositionsBuildBlock The already selected positions where a player can build with his worker.
     * @return The response arrived from the client.
     * @throws GameEndedException if the player decides to leave the game during his turn.
     */
    public ActionResponse askForBuild(GameSession gameSession, HashMap<Coord, ArrayList<Coord>> availablePositionsBuildBlock, String message) throws GameEndedException {
        return godComponent.askForBuild(gameSession, availablePositionsBuildBlock, message);
    }

    /**
     * This method checks if the player that owns this card has won the game.
     * @param gameSession This is a reference to the main access to the game database.
     * @return True if the player has won the game, false otherwise.
     */
    public boolean checkConditionsToWin(GameSession gameSession) {
        return godComponent.checkConditionsToWin(gameSession);
    }

    /**
     * This method is used to clean the card from possible decorators which could block some functionalities.
     * It is called when the blocker begins a new turn.
     * @param nameOfEffect The effect that the blocker has activated by doing a determined action.
     * @return The card cleaned by the blocking decorator passed as parameter.
     */
    public AbstractGodCard cleanFromEffects(String nameOfEffect) {
        return godComponent.cleanFromEffects(nameOfEffect);
    }

    /**
     * This method prints all the data of the card.
     */
    public void print() {
        godComponent.print();
    }
}
