package it.polimi.ingsw.PSP43.server.model.card.behaviours.buildBehaviours;

import it.polimi.ingsw.PSP43.client.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.client.networkMessages.ResponseMessage;
import it.polimi.ingsw.PSP43.server.model.DataToBuild;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.modelHandlers.CellsHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.networkMessages.RequestMessage;

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
     * This method checks if the player wants to build another time
     *
     * @param gameSession This is a reference to the center of the game database.
     */
    public void handleInitBuild(GameSession gameSession) throws GameEndedException {
        Player currentPlayer = gameSession.getCurrentPlayer();
        CellsHandler cellsHandler = gameSession.getCellsHandler();
        DataToBuild dataToBuild = genericAskForBuild(gameSession);

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
     *
     * @param oldDataToBuild The data of the previous build, used to check and not to give the possibility to the player
     *                       to build in the same position of the previous one.
     */
    private void buildAnotherTime(DataToBuild oldDataToBuild) throws GameEndedException {
        GameSession game = oldDataToBuild.getGameSession();
        Player currentPlayer = game.getCurrentPlayer();

        HashMap<Coord, ArrayList<Coord>> availablePositionsToBuildBlock = findAvailablePositionsToBuildBlock(game);
        filterAllowedPositions(availablePositionsToBuildBlock, oldDataToBuild);

        HashMap<Coord, ArrayList<Coord>> availablePositionsToBuildDome = findAvailablePositionsToBuildDome(game);
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
            actionResponse = askForBuild(game, availablePositionsToBuildBlock, message);
            build(new DataToBuild(game, currentPlayer, oldDataToBuild.getWorker(), actionResponse.getPosition(), Boolean.TRUE));
        } else {
            if (availablePositionsToBuildBlock.size() != 0) {
                message = "Choose where to build a block.";
                actionResponse = askForBuild(game, availablePositionsToBuildBlock, message);
                build(new DataToBuild(game, currentPlayer, oldDataToBuild.getWorker(), actionResponse.getPosition(), Boolean.FALSE));
            }
        }
    }

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
        }

    }
}