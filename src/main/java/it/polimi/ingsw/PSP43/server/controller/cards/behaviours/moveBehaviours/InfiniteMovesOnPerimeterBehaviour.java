package it.polimi.ingsw.PSP43.server.controller.cards.behaviours.moveBehaviours;

import it.polimi.ingsw.PSP43.client.network.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.client.network.networkMessages.ResponseMessage;
import it.polimi.ingsw.PSP43.server.controller.cards.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.controller.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.DataToMove;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.WorkersHandler;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException.GameLostException;
import it.polimi.ingsw.PSP43.server.network.networkMessages.ActionRequest;
import it.polimi.ingsw.PSP43.server.network.networkMessages.RequestMessage;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class implements Triton's behaviour that permits to move workers infinite times when
 * the worker moves on a perimeter position.
 */
public class InfiniteMovesOnPerimeterBehaviour extends BasicMoveBehaviour{
    private static final long serialVersionUID = 1879532628217866328L;

    /**
     * This method handles moves of workers allowing the player to move infinite times his worker
     * when he moves it on a perimeter location.
     * @param gameSession This is a reference to the main access to the game database.
     * @throws GameEndedException if the player decides to leave the game during his turn.
     * @throws GameLostException if the player can't do any move.
     */
    public void handleInitMove(GameSession gameSession) throws GameEndedException, GameLostException {
        super.handleInitMove(gameSession);
        infiniteMoves(gameSession);
    }

    /**
     * This method handles the "infinite" cycle of request to the player, asking him if he wants to move
     * another time after he moved once.
     * @param gameSession This is a reference to the main access to the game database.
     * @throws GameEndedException if the player decides to leave the game during his turn.
     */
    private void infiniteMoves(GameSession gameSession) throws GameEndedException {
        Player currentPlayer = gameSession.getCurrentPlayer();
        WorkersHandler workersHandler = gameSession.getWorkersHandler();
        Integer[] wIds = gameSession.getCurrentPlayer().getWorkersIdsArray();
        AbstractGodCard card = gameSession.getCardsHandler().getPlayerCard(currentPlayer.getNickname());

        Worker workerMoved = null;
        Worker workerNotMoved = null;

        for (Integer i : wIds) {
            Worker worker = workersHandler.getWorker(i);

            if (worker.isLatestMoved()) workerMoved = worker;
            else workerNotMoved = worker;
        }

        assert workerMoved != null;
        assert workerNotMoved != null;

        if (workersHandler.isOnPerimetralCell(workerMoved)) {
            RequestMessage requestMessage = new RequestMessage("Do you want to move " +
                    "another time your worker?");
            ResponseMessage responseMessage = gameSession.sendRequest(requestMessage,
                    currentPlayer.getNickname(), ResponseMessage.class);

            if (responseMessage.isResponse()) {
                HashMap<Coord, ArrayList<Coord>> availablePositions = card.findAvailablePositionsToMove(gameSession);
                availablePositions.remove(workerNotMoved.getCurrentPosition());

                ActionRequest actionRequest = new ActionRequest("Choose a position where to " +
                        "move your worker.", availablePositions);
                ActionResponse actionResponse = gameSession.sendRequest(actionRequest,
                        currentPlayer.getNickname(), ActionResponse.class);

                move(new DataToMove(gameSession, currentPlayer, workerMoved, actionResponse.getPosition()));

                infiniteMoves(gameSession);
            }
        }
    }
}