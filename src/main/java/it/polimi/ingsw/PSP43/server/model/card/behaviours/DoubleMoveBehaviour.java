package it.polimi.ingsw.PSP43.server.model.card.behaviours;

import it.polimi.ingsw.PSP43.client.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.client.networkMessages.ResponseMessage;
import it.polimi.ingsw.PSP43.server.DataToAction;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlers.CellsHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;
import it.polimi.ingsw.PSP43.server.networkMessages.ActionRequest;
import it.polimi.ingsw.PSP43.server.networkMessages.RequestMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is made to give run-time the possibility to a card
 * to move twice a worker, but not back to its initial position.
 */
public class DoubleMoveBehaviour extends AbstractGodCard implements MoveBehavior {
    private static final long serialVersionUID = -6730459810903042771L;

    /**
     * This method handles the move of the player's worker and gives him to move it twice, but not back to the previous position.
     * @param dataToAction The data used to compute the move of the worker.
     * @throws WinnerCaughtException if at the end of the move the worker satisfies one of the winning conditions.
     */
    public void handleMove(DataToAction dataToAction) throws IOException, ClassNotFoundException, WinnerCaughtException, InterruptedException {
        super.move(dataToAction);
        RequestMessage requestMessage = new RequestMessage("Do you want to move another time?");
        ResponseMessage responseMessage;
        do {
            try {
                responseMessage = dataToAction.getGameSession().sendRequest(  requestMessage,
                                                                        dataToAction.getPlayer().getNickname(),
                                                                        new ResponseMessage());
            } catch (GameEndedException e) {
                dataToAction.getGameSession().setActive();
                return;
            }
        } while (responseMessage == null);

        boolean response  = responseMessage.isResponse();
        if (response) {
            ArrayList<Worker> workersToMove = new ArrayList<>();
            workersToMove.add(dataToAction.getWorker());

            HashMap<Coord, ArrayList<Coord>> availablePositions = findAvailablePositionsToMove(dataToAction.getGameSession().getCellsHandler(), workersToMove, dataToAction.getWorker().getPreviousPosition());

            ActionRequest request = new ActionRequest("Choose a cell where to move.", availablePositions);
            ActionResponse actionResponse;
            do {
                try {
                    actionResponse = dataToAction.getGameSession().sendRequest(request, dataToAction.getPlayer().getNickname(), new ActionResponse());
                } catch (GameEndedException e) {
                    dataToAction.getGameSession().setActive();
                    return;
                }
            } while (actionResponse == null);

            Coord coordChosen = actionResponse.getPosition();
            super.move(new DataToAction(dataToAction.getGameSession(), dataToAction.getPlayer(), dataToAction.getWorker(), coordChosen));
        }
    }

    /**
     * This method finds all the positions in which the workers passed as parameter can move for the second time, excluding
     * the previous position.
     * @param cellsHandler The handler of the cells in the database of the game.
     * @param workersToMove All the workers to which the method has to find the available cells.
     * @param coordToExclude The coordinates excluded from the possible ones in which the workers can move.
     * @return The HashMap that contains as a key value the coordinates of the current position of the worker and as values
     * all the coordinates in which the worker can move the second time.
     */
    protected HashMap<Coord, ArrayList<Coord>> findAvailablePositionsToMove(CellsHandler cellsHandler, ArrayList<Worker> workersToMove, Coord coordToExclude) {
        HashMap<Coord, ArrayList<Coord>> availablePositions =  super.findAvailablePositionsToMove(cellsHandler, workersToMove);
        for (Coord c : availablePositions.keySet()) {
            availablePositions.get(c).removeIf(c1
                    -> c1.getY() == coordToExclude.getY() && c1.getX() == coordToExclude.getX());
        }

        return availablePositions;
    }
}
