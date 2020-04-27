package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.client.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.server.DataToAction;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlers.PlayersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlers.WorkersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;
import it.polimi.ingsw.PSP43.server.networkMessages.TextMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MoveState extends TurnState {
    private static final int FIRSTPOSITION = 0;
    private int initFirst = -1;

    public MoveState(GameSession gameSession) {
        super(gameSession);
    }

    public void initState() throws IOException, ClassNotFoundException, WinnerCaughtException {
        GameSession game = super.getGameSession();
        PlayersHandler playersHandler = game.getPlayersHandler();
        WorkersHandler handler = game.getWorkersHandler();
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
        TextMessage broadcastMessage = new TextMessage(currentPlayer.getNickname() + " is making his move.");
        ArrayList<String> nicksExcluded = new ArrayList<>();
        nicksExcluded.add(currentPlayer.getNickname());
        game.sendBroadCast(broadcastMessage, nicksExcluded);

        for (Worker w : handler.getWorkers()) {
            w.setLatestMoved(false);
        }

        try {
            executeState();
        } catch (WinnerCaughtException | IOException | ClassNotFoundException e) {
            int winnerStateIndex = game.getTurnMap().size() - 1;
            WinState nextState = (WinState) game.getTurnMap().get(winnerStateIndex);
            nextState.setWinner(currentPlayer.getNickname());
            game.setNextState(nextState);
            game.transitToNextState();
        }
    }

    public void executeState() throws WinnerCaughtException, IOException, ClassNotFoundException {
        GameSession game = super.getGameSession();
        WorkersHandler workersHandler = game.getWorkersHandler();
        Player currentPlayer = game.getCurrentPlayer();
        AbstractGodCard playerCard = currentPlayer.getAbstractGodCard();
        String nicknameCurrentPlayer = currentPlayer.getNickname();

        HashMap<Coord, ArrayList<Coord>> availablePositions;

        int[] workerIds = currentPlayer.getWorkersIdsArray();
        ArrayList<Worker> workers = new ArrayList<>();
        for (int id : workerIds) {
            workers.add(workersHandler.getWorker(id));
        }
        availablePositions = playerCard.findAvailablePositionsToMove(game.getCellsHandler(), (Worker[]) workers.toArray());
        PossibleMovesMessage message = new PossibleMovesMessage("Choose a position where to place your worker next.", availablePositions);
        ActionResponse response = null;
        boolean delivered;
        do {
            delivered = game.sendRequest(message, nicknameCurrentPlayer, response);
        } while (!delivered);

        Coord nextPositionChosen = response.getPosition();
        Coord oldPosition = response.getWorkerPosition();
        Worker workerMoved = workersHandler.getWorker(oldPosition);
        workerMoved.setLatestMoved(true);

        playerCard.move(new DataToAction(game, currentPlayer, workerMoved, nextPositionChosen));

        findNextState();
    }

    public void findNextState() throws IOException, ClassNotFoundException, WinnerCaughtException {
        GameSession game = super.getGameSession();
        TurnState currentState = game.getCurrentState();
        int indexCurrentState = game.getTurnMap().indexOf(currentState);
        TurnState nextState = game.getTurnMap().get(indexCurrentState + 1);
        game.setNextState(nextState);
        game.transitToNextState();
    }
}
