package it.polimi.ingsw.PSP43.server.model.card.behaviours.moveBehaviours;

import it.polimi.ingsw.PSP43.client.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.client.networkMessages.ResponseMessage;
import it.polimi.ingsw.PSP43.server.model.DataToMove;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.modelHandlers.WorkersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameLostException;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;
import it.polimi.ingsw.PSP43.server.networkMessages.RequestMessage;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is made to give run-time the possibility to a card
 * to move twice a worker, but not back to its initial position.
 */
public class DoubleMoveBehaviour extends BasicMoveBehaviour {
    private static final long serialVersionUID = -6730459810903042771L;

    /**
     * This method handles the move of the player's worker and gives him to move it twice, but not back to the previous position.
     * @param gameSession This is a reference to the center of the game database.
     * @throws WinnerCaughtException if at the end of the move the worker satisfies one of the winning conditions.
     */
    public void handleInitMove(GameSession gameSession) throws WinnerCaughtException, GameEndedException, GameLostException {
        Player currentPlayer = gameSession.getCurrentPlayer();
        WorkersHandler workersHandler = gameSession.getWorkersHandler();

        ActionResponse response = askForMove(gameSession);

        Coord nextPositionChosen = response.getPosition();
        Worker workerMoved = workersHandler.getWorker(response.getWorkerPosition());

        this.move(new DataToMove(gameSession, gameSession.getCurrentPlayer(), workerMoved, nextPositionChosen));

        RequestMessage requestMessage = new RequestMessage("Do you want to move another time?");
        ResponseMessage responseMessage = gameSession.sendRequest(requestMessage, currentPlayer.getNickname(), ResponseMessage.class);

        if (responseMessage.isResponse()) {
            HashMap<Coord, ArrayList<Coord>> availablePositionsNextMove = findAvailablePositionsToMove(gameSession, workerMoved.getPreviousPosition());

            response = askForMove(gameSession, availablePositionsNextMove);

            nextPositionChosen = response.getPosition();
            this.move(new DataToMove(gameSession, gameSession.getCurrentPlayer(), workerMoved, nextPositionChosen));
        }
    }

    /**
     * This method finds all the positions in which the workers passed as parameter can move for the second time, excluding
     * the previous position.
     * @param gameSession This is a reference to the center of the game database.
     * @param coordToExclude The coordinates excluded from the possible ones in which the workers can move.
     * @return The HashMap that contains as a key value the coordinates of the current position of the worker and as values
     * all the coordinates in which the worker can move the second time.
     */
    public HashMap<Coord, ArrayList<Coord>> findAvailablePositionsToMove(GameSession gameSession, Coord coordToExclude) {
        HashMap<Coord, ArrayList<Coord>> availablePositions =  super.findAvailablePositionsToMove(gameSession);
        for (Coord c : availablePositions.keySet()) {
            availablePositions.get(c).removeIf(c1
                    -> c1.getY() == coordToExclude.getY() && c1.getX() == coordToExclude.getX());
        }

        return availablePositions;
    }
}
