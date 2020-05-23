package it.polimi.ingsw.PSP43.server.model.card.behaviours.moveBehaviours;

import it.polimi.ingsw.PSP43.client.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.client.networkMessages.ResponseMessage;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.*;
import it.polimi.ingsw.PSP43.server.modelHandlers.CellsHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.networkMessages.ActionRequest;
import it.polimi.ingsw.PSP43.server.networkMessages.RequestMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ForceToOppSideBehaviour extends BasicMoveBehaviour {
    private static final long serialVersionUID = -8412302139395339178L;


    public void initMove(GameSession gameSession) throws GameEndedException {
        Player currentPlayer = gameSession.getCurrentPlayer();

        Coord coordForcingWorker = askIfWantToForce(gameSession);
        Worker forcingWorker = gameSession.getWorkersHandler().getWorker(coordForcingWorker);

        if (coordForcingWorker == null) initMove(gameSession);
        else {
            HashMap<Coord, ArrayList<Coord>> availablePositionsToMove = findAvailablePositionsToMove(gameSession);
            availablePositionsToMove.keySet().removeIf(keyCoord -> !(keyCoord.equals(coordForcingWorker)));

            ActionResponse actionResponse = askForMove(gameSession, availablePositionsToMove);
            Coord coordChosenToMove = actionResponse.getPosition();

            move(new DataToMove(gameSession, currentPlayer, forcingWorker, coordChosenToMove));
        }
    }

    private Coord askIfWantToForce(GameSession gameSession) throws GameEndedException {
        Player currentPlayer = gameSession.getCurrentPlayer();
        CellsHandler cellsHandler = gameSession.getCellsHandler();

        HashMap<Coord, ArrayList<Coord>> neighbouringWorkersCoords = cellsHandler.findWorkersNeighbouringCoords(currentPlayer);
        selectPositionsWorkersToForce(cellsHandler, neighbouringWorkersCoords);

        if (neighbouringWorkersCoords.size() != 0) {
            RequestMessage actionRequest = new RequestMessage("Do you want to force an opponent" +
                    "to the space directly on the other side of your Worker? (Remember you will " +
                    "have to use that Worker during your move and build of this turn).");
            ResponseMessage actionResponse = gameSession.sendRequest(actionRequest,
                    currentPlayer.getNickname(), ResponseMessage.class);

            if (actionResponse.isResponse())
                return askWhereToForce(gameSession, neighbouringWorkersCoords);
            else return null;
        }

        return null;
    }

    private void selectPositionsWorkersToForce(CellsHandler cellsHandler, HashMap<Coord, ArrayList<Coord>> neighbouringWorkersCoords) {
        for (Iterator<Coord> coordKeyIterator = neighbouringWorkersCoords.keySet().iterator(); coordKeyIterator.hasNext(); ) {
            Coord coordForcer = coordKeyIterator.next();
            ArrayList<Coord> actualKeyValues = neighbouringWorkersCoords.get(coordForcer);

            for (Iterator<Coord> coordIterator = actualKeyValues.iterator(); coordIterator.hasNext(); ) {
                Coord coordForced = coordIterator.next();
                Cell cell = cellsHandler.getCell(coordForced);

                if (!(cell.getOccupiedByWorker())) {
                    coordIterator.remove();
                }
                else {
                    if (!(cellsHandler.isOppositeCoordFree(coordForcer, coordForced))) {
                        coordIterator.remove();
                    }
                }
            }

            if (actualKeyValues.size() == 0) coordKeyIterator.remove();
        }
    }

    private Coord askWhereToForce(GameSession gameSession, HashMap<Coord, ArrayList<Coord>> availableWorkersToForce) throws GameEndedException {
        ActionRequest actionRequest = new ActionRequest("Choose a worker to force in another position.", availableWorkersToForce);
        ActionResponse actionResponse = gameSession.sendRequest(actionRequest, gameSession.getCurrentPlayer().getNickname(), ActionResponse.class);

        Coord coordWorkerForcer = actionResponse.getWorkerPosition();
        Coord coordWorkerForced = actionResponse.getPosition();

        Coord coordToForce = gameSession.getCellsHandler().findOppositeCoordFree(coordWorkerForcer, coordWorkerForced);

        Worker workerForced = gameSession.getWorkersHandler().getWorker(coordWorkerForced);
        gameSession.getWorkersHandler().changePosition(workerForced, coordToForce);

        return coordWorkerForcer;
    }
}
