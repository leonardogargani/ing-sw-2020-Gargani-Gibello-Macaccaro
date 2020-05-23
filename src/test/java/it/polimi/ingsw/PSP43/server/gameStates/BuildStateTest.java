package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.server.initialisers.GameInitialiser;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class BuildStateTest {
    GameSession gameSession;
    GameSession spyGame;
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

        mockCard = mock(AbstractGodCard.class);
        currentPlayer = spyGame.getPlayersHandler().getPlayer(0);
        spyGame.getPlayersHandler().setCardToPlayer(currentPlayer, mockCard);
        spyGame.setCurrentPlayer(currentPlayer);
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