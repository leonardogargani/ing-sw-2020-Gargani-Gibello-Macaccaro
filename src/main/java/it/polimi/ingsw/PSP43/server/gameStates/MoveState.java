package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlers.PlayersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlers.WorkersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;

import java.util.ArrayList;
import java.util.HashMap;

public class MoveState extends TurnState {
    private static final int FIRSTPOSITION = 0;
    private int initFirst = -1;

    public MoveState(GameSession gameSession) {
        super(gameSession);
    }

    public void initState() {
        GameSession game = super.getGameSession();
        PlayersHandler playersHandler = game.getPlayersHandler();
        Player currentPlayer;
        Player nextPlayer;

        if (initFirst == -1) {
            game.setCurrentPlayer(playersHandler.getPlayer(FIRSTPOSITION));
            initFirst = FIRSTPOSITION;
        }
        else {
            currentPlayer = game.getCurrentPlayer();
            nextPlayer = playersHandler.getNextPlayer(currentPlayer.getNickname());
            game.setCurrentPlayer(nextPlayer);
        }

        currentPlayer = game.getCurrentPlayer();

        try {
            executeState();
        } catch (WinnerCaughtException e) {
            int winnerStateIndex = game.getTurnMap().size() - 1;
            WinState nextState = (WinState) game.getTurnMap().get(winnerStateIndex);
            nextState.setWinner(currentPlayer.getNickname());
            game.setNextState(nextState);
            game.transitToNextState();
        }
    }

    public void executeState() throws WinnerCaughtException {
        GameSession game = super.getGameSession();
        PlayersHandler playersHandler = game.getPlayersHandler();
        WorkersHandler workersHandler = game.getWorkersHandler();
        Player currentPlayer = game.getCurrentPlayer();
        AbstractGodCard playerCard = currentPlayer.getAbstractGodCard();
        String nicknameCurrentPlayer = currentPlayer.getNickname();

        HashMap<Coord, ArrayList<Integer>> availablePositions;

        String message = "Choose a position where to place your worker next.";
        int[] workerIds = currentPlayer.getWorkersIdsArray();
        ArrayList<Worker> workers = new ArrayList<>();
        for (int id : workerIds) {
            workers.add(workersHandler.getWorker(id));
        }
        availablePositions = playerCard.findAvailablePositionsToMove(game.getCellsHandler(), (Worker[]) workers.toArray());
        // TODO : send a message to the player requestinq him where he wants to move his player
        Coord nextPositionChosen = null;
        Integer idWorkerMoved = -1;
        // TODO : receive the Coord chosen by the player
        Worker workerMoved = workersHandler.getWorker(idWorkerMoved);

        playerCard.move(game, currentPlayer, workerMoved, nextPositionChosen);
        findNextState();
    }

    public void findNextState() {
        GameSession game = super.getGameSession();
        TurnState currentState = game.getCurrentState();
        int indexCurrentState = game.getTurnMap().indexOf(currentState);
        TurnState nextState = game.getTurnMap().get(indexCurrentState + 1);
        game.setNextState(nextState);
        game.transitToNextState();
    }
}
