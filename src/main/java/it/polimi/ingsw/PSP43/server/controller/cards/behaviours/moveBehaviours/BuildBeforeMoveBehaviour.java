package it.polimi.ingsw.PSP43.server.controller.cards.behaviours.moveBehaviours;

import it.polimi.ingsw.PSP43.client.network.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.client.network.networkMessages.ResponseMessage;
import it.polimi.ingsw.PSP43.server.model.DataToBuild;
import it.polimi.ingsw.PSP43.server.model.DataToMove;
import it.polimi.ingsw.PSP43.server.controller.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.controller.cards.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.CardsHandler;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.CellsHandler;
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
 * to build a block before moving.
 */
public class BuildBeforeMoveBehaviour extends BasicMoveBehaviour {
    private static final long serialVersionUID = 2762718994928009618L;

    /**
     * This method handles the interaction with the client, asking him if does he want to
     * build a block before moving, if the moves does not imply for the worker to move up
     * to an upper level.
     * @param gameSession The necessary data to do the move made by the player.
     */
    public void handleInitMove(GameSession gameSession) throws GameEndedException, GameLostException {
        Player currentPlayer = gameSession.getCurrentPlayer();

        WorkersHandler workersHandler = gameSession.getWorkersHandler();

        ActionResponse actionResponse = askForMove(gameSession);

        Worker workerMoved = workersHandler.getWorker(actionResponse.getWorkerPosition());
        Coord nextCoordChosen = actionResponse.getPosition();
        DataToMove dataToMove = new DataToMove(gameSession, currentPlayer, workerMoved, nextCoordChosen);
        workerMoved.setLatestMoved(true);

        buildBeforeMove(dataToMove);

        super.move(dataToMove);
    }

    /**
     * This method is called if the player decides that he wants to build a block before moving
     * and it computes all the cells where it's possible to build, asks to the player the cell
     * where to build and calls the build method.
     * @param oldData The data used to recognise which worker is going to move (and so has the
     *                right to build).
     */
    private void buildBeforeMove(DataToMove oldData) throws GameEndedException {
        GameSession gameSession = oldData.getGameSession();
        Player currentPlayer = gameSession.getCurrentPlayer();
        CardsHandler cardsHandler = gameSession.getCardsHandler();

        HashMap<Coord, ArrayList<Coord>> hashMapPositionsToBuildBlock = findAvailablePositionsToBuildBlock(gameSession, oldData);
        HashMap<Coord, ArrayList<Coord>> hashMapPositionsToBuildDome = findAvailablePositionsToBuildDome(gameSession, oldData);

        ResponseMessage responseMessage;
        if (hashMapPositionsToBuildBlock.size() != 0 || hashMapPositionsToBuildDome.size() !=0) {
            CellsHandler cellsHandler = gameSession.getCellsHandler();
            Cell current = cellsHandler.getCell(oldData.getWorker().getCurrentPosition());
            Cell next = cellsHandler.getCell(oldData.getNewPosition());

            if (current.getHeight() - next.getHeight() == 0) {
                RequestMessage requestMessage = new RequestMessage("Do you want to build two times " +
                        "considering that your worker didn't rise to a higher level?");
                responseMessage = gameSession.sendRequest(requestMessage, currentPlayer.getNickname(), ResponseMessage.class);

                if (responseMessage.isResponse()) {
                    if (hashMapPositionsToBuildDome.size() != 0 && hashMapPositionsToBuildBlock.size() !=0) {
                        requestMessage = new RequestMessage("Do you want to build a dome? Otherwise you will build a block.");
                        responseMessage = gameSession.sendRequest(requestMessage, gameSession.getCurrentPlayer().getNickname(), ResponseMessage.class);
                    }
                    else if (hashMapPositionsToBuildBlock.size() == 0) responseMessage = new ResponseMessage(true);
                    else responseMessage = new ResponseMessage(false);

                    String message;
                    if (responseMessage.isResponse()) {
                        message = "Choose a position where to build a dome.";
                        AbstractGodCard currentCard = cardsHandler.getPlayerCard(currentPlayer.getNickname());
                        ActionResponse actionResponse = currentCard.askForBuild(gameSession, hashMapPositionsToBuildDome, message);
                        build(new DataToBuild(gameSession, gameSession.getCurrentPlayer(), oldData.getWorker(), actionResponse.getPosition(), Boolean.TRUE));
                    }
                    else {
                        if (hashMapPositionsToBuildBlock.size() != 0) {
                            message = "Choose a position where to build a block.";
                            AbstractGodCard currentCard = cardsHandler.getPlayerCard(currentPlayer.getNickname());
                            ActionResponse actionResponse = currentCard.askForBuild(gameSession, hashMapPositionsToBuildBlock, message);
                            build(new DataToBuild(gameSession, gameSession.getCurrentPlayer(), oldData.getWorker(), actionResponse.getPosition(), Boolean.FALSE));
                        }
                    }
                }
            }
        }
    }

    /**
     * This method finds all the available positions where the player can build a block before moving his worker.
     * @param gameSession This is a reference to the main access to the game database.
     * @param oldData The data that will be used to change the model after the move done by the player.
     * @return A map where the keys are the coordinates of the workers that can build a block and the values are all the positions where these workers can build.
     */
    public HashMap<Coord, ArrayList<Coord>> findAvailablePositionsToBuildBlock(GameSession gameSession, DataToMove oldData) {
        HashMap<Coord, ArrayList<Coord>> availablePositionsBuildBlock = super.findAvailablePositionsToBuildBlock(gameSession);

        return filterPositions(availablePositionsBuildBlock, gameSession, oldData);
    }

    /**
     * This method finds all the available positions where the player can build a dome before moving his worker.
     * @param gameSession This is a reference to the main access to the game database.
     * @param oldData The data that will be used to change the model after the move done by the player.
     * @return A map where the keys are the coordinates of the workers that can build a dome and the values are all the positions where these workers can build.
     */
    public HashMap<Coord, ArrayList<Coord>> findAvailablePositionsToBuildDome(GameSession gameSession, DataToMove oldData) {
        HashMap<Coord, ArrayList<Coord>> availablePositionsBuildDome = super.findAvailablePositionsToBuildDome(gameSession);

        return filterPositions(availablePositionsBuildDome, gameSession, oldData);
    }

    /**
     * This method filters all the positions found to build a block or a dome if they corresponds to the one in which the worker is moving. It eliminates from the keys the worker that is going to be unmoved.
     * @param positionsToFilter The positions found to build and that has to be filtered.
     * @param gameSession This is a reference to the main access to the game database.
     * @param oldData The data that will be used to change the model after the move done by the player.
     * @return The map of coordinates where to build filtered.
     */
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
                    currentCoord.getY() == oldData.getNewPosition().getY() && currentCoord.getX() == oldData.getNewPosition().getX());

            currentEntry.setValue(availableNeighbouringPositions);

            if (availableNeighbouringPositions.size() == 0) iter.remove();
        }

        return positionsToFilter;
    }
}
