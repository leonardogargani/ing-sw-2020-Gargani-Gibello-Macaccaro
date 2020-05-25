package it.polimi.ingsw.PSP43.server.controllers;

import it.polimi.ingsw.PSP43.client.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.server.model.DataToBuild;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.*;
import it.polimi.ingsw.PSP43.server.controllers.behaviours.buildBehaviours.BasicBuildBehaviour;
import it.polimi.ingsw.PSP43.server.controllers.behaviours.moveBehaviours.BasicMoveBehaviour;
import it.polimi.ingsw.PSP43.server.modelHandlers.CellsHandler;
import it.polimi.ingsw.PSP43.server.modelHandlers.WorkersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameLostException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * AbstractGodCard is the class that represents the cards with God Powers, thus it is associated to a God and the
 * corresponding description of its power.
 * Each player owns a single card for the entire duration of the game.
 */
public class BasicGodCard extends AbstractGodCard {
    private static final long serialVersionUID = 6236626696645439491L;

    private BasicMoveBehaviour moveBehaviour;
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
        this.moveBehaviour = moveBehaviour;
        this.buildBehaviour = buildBehaviour;
    }

    public AbstractGodCard cleanFromEffects(String nameOfEffect) {
        return new BasicGodCard(super.getGodName(), super.getDescription(), super.getPower(), moveBehaviour, buildBehaviour);
    }

    public void initMove(GameSession gameSession) throws GameEndedException, GameLostException {
        moveBehaviour.handleInitMove(gameSession);
    }

    public void initBuild(GameSession gameSession) throws GameEndedException {
        buildBehaviour.handleInitBuild(gameSession);
    }

    public ActionResponse askForMove(GameSession gameSession) throws GameEndedException, GameLostException {
        return moveBehaviour.askForMove(gameSession);
    }

    public ActionResponse askForMove(GameSession gameSession, HashMap<Coord, ArrayList<Coord>> availablePositions) throws GameEndedException {
        return moveBehaviour.askForMove(gameSession, availablePositions);
    }

    public DataToBuild genericAskForBuild(GameSession gameSession) throws GameEndedException {
        return buildBehaviour.genericAskForBuild(gameSession);
    }

    public ActionResponse askForBuild(GameSession gameSession, HashMap<Coord, ArrayList<Coord>> availablePositionsBuildBlock, String message) throws GameEndedException {
        return buildBehaviour.askForBuild(gameSession, availablePositionsBuildBlock, message);
    }

    public boolean checkConditionsToWin(GameSession gameSession) {
        WorkersHandler workersHandler = gameSession.getWorkersHandler();
        CellsHandler cellsHandler = gameSession.getCellsHandler();

        Integer[] wIds = gameSession.getCurrentPlayer().getWorkersIdsArray();
        for (Integer i : wIds) {
            Worker worker = workersHandler.getWorker(i);
            if (worker.isLatestMoved()) {

                Coord workerCoordUpdated = worker.getCurrentPosition();
                Cell workerCellUpdated = cellsHandler.getCell(workerCoordUpdated);

                return workerCellUpdated.getHeight() == 3;
            }
        }
        return false;
    }
}