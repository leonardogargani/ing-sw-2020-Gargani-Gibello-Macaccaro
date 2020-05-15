package it.polimi.ingsw.PSP43.server.model.card;

import it.polimi.ingsw.PSP43.client.networkMessages.ClientMessage;
import it.polimi.ingsw.PSP43.server.DataToBuild;
import it.polimi.ingsw.PSP43.server.DataToMove;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.modelHandlers.CellsHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;

import java.io.IOException;
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
     *
     * @param godName     name of the god whose power has been chosen by the player
     * @param description description of the ability of the god
     */
    public AbstractGodCard(String godName, String description, String power) {
        this.godName = godName;
        this.description = description;
        this.power = power;
    }

    public void setGodName(String godName) {
        this.godName = godName;
    }

    /**
     * Method that returns the name of the god.
     *
     * @return name of the god
     */
    public String getGodName() {
        return godName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Method that returns the description of the power of the god.
     *
     * @return description of the power of the god
     */
    public String getDescription() {
        return description;
    }

    public void setPower(String power) {
        this.power = power;
    }

    /**
     * Method that returns the description of the power of the god.
     *
     * @return description of the power of the god
     */
    public String getPower() {
        return power;
    }

    public void move(DataToMove dataToMove) throws IOException, ClassNotFoundException, WinnerCaughtException, InterruptedException {
        GameSession gameSession = dataToMove.getGameSession();
        Coord newPosition = dataToMove.getNewPosition();
        Worker workerToMove = dataToMove.getWorker();

        gameSession.getWorkersHandler().changePosition(workerToMove, newPosition);

        Worker workerUpdated = gameSession.getWorkersHandler().getWorker(workerToMove.getId());
        Coord workerCoordUpdated = workerUpdated.getCurrentPosition();
        Cell workerCellUpdated = gameSession.getCellsHandler().getCell(workerCoordUpdated);

        if (workerCellUpdated.getHeight() == 3) throw new WinnerCaughtException(dataToMove.getPlayer().getNickname());
    }

    public HashMap<Coord, ArrayList<Coord>> findAvailablePositionsToMove(GameSession gameSession) {
        CellsHandler cellsHandler = gameSession.getCellsHandler();
        Player currentPlayer = gameSession.getCurrentPlayer();

        Integer[] workerIds = currentPlayer.getWorkersIdsArray();
        ArrayList<Worker> workers = gameSession.getWorkersHandler().getWorkers(workerIds);

        HashMap<Coord, ArrayList<Coord>> neighbouringCoords = cellsHandler.findWorkersNeighbouringCoords(workers);
        Cell actualCell;
        int actualHeight;
        int newHeight;
        for (Map.Entry<Coord, ArrayList<Coord>> pair : neighbouringCoords.entrySet()) {
            ArrayList<Coord> coordsAvailable = pair.getValue();
            for (Iterator<Coord> coordIterator = coordsAvailable.iterator(); coordIterator.hasNext(); ) {
                Coord c1 = coordIterator.next();
                if (!cellsHandler.getCell(c1).getOccupiedByWorker() && !cellsHandler.getCell(c1).getOccupiedByDome()) {
                    actualCell = cellsHandler.getCell(pair.getKey());
                    actualHeight = actualCell.getHeight();
                    newHeight = cellsHandler.getCell(c1).getHeight();
                    if (newHeight - actualHeight > 1) coordIterator.remove();
                } else coordIterator.remove();
            }
        }
        return neighbouringCoords;
    }

    public void buildBlock(DataToBuild dataToBuild) throws IOException, ClassNotFoundException, InterruptedException {
        CellsHandler handler = dataToBuild.getGameSession().getCellsHandler();
        Cell newCell = handler.getCell(dataToBuild.getNewPosition());
        newCell.setHeight(newCell.getHeight() + 1);
        handler.changeStateOfCell(newCell, dataToBuild.getNewPosition());
    }

    public HashMap<Coord, ArrayList<Coord>> findAvailablePositionsToBuildBlock(GameSession gameSession) {
        CellsHandler cellsHandler = gameSession.getCellsHandler();
        Player currentPlayer = gameSession.getCurrentPlayer();

        Integer[] workerIds = currentPlayer.getWorkersIdsArray();
        ArrayList<Worker> workers = gameSession.getWorkersHandler().getWorkers(workerIds);

        HashMap<Coord, ArrayList<Coord>> neighbouringCoords = cellsHandler.findWorkersNeighbouringCoords(workers);


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

    public void buildDome(DataToBuild dataToBuild) throws IOException {
        CellsHandler handler = dataToBuild.getGameSession().getCellsHandler();
        Cell newCell = handler.getCell(dataToBuild.getNewPosition());
        newCell.setOccupiedByDome(true);
        handler.changeStateOfCell(newCell, dataToBuild.getNewPosition());
    }

    public HashMap<Coord, ArrayList<Coord>> findAvailablePositionsToBuildDome(GameSession gameSession) {
        CellsHandler cellsHandler = gameSession.getCellsHandler();
        Player currentPlayer = gameSession.getCurrentPlayer();

        Integer[] workerIds = currentPlayer.getWorkersIdsArray();
        ArrayList<Worker> workers = gameSession.getWorkersHandler().getWorkers(workerIds);

        HashMap<Coord, ArrayList<Coord>> neighbouringCoords = cellsHandler.findWorkersNeighbouringCoords(workers);
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

    public AbstractGodCard cleanFromEffects(String nameOfEffect) throws ClassNotFoundException {
        return null;
    }

    public void initMove(GameSession gameSession) throws ClassNotFoundException, WinnerCaughtException, InterruptedException, IOException, GameEndedException {}

    public void initBuild(GameSession gameSession) throws GameEndedException, IOException, InterruptedException, ClassNotFoundException {}

    public <T extends ClientMessage> T askForMove(GameSession gameSession) throws GameEndedException {
        return null;
    }

    public <T extends ClientMessage> T askForMove(GameSession gameSession, HashMap<Coord, ArrayList<Coord>> availablePositions) throws GameEndedException {
        return null;
    }

    public DataToBuild genericAskForBuild(GameSession gameSession) throws GameEndedException, InterruptedException, IOException, ClassNotFoundException {
        return null;
    }

    public <T extends ClientMessage> T askForBuild(GameSession gameSession, HashMap<Coord, ArrayList<Coord>> availablePositionsBuildBlock, String message) throws GameEndedException, InterruptedException, IOException, ClassNotFoundException {
        return null;
    }

    public void print() {
        System.out.println("    Godname : " + godName);
        System.out.println("    Description : " + description);
        System.out.println("    Power : " + power);
    }
}
