package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.Color;
import it.polimi.ingsw.PSP43.client.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.client.networkMessages.WorkersColorResponse;
import it.polimi.ingsw.PSP43.server.initialisers.GameInitialiser;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.modelHandlersException.NicknameAlreadyInUseException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;

public class ChooseWorkerStateTest {
    GameSessionForTest gameSession;
    GameSessionForTest spyGame;
    ChooseWorkerState state;
    ChooseWorkerState spyState;

    @Before
    public void setUp() throws ClassNotFoundException, ParserConfigurationException, NicknameAlreadyInUseException, SAXException, IOException {
        gameSession = GameInitialiser.initialiseGame();
        GameInitialiser.initialisePlayers(gameSession);
        GameInitialiser.initialiseWorkers(gameSession);
        spyGame = Mockito.spy(gameSession);
        state = new ChooseWorkerState(spyGame);
        spyState = Mockito.spy(state);
    }

    @Test
    public void initState() {
        Mockito.doNothing().when(spyGame).sendBroadCast(any());
        Mockito.doNothing().when(spyState).executeState();

        spyState.initState();

        assertEquals("Gibi", spyGame.getCurrentPlayer().getNickname());
    }

    @Test
    public void executeState() throws GameEndedException {
        Mockito.doReturn(new WorkersColorResponse(Color.ANSI_BLUE), new ActionResponse(new Coord(0, 0), new Coord(1, 1)),
                         new ActionResponse(new Coord(0, 0), new Coord(1, 3)),
                         new WorkersColorResponse(Color.ANSI_RED), new ActionResponse(new Coord(0,0), new Coord(4,2)),
                         new ActionResponse(new Coord(0,0), new Coord(4,3)))
                .when(spyGame).sendRequest(any(), any(), any());
        Mockito.doNothing().when(spyState).findNextState();

        spyGame.setCurrentPlayer(gameSession.getPlayersHandler().getPlayer("Gibi"));
        GameInitialiser.initialiseCards(spyGame);

        spyState.executeState();

        // checks that every player has a different worker ids from the others
        // because that is a fact not true at the instantiation of the array of workers during
        // the instantiation of each player
        ArrayList<Integer> idsWorkers =  new ArrayList<>();
        for (int i=0; i<gameSession.getPlayersHandler().getNumOfPlayers(); i++) {
            Integer[] workers = gameSession.getPlayersHandler().getPlayer(i).getWorkersIdsArray();
            idsWorkers.addAll(Arrays.asList(workers));
        }

        boolean equal = true;
        for (int i = 0; i<idsWorkers.size() - 1; i++) {
            for (int j = i+1; j<idsWorkers.size(); j++) {
                if (idsWorkers.get(i).equals(idsWorkers.get(j))) {
                    equal = false;
                    break;
                }
            }
        }

        assertTrue(equal);
    }

    @Test
    public void findNextState() {
        Mockito.doNothing().when(spyGame).transitToNextState();
        Mockito.doNothing().when(spyGame).sendBroadCast(any());

        spyGame.setCurrentState(spyGame.getTurnStateMap().get(2));

        spyState.findNextState();

        assertTrue(spyGame.getNextState() instanceof MoveState);
    }
}