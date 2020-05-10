package it.polimi.ingsw.PSP43.server.model.card.behaviours;

import it.polimi.ingsw.PSP43.client.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.client.networkMessages.ResponseMessage;
import it.polimi.ingsw.PSP43.server.DataToAction;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;
import it.polimi.ingsw.PSP43.server.networkMessages.ActionRequest;
import it.polimi.ingsw.PSP43.server.networkMessages.RequestMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BuildBeforeMoveBehaviour extends AbstractGodCard implements MoveBehavior {
    private static final long serialVersionUID = 2762718994928009618L;

    public void handleMove(DataToAction dataToAction) throws IOException, ClassNotFoundException, WinnerCaughtException, InterruptedException {
        GameSession gameSession = dataToAction.getGameSession();
        Player player = dataToAction.getPlayer();
        Worker worker = dataToAction.getWorker();
        Cell oldCell = gameSession.getCellsHandler().getCell(worker.getCurrentPosition());
        Cell newCell = gameSession.getCellsHandler().getCell(dataToAction.getNewPosition());
        if (newCell.getHeight() - oldCell.getHeight() == 0) {
            RequestMessage message = new RequestMessage("Do you want to build before moving?");
            ResponseMessage responseMessage;
            do {
                try {
                    responseMessage = gameSession.sendRequest(message, player.getNickname(), new ResponseMessage());
                } catch (GameEndedException e) {
                    gameSession.setActive();
                    return;
                }
            } while (responseMessage == null);

            boolean response = responseMessage.isResponse();
            if (response) {
                buildBeforeMove(dataToAction);
            }
        }
        super.move(dataToAction);
    }

    private void buildBeforeMove(DataToAction oldData) throws InterruptedException, IOException, ClassNotFoundException {
        GameSession gameSession = oldData.getGameSession();
        Worker workerAllowedToBuild = oldData.getWorker();
        HashMap<Coord, ArrayList<Coord>> hashMapNeighbouringPositions = gameSession.getCellsHandler().findWorkerNeighbouringCoords(workerAllowedToBuild);
        for (Iterator<Map.Entry<Coord, ArrayList<Coord>>> coordsIterator = hashMapNeighbouringPositions.entrySet().iterator(); coordsIterator.hasNext(); ) {
            Map.Entry<Coord, ArrayList<Coord>> currentEntry = coordsIterator.next();
            ArrayList<Coord> neighbouringPositions = currentEntry.getValue();

            ArrayList<Coord> availableNeighbouringPositions = gameSession.getCellsHandler().selectAllFreeCoords(neighbouringPositions);
            availableNeighbouringPositions.removeIf(currentCoord -> (currentCoord.getY() == oldData.getNewPosition().getY() && currentCoord.getX() == oldData.getNewPosition().getX()) ||
                    currentCoord.getY() == workerAllowedToBuild.getCurrentPosition().getY() && currentCoord.getX() == workerAllowedToBuild.getCurrentPosition().getX());

            currentEntry.setValue(availableNeighbouringPositions);
        }

        ActionRequest request = new ActionRequest("Choose a cell where to build.", hashMapNeighbouringPositions);
        ActionResponse actionResponse;
        do {
            try {
                actionResponse = gameSession.sendRequest(request, gameSession.getCurrentPlayer().getNickname(), new ActionResponse());
            } catch (GameEndedException e) {
                gameSession.setActive();
                return;
            }
        } while (actionResponse == null);

        Coord coordToBuild = actionResponse.getPosition();
        DataToAction dataBuild = new DataToAction(gameSession, oldData.getPlayer(), workerAllowedToBuild, coordToBuild);
        super.buildBlock(dataBuild);
    }
}
