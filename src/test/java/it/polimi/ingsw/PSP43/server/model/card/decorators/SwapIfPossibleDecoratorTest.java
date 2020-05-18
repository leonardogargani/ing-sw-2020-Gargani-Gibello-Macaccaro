package it.polimi.ingsw.PSP43.server.model.card.decorators;

import it.polimi.ingsw.PSP43.client.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.initialisers.DOMCardsParser;
import it.polimi.ingsw.PSP43.server.initialisers.GameInitialiser;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.model.card.BasicGodCard;
import it.polimi.ingsw.PSP43.server.model.card.behaviours.buildBehaviours.BasicBuildBehaviour;
import it.polimi.ingsw.PSP43.server.model.card.behaviours.moveBehaviours.BasicMoveBehaviour;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class SwapIfPossibleDecoratorTest {
    GameSession gameSession;
    GameSession spyGame;
    ArrayList<AbstractGodCard> deck;
    Player currentPlayer;
    AbstractGodCard abstractGodCard;

    @Before
    public void setUp() throws Exception {
        gameSession = new GameSession(9);
        GameInitialiser.initialisePlayers(gameSession);
        GameInitialiser.initialiseWorkers(gameSession);
        GameInitialiser.initialiseCards(gameSession);
        spyGame = spy(gameSession);
        deck = DOMCardsParser.buildDeck();

        currentPlayer = spyGame.getPlayersHandler().getPlayer(0);
        spyGame.setCurrentPlayer(currentPlayer);

        abstractGodCard = new SwapIfPossibleDecorator(new SwapMoveDecorator(new BasicGodCard("", "", "", new BasicMoveBehaviour(), new BasicBuildBehaviour())));
    }

    @Test
    public void move() throws ClassNotFoundException, GameEndedException, InterruptedException, IOException, WinnerCaughtException {
        Coord coordToFirstMove = new Coord(1, 2);
        Worker firstMoveWorker = spyGame.getWorkersHandler().getWorker(new Coord(1, 1));

        doReturn(new ActionResponse(firstMoveWorker.getCurrentPosition(), coordToFirstMove)).when(spyGame).sendRequest(any(), any(), any());
        abstractGodCard.initMove(spyGame);
        assertEquals(firstMoveWorker.getCurrentPosition(), coordToFirstMove);

        Coord coordToForce = new Coord(4, 0);
        Coord coordToSecondMove = new Coord(4, 2);
        Worker secondMoveWorker = spyGame.getWorkersHandler().getWorker(new Coord(4, 3));
        Worker workerForced = spyGame.getWorkersHandler().getWorker(coordToSecondMove);

        doReturn(new ActionResponse(secondMoveWorker.getCurrentPosition(), coordToSecondMove),
                new ActionResponse(new Coord(0, 0), coordToForce)).when(spyGame).sendRequest(any(), any(), any());
        abstractGodCard.initMove(spyGame);
        assertEquals(secondMoveWorker.getCurrentPosition(), coordToSecondMove);
        assertEquals(workerForced.getCurrentPosition(), coordToForce);
    }

    @Test
    public void findAvailablePositionsToMove() throws IOException {
        Worker secondWorker = spyGame.getWorkersHandler().getWorker(new Coord(4, 2));
        Worker firstWorker = spyGame.getWorkersHandler().getWorker(new Coord(0, 0));
        spyGame.getWorkersHandler().changePosition(firstWorker, new Coord(2, 2));

        Coord coordWithBlock = new Coord(4, 4);
        Cell cellWithBlock = spyGame.getCellsHandler().getCell(coordWithBlock);
        cellWithBlock.setHeight(2);
        spyGame.getCellsHandler().changeStateOfCell(cellWithBlock, coordWithBlock);

        Coord coordWithBlock1 = new Coord(3, 3);
        Cell cellWithBlock1 = spyGame.getCellsHandler().getCell(coordWithBlock1);
        cellWithBlock1.setHeight(2);
        spyGame.getCellsHandler().changeStateOfCell(cellWithBlock1, coordWithBlock1);

        HashMap<Coord, ArrayList<Coord>> availablePositions = abstractGodCard.findAvailablePositionsToMove(spyGame);

        boolean equals = true;
        for (Coord key : availablePositions.keySet()) {
            ArrayList<Coord> actual = availablePositions.get(key);
            if (key.equals(new Coord(1, 1))) {
                if (actual.contains(firstWorker.getCurrentPosition())) equals = false;
            }
            if (key.equals(new Coord(4, 3))) {
                if (!(actual.contains(secondWorker.getCurrentPosition()))) equals = false;
            }
        }

        assertTrue(equals);
    }

    @Test
    public void cleanFromEffects() throws ClassNotFoundException {
        abstractGodCard.cleanFromEffects("it.polimi.ingsw.PSP43.server.model.card.decorators.BlockRiseDecorator");

        boolean verified = true;
        while (true) {
            if (!(abstractGodCard instanceof SwapIfPossibleDecorator) && !(abstractGodCard instanceof SwapMoveDecorator) && !(abstractGodCard instanceof BasicGodCard)) {
                verified = false;
            }
            if (abstractGodCard instanceof BasicGodCard) break;
            else abstractGodCard = ((PowerGodDecorator) abstractGodCard).getGodComponent();
        }

        assertTrue(verified);
    }
}