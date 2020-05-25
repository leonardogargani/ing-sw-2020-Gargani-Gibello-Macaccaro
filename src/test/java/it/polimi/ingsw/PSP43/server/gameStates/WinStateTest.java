package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.client.networkMessages.RegistrationMessage;
import it.polimi.ingsw.PSP43.server.ClientListener;
import it.polimi.ingsw.PSP43.server.model.Player;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class WinStateTest {
    ClientListener mock;
    GameSessionForTest spyGame;
    Player winner;

    @Before
    public void setUp() throws Exception {
        spyGame = spy(new GameSessionForTest(9));
        mock = mock(ClientListener.class);
    }

    @Test
    public void executeState() {
        WinState state = new WinState(spyGame);

        String firstNick = "Gibi";
        String secondNick = "Rob";
        String thirdNick = "Leo";

        doNothing().when(spyGame).transitToNextState();

        spyGame.setCurrentState(mock(PlayerRegistrationState.class));
        spyGame.registerToTheGame(new RegistrationMessage(firstNick), mock);
        spyGame.maxNumPlayers = 3;
        spyGame.registerToTheGame(new RegistrationMessage(secondNick), mock);
        spyGame.registerToTheGame(new RegistrationMessage(thirdNick), mock);

        spyGame.setCurrentState(state);

        state.executeState();

        verify(mock, times(3)).sendMessage(any());
    }
}