package it.polimi.ingsw.PSP43.server.controller.cards.behaviours.moveBehaviours;

import it.polimi.ingsw.PSP43.client.network.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.client.network.networkMessages.ResponseMessage;
import it.polimi.ingsw.PSP43.server.controller.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.*;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.CellsHandler;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException.GameLostException;
import it.polimi.ingsw.PSP43.server.network.networkMessages.ActionRequest;
import it.polimi.ingsw.PSP43.server.network.networkMessages.RequestMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class ForceToOppSideBehaviour extends BasicMoveBehaviour {
    private static final long serialVersionUID = -8412302139395339178L;

    public void handleInitMove(GameSession gameSession) throws GameEndedException, GameLostException {
        Player currentPlayer = gameSession.getCurrentPlayer();

        Coord coordForcingWorker = askIfWantToForce(gameSession);

        if (coordForcingWorker == null) super.handleInitMove(gameSession);
        else {
            Worker forcingWorker = gameSession.getWorkersHandler().getWorker(coordForcingWorker);
            HashMap<Coord, ArrayList<Coord>> availablePositionsToMove = findAvailablePositionsToMove(gameSession);
            availablePositionsToMove.keySet().removeIf(keyCoord -> !(keyCoord.equals(coordForcingWorker)));

            ActionResponse actionResponse = askForMove(gameSession, availablePositionsToMove);
            Coord coordChosenToMove = actionResponse.getPosition();

            move(new DataToMove(gameSession, currentPlayer, forcingWorker, coordChosenToMove));
        }
    }

    public Coord askIfWantToForce(GameSession gameSession) throws GameEndedException {
        Player currentPlayer = gameSession.getCurrentPlayer();
        CellsHandler cellsHandler = gameSession.getCellsHandler();

        HashMap<Coord, ArrayList<Coord>> neighbouringCoordsSelected = selectPositionsWorkersToForce(cellsHandler, currentPlayer);
        if (neighbouringCoordsSelected.size() != 0) {
            RequestMessage actionRequest = new RequestMessage("Do you want to force an opponent " +
                    "to the space directly on the other side of your Worker? (Remember you will " +
                    "have to use that Worker during your move and build of this turn).");
            ResponseMessage actionResponse = gameSession.sendRequest(actionRequest,
                    currentPlayer.getNickname(), ResponseMessage.class);

            if (actionResponse.isResponse())
                return askWhereToForce(gameSession, neighbouringCoordsSelected);
            else return null;
        }

        return null;
    }

    public HashMap<Coord, ArrayList<Coord>> selectPositionsWorkersToForce(CellsHandler cellsHandler, Player currentPlayer) {
        HashMap<Coord, ArrayList<Coord>> neighbouringCoordsSelected = cellsHandler.findWorkersNeighbouringCoords(currentPlayer);
        ArrayList<Coord> key = new ArrayList<>();
        for (Coord c : neighbouringCoordsSelected.keySet()) {
            key.add(c.clone());
        }

        for (Iterator<Coord> coordKeyIterator = neighbouringCoordsSelected.keySet().iterator(); coordKeyIterator.hasNext(); ) {
            Coord coordForcer = coordKeyIterator.next();
            ArrayList<Coord> actualKeyValues = neighbouringCoordsSelected.get(coordForcer);

            for (Iterator<Coord> coordIterator = actualKeyValues.iterator(); coordIterator.hasNext(); ) {
                Coord coordForced = coordIterator.next();
                Cell cell = cellsHandler.getCell(coordForced);

                if (!(cell.getOccupiedByWorker())) {
                    coordIterator.remove();
                }
                else {
                    if (!(cellsHandler.isOppositeCoordFree(coordForcer, coordForced)) || key.contains(coordForced)) {
                        coordIterator.remove();
                    }
                }
            }

            if (actualKeyValues.size() == 0) coordKeyIterator.remove();
        }
        return neighbouringCoordsSelected;
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
