package it.polimi.ingsw.PSP43.server.controller.cards.behaviours.buildBehaviours;

import it.polimi.ingsw.PSP43.client.network.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.client.network.networkMessages.ResponseMessage;
import it.polimi.ingsw.PSP43.server.controller.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.CellsHandler;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.WorkersHandler;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.network.networkMessages.ActionRequest;
import it.polimi.ingsw.PSP43.server.network.networkMessages.RequestMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class RemoveBlockFromNeighbourBehaviour extends BasicBuildBehaviour {
    private static final long serialVersionUID = -3159821896946449486L;

    public void handleInitBuild(GameSession gameSession) throws GameEndedException {
        super.handleInitBuild(gameSession);

        HashMap<Coord, ArrayList<Coord>> availablePositionsToRemove = findPositionsToRemoveBlock(gameSession);

        if (availablePositionsToRemove.keySet().size() != 0) askBlockToRemove(gameSession, availablePositionsToRemove);
    }

    public void askBlockToRemove(GameSession gameSession, HashMap<Coord, ArrayList<Coord>> availablePositions) throws GameEndedException {
        CellsHandler cellsHandler = gameSession.getCellsHandler();

        RequestMessage requestMessage = new RequestMessage("Do you want to remove a block with your unmoved player?");
        ResponseMessage responseMessage = gameSession.sendRequest(requestMessage,
                gameSession.getCurrentPlayer().getNickname(), ResponseMessage.class);

        if (responseMessage.isResponse()) {
            ActionRequest actionRequest = new ActionRequest("Choose a block to remove.",
                    Collections.unmodifiableMap(new HashMap<>(availablePositions)));
            ActionResponse actionResponse = gameSession.sendRequest(actionRequest,
                    gameSession.getCurrentPlayer().getNickname(), ActionResponse.class);

            Coord coordToRemoveBlock = actionResponse.getPosition();
            Cell cellToRemoveBlock = cellsHandler.getCell(coordToRemoveBlock);
            cellToRemoveBlock.setHeight(cellToRemoveBlock.getHeight() - 1);
            cellsHandler.changeStateOfCell(cellToRemoveBlock, coordToRemoveBlock);
        }
    }

    public HashMap<Coord, ArrayList<Coord>> findPositionsToRemoveBlock(GameSession gameSession) {
        WorkersHandler workersHandler = gameSession.getWorkersHandler();
        CellsHandler cellsHandler = gameSession.getCellsHandler();
        HashMap<Coord, ArrayList<Coord>> availablePositions = new HashMap<>();

        Integer[] wIds = gameSession.getCurrentPlayer().getWorkersIdsArray();
        for (Integer i : wIds) {
            Worker worker = workersHandler.getWorker(i);

            if (!(worker.isLatestMoved())) {
                ArrayList<Coord> neighbouringPositions = cellsHandler.findNeighbouringCoords(worker.getCurrentPosition());
                for (Iterator<Coord> coordIterator = neighbouringPositions.iterator(); coordIterator.hasNext(); ) {
                    Cell cell = cellsHandler.getCell(coordIterator.next());

                    if (cell.getOccupiedByDome() || cell.getHeight() == 0 || cell.getOccupiedByWorker()) {
                        coordIterator.remove();
                    }
                }
                if (neighbouringPositions.size() != 0) availablePositions.put(worker.getCurrentPosition(), neighbouringPositions);
            }
        }

        return availablePositions;
    }
}
