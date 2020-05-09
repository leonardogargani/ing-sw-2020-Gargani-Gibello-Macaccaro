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

        HashMap<Coord, ArrayList<Coord>> availablePositions;

        RequestMessage request = new RequestMessage("Do you want to build a dome or a block?");
        ResponseMessage response;
        do {
            response = game.sendRequest(request, nicknameCurrentPlayer, ResponseMessage.class);
        } while (response == null);

        boolean buildBlock = response.isResponse();

        int[] workerIds = currentPlayer.getWorkersIdsArray();
        ArrayList<Worker> workersOfPlayer = new ArrayList<>();
        for (int id : workerIds) {
            if (workersHandler.getWorker(id).isLatestMoved())
                workersOfPlayer.add(workersHandler.getWorker(id));
        }
        if (buildBlock) availablePositions = playerCard.findAvailablePositionsToBuildBlock(game.getCellsHandler(), workersOfPlayer);
        else availablePositions = playerCard.findAvailablePositionsToBuildDome(game.getCellsHandler(), workersOfPlayer);

        ActionRequest message = new ActionRequest("Choose where to build.", availablePositions);
        ActionResponse actionResponse;
        do {
            actionResponse = game.sendRequest(message, nicknameCurrentPlayer, ActionResponse.class);
        } while (actionResponse == null);

        Coord coordToBuild = actionResponse.getPosition();
        Worker workerToBuild = workersHandler.getWorker(actionResponse.getWorkerPosition());
        // TODO : Is the worker necessary in the buildBlock method???
        if (buildBlock) playerCard.buildBlock(new DataToAction(game, currentPlayer, workerToBuild, coordToBuild));
        else playerCard.buildDome(new DataToAction(game, currentPlayer, workerToBuild, coordToBuild));
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
