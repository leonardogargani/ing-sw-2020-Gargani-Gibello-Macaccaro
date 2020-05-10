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

/**
 * This class is made to give run-time the possibility to a card
 * to build a block before moving.
 */
public class BuildBeforeMoveBehaviour extends AbstractGodCard implements MoveBehavior {
    private static final long serialVersionUID = 2762718994928009618L;

    /**
     * This method handles the interaction with the client, asking him if does he want to
     * build a block before moving, if the moves does not imply for the worker to move up
     * to an upper level.
     * @param dataToAction The necessary data to do the move made by the player.
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws WinnerCaughtException
     * @throws InterruptedException
     */
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

    /**
     * This method is called if the player decides that he wants to build a block before moving
     * and it computes all the cells where it's possible to build, asks to the player the cell
     * where to build and calls the build method.
     * @param oldData The data used to recognise which worker is going to move (and so has the
     *                right to build).
     * @throws InterruptedException
     * @throws IOException
     * @throws ClassNotFoundException
     */
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
