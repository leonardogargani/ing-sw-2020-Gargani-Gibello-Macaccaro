package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlers.PlayersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlers.WorkersHandler;

import java.util.ArrayList;
import java.util.HashMap;

public class BuildState extends TurnState {
    private static final int FIRSTPOSITION = 0;

    public BuildState(GameSession gameSession) {
        super(gameSession);
    }

    public void initState() {
        super.initState();
        GameSession game = super.getGameSession();
        PlayersHandler playersHandler = game.getPlayersHandler();
        game.setCurrentPlayer(playersHandler.getPlayer(FIRSTPOSITION));
        executeState();
    }

    public void executeState() {
        GameSession game = super.getGameSession();
        PlayersHandler playersHandler = game.getPlayersHandler();
        WorkersHandler workersHandler = game.getWorkersHandler();
        Player currentPlayer = game.getCurrentPlayer();
        AbstractGodCard playerCard = currentPlayer.getAbstractGodCard();
        String nicknameCurrentPlayer = currentPlayer.getNickname();

        HashMap<Coord, ArrayList<Integer>> availablePositions;

        // TODO : send a message to the client requesting him if he wants to build a dome or a block
        boolean buildBlock = true; // initialised only not to have an error

        // TODO : send a message to the player requestinq him where he wants to build with his player his player
        String message = "Choose a position where to place your worker next.";
        int[] workerIds = currentPlayer.getWorkersIdsArray();
        ArrayList<Worker> workers = new ArrayList<>();
        for (int id : workerIds) {
            workers.add(workersHandler.getWorker(id));
        }
        if (buildBlock) availablePositions = playerCard.findAvailablePositionsToBuildBlock(game.getCellsHandler(), (Worker[]) workers.toArray());
        else availablePositions = playerCard.findAvailablePositionsToBuildDome(game.getCellsHandler(), (Worker[]) workers.toArray());
        // TODO : send a message to the player requestinq him where he wants to build
        Coord coordToBuild = null;
        // TODO : Is the worker necessary in the buildBlock method???
        if (buildBlock) playerCard.buildBlock(game, currentPlayer, null, coordToBuild);
        else playerCard.buildDome(game, currentPlayer, null, coordToBuild);
        findNextState();
    }

    public void findNextState() {
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
