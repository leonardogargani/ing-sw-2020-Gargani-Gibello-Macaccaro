package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.client.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.client.networkMessages.ResponseMessage;
import it.polimi.ingsw.PSP43.server.DataToAction;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlers.PlayersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlers.WorkersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;
import it.polimi.ingsw.PSP43.server.networkMessages.ActionRequest;
import it.polimi.ingsw.PSP43.server.networkMessages.RequestMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class BuildState extends TurnState {
    private static final int FIRSTPOSITION = 0;

    public BuildState(GameSession gameSession) {
        super(gameSession);
    }

    public void initState() throws IOException, ClassNotFoundException, WinnerCaughtException, InterruptedException {
        super.initState();
    }

    public void executeState() throws IOException, ClassNotFoundException, WinnerCaughtException, InterruptedException {
        GameSession game = super.getGameSession();
        PlayersHandler playersHandler = game.getPlayersHandler();
        WorkersHandler workersHandler = game.getWorkersHandler();
        Player currentPlayer = game.getCurrentPlayer();
        AbstractGodCard playerCard = currentPlayer.getAbstractGodCard();
        String nicknameCurrentPlayer = currentPlayer.getNickname();


        int[] workerIds = currentPlayer.getWorkersIdsArray();
        ArrayList<Worker> workersOfPlayer = new ArrayList<>();
        for (int id : workerIds) {
            if (workersHandler.getWorker(id).isLatestMoved())
                workersOfPlayer.add(workersHandler.getWorker(id));
        }
        HashMap<Coord, ArrayList<Coord>> availablePositionsBuildBlock = playerCard.findAvailablePositionsToBuildBlock(game.getCellsHandler(), workersOfPlayer);
        HashMap<Coord, ArrayList<Coord>> availablePositionsBuildDome = playerCard.findAvailablePositionsToBuildDome(game.getCellsHandler(), workersOfPlayer);

        boolean buildDome = false;
        ResponseMessage response = null;

        if (availablePositionsBuildDome.size() != 0) {
            RequestMessage request = new RequestMessage("Do you want to build a dome? Otherwise you will " +
                    "select a cell where to build a block.");
            do {
                try {
                    response = game.sendRequest(request, nicknameCurrentPlayer, new ResponseMessage());
                } catch (GameEndedException e) {
                    game.setActive();
                    return;
                }
            } while (response == null);
        }


        ActionResponse actionResponse = null;
        ActionRequest message;
        if (response != null && response.isResponse()) {
            message = new ActionRequest("Choose where to build a dome.", availablePositionsBuildDome);
        } else {
            message = new ActionRequest("Choose where to build.", availablePositionsBuildBlock);
        }
        do {
            try {
                actionResponse = game.sendRequest(message, nicknameCurrentPlayer, new ActionResponse());
            } catch (GameEndedException e) {
                game.setActive();
                return;
            }
        } while (actionResponse == null);

        Coord coordToBuild = actionResponse.getPosition();
        Worker workerToBuild = workersHandler.getWorker(actionResponse.getWorkerPosition());
        // TODO : Is the worker necessary in the buildBlock method???
        if (response != null && response.isResponse()) playerCard.buildDome(new DataToAction(game, currentPlayer, workerToBuild, coordToBuild));
        else playerCard.buildBlock(new DataToAction(game, currentPlayer, workerToBuild, coordToBuild));
        findNextState();
    }

    public void findNextState() throws IOException, ClassNotFoundException, WinnerCaughtException, InterruptedException {
        GameSession game = super.getGameSession();
        TurnState currentState = game.getCurrentState();
        int indexCurrentState = game.getTurnMap().indexOf(currentState);
        TurnState nextState = game.getTurnMap().get(indexCurrentState - 1);
        game.setNextState(nextState);
        game.transitToNextState();
    }
}
