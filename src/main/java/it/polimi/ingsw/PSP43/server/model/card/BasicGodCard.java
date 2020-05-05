package it.polimi.ingsw.PSP43.server.model.card;

import it.polimi.ingsw.PSP43.server.DataToAction;
import it.polimi.ingsw.PSP43.server.model.*;
import it.polimi.ingsw.PSP43.server.model.card.behaviours.BuildBlockBehaviour;
import it.polimi.ingsw.PSP43.server.model.card.behaviours.MoveBehavior;
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
public class BasicGodCard extends AbstractGodCard {
    private MoveBehavior moveBehavior;
    private BuildBlockBehaviour buildBlockBehaviour;

    public BasicGodCard() {
    }

    /**
     * Non-default constructor, it initializes the card with the name of the god and its description.
     * @param godName name of the god whose power has been chosen by the player
     * @param description description of the ability of the god
     */
    public BasicGodCard(String godName, String description, String power, MoveBehavior moveBehaviour, BuildBlockBehaviour buildBlockBehaviour) {
        super(godName, description, power);
        this.moveBehavior = moveBehaviour;
        this.buildBlockBehaviour = buildBlockBehaviour;
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

    public MoveBehavior getMoveBehavior() {
        return moveBehavior;
    }

    public void setMoveBehavior(MoveBehavior moveBehavior) {
        this.moveBehavior = moveBehavior;
    }

    public BuildBlockBehaviour getBuildBlockBehaviour() {
        return buildBlockBehaviour;
    }

    public void setBuildBlockBehaviour(BuildBlockBehaviour buildBlockBehaviour) {
        this.buildBlockBehaviour = buildBlockBehaviour;
    }

    public void move(DataToAction dataToAction) throws IOException, ClassNotFoundException, WinnerCaughtException, InterruptedException {
        if (moveBehavior == null) {
            super.move(dataToAction);
        }
        else moveBehavior.handleMove(dataToAction);
    }

    public void buildBlock(DataToAction dataToAction) throws IOException, ClassNotFoundException, InterruptedException {
        if (buildBlockBehaviour == null) {
            super.buildBlock(dataToAction);
        }
        else buildBlockBehaviour.handleBuildBlock(dataToAction);
    }

    public void buildDome(DataToAction dataToAction) {
        super.buildDome(dataToAction);
    }

    public HashMap<Coord, ArrayList<Coord>> findAvailablePositionsToMove(CellsHandler handler, ArrayList<Worker> workers) {
        return super.findAvailablePositionsToMove(handler, workers);
    }

    public HashMap<Coord, ArrayList<Coord>> findAvailablePositionsToBuildBlock(CellsHandler handler, ArrayList<Worker> workers) {
        return super.findAvailablePositionsToBuildBlock(handler, workers);
    }

    public HashMap<Coord, ArrayList<Coord>> findAvailablePositionsToBuildDome(CellsHandler handler, ArrayList<Worker> workers) {
        return super.findAvailablePositionsToBuildDome(handler, workers);
    }

    public AbstractGodCard cleanFromEffects(String nameOfEffect) throws ClassNotFoundException {
        AbstractGodCard newCard;
        return newCard = new BasicGodCard(super.getGodName(), super.getDescription(), super.getPower(), moveBehavior, buildBlockBehaviour);
    }
}