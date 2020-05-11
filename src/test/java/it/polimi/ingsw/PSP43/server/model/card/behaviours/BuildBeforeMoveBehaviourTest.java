package it.polimi.ingsw.PSP43.server.model.card.behaviours;

import it.polimi.ingsw.PSP43.client.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.client.networkMessages.ResponseMessage;
import it.polimi.ingsw.PSP43.server.BoardObserver;
import it.polimi.ingsw.PSP43.server.DataToAction;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.initialisers.DOMCardsParser;
import it.polimi.ingsw.PSP43.server.initialisers.GameInitialiser;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class BuildBeforeMoveBehaviourTest {
    GameSession gameSession;
    GameSession spyGame;
    BoardObserver obs;
    BoardObserver spyObs;
    ArrayList<AbstractGodCard> deck;

    @Before
    public void setUp() throws Exception {
        gameSession = new GameSession(9);
        GameInitialiser.initialisePlayers(gameSession);
        GameInitialiser.initialiseWorkers(gameSession);
        spyGame = spy(gameSession);
        deck = DOMCardsParser.buildDeck();

        Player currentPlayer = spyGame.getPlayersHandler().getPlayer(0);
        spyGame.setCurrentPlayer(currentPlayer);

        for (AbstractGodCard c : deck) {
            if (c.getGodName().equals("Prometheus")) spyGame.getPlayersHandler().setCardToPlayer(currentPlayer, c);
        }
    }

    /**
     * This test is made to check the private method "buildBeforeMove(DataToAction oldData)" of the class,
     * which is the one that has to give the opportunity at runtime to the player to build before moving,
     * respecting certain conditions. The method doesn't check if the move is successful because it is
     * already checked in the superclass method.
     */
    @Test
    public void handleMove() throws InterruptedException, IOException, ClassNotFoundException, WinnerCaughtException, GameEndedException {
        Player currentPlayer = spyGame.getPlayersHandler().getPlayer(0);
        int[] playerWorkers = currentPlayer.getWorkersIdsArray();
        Worker workerToBuild = null;
        for (int i=0; i<playerWorkers.length; i++) {
            workerToBuild = spyGame.getWorkersHandler().getWorker(playerWorkers[i]);
            if (workerToBuild.getCurrentPosition().equals(new Coord(4, 3))) break;
        }

        assert workerToBuild != null;
        doReturn(new ResponseMessage(true), new ActionResponse(workerToBuild.getCurrentPosition(), new Coord(3, 2))).when(spyGame).sendRequest(any(), any(), any());

        AbstractGodCard spyCard = spy(currentPlayer.getAbstractGodCard());
        spyCard.move(new DataToAction(spyGame, currentPlayer, workerToBuild, new Coord(3,3)));

        assertEquals(1, spyGame.getCellsHandler().getCell(new Coord(3, 2)).getHeight());
    }
}