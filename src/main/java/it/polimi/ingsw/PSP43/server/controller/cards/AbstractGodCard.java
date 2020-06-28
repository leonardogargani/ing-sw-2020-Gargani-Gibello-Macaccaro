package it.polimi.ingsw.PSP43.server.controller.cards;

import it.polimi.ingsw.PSP43.client.network.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.server.model.DataToBuild;
import it.polimi.ingsw.PSP43.server.model.DataToMove;
import it.polimi.ingsw.PSP43.server.controller.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.CellsHandler;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException.GameLostException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * AbstractGodCard is the class that represents the cards with God Powers, thus it is associated to a God and the
 * corresponding description of its power.
 * Each player owns a single card for the entire duration of the game.
 */
public abstract class AbstractGodCard implements Serializable {

    private static final long serialVersionUID = -4690178671081548142L;

    private String godName;
    private String description;
    private String power;

    public AbstractGodCard() {
    }

    /**
     * Non-default constructor, it initializes the card with the name of the god and its description.
     * @param godName     name of the god whose power has been chosen by the player
     * @param description description of the ability of the god
     * @param power the power of the god
     */
    public AbstractGodCard(String godName, String description, String power) {
        this.godName = godName;
        this.description = description;
        this.power = power;
    }

    /**
     * Method that returns the name of the god.
     * @return name of the god
     */
    public String getGodName() {
        return godName;
    }

    /**
     * Method that returns the description of the power of the god.
     * @return description of the power of the god
     */
    public String getDescription() {
        return description;
    }

    /**
     * Method that returns the description of the power of the god.
     * @return description of the power of the god
     */
    public String getPower() {
        return power;
    }

    /**
     * This method is the basic to move a worker on the board.
     * @param dataToMove The data used to change the model and obtained by the interaction with the client.
     */
    public void move(DataToMove dataToMove) {
        GameSession gameSession = dataToMove.getGameSession();
        Coord newPosition = dataToMove.getNewPosition();
        Worker workerToMove = dataToMove.getWorker();

        gameSession.getWorkersHandler().changePosition(workerToMove, newPosition);
    }

    /**
     * This method finds all the available positions where a player can move his workers.
     * @param gameSession This is a reference to the main access to the game database.
     * @return All the available positions where a player can move his workers.
     */
    public HashMap<Coord, ArrayList<Coord>> findAvailablePositionsToMove(GameSession gameSession) {
        CellsHandler cellsHandler = gameSession.getCellsHandler();

        HashMap<Coord, ArrayList<Coord>> neighbouringCoords = cellsHandler.findWorkersNeighbouringCoords(gameSession.getCurrentPlayer());

        Cell actualCell;
        int actualHeight;
        int newHeight;
        for (Iterator<Map.Entry<Coord, ArrayList<Coord>>> iter = neighbouringCoords.entrySet().iterator(); iter.hasNext(); ) {
            Map.Entry<Coord, ArrayList<Coord>> currentEntry = iter.next();
            ArrayList<Coord> coordsAvailable = currentEntry.getValue();
            for (Iterator<Coord> coordIterator = coordsAvailable.iterator(); coordIterator.hasNext(); ) {
                Coord c1 = coordIterator.next();
                if (!(cellsHandler.getCell(c1).getOccupiedByWorker()) && !(cellsHandler.getCell(c1).getOccupiedByDome())) {
                    actualCell = cellsHandler.getCell(currentEntry.getKey());
                    actualHeight = actualCell.getHeight();
                    newHeight = cellsHandler.getCell(c1).getHeight();
                    if (newHeight - actualHeight > 1) coordIterator.remove();
                } else coordIterator.remove();
            }

            if (coordsAvailable.size() == 0) iter.remove();
        }
        return neighbouringCoords;
    }

    /**
     * This method performs a build of a block or a dome calling methods on model handlers that change the model.
     * @param dataToBuild The data used to update the model.
     */
    public void build(DataToBuild dataToBuild) {
        CellsHandler handler = dataToBuild.getGameSession().getCellsHandler();
        Cell newCell = handler.getCell(dataToBuild.getNewPosition());

        if (dataToBuild.getBuildDome()) newCell.setOccupiedByDome(true);
        else newCell.setHeight(newCell.getHeight() + 1);

        handler.changeStateOfCell(newCell, dataToBuild.getNewPosition());
    }

    /**
     * This method finds all the available positions where a player can build a block.
     * @param gameSession This is a reference to the main access to the game database.
     * @return All the available positions where a player can build a block.
     */
    public HashMap<Coord, ArrayList<Coord>> findAvailablePositionsToBuildBlock(GameSession gameSession) {
        CellsHandler cellsHandler = gameSession.getCellsHandler();

        HashMap<Coord, ArrayList<Coord>> neighbouringCoords = cellsHandler.findWorkersNeighbouringCoordsExclude(gameSession.getCurrentPlayer());

        Iterator<Map.Entry<Coord, ArrayList<Coord>>> iter = neighbouringCoords.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Coord, ArrayList<Coord>> currentEntry = iter.next();
            ArrayList<Coord> currentPositions = currentEntry.getValue();
            currentPositions.removeIf(c -> cellsHandler.getCell(c).getOccupiedByWorker() || cellsHandler.getCell(c).getOccupiedByDome() || cellsHandler.getCell(c).getHeight() >= 3);

            if (currentEntry.getValue().size() == 0) {
                iter.remove();
            }
        }

        return neighbouringCoords;
    }

    /**
     * This method finds all the available positions where a player can build a dome.
     * @param gameSession This is a reference to the main access to the game database.
     * @return All the available positions where a player can build a dome.
     */
    public HashMap<Coord, ArrayList<Coord>> findAvailablePositionsToBuildDome(GameSession gameSession) {
        CellsHandler cellsHandler = gameSession.getCellsHandler();

        HashMap<Coord, ArrayList<Coord>> neighbouringCoords = cellsHandler.findWorkersNeighbouringCoordsExclude(gameSession.getCurrentPlayer());
        Iterator<Map.Entry<Coord, ArrayList<Coord>>> iter = neighbouringCoords.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Coord, ArrayList<Coord>> currentEntry = iter.next();
            ArrayList<Coord> currentPositions = currentEntry.getValue();
            currentPositions.removeIf(c -> cellsHandler.getCell(c).getOccupiedByWorker() || cellsHandler.getCell(c).getOccupiedByDome() || cellsHandler.getCell(c).getHeight() < 3);

            if (currentEntry.getValue().size() == 0) {
                iter.remove();
            }
        }

        return neighbouringCoords;
    }

    /**
     * This method is used to clean the card from possible decorators which could block some functionalities.
     * It is called when the blocker begins a new turn.
     * @param nameOfEffect The effect that the blocker has activated by doing a determined action.
     * @return The card cleaned by the blocking decorator passed as parameter.
     */
    public AbstractGodCard cleanFromEffects(String nameOfEffect) { return null; }

    /**
     * This method handles the move during a turn.
     * @param gameSession This is a reference to the main access to the game database.
     * @throws GameEndedException if the player decides to leave the game during his turn.
     * @throws GameLostException if the player can't do any move.
     */
    public void initMove(GameSession gameSession) throws GameEndedException, GameLostException {}

    /**
     * This method handles the build during a turn.
     * @param gameSession This is a reference to the main access to the game database.
     * @throws GameEndedException if the player decides to leave the game during his turn.
     */
    public void initBuild(GameSession gameSession) throws GameEndedException {}

    /**
     * This method asks to the client to move a worker.
     * @param gameSession This is a reference to the main access to the game database.
     * @return The response containing the choice of the player.
     * @throws GameEndedException if the player decides to leave the game during his turn.
     * @throws GameLostException if the player can't do any move.
     */
    public ActionResponse askForMove(GameSession gameSession) throws GameEndedException, GameLostException { return null; }

    /**
     * This method asks to the player for a move, between some positions already selected.
     * @param gameSession This is a reference to the main access to the game database.
     * @param availablePositions The positions among which the player can choose where to move a worker.
     * @return The response from the player about where to move a worker.
     * @throws GameEndedException if the player decides to leave the game during his turn.
     */
    public ActionResponse askForMove(GameSession gameSession, HashMap<Coord, ArrayList<Coord>> availablePositions) throws GameEndedException { return null; }

    /**
     * This method asks to the player where he wants to build.
     * @param gameSession This is a reference to the main access to the game database.
     * @return The data used to perform the build changing the model.
     * @throws GameEndedException if the player decides to leave the game during his turn.
     */
    public DataToBuild genericAskForBuild(GameSession gameSession) throws GameEndedException { return null; }

    /**
     * This method asks to the player where to build among some already selected cells.
     * @param gameSession This is a reference to the main access to the game database.
     * @param availablePositionsBuildBlock The positions selected where the player can decide to build.
     * @param message The message sent to the player.
     * @return The response from the player.
     * @throws GameEndedException if the player decides to leave the game during his turn.
     */
    public ActionResponse askForBuild(GameSession gameSession, HashMap<Coord, ArrayList<Coord>> availablePositionsBuildBlock, String message) throws GameEndedException { return null; }

    /**
     * This method checks if the player has won the game.
     * @param gameSession This is a reference to the main access to the game database.
     * @return True if the player won, false otherwise.
     */
    public boolean checkConditionsToWin(GameSession gameSession) {
        return true;
    }

    /**
     * This method is used to print the information of the god.
     */
    public void print() {
        System.out.println("    Godname : " + godName.toUpperCase());
        System.out.println("    Description : " + description);
        System.out.println("    Power : " + power);
        System.out.println("\n");
    }
}
