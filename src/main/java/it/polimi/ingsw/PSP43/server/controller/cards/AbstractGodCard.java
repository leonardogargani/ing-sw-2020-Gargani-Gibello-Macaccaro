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
     *
     * @param gameSession
     * @return
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
     *
     * @param dataToBuild
     */
    public void build(DataToBuild dataToBuild) {
        CellsHandler handler = dataToBuild.getGameSession().getCellsHandler();
        Cell newCell = handler.getCell(dataToBuild.getNewPosition());

        if (dataToBuild.getBuildDome()) newCell.setOccupiedByDome(true);
        else newCell.setHeight(newCell.getHeight() + 1);

        handler.changeStateOfCell(newCell, dataToBuild.getNewPosition());
    }

    /**
     *
     * @param gameSession
     * @return
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
     *
     * @param gameSession
     * @return
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

    public AbstractGodCard cleanFromEffects(String nameOfEffect) { return null; }

    public void initMove(GameSession gameSession) throws GameEndedException, GameLostException {}

    public void initBuild(GameSession gameSession) throws GameEndedException {}

    public ActionResponse askForMove(GameSession gameSession) throws GameEndedException, GameLostException { return null; }

    public ActionResponse askForMove(GameSession gameSession, HashMap<Coord, ArrayList<Coord>> availablePositions) throws GameEndedException { return null; }

    public DataToBuild genericAskForBuild(GameSession gameSession) throws GameEndedException { return null; }

    public ActionResponse askForBuild(GameSession gameSession, HashMap<Coord, ArrayList<Coord>> availablePositionsBuildBlock, String message) throws GameEndedException { return null; }

    /**
     *
     * @param gameSession
     * @return
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
