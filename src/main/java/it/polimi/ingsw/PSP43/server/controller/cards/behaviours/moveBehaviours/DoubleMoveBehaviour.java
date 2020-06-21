package it.polimi.ingsw.PSP43.server.controller.cards.behaviours.moveBehaviours;

import it.polimi.ingsw.PSP43.client.network.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.client.network.networkMessages.ResponseMessage;
import it.polimi.ingsw.PSP43.server.model.DataToMove;
import it.polimi.ingsw.PSP43.server.controller.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.controller.cards.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.CardsHandler;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.WorkersHandler;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException.GameLostException;
import it.polimi.ingsw.PSP43.server.network.networkMessages.RequestMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This class is made to give run-time the possibility to a card
 * to move twice a worker, but not back to its initial position.
 */
public class DoubleMoveBehaviour extends BasicMoveBehaviour {
    private static final long serialVersionUID = -6730459810903042771L;

    /**
     * This method handles the move of the player's worker and gives him to move it twice, but not back to the previous position.
     * @param gameSession This is a reference to the main access to the game database.
     */
    public void handleInitMove(GameSession gameSession) throws GameEndedException, GameLostException {
        Player currentPlayer = gameSession.getCurrentPlayer();
        WorkersHandler workersHandler = gameSession.getWorkersHandler();

        ActionResponse response = askForMove(gameSession);

        Coord nextPositionChosen = response.getPosition();
        Worker workerMoved = workersHandler.getWorker(response.getWorkerPosition());

        this.move(new DataToMove(gameSession, gameSession.getCurrentPlayer(), workerMoved, nextPositionChosen));

        RequestMessage requestMessage = new RequestMessage("Do you want to move another time?");
        ResponseMessage responseMessage = gameSession.sendRequest(requestMessage, currentPlayer.getNickname(), ResponseMessage.class);

        if (responseMessage.isResponse()) {
            HashMap<Coord, ArrayList<Coord>> availablePositionsNextMove =
                    findAvailablePositionsToMove(gameSession, workerMoved.getPreviousPosition(), workerMoved.getCurrentPosition());

            response = askForMove(gameSession, availablePositionsNextMove);

            nextPositionChosen = response.getPosition();
            this.move(new DataToMove(gameSession, gameSession.getCurrentPlayer(), workerMoved, nextPositionChosen));
        }
    }

    /**
     * This method finds all the positions in which the workers passed as parameter can move for the second time, excluding
     * the previous position.
     * @param gameSession This is a reference to the main access to the game database.
     * @param coordToExclude The coordinates excluded from the possible ones in which the workers can move.
     * @return The HashMap that contains as a key value the coordinates of the current position of the worker and as values
     * all the coordinates in which the worker can move the second time.
     */
    public HashMap<Coord, ArrayList<Coord>> findAvailablePositionsToMove(GameSession gameSession, Coord coordToExclude, Coord coordWorkerMoved) {
        CardsHandler cardsHandler = gameSession.getCardsHandler();
        AbstractGodCard abstractGodCard = cardsHandler.getPlayerCard(gameSession.getCurrentPlayer().getNickname());

        HashMap<Coord, ArrayList<Coord>> availablePositions =  abstractGodCard.findAvailablePositionsToMove(gameSession);
        for (Coord c : availablePositions.keySet()) {
            availablePositions.get(c).removeIf(c1
                    -> c1.getY() == coordToExclude.getY() && c1.getX() == coordToExclude.getX());
        }

        excludeCoordWorkerNotMoved(availablePositions, coordWorkerMoved);

        return availablePositions;
    }

    /**
     * This method is created to support the method findAvailablePositionsToMove(...) of this class to find all the
     * available positions in which the player can move twice, eliminating the positions related to
     * the unmoved worker.
     * @param availablePositions The available positions finded in a basic way by findAvailablePositionsToMove(...).
     * @param coordNotEliminate The coord of the worker that can move in this turn (because it has just been moved).
     */
    private void excludeCoordWorkerNotMoved(HashMap<Coord, ArrayList<Coord>> availablePositions, Coord coordNotEliminate) {
        for (Iterator<Map.Entry<Coord, ArrayList<Coord>>> entryIterator = availablePositions.entrySet().iterator(); entryIterator.hasNext(); ) {
            Map.Entry<Coord, ArrayList<Coord>> currentEntry = entryIterator.next();
            Coord actualWorkerPosition = currentEntry.getKey();

            if (!(actualWorkerPosition.equals(coordNotEliminate)))
                entryIterator.remove();
        }
    }
}
