package it.polimi.ingsw.PSP43.server.controller.gameStates;

import it.polimi.ingsw.PSP43.client.network.networkMessages.RegistrationMessage;
import it.polimi.ingsw.PSP43.server.controller.cards.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.CardsHandler;

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
        CardsHandler cardsHandler = gameSession.getCardsHandler();
        String nickWinner = null;

        if (card.checkConditionsToWin(gameSession)) {
            WinState nextState;

            for (String s : cardsHandler.getMapOwnersCard().keySet()) {
                AbstractGodCard abstractGodCard = cardsHandler.getMapOwnersCard().get(s);
                if (abstractGodCard.getGodName().equals(card.getGodName()))
                    nickWinner = s;
            }

            for (TurnState t : gameSession.getTurnStateMap()) {
                if (TurnName.WIN_STATE == t.getTurnName()) {
                    nextState = (WinState) t;
                    nextState.setWinner(nickWinner);
                    gameSession.setNextState(t);
                }
            }
            return true;
        }
        else return false;
    }
}