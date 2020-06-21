package it.polimi.ingsw.PSP43.server.controller.cards.behaviours.buildBehaviours;

import it.polimi.ingsw.PSP43.client.network.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.client.network.networkMessages.ResponseMessage;
import it.polimi.ingsw.PSP43.server.controller.gameStates.GameSessionForTest;
import it.polimi.ingsw.PSP43.server.model.initialisers.DOMCardsParser;
import it.polimi.ingsw.PSP43.server.model.initialisers.GameInitialiser;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.DataToBuild;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.controller.cards.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.controller.cards.BasicGodCard;
import it.polimi.ingsw.PSP43.server.controller.cards.behaviours.moveBehaviours.BasicMoveBehaviour;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException.GameEndedException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

public class DoubleDifferentSpaceBehaviourTest {
    GameSessionForTest gameSession;
    GameSessionForTest spyGame;
    ArrayList<AbstractGodCard> deck;
    Player currentPlayer;
    Worker workerToBuildTwice;
    AbstractGodCard abstractGodCard;

    @Before
    public void setUp() throws Exception {
        gameSession = GameInitialiser.initialiseGame();
        GameInitialiser.initialisePlayers(gameSession);
        GameInitialiser.initialiseWorkers(gameSession);
        spyGame = Mockito.spy(gameSession);
        deck = DOMCardsParser.buildDeck();

        currentPlayer = spyGame.getPlayersHandler().getPlayer("Gibi");
        spyGame.setCurrentPlayer(currentPlayer);

        workerToBuildTwice = spyGame.getWorkersHandler().getWorker(new Coord(4, 3));
        workerToBuildTwice.setLatestMoved(true);

        abstractGodCard = new BasicGodCard("", "", "", new BasicMoveBehaviour(), new DoubleDifferentSpaceBehaviour());
        spyGame.getCardsHandler().setCardToPlayer(currentPlayer.getNickname(), abstractGodCard);
    }


    @Test
    public void handleInitBuild() throws IOException, InterruptedException, GameEndedException, ClassNotFoundException {
        Coord firstBuild = new Coord(3, 3);
        Coord secondBuild = new Coord(3, 2);
        doReturn(new ActionResponse(workerToBuildTwice.getCurrentPosition(), firstBuild),
                new ResponseMessage(true),
                new ActionResponse(workerToBuildTwice.getCurrentPosition(), secondBuild),
                new ActionResponse(workerToBuildTwice.getCurrentPosition(), firstBuild),
                new ResponseMessage(true),
                new ActionResponse(workerToBuildTwice.getCurrentPosition(), secondBuild)).when(spyGame).sendRequest(any(), any(), any());

        abstractGodCard.initBuild(spyGame);
        assertEquals(1, spyGame.getCellsHandler().getCell(secondBuild).getHeight());
        assertEquals(1, spyGame.getCellsHandler().getCell(firstBuild).getHeight());

        abstractGodCard.initBuild(spyGame);
        assertEquals(2, spyGame.getCellsHandler().getCell(secondBuild).getHeight());
        assertEquals(2, spyGame.getCellsHandler().getCell(firstBuild).getHeight());

        doReturn(new ActionResponse(workerToBuildTwice.getCurrentPosition(), firstBuild),
                new ResponseMessage(true), new ResponseMessage(true),
                new ActionResponse(workerToBuildTwice.getCurrentPosition(), firstBuild)).when(spyGame).sendRequest(any(), any(), any());

        abstractGodCard.initBuild(spyGame);
        assertEquals(4, spyGame.getCellsHandler().getCell(firstBuild).getHeight());
        assertTrue(spyGame.getCellsHandler().getCell(firstBuild).getOccupiedByDome());
    }

    @Test
    public void filterAllowedPositions() {
        DoubleDifferentSpaceBehaviour doubleDifferentSpaceBehaviour = new DoubleDifferentSpaceBehaviour();

        Coord coordFirstBuild = new Coord(3, 2);
        DataToBuild oldDataToBuild = new DataToBuild(spyGame, currentPlayer, workerToBuildTwice, coordFirstBuild, Boolean.FALSE);

        HashMap<Coord, ArrayList<Coord>> availablePositionsBuildBlock = abstractGodCard.findAvailablePositionsToBuildBlock(spyGame);

        doubleDifferentSpaceBehaviour.filterAllowedPositions(availablePositionsBuildBlock, oldDataToBuild);

        for (Coord key : availablePositionsBuildBlock.keySet()) {
            assertEquals(key, workerToBuildTwice.getCurrentPosition());

            for (Coord c : availablePositionsBuildBlock.get(key)) {
                assertNotEquals(c, coordFirstBuild);
            }
        }
    }
}