package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.Color;
import it.polimi.ingsw.PSP43.client.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.client.networkMessages.WorkersColorResponse;
import it.polimi.ingsw.PSP43.server.BoardObserver;
import it.polimi.ingsw.PSP43.server.initialisers.DOMCardsParser;
import it.polimi.ingsw.PSP43.server.initialisers.GameInitialiser;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlersException.NicknameAlreadyInUseException;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;

public class ChooseWorkerStateTest {
    GameSession gameSession;
    GameSession spyGame;
    BoardObserver obs;
    BoardObserver spyObs;
    ArrayList<AbstractGodCard> deck;
    ChooseWorkerState state;
    ChooseWorkerState spyState;

    @Before
    public void setUp() throws ClassNotFoundException, ParserConfigurationException, NicknameAlreadyInUseException, SAXException, IOException {
        gameSession = GameInitialiser.initialiseGame();
        gameSession = GameInitialiser.initialisePlayers(gameSession);
        spyGame = Mockito.spy(gameSession);
        obs = new BoardObserver();
        spyObs = Mockito.spy(obs);
        deck = DOMCardsParser.buildDeck();
        state = new ChooseWorkerState(spyGame);
        spyState = Mockito.spy(state);
    }

    @Test
    public void initState() throws IOException, WinnerCaughtException, InterruptedException, ClassNotFoundException {
        Mockito.doNothing().when(spyGame).sendBroadCast(any());
        Mockito.doNothing().when(spyState).executeState();

        spyState.initState();

        assertTrue(spyGame.getCurrentPlayer().getNickname().equals("Gibi"));
    }

    @Test
    public void executeState() throws InterruptedException, IOException, ClassNotFoundException, WinnerCaughtException {
        Mockito.doReturn(new WorkersColorResponse(Color.ANSI_BLUE), new ActionResponse(new Coord(0, 0), new Coord(1, 1)),
                         new ActionResponse(new Coord(0, 0), new Coord(1, 3)),
                         new WorkersColorResponse(Color.ANSI_RED), new ActionResponse(new Coord(0,0), new Coord(4,2)),
                         new ActionResponse(new Coord(0,0), new Coord(4,3)))
                .when(spyGame).sendRequest(any(), any(), any());
        Mockito.doNothing().when(spyState).findNextState();

        spyGame.setCurrentPlayer(gameSession.getPlayersHandler().getPlayer("Gibi"));
        GameInitialiser.initialiseCards(spyGame);

        spyState.executeState();

        // checks that every color was set right
        for (int i=0; i<gameSession.getPlayersHandler().getNumOfPlayers(); i++) {
            int[] idsWorkers = gameSession.getPlayersHandler().getPlayer(i).getWorkersIdsArray();
            for (int j : idsWorkers) {
                if (j < 2) assertSame(gameSession.getWorkersHandler().getWorker(j).getColor(), Color.ANSI_BLUE);
                else assertSame(gameSession.getWorkersHandler().getWorker(j).getColor(), Color.ANSI_RED);

                Coord coord = gameSession.getWorkersHandler().getWorker(j).getCurrentPosition();
                assertTrue(gameSession.getCellsHandler().getCell(coord).getOccupiedByWorker());
            }
        }
    }

    @Test
    public void findNextState() throws ClassNotFoundException, WinnerCaughtException, InterruptedException, IOException {
        Mockito.doNothing().when(spyGame).transitToNextState();
        Mockito.doNothing().when(spyGame).sendBroadCast(any());

        spyGame.setCurrentState(spyGame.getTurnMap().get(2));

        spyState.findNextState();

        assertTrue(spyGame.getNextState() instanceof MoveState);
    }
}