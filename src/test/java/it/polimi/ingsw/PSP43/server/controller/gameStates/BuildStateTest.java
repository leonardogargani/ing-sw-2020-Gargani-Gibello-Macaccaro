package it.polimi.ingsw.PSP43.server.controller.gameStates;

import it.polimi.ingsw.PSP43.server.model.initialisers.GameInitialiser;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.controller.cards.AbstractGodCard;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class BuildStateTest {
    GameSessionForTest gameSession;
    GameSessionForTest spyGame;
    BuildState state;
    BuildState spyState;
    Player currentPlayer;
    AbstractGodCard mockCard;

    @Before
    public void setUp() throws Exception {
        gameSession = GameInitialiser.initialiseGame();
        GameInitialiser.initialisePlayers(gameSession);
        spyGame = Mockito.spy(gameSession);
        state = new BuildState(spyGame);
        spyState = Mockito.spy(state);

        currentPlayer = spyGame.getPlayersHandler().getPlayer(0);
        spyGame.setCurrentPlayer(currentPlayer);

        mockCard = mock(AbstractGodCard.class);
        spyGame.getCardsHandler().setCardToPlayer(currentPlayer.getNickname(), mockCard);
    }

    @Test
    public void initState() {
        spyState.initState();
    }

    @Test
    public void executeState() {
        spyState.executeState();
    }

    @Test
    public void findNextState() {
        spyState.findNextState();

        assertSame(spyGame.getNextState().getTurnName(), TurnState.TurnName.MOVE_STATE);
    }
}