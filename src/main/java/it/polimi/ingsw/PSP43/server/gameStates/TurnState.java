package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.client.networkMessages.RegistrationMessage;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;

/**
 * This is an abstract class to represent a general structure of turns of a game.
 */
public abstract class TurnState {

    /**
     * This enum represents the names of turn types of the game.
     */
    public enum TurnName {
        PLAYER_REGISTRATION_STATE,
        CHOOSE_CARD_STATE,
        CHOOSE_WORKER_STATE,
        MOVE_STATE,
        BUILD_STATE,
        WIN_STATE
    }

    private final GameSession gameSession;
    private final TurnName turnName;

    public TurnState(GameSession gameSession, TurnName turnName) {
        this.gameSession = gameSession;
        this.turnName = turnName;
    }

    /**
     * This method returns the reference to the database of the game.
     * @return the reference to the database of the game.
     */
    public GameSession getGameSession() {
        return gameSession;
    }

    /**
     * This method returns the name of the type of turn.
     * @return the name of the type of turn.
     */
    public TurnName getTurnName() {
        return turnName;
    }

    /**
     * This method initialises the turn of the game.
     */
    public void initState() { executeState();}

    /**
     * This method executes the turn of the game.
     * @param message The message of registration arrived from the player on the client.
     */
    public void executeState(RegistrationMessage message) { findNextState(); }

    /**
     * This method executes the turn of the game.
     */
    public void executeState() { findNextState(); }

    /**
     * This method finds the next turn of the game, saving it into a variable of the GameSession.
     */
    public void findNextState() {}

    public boolean checkForWinner(AbstractGodCard card, GameSession gameSession) {
        if (card.checkConditionsToWin(gameSession)) {
            WinState nextState;
            for (TurnState t : gameSession.getTurnStateMap()) {
                if (TurnName.WIN_STATE == t.getTurnName()) {
                    nextState = (WinState) t;
                    nextState.setWinner(gameSession.getCurrentPlayer().getNickname());
                    gameSession.setNextState(t);
                }
            }
            return true;
        }
        else return false;
    }
}