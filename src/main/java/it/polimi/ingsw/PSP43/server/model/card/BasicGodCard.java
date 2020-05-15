package it.polimi.ingsw.PSP43.server.model.card;

import it.polimi.ingsw.PSP43.client.networkMessages.ClientMessage;
import it.polimi.ingsw.PSP43.server.DataToBuild;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.*;
import it.polimi.ingsw.PSP43.server.model.card.behaviours.buildBehaviours.BasicBuildBehaviour;
import it.polimi.ingsw.PSP43.server.model.card.behaviours.buildBehaviours.BuildBehaviour;
import it.polimi.ingsw.PSP43.server.model.card.behaviours.moveBehaviours.BasicMoveBehaviour;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameEndedException;
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
    private static final long serialVersionUID = 6236626696645439491L;

    private BasicMoveBehaviour moveBehavior;
    private BasicBuildBehaviour buildBehaviour;

    public BasicGodCard() {
    }

    /**
     * Non-default constructor, it initializes the card with the name of the god and its description.
     * @param godName name of the god whose power has been chosen by the player
     * @param description description of the ability of the god
     */
    public BasicGodCard(String godName, String description, String power, BasicMoveBehaviour moveBehaviour, BasicBuildBehaviour buildBehaviour) {
        super(godName, description, power);
        this.moveBehavior = moveBehaviour;
        this.buildBehaviour = buildBehaviour;
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

    public BasicBuildBehaviour getBuildBehaviour() {
        return buildBehaviour;
    }

    public AbstractGodCard cleanFromEffects(String nameOfEffect) {
        return new BasicGodCard(super.getGodName(), super.getDescription(), super.getPower(), moveBehavior, buildBehaviour);
    }

    public void initMove(GameSession gameSession) throws GameEndedException, ClassNotFoundException, WinnerCaughtException, InterruptedException, IOException {
        moveBehavior.handleInitMove(gameSession);
    }

    public void initBuild(GameSession gameSession) throws GameEndedException, IOException, InterruptedException, ClassNotFoundException {
        buildBehaviour.handleInitBuild(gameSession);
    }

    public <T extends ClientMessage> T askForMove(GameSession gameSession) throws GameEndedException {
        return moveBehavior.askForMove(gameSession);
    }

    public <T extends ClientMessage> T askForMove(GameSession gameSession, HashMap<Coord, ArrayList<Coord>> availablePositions) throws GameEndedException {
        return moveBehavior.askForMove(gameSession, availablePositions);
    }

    public DataToBuild genericAskForBuild(GameSession gameSession) throws GameEndedException, InterruptedException, IOException, ClassNotFoundException {
        return buildBehaviour.genericAskForBuild(gameSession);
    }

    public <T extends ClientMessage> T askForBuild(GameSession gameSession, HashMap<Coord, ArrayList<Coord>> availablePositionsBuildBlock, String message) throws GameEndedException, InterruptedException, IOException, ClassNotFoundException {
        return buildBehaviour.askForBuild(gameSession, availablePositionsBuildBlock, message);
    }

    public BasicMoveBehaviour getMoveBehavior() {
        return moveBehavior;
    }


}