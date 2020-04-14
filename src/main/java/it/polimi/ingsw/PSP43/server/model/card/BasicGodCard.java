package it.polimi.ingsw.PSP43.server.model.card;


import it.polimi.ingsw.PSP43.server.model.*;
import it.polimi.ingsw.PSP43.server.modelHandlers.CellsHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.CellAlreadyOccupiedExeption;
import it.polimi.ingsw.PSP43.server.modelHandlersException.CellHeightException;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

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
     * @throws CellHeightException
     * @throws CellAlreadyOccupiedExeption if it is not possible to place a worker in newPosition because the cell is already occupied by another worker or dome
     */
    public void move(GameSession gameSession, Player player, Worker workerToMove, Coord newPosition) throws CellAlreadyOccupiedExeption, CellHeightException {
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
     * @throws CellHeightException if it is not possible to build anymore on the cell because level three is already reached
     * @throws CellAlreadyOccupiedExeption if it is not possible to build in newPosition because the cell is already occupied by another worker or dome
     */
    public void buildBlock(GameSession gameSession, Player player, Worker worker, Coord position) throws CellHeightException, CellAlreadyOccupiedExeption {
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
     * @throws CellHeightException
     * @throws CellAlreadyOccupiedExeption if it is not possible to build in newPosition because the cell is already occupied by another worker or dome
     */
    public void buildDome(GameSession gameSession, Player player, Worker worker, Coord position) throws CellHeightException, CellAlreadyOccupiedExeption {
        CellsHandler handler = gameSession.getCellsHandler();
        Cell newCell = handler.getCell(position);
        newCell.setOccupiedByDome(true);
        handler.changeStateOfCell(newCell, position);
    }

    public void print() {
        System.out.println("    Godname : " + super.getGodName());
        System.out.println("    Description : " + super.getDescription());
        System.out.println("    Power : " + super.getPower());
    }
}
