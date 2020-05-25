package it.polimi.ingsw.PSP43.server.controllers.decorators;

import it.polimi.ingsw.PSP43.client.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.client.networkMessages.ResponseMessage;
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
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class UnconditionedDomeBuildDecoratorTest {
    GameSessionForTest gameSession;
    GameSessionForTest spyGame;
    ArrayList<AbstractGodCard> deck;
    Player currentPlayer;
    AbstractGodCard abstractGodCard;
    Worker workerToBuild;

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

        abstractGodCard = new UnconditionedDomeBuildDecorator(new BasicGodCard("", "", "", new BasicMoveBehaviour(), new BasicBuildBehaviour()));
        spyGame.getCardsHandler().setCardToPlayer(currentPlayer.getNickname(), abstractGodCard);

        workerToBuild = spyGame.getWorkersHandler().getWorker(new Coord(4, 3));
    }

    @Test
    public void initBuild() throws GameEndedException {
        Coord coordBuildDome = new Coord(3, 3);
        Coord coordBuildBlock = new Coord(3, 4);

        doReturn(new ResponseMessage(true),
                new ActionResponse(workerToBuild.getCurrentPosition(), coordBuildDome)).when(spyGame).sendRequest(any(), any(), any());

        abstractGodCard.initBuild(spyGame);
        assertTrue(spyGame.getCellsHandler().getCell(coordBuildDome).getOccupiedByDome());

        doReturn(new ResponseMessage(false),
                new ActionResponse(workerToBuild.getCurrentPosition(), coordBuildBlock)).when(spyGame).sendRequest(any(), any(), any());

        abstractGodCard.initBuild(spyGame);
        assertEquals(1, spyGame.getCellsHandler().getCell(coordBuildBlock).getHeight());
    }

    @Test
    public void findAvailablePositionsToBuildDome() {
        HashMap<Coord, ArrayList<Coord>> availablePositions = abstractGodCard.findAvailablePositionsToBuildDome(spyGame);

        ArrayList<Coord> expectedFirst = new ArrayList<>();
        expectedFirst.add(new Coord(3,2));
        expectedFirst.add(new Coord(3,3));
        expectedFirst.add(new Coord(3,4));
        expectedFirst.add(new Coord(4,4));

        ArrayList<Coord> expectedSecond = new ArrayList<>();
        expectedSecond.add(new Coord(1,0));
        expectedSecond.add(new Coord(2,1));
        expectedSecond.add(new Coord(2,0));
        expectedSecond.add(new Coord(2,2));
        expectedSecond.add(new Coord(1,2));
        expectedSecond.add(new Coord(0,1));
        expectedSecond.add(new Coord(0,2));

        boolean equals = true;
        for (Coord key : availablePositions.keySet()) {
            ArrayList<Coord> actualPositions = availablePositions.get(key);
            if (key.equals(new Coord(4,3))) {
                if (actualPositions.size() != expectedFirst.size() || !(actualPositions.containsAll(expectedFirst))) equals = false;
            }
            if (key.equals(new Coord(1, 1))) {
                if (actualPositions.size() != expectedSecond.size() || !(actualPositions.containsAll(expectedSecond))) equals = false;
            }
        }

        assertTrue(equals);
    }

    @Test
    public void cleanFromEffects() throws ClassNotFoundException {
        abstractGodCard.cleanFromEffects("it.polimi.ingsw.PSP43.server.controllers.decorators.BlockRiseDecorator");

        boolean verified = true;
        while (true) {
            if (!(abstractGodCard instanceof UnconditionedDomeBuildDecorator) && !(abstractGodCard instanceof BasicGodCard)) {
                verified = false;
            }
            if (abstractGodCard instanceof BasicGodCard) break;
            else abstractGodCard = ((PowerGodDecorator) abstractGodCard).getGodComponent();
        }

        assertTrue(verified);
    }
}