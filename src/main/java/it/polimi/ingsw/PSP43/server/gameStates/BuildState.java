package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlers.CardsHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameEndedException;

import java.util.Map;

/**
 * This is the State of the game where the current player is asked to make a build
 * according to the powers of his god.
 */
public class BuildState extends TurnState {

    public BuildState(GameSession gameSession) {
        super(gameSession, TurnName.BUILD_STATE);
    }

    /**
     * This method initialises the turn. It simply calls the super method of the class TurnState.
     */
    public void initState() {
        super.initState();
    }

    /**
     * This method executes the turn. It gives the possibility to the current player to build a dome or a block,
     * accordingly to the powers of the god he owns.
     */
    public void executeState() {
        GameSession game = super.getGameSession();
        Player currentPlayer = game.getCurrentPlayer();
        AbstractGodCard playerCard = currentPlayer.getAbstractGodCard();

        try {
            playerCard.initBuild(game);
        } catch (GameEndedException e) {
            game.setActive();
            return;
        }

        Map<String, AbstractGodCard> map = game.getCardsHandler().getMapOwnersCard();
        for (String s : map.keySet()) {
            AbstractGodCard card = map.get(s);

            if (super.checkForWinner(card, game)) {
                return;
            }
        }

        findNextState();
    }

    /**
     * This method finds the next turn of the game (saving it into a variable in the GameSession database),
     * which will be always a MoveState.
     */
    public void findNextState() {
        GameSession game = super.getGameSession();

        for (TurnState t : game.getTurnStateMap()) {
            if (t.getTurnName() == TurnName.MOVE_STATE)
                game.setNextState(t);
        }
    }
}