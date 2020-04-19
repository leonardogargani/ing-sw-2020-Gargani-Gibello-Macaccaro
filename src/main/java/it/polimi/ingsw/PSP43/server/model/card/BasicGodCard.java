package it.polimi.ingsw.PSP43.server.model.card;


import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.*;
import it.polimi.ingsw.PSP43.server.modelHandlers.CellsHandler;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * AbstractGodCard is the class that represents the cards with God Powers, thus it is associated to a God and the
 * corresponding description of its power.
 * Each player owns a single card for the entire duration of the game.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BasicGodCard extends AbstractGodCard {

    public BasicGodCard() {
    }

    /**
     * Non-default constructor, it initializes the card with the name of the god and its description.
     * @param godName name of the god whose power has been chosen by the player
     * @param description description of the ability of the god
     */
    public BasicGodCard(String godName, String description, String power) {
        super(godName, description, power);
    }

    public void setGodName(String godName) {
        super.setGodName(godName);
    }

    /**
     * Method that returns the name of the god.
     * @return name of the god
     */
    public String getGodName() {
        return super.getGodName();
    }

    public void setDescription(String description) {
        super.setDescription(description);
    }

    /**
     * Method that returns the description of the power of the god.
     * @return description of the power of the god
     */
    public String getDescription() {
        return super.getDescription();
    }

    public void setPower(String power) {
        super.setPower(power);
    }

    /**
     * Method that returns the description of the power of the god.
     * @return description of the power of the god
     */
    public String getPower() {
        return super.getPower();
    }

    /**
     *
     *
     * @param gameSession The session of the game, from which it is possible to retrieve the ClientListener
     * @param player The player who did the move
     * @param workerToMove The worker that is moving
     * @param newPosition The position in which the player is going to place his worker
     * */
    public void move(GameSession gameSession, Player player, Worker workerToMove, Coord newPosition) {
        //Coord oldPosition = workerToMove.getCurrentPosition();
        //handler.movePlayer(newPosition, oldPosition);
        //workerToMove.setCurrentPosition(newPosition);

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

    /**
     *
     *
     * @param gameSession The session of the game, from which it is possible to retrieve the ClientListener
     * @param player The player who did the build
     * @param worker The worker that is going to build a block
     * @param position The position in which the player's worker is going to build the block
     */
    public void buildBlock(GameSession gameSession, Player player, Worker worker, Coord position) {
        CellsHandler handler = gameSession.getCellsHandler();
        Cell newCell = handler.getCell(position);
        newCell.setHeight(newCell.getHeight()+1);
        handler.changeStateOfCell(newCell, position);
    }

    /**
     *
     *
     * @param gameSession The session of the game, from which it is possible to retrieve the ClientListener
     * @param player The player who did the build
     * @param worker The worker that is going to build a dome
     * @param position The position in which the player's worker is going to build the dome
     */
    public void buildDome(GameSession gameSession, Player player, Worker worker, Coord position) {
        CellsHandler handler = gameSession.getCellsHandler();
        Cell newCell = handler.getCell(position);
        newCell.setOccupiedByDome(true);
        handler.changeStateOfCell(newCell, position);
    }

    public HashMap<Coord, ArrayList<Integer>> findAvailablePositionsToMove(CellsHandler handler, Worker[] workers) {
        HashMap<Coord, ArrayList<Integer>> neighbouringCoords = handler.findNeighbouringCells(workers);
        Cell actualCell;
        int actualHeight;
        int newHeight;
        Worker actualWorker = null;
        for (Coord c : neighbouringCoords.keySet()) {
            if (!handler.getCell(c).getOccupiedByWorker() && !handler.getCell(c).getOccupiedByDome()) {
                for (int wId : neighbouringCoords.get(c)) {
                    for (Worker w : workers) {
                        if (w.getId() == wId) actualWorker = w;
                    }
                    actualCell = handler.getCell(actualWorker.getCurrentPosition());
                    actualHeight = actualCell.getHeight();
                    newHeight = handler.getCell(c).getHeight();
                    if (newHeight - actualHeight > 1) neighbouringCoords.get(c).remove(wId);
                }
            }
            else neighbouringCoords.remove(c);
        }
        return neighbouringCoords;
    }

    public HashMap<Coord, ArrayList<Integer>> findAvailablePositionsToBuildBlock(CellsHandler handler, Worker[] workers) {
        HashMap<Coord, ArrayList<Integer>> neighbouringCoords = handler.findNeighbouringCells(workers);
        for (Coord c : neighbouringCoords.keySet()) {
            if (handler.getCell(c).getOccupiedByWorker() || handler.getCell(c).getOccupiedByDome()) {
                neighbouringCoords.remove(c);
            }
        }
        return neighbouringCoords;
    }

    public HashMap<Coord, ArrayList<Integer>> findAvailablePositionsToBuildDome(CellsHandler handler, Worker[] workers) {
        HashMap<Coord, ArrayList<Integer>> neighbouringCoords = handler.findNeighbouringCells(workers);
        for (Coord c : neighbouringCoords.keySet()) {
            if (handler.getCell(c).getOccupiedByWorker() || handler.getCell(c).getOccupiedByDome() || handler.getCell(c).getHeight() != 3) {
                neighbouringCoords.remove(c);
            }
        }
        return neighbouringCoords;
    }

    public void print() {
        System.out.println("    Godname : " + super.getGodName());
        System.out.println("    Description : " + super.getDescription());
        System.out.println("    Power : " + super.getPower());
    }
}
