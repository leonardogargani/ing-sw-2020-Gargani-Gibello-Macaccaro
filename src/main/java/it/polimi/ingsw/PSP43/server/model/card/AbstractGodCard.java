package it.polimi.ingsw.PSP43.server.model.card;

import it.polimi.ingsw.PSP43.server.DataToAction;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.modelHandlers.CellsHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * AbstractGodCard is the class that represents the cards with God Powers, thus it is associated to a God and the
 * corresponding description of its power.
 * Each player owns a single card for the entire duration of the game.
 */
public abstract class AbstractGodCard {

    private String godName;
    private String description;
    private String power;

    public AbstractGodCard() {
    }

    /**
     * Non-default constructor, it initializes the card with the name of the god and its description.
     * @param godName name of the god whose power has been chosen by the player
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
     * @return description of the power of the god
     */
    public String getPower() {
        return power;
    }

    public void move(DataToAction dataToAction) throws IOException, ClassNotFoundException, WinnerCaughtException {
        GameSession gameSession = dataToAction.getGameSession();
        Coord newPosition = dataToAction.getNewPosition();
        Worker workerToMove = dataToAction.getWorker();

        CellsHandler handler = gameSession.getCellsHandler();
        Cell newCell = handler.getCell(newPosition);
        Coord oldPosition = workerToMove.getCurrentPosition();
        Cell oldCell = handler.getCell(oldPosition);
        newCell.setOccupiedByWorker(true);
        oldCell.setOccupiedByWorker(false);
        handler.changeStateOfCell(newCell, newPosition);
        handler.changeStateOfCell(oldCell, oldPosition);
        workerToMove.setCurrentPosition(newPosition);
    }

    public void buildBlock(DataToAction dataToAction) throws IOException, ClassNotFoundException {
        CellsHandler handler = dataToAction.getGameSession().getCellsHandler();
        Cell newCell = handler.getCell(dataToAction.getNewPosition());
        newCell.setHeight(newCell.getHeight()+1);
        handler.changeStateOfCell(newCell, dataToAction.getNewPosition());
    }

    public void buildDome(DataToAction dataToAction) {
        CellsHandler handler = dataToAction.getGameSession().getCellsHandler();
        Cell newCell = handler.getCell(dataToAction.getNewPosition());
        newCell.setOccupiedByDome(true);
        handler.changeStateOfCell(newCell, dataToAction.getNewPosition());
    }

    public HashMap<Coord, ArrayList<Coord>> findAvailablePositionsToMove(CellsHandler handler, Worker[] workers) {
        HashMap<Coord, ArrayList<Coord>> neighbouringCoords = handler.findNeighbouringCoords(workers);
        Cell actualCell;
        int actualHeight;
        int newHeight;
        for (Coord c : neighbouringCoords.keySet()) {
            for (Coord c1 : neighbouringCoords.get(c)) {
                if (!handler.getCell(c1).getOccupiedByWorker() && !handler.getCell(c1).getOccupiedByDome()) {
                    actualCell = handler.getCell(c);
                    actualHeight = actualCell.getHeight();
                    newHeight = handler.getCell(c1).getHeight();
                    if (newHeight - actualHeight > 1) neighbouringCoords.get(c).remove(c1);
                }
                else neighbouringCoords.remove(c);
            }
        }
        return neighbouringCoords;
    }

    public HashMap<Coord, ArrayList<Coord>> findAvailablePositionsToBuildBlock(CellsHandler handler, Worker[] workers) {
        HashMap<Coord, ArrayList<Coord>> neighbouringCoords = handler.findNeighbouringCoords(workers);
        for (Coord c : neighbouringCoords.keySet()) {
            neighbouringCoords.get(c).removeIf(c1 -> handler.getCell(c1).getOccupiedByWorker() || handler.getCell(c1).getOccupiedByDome() || handler.getCell(c1).getHeight() == 3);
        }
        return neighbouringCoords;
    }

    public HashMap<Coord, ArrayList<Coord>> findAvailablePositionsToBuildDome(CellsHandler handler, Worker[] workers) {
        HashMap<Coord, ArrayList<Coord>> neighbouringCoords = handler.findNeighbouringCoords(workers);
        for (Coord c : neighbouringCoords.keySet()) {
            if (handler.getCell(c).getOccupiedByWorker() || handler.getCell(c).getOccupiedByDome() || handler.getCell(c).getHeight() < 4) {
                neighbouringCoords.remove(c);
            }
        }
        return neighbouringCoords;
    }

    public Worker[] findWorkersToBuild(Worker[] workers) {
        Worker workerToBuild = null;
        for (Worker w : workers) {
            if (w.isLatestMoved()) workerToBuild = w;
        }

        Worker[] newWorker = new Worker[1];
        newWorker[0] = workerToBuild;
        return newWorker;
    }

    public AbstractGodCard cleanFromEffects(String nameOfEffect) throws ClassNotFoundException {
        return null;
    }

    public void print() {
        System.out.println("    Godname : " + godName);
        System.out.println("    Description : " + description);
        System.out.println("    Power : " + power);
    }
}
