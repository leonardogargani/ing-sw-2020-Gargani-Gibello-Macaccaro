package it.polimi.ingsw.PSP43.server.controller.gameStates;

import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.controller.cards.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.CardsHandler;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.PlayersHandler;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.WorkersHandler;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException.GameLostException;
import it.polimi.ingsw.PSP43.server.network.networkMessages.TextMessage;

/**
 * This is the State of the game where the current player is asked to make a move
 * according to the powers of his god.
 */
public class MoveState extends TurnState {
    protected int initFirst = -1;

    public MoveState(GameSession gameSession) {
        super(gameSession, TurnName.MOVE_STATE);
    }

    /**
     * This method initialises the turn by calling methods that :
     * -    find the current player of the game;
     * -    send a message to all the other players (different from the current) telling that they have to wait for their turn;
     * -    set all the workers as unmoved (flag set true if the worker is moved, to recognise it as build-allowed
     *      during the turn);
     */
    public void initState() {
        findCurrentPlayer();
    }

    /**
     * This method finds the current player of the game.
     */
    private void findCurrentPlayer() {
        GameSession game = super.getGameSession();
        PlayersHandler playersHandler = game.getPlayersHandler();
        Player currentPlayer;
        Player nextPlayer;

        if (initFirst == -1) {
            for (TurnState t : game.getTurnStateMap()) {
                if (t.getTurnName() == TurnName.CHOOSE_WORKER_STATE) {
                    game.setCurrentPlayer(playersHandler.getPlayer(((ChooseWorkerState) t).starterPlayer));
                }
            }
            initFirst = 0;
        } else {
            currentPlayer = game.getCurrentPlayer();
            nextPlayer = playersHandler.getNextPlayer(currentPlayer.getNickname());
            game.setCurrentPlayer(nextPlayer);
        }
        sendAllWaitingMessage();
    }

    /**
     * This method sends a message to all the players (different from the current) telling them they have to
     * wait for their turn.
     */
    private void sendAllWaitingMessage() {
        GameSession game = super.getGameSession();
        Player currentPlayer = game.getCurrentPlayer();

        TextMessage broadcastMessage = new TextMessage(currentPlayer.getNickname(), TextMessage.PositionInScreen.HIGH_CENTER);
        game.sendBroadCast(broadcastMessage, currentPlayer.getNickname());

        setAllWorkersUnmoved();
    }

    /**
     * This method sets all the workers as "unmoved". It is important to know during the build state which worker is
     * allowed to build.
     */
    private void setAllWorkersUnmoved() {
        WorkersHandler workersHandler = super.getGameSession().getWorkersHandler();

        for (Worker w : workersHandler.getWorkers()) {
            w.setLatestMoved(false);
        }

        executeState();
    }

    /**
     * This method executes the turn. It gives the possibility to the current player to move a worker,
     * accordingly to the powers of the god he owns.
     */
    public void executeState() {
        GameSession game = super.getGameSession();
        Player currentPlayer = game.getCurrentPlayer();
        CardsHandler cardsHandler = game.getCardsHandler();

        AbstractGodCard playerCard = cardsHandler.getPlayerCard(currentPlayer.getNickname());

        try {
            playerCard.initMove(game);
        } catch (GameEndedException e) {
            game.setActive();
            return;
        } catch (GameLostException e) {
            Player nextPlayer = game.getPlayersHandler().getNextPlayer(currentPlayer.getNickname());
            game.eliminatePlayer(currentPlayer);
            game.setCurrentPlayer(nextPlayer);

            if (game.getPlayersHandler().getNumOfPlayers() == 1) {
                for (TurnState t : game.getTurnStateMap()) {
                    if (t.getTurnName() == TurnName.WIN_STATE) {
                        WinState winState = (WinState) t;
                        winState.winner = game.getCurrentPlayer().getNickname();
                        game.setNextState(t);
                        return;
                    }
                }
            }
            else {
                this.sendAllWaitingMessage();
                return;
            }
        }

        if (super.checkForWinner(playerCard, game)) {
            return;
        }

        findNextState();
    }

    /**
     * This method finds the next turn of the game (saving it into a variable in the GameSession database),
     * which will be always a BuildState.
     */
    public void findNextState() {
        GameSession game = super.getGameSession();

        for (TurnState t : game.getTurnStateMap()) {
            if (t.getTurnName() == TurnName.BUILD_STATE)
                game.setNextState(t);
        }
    }
}