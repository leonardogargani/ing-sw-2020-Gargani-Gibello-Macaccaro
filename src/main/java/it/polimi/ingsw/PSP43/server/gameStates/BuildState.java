package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameEndedException;

import java.io.IOException;

public class BuildState extends TurnState {
    private static final int FIRSTPOSITION = 0;

    public BuildState(GameSession gameSession) {
        super(gameSession, TurnName.BUILD_STATE);
    }

    public void initState() throws IOException, ClassNotFoundException, InterruptedException {
        super.initState();
    }

    public void executeState() throws IOException, ClassNotFoundException, InterruptedException {
        GameSession game = super.getGameSession();
        Player currentPlayer = game.getCurrentPlayer();
        AbstractGodCard playerCard = currentPlayer.getAbstractGodCard();

        try {
            playerCard.initBuild(game);
        } catch (GameEndedException e) {
            game.setActive();
            return;
        }

        findNextState();
    }

    public void findNextState() throws IOException, ClassNotFoundException, InterruptedException {
        GameSession game = super.getGameSession();
        TurnState currentState = game.getCurrentState();
        int indexCurrentState = game.getTurnMap().indexOf(currentState);
        TurnState nextState = game.getTurnMap().get(indexCurrentState - 1);
        game.setNextState(nextState);
        game.transitToNextState();
    }
}
