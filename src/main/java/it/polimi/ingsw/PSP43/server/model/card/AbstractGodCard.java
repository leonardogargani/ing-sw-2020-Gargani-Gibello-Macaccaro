package it.polimi.ingsw.PSP43.server.model.card;


import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.GameSession;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.modelHandlersException.CellAlreadyOccupiedExeption;
import it.polimi.ingsw.PSP43.server.modelHandlersException.CellHeightException;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * AbstractGodCard is the class that represents the cards with God Powers, thus it is associated to a God and the
 * corresponding description of its power.
 * Each player owns a single card for the entire duration of the game.
 */
@XmlAccessorType(XmlAccessType.FIELD)
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

    /**
     *
     *
     * @param gameSession The session of the game, from which it is possible to retrieve the ClientListener
     * @param player The player who did the move
     * @param workerToMove The worker that is moving
     * @param newPosition The position in which the player is going to place his worker
     * @throws CellHeightException
     * @throws CellAlreadyOccupiedExeption if it is not possible to place a worker in newPosition because the cell is already occupied by another worker or dome
     * @throws WinnerCaughtException if one of the conditions are reached to win
     */
    public void move(GameSession gameSession, Player player, Worker workerToMove, Coord newPosition) throws CellHeightException, CellAlreadyOccupiedExeption, WinnerCaughtException {}

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
    public void buildBlock(GameSession gameSession, Player player, Worker worker, Coord position) throws CellHeightException, CellAlreadyOccupiedExeption {}

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
    public void buildDome(GameSession gameSession, Player player, Worker worker, Coord position) throws CellHeightException, CellAlreadyOccupiedExeption {}

    public void print() {
        System.out.println("    Godname : " + godName);
        System.out.println("    Description : " + description);
        System.out.println("    Power : " + power);
    }
}
