package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlers.PlayersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlers.WorkersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameLostException;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;
import it.polimi.ingsw.PSP43.server.networkMessages.TextMessage;

import java.io.IOException;
import java.util.ArrayList;

public class MoveState extends TurnState {
    private static final int FIRSTPOSITION = 0;
    protected int initFirst = -1;

    public MoveState(GameSession gameSession) {
        super(gameSession, TurnName.MOVE_STATE);
    }

    public void initState() {
        GameSession game = super.getGameSession();
        PlayersHandler playersHandler = game.getPlayersHandler();
        WorkersHandler handler = game.getWorkersHandler();
        Player currentPlayer;
        Player nextPlayer;

        for (Worker w : handler.getWorkers()) {
            w.setLatestMoved(false);
        }

        findCurrentPlayer();
    }

    private void findCurrentPlayer() {
        GameSession game = super.getGameSession();
        PlayersHandler playersHandler = game.getPlayersHandler();
        WorkersHandler handler = game.getWorkersHandler();
        Player currentPlayer;
        Player nextPlayer;

        if (initFirst == -1) {
            game.setCurrentPlayer(playersHandler.getPlayer(FIRSTPOSITION));
            initFirst = FIRSTPOSITION;
        } else {
            currentPlayer = game.getCurrentPlayer();
            nextPlayer = playersHandler.getNextPlayer(currentPlayer.getNickname());
            game.setCurrentPlayer(nextPlayer);
        }
        sendAllWaitingMessage();
    }

    private void sendAllWaitingMessage() {
        GameSession game = super.getGameSession();
        Player currentPlayer = game.getCurrentPlayer();

        TextMessage broadcastMessage = new TextMessage(currentPlayer.getNickname());
        ArrayList<String> nicksExcluded = new ArrayList<>();
        nicksExcluded.add(currentPlayer.getNickname());
        game.sendBroadCast(broadcastMessage, nicksExcluded);

        setAllWorkersUnmoved();
    }

    private void setAllWorkersUnmoved() {
        WorkersHandler workersHandler = super.getGameSession().getWorkersHandler();

        for (Worker w : workersHandler.getWorkers()) {
            w.setLatestMoved(false);
        }

        executeState();
    }

    public void executeState() {
        GameSession game = super.getGameSession();
        Player currentPlayer = game.getCurrentPlayer();
        AbstractGodCard playerCard = currentPlayer.getAbstractGodCard();

        try {
            playerCard.initMove(game);
        } catch (WinnerCaughtException e) {
            int winnerStateIndex = game.getTurnMap().size() - 1;
            WinState nextState = (WinState) game.getTurnMap().get(winnerStateIndex);
            nextState.setWinner(e.getWinner());
            game.setNextState(nextState);
            game.transitToNextState();
        } catch (GameEndedException e) {
            game.setActive();
            return;
        } catch (GameLostException e) {
            Player nextPlayer = game.getPlayersHandler().getNextPlayer(currentPlayer.getNickname());
            game.setCurrentPlayer(nextPlayer);
            game.eliminatePlayer(currentPlayer);

            this.sendAllWaitingMessage();
        }

        findNextState();
    }

    public void findNextState() {
        GameSession game = super.getGameSession();
        TurnState currentState = game.getCurrentState();
        int indexCurrentState = game.getTurnMap().indexOf(currentState);
        TurnState nextState = game.getTurnMap().get(indexCurrentState + 1);
        game.setNextState(nextState);
    }
}
