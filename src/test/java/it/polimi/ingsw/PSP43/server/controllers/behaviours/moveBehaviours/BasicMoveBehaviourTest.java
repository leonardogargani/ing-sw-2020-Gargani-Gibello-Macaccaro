package it.polimi.ingsw.PSP43.server.controllers.behaviours.moveBehaviours;

import it.polimi.ingsw.PSP43.client.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.gameStates.GameSessionForTest;
import it.polimi.ingsw.PSP43.server.initialisers.DOMCardsParser;
import it.polimi.ingsw.PSP43.server.initialisers.GameInitialiser;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.controllers.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.controllers.BasicGodCard;
import it.polimi.ingsw.PSP43.server.controllers.behaviours.buildBehaviours.BasicBuildBehaviour;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameLostException;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class BasicMoveBehaviourTest {
    GameSessionForTest gameSession;
    GameSessionForTest spyGame;
    ArrayList<AbstractGodCard> deck;
    Player currentPlayer;
    AbstractGodCard abstractGodCard;

    @Before
    public void setUp() throws Exception {
        gameSession = new GameSessionForTest(9);
        GameInitialiser.initialisePlayers(gameSession);
        GameInitialiser.initialiseWorkers(gameSession);
        GameInitialiser.initialiseCards(gameSession);
        spyGame = spy(gameSession);
        deck = DOMCardsParser.buildDeck();

        currentPlayer = spyGame.getPlayersHandler().getPlayer(0);
        spyGame.setCurrentPlayer(currentPlayer);

        abstractGodCard = new BasicGodCard("", "", "", new BasicMoveBehaviour(), new BasicBuildBehaviour());
        spyGame.getCardsHandler().setCardToPlayer(currentPlayer.getNickname(), abstractGodCard);
    }

    @Test
    public void testAskForMove() throws GameEndedException, GameLostException {
        Worker workerToMove = spyGame.getWorkersHandler().getWorker(new Coord(4, 3));
        Coord coordToMove = new Coord(3, 3);
        doReturn(new ActionResponse(workerToMove.getCurrentPosition(), coordToMove)). when(spyGame).sendRequest(any(),any(),any());

        ActionResponse actionResponse = abstractGodCard.askForMove(spyGame);

        assertEquals(actionResponse.getPosition(), coordToMove);
        assertEquals(actionResponse.getWorkerPosition(), workerToMove.getCurrentPosition());
    }

    @Test
    public void testHandleInitMove() throws GameEndedException, WinnerCaughtException, GameLostException {
        Coord workerCoord = new Coord(4,3);
        Worker workerToMove = spyGame.getWorkersHandler().getWorker(workerCoord);
        Coord coordToMove = new Coord(3, 3);
        doReturn(new ActionResponse(workerToMove.getCurrentPosition(), coordToMove)). when(spyGame).sendRequest(any(),any(),any());

        abstractGodCard.initMove(spyGame);

        assertEquals(workerToMove.getCurrentPosition(), coordToMove);
        assertEquals(workerToMove.getPreviousPosition(), workerCoord);
        assertFalse(spyGame.getCellsHandler().getCell(workerCoord).getOccupiedByWorker());
        assertTrue(spyGame.getCellsHandler().getCell(coordToMove).getOccupiedByWorker());
    }
}