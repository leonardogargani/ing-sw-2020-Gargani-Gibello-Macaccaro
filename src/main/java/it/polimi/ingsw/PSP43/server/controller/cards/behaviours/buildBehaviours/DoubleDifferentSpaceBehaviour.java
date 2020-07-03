package it.polimi.ingsw.PSP43.server.controller.cards.behaviours.buildBehaviours;

import it.polimi.ingsw.PSP43.client.network.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.client.network.networkMessages.ResponseMessage;
import it.polimi.ingsw.PSP43.server.model.DataToBuild;
import it.polimi.ingsw.PSP43.server.controller.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.controller.cards.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.CardsHandler;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.CellsHandler;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.network.networkMessages.RequestMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This class is made to give run-time the possibility to a card
 * to build another block, but not in the same position.
 */
public class DoubleDifferentSpaceBehaviour extends BasicBuildBehaviour {
    private static final long serialVersionUID = 5472654096820247816L;

    /**
     * This method checks if the player wants to build another time.
     * @param gameSession This is a reference to the main access to the game database.
     */
    public void handleInitBuild(GameSession gameSession) throws GameEndedException {
        Player currentPlayer = gameSession.getCurrentPlayer();
        CellsHandler cellsHandler = gameSession.getCellsHandler();
        DataToBuild dataToBuild = genericAskForBuild(gameSession);
        if (dataToBuild == null) return;

        build(dataToBuild);

        if (cellsHandler.getCell(dataToBuild.getNewPosition()).getHeight() <= 3) {
            RequestMessage requestMessage = new RequestMessage("Do you want to build another time on a different space?");
            ResponseMessage responseMessage = gameSession.sendRequest(requestMessage, currentPlayer.getNickname(), ResponseMessage.class);

            if (responseMessage.isResponse()) {
                buildAnotherTime(dataToBuild);
            }
        }
    }

    /**
     * This method is used to give the possibility to the player to build twice, but not on the same space.
     * @param oldDataToBuild The data of the previous build, used to check and not to give the possibility to the player
     *                       to build in the same position of the previous one.
     */
    private void buildAnotherTime(DataToBuild oldDataToBuild) throws GameEndedException {
        GameSession game = oldDataToBuild.getGameSession();
        Player currentPlayer = game.getCurrentPlayer();
        CardsHandler cardsHandler = game.getCardsHandler();
        AbstractGodCard abstractGodCard = cardsHandler.getPlayerCard(currentPlayer.getNickname());

        HashMap<Coord, ArrayList<Coord>> availablePositionsToBuildBlock = abstractGodCard.findAvailablePositionsToBuildBlock(game);
        filterAllowedPositions(availablePositionsToBuildBlock, oldDataToBuild);

        HashMap<Coord, ArrayList<Coord>> availablePositionsToBuildDome = abstractGodCard.findAvailablePositionsToBuildDome(game);
        filterAllowedPositions(availablePositionsToBuildDome, oldDataToBuild);

        ResponseMessage responseMessage = new ResponseMessage(false);
        if (availablePositionsToBuildDome.size() != 0) {
            RequestMessage requestMessage = new RequestMessage("Do you want to build a dome? Otherwise you will build a block.");
            responseMessage = game.sendRequest(requestMessage, currentPlayer.getNickname(), ResponseMessage.class);
        }

        ActionResponse actionResponse;
        String message;
        if (responseMessage.isResponse()) {
            message = "Choose where to build a dome.";
            actionResponse = askForBuild(game, availablePositionsToBuildDome, message);
            build(new DataToBuild(game, currentPlayer, oldDataToBuild.getWorker(), actionResponse.getPosition(), Boolean.TRUE));
        } else {
            if (availablePositionsToBuildBlock.size() != 0) {
                message = "Choose where to build a block.";
                actionResponse = askForBuild(game, availablePositionsToBuildBlock, message);
                build(new DataToBuild(game, currentPlayer, oldDataToBuild.getWorker(), actionResponse.getPosition(), Boolean.FALSE));
            }
        }
    }

    /**
     * This method is used to filter all the positions available to build the second time. The position that is
     * filtered is the one in which the worker has built the first time.
     * @param availablePositions The available positions in which the worker could build, excluded the ones that will be filtered.
     * @param oldData The data used during the first build.
     */
    protected void filterAllowedPositions(HashMap<Coord, ArrayList<Coord>> availablePositions, DataToBuild oldData) {
        Iterator<Map.Entry<Coord, ArrayList<Coord>>> iter = availablePositions.entrySet().iterator();
        Coord coordWorkerAllowedToBuild = oldData.getWorker().getCurrentPosition();

        while (iter.hasNext()) {
            Map.Entry<Coord, ArrayList<Coord>> currentEntry = iter.next();

            if (!(coordWorkerAllowedToBuild.equals(currentEntry.getKey()))) {
                iter.remove();
            }

            ArrayList<Coord> coordsWhereToBuild = currentEntry.getValue();
            Coord oldCoordBuilt = oldData.getNewPosition();

            coordsWhereToBuild.removeIf(actualCoord -> actualCoord.getY() == oldCoordBuilt.getY() && actualCoord.getX() == oldCoordBuilt.getX());
            if (coordsWhereToBuild.size() == 0) iter.remove();
        }

    }
}