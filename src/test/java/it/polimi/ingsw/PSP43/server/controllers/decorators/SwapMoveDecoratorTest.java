package it.polimi.ingsw.PSP43.server.controllers.decorators;

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
import it.polimi.ingsw.PSP43.server.controllers.behaviours.moveBehaviours.BasicMoveBehaviour;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameLostException;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class SwapMoveDecoratorTest {
    GameSessionForTest gameSession;
    GameSessionForTest spyGame;
    ArrayList<AbstractGodCard> deck;
    Player currentPlayer;
    AbstractGodCard abstractGodCard;
    Worker workerToMove;

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

        abstractGodCard = new SwapMoveDecorator(new BasicGodCard("", "", "", new BasicMoveBehaviour(), new BasicBuildBehaviour()));
        spyGame.getCardsHandler().setCardToPlayer(currentPlayer.getNickname(), abstractGodCard);

        workerToMove = spyGame.getWorkersHandler().getWorker(new Coord(4, 3));
    }

    @Test
    public void move() throws GameEndedException, WinnerCaughtException, GameLostException {
        Worker workerForced = spyGame.getWorkersHandler().getWorker(new Coord(4,2));
        doReturn(new ActionResponse(workerToMove.getCurrentPosition(), new Coord(4, 2))).when(spyGame).sendRequest(any(), any(), any());

        abstractGodCard.initMove(spyGame);

        assertEquals(workerToMove.getCurrentPosition(), new Coord(4, 2));
        assertEquals(workerForced.getCurrentPosition(), new Coord(4, 3));
    }

    @Test
    public void findAvailablePositionsToMove() {
        Coord coordFirstSwap = new Coord(4, 2);
        Coord coordSecondSwap = new Coord(0, 0);

        HashMap<Coord, ArrayList<Coord>> availablePositions = abstractGodCard.findAvailablePositionsToMove(spyGame);

        boolean equals = true;
        for (Coord key : availablePositions.keySet()) {
            ArrayList<Coord> actualPositions = availablePositions.get(key);
            if (key.equals(new Coord(4, 3))) {
                if (actualPositions.size() != 5 || !(actualPositions.contains(coordFirstSwap))) equals = false;
            }
            if (key.equals(new Coord(1, 1))) {
                if (actualPositions.size() != 8 || !(actualPositions.contains(coordSecondSwap))) equals = false;
            }
        }

        assertTrue(equals);
    }

    @Test
    public void cleanFromEffects() {
        abstractGodCard.cleanFromEffects("it.polimi.ingsw.PSP43.server.controllers.decorators.BlockRiseDecorator");

        boolean verified = true;
        while (true) {
            if (!(abstractGodCard instanceof SwapMoveDecorator) && !(abstractGodCard instanceof BasicGodCard)) {
                verified = false;
            }
            if (abstractGodCard instanceof BasicGodCard) break;
            else abstractGodCard = ((PowerGodDecorator) abstractGodCard).getGodComponent();
        }

        assertTrue(verified);
    }
}