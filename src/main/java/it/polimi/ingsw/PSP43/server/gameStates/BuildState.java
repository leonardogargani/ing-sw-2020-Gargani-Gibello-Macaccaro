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
import it.polimi.ingsw.PSP43.server.networkMessages.PossibleBuildsMessage;
import it.polimi.ingsw.PSP43.server.networkMessages.RequestMessage;
import it.polimi.ingsw.PSP43.server.networkMessages.TextMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class BuildState extends TurnState {
    private static final int FIRSTPOSITION = 0;

    public BuildState(GameSession gameSession) {
        super(gameSession);
    }

    public void initState() throws IOException, ClassNotFoundException, WinnerCaughtException {
        super.initState();
        GameSession game = super.getGameSession();
        PlayersHandler playersHandler = game.getPlayersHandler();
        game.setCurrentPlayer(playersHandler.getPlayer(FIRSTPOSITION));

        TextMessage broadcastMessage = new TextMessage(game.getCurrentPlayer().getNickname() + " is making his build.");
        ArrayList<String> nicksExcluded = new ArrayList<>();
        nicksExcluded.add(game.getCurrentPlayer().getNickname());
        game.sendBroadCast(broadcastMessage, nicksExcluded);

        executeState();
    }

    public void executeState() throws IOException, ClassNotFoundException, WinnerCaughtException {
        GameSession game = super.getGameSession();
        PlayersHandler playersHandler = game.getPlayersHandler();
        WorkersHandler workersHandler = game.getWorkersHandler();
        Player currentPlayer = game.getCurrentPlayer();
        AbstractGodCard playerCard = currentPlayer.getAbstractGodCard();
        String nicknameCurrentPlayer = currentPlayer.getNickname();

        HashMap<Coord, ArrayList<Coord>> availablePositions;

        RequestMessage request = new RequestMessage("Do you want to build a dome or a block?");
        ResponseMessage response = null;
        boolean delivered;
        do {
            delivered = game.sendRequest(request, nicknameCurrentPlayer, response);
        } while (!delivered);

        boolean buildBlock = response.isResponse();

        int[] workerIds = currentPlayer.getWorkersIdsArray();
        ArrayList<Worker> workersOfPlayer = new ArrayList<>();
        for (int id : workerIds) {
            workersOfPlayer.add(workersHandler.getWorker(id));
        }
        if (buildBlock) availablePositions = playerCard.findAvailablePositionsToBuildBlock(game.getCellsHandler(), (Worker[]) workersOfPlayer.toArray());
        else availablePositions = playerCard.findAvailablePositionsToBuildDome(game.getCellsHandler(), (Worker[]) workersOfPlayer.toArray());

        PossibleBuildsMessage message = new PossibleBuildsMessage("Choose where to build.", availablePositions);
        ActionResponse actionResponse = null;
        do {
            delivered = game.sendRequest(request, nicknameCurrentPlayer, actionResponse);
        } while (!delivered);

        Coord coordToBuild = actionResponse.getPosition();
        Worker workerToBuild = workersHandler.getWorker(actionResponse.getWorkerPosition());
        // TODO : Is the worker necessary in the buildBlock method???
        if (buildBlock) playerCard.buildBlock(new DataToAction(game, currentPlayer, workerToBuild, coordToBuild));
        else playerCard.buildDome(new DataToAction(game, currentPlayer, workerToBuild, coordToBuild));
        findNextState();
    }

    public void findNextState() throws IOException, ClassNotFoundException, WinnerCaughtException {
        GameSession game = super.getGameSession();
        Player currentPlayer = game.getCurrentPlayer();
        PlayersHandler handler = game.getPlayersHandler();
        game.setCurrentPlayer(handler.getNextPlayer(currentPlayer.getNickname()));
        TurnState currentState = game.getCurrentState();
        int indexCurrentState = game.getTurnMap().indexOf(currentState);
        TurnState nextState = game.getTurnMap().get(indexCurrentState - 1);
        game.setNextState(nextState);
        game.transitToNextState();
    }
}
