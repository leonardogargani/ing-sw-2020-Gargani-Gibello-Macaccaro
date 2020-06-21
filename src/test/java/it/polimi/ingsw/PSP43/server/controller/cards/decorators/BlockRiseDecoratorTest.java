package it.polimi.ingsw.PSP43.server.controller.cards.decorators;

import it.polimi.ingsw.PSP43.client.network.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.server.controller.gameStates.GameSessionForTest;
import it.polimi.ingsw.PSP43.server.model.initialisers.DOMCardsParser;
import it.polimi.ingsw.PSP43.server.model.initialisers.GameInitialiser;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.controller.cards.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.controller.cards.BasicGodCard;
import it.polimi.ingsw.PSP43.server.controller.cards.behaviours.buildBehaviours.BasicBuildBehaviour;
import it.polimi.ingsw.PSP43.server.controller.cards.behaviours.moveBehaviours.BasicMoveBehaviour;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException.GameLostException;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException.WinnerCaughtException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class BlockRiseDecoratorTest {
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

        abstractGodCard = new BlockRiseDecorator(new BasicGodCard("", "", "", new BasicMoveBehaviour(), new BasicBuildBehaviour()));
        spyGame.getCardsHandler().setCardToPlayer(currentPlayer.getNickname(), abstractGodCard);

        workerToMove = spyGame.getWorkersHandler().getWorker(new Coord(4, 3));
    }

    @Test
    public void findAvailablePositionsToMove() {
        Coord coordHigher = new Coord(3,3);
        Cell cellHigher = spyGame.getCellsHandler().getCell(coordHigher);
        cellHigher.setHeight(1);
        spyGame.getCellsHandler().changeStateOfCell(cellHigher, coordHigher);

        HashMap<Coord, ArrayList<Coord>> availablePositions = abstractGodCard.findAvailablePositionsToMove(spyGame);

        boolean right = true;
        for(Coord key : availablePositions.keySet()) {
            if (key.equals(new Coord(4,3))) {
                if (availablePositions.get(key).contains(coordHigher)) right = false;
            }
        }

        assertTrue(right);
    }

    @Test
    public void cleanFromEffects() {
        AbstractGodCard cleanCard = abstractGodCard.cleanFromEffects("it.polimi.ingsw.PSP43.server.controller.cards.decorators.BlockRiseDecorator");

        assertFalse(cleanCard instanceof BlockRiseDecorator);
    }

    @Test
    public void initMove() throws GameEndedException, WinnerCaughtException, GameLostException {
        Coord coordToMove = new Coord(3, 4);

        doReturn(new ActionResponse(workerToMove.getCurrentPosition(), coordToMove)).when(spyGame).sendRequest(any(), any(), any());

        abstractGodCard.initMove(spyGame);

        assertEquals(workerToMove.getCurrentPosition(), coordToMove);
    }
}