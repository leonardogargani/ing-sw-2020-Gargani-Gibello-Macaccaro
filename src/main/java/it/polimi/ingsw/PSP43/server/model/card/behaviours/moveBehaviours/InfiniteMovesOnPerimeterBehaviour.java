package it.polimi.ingsw.PSP43.server.model.card.behaviours.moveBehaviours;

import it.polimi.ingsw.PSP43.client.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.client.networkMessages.ResponseMessage;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.DataToMove;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.modelHandlers.WorkersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameLostException;
import it.polimi.ingsw.PSP43.server.networkMessages.ActionRequest;
import it.polimi.ingsw.PSP43.server.networkMessages.RequestMessage;

import java.util.ArrayList;
import java.util.HashMap;

public class InfiniteMovesOnPerimeterBehaviour extends BasicMoveBehaviour{
    private static final long serialVersionUID = 1879532628217866328L;

    public void initMove(GameSession gameSession) throws GameEndedException, GameLostException {
        super.initMove(gameSession);
    }

    private void infiniteMoves(GameSession gameSession) throws GameEndedException, GameLostException {
        Player currentPlayer = gameSession.getCurrentPlayer();
        WorkersHandler workersHandler = gameSession.getWorkersHandler();
        Integer[] wIds = gameSession.getCurrentPlayer().getWorkersIdsArray();

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
            RequestMessage requestMessage = new RequestMessage("Do you want to move" +
                    "another time your worker?");
            ResponseMessage responseMessage = gameSession.sendRequest(requestMessage,
                    currentPlayer.getNickname(), ResponseMessage.class);

            if (responseMessage.isResponse()) {
                HashMap<Coord, ArrayList<Coord>> availablePositions = findAvailablePositionsToMove(gameSession);
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