package it.polimi.ingsw.PSP43.server.controllers.behaviours.moveBehaviours;

import it.polimi.ingsw.PSP43.client.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.client.networkMessages.ResponseMessage;
import it.polimi.ingsw.PSP43.server.model.DataToBuild;
import it.polimi.ingsw.PSP43.server.model.DataToMove;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.controllers.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlers.CardsHandler;
import it.polimi.ingsw.PSP43.server.modelHandlers.CellsHandler;
import it.polimi.ingsw.PSP43.server.modelHandlers.WorkersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameLostException;
import it.polimi.ingsw.PSP43.server.networkMessages.RequestMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This class is made to give run-time the possibility to a card
 * to build a block before moving.
 */
public class BuildBeforeMoveBehaviour extends BasicMoveBehaviour {
    private static final long serialVersionUID = 2762718994928009618L;

    /**
     * This method handles the interaction with the client, asking him if does he want to
     * build a block before moving, if the moves does not imply for the worker to move up
     * to an upper level.
     *
     * @param gameSession The necessary data to do the move made by the player.
     */
    public void handleInitMove(GameSession gameSession) throws GameEndedException, GameLostException {
        Player currentPlayer = gameSession.getCurrentPlayer();

        WorkersHandler workersHandler = gameSession.getWorkersHandler();

        ActionResponse actionResponse = askForMove(gameSession);

        Worker workerMoved = workersHandler.getWorker(actionResponse.getWorkerPosition());
        Coord nextCoordChosen = actionResponse.getPosition();
        DataToMove dataToMove = new DataToMove(gameSession, currentPlayer, workerMoved, nextCoordChosen);
        super.move(dataToMove);

        CellsHandler cellsHandler = gameSession.getCellsHandler();
        Cell nextCellChosen = cellsHandler.getCell(nextCoordChosen);
        Cell currentCell = cellsHandler.getCell(workerMoved.getPreviousPosition());

        if (nextCellChosen.getHeight() - currentCell.getHeight() == 0) {
            RequestMessage requestMessage = new RequestMessage("Do you want to build two times " +
                    "considering that your worker didn't rise to a higher level?");
            ResponseMessage responseMessage = gameSession.sendRequest(requestMessage, currentPlayer.getNickname(), ResponseMessage.class);

            if (responseMessage.isResponse()) {
                buildBeforeMove(dataToMove);
            }
        }
    }

    /**
     * This method is called if the player decides that he wants to build a block before moving
     * and it computes all the cells where it's possible to build, asks to the player the cell
     * where to build and calls the build method.
     *
     * @param oldData The data used to recognise which worker is going to move (and so has the
     *                right to build).
     */
    private void buildBeforeMove(DataToMove oldData) throws GameEndedException {
        GameSession gameSession = oldData.getGameSession();
        Player currentPlayer = gameSession.getCurrentPlayer();
        CardsHandler cardsHandler = gameSession.getCardsHandler();

        HashMap<Coord, ArrayList<Coord>> hashMapPositionsToBuildBlock = findAvailablePositionsToBuildBlock(gameSession, oldData);
        HashMap<Coord, ArrayList<Coord>> hashMapPositionsToBuildDome = findAvailablePositionsToBuildDome(gameSession, oldData);

        ResponseMessage responseMessage = new ResponseMessage(false);
        if (hashMapPositionsToBuildDome.size() != 0) {
            RequestMessage requestMessage = new RequestMessage("Do you want to build a dome? Otherwise you will build a block.");
            responseMessage = gameSession.sendRequest(requestMessage, gameSession.getCurrentPlayer().getNickname(), ResponseMessage.class);
        }

        String message;
        if (responseMessage.isResponse()) {
            message = "Choose a position where to build a dome.";
            AbstractGodCard currentCard = cardsHandler.getPlayerCard(currentPlayer.getNickname());
            ActionResponse actionResponse = currentCard.askForBuild(gameSession, hashMapPositionsToBuildDome, message);
            build(new DataToBuild(gameSession, gameSession.getCurrentPlayer(), oldData.getWorker(), actionResponse.getPosition(), Boolean.TRUE));
        }
        else {
            message = "Choose a position where to build a block.";
            AbstractGodCard currentCard = cardsHandler.getPlayerCard(currentPlayer.getNickname());
            ActionResponse actionResponse = currentCard.askForBuild(gameSession, hashMapPositionsToBuildBlock, message);
            build(new DataToBuild(gameSession, gameSession.getCurrentPlayer(), oldData.getWorker(), actionResponse.getPosition(), Boolean.FALSE));
        }
    }

    public HashMap<Coord, ArrayList<Coord>> findAvailablePositionsToBuildBlock(GameSession gameSession, DataToMove oldData) {
        HashMap<Coord, ArrayList<Coord>> availablePositionsBuildBlock = super.findAvailablePositionsToBuildBlock(gameSession);

        return filterPositions(availablePositionsBuildBlock, gameSession, oldData);
    }

    public HashMap<Coord, ArrayList<Coord>> findAvailablePositionsToBuildDome(GameSession gameSession, DataToMove oldData) {
        HashMap<Coord, ArrayList<Coord>> availablePositionsBuildDome = super.findAvailablePositionsToBuildDome(gameSession);

        return filterPositions(availablePositionsBuildDome, gameSession, oldData);
    }

    private HashMap<Coord, ArrayList<Coord>> filterPositions(HashMap<Coord, ArrayList<Coord>> positionsToFilter, GameSession gameSession, DataToMove oldData) {
        Worker workerAllowedToBuild = oldData.getWorker();

        Iterator<Map.Entry<Coord,ArrayList<Coord>>> iter = positionsToFilter.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Coord,ArrayList<Coord>> currentEntry = iter.next();
            if (!(currentEntry.getKey().equals(workerAllowedToBuild.getCurrentPosition()))) {
                iter.remove();
            }

            ArrayList<Coord> neighbouringPositions = currentEntry.getValue();

            ArrayList<Coord> availableNeighbouringPositions = gameSession.getCellsHandler().selectAllFreeCoords(neighbouringPositions);
            availableNeighbouringPositions.removeIf(currentCoord ->
                    (currentCoord.getY() == workerAllowedToBuild.getCurrentPosition().getY() && currentCoord.getX() == workerAllowedToBuild.getCurrentPosition().getX()) ||
                    currentCoord.getY() == workerAllowedToBuild.getPreviousPosition().getY() && currentCoord.getX() == workerAllowedToBuild.getPreviousPosition().getX());

            currentEntry.setValue(availableNeighbouringPositions);
        }

        return positionsToFilter;
    }
}
