package it.polimi.ingsw.PSP43.server.controller.cards;

import it.polimi.ingsw.PSP43.client.network.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.server.model.DataToBuild;
import it.polimi.ingsw.PSP43.server.controller.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.*;
import it.polimi.ingsw.PSP43.server.controller.cards.behaviours.buildBehaviours.BasicBuildBehaviour;
import it.polimi.ingsw.PSP43.server.controller.cards.behaviours.moveBehaviours.BasicMoveBehaviour;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.CellsHandler;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.PlayersHandler;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.WorkersHandler;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException.GameLostException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
        PlayersHandler playersHandler = gameSession.getPlayersHandler();

        String godName = super.getGodName();
        Map<String, AbstractGodCard> mapPlayerCard = gameSession.getCardsHandler().getMapOwnersCard();
        Player currentPlayer = null;

        for (String s : mapPlayerCard.keySet()) {
            AbstractGodCard abstractGodCard = mapPlayerCard.get(s);
                if (abstractGodCard.getGodName().equals(godName)) {
                currentPlayer = playersHandler.getPlayer(s);
            }
        }

        assert currentPlayer != null;
        Integer[] wIds = currentPlayer.getWorkersIdsArray();

        // here I check if the player has won, taking care of verifying this condition
        // only if the worker was moved on this turn (to avoid the problem about
        // forcing an opponent to a third level space)
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