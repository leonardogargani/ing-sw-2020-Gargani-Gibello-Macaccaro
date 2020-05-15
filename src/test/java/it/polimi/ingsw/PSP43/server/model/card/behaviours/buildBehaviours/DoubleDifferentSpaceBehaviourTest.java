package it.polimi.ingsw.PSP43.server.model.card.behaviours.buildBehaviours;

import it.polimi.ingsw.PSP43.client.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.client.networkMessages.ResponseMessage;
import it.polimi.ingsw.PSP43.server.BoardObserver;
import it.polimi.ingsw.PSP43.server.DataToBuild;
import it.polimi.ingsw.PSP43.server.DataToMove;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.initialisers.DOMCardsParser;
import it.polimi.ingsw.PSP43.server.initialisers.GameInitialiser;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameEndedException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

public class DoubleDifferentSpaceBehaviourTest {
    GameSession gameSession;
    GameSession spyGame;
    BoardObserver obs;
    BoardObserver spyObs;
    ArrayList<AbstractGodCard> deck;

    @Before
    public void setUp() throws Exception {
        gameSession = GameInitialiser.initialiseGame();
        GameInitialiser.initialisePlayers(gameSession);
        GameInitialiser.initialiseWorkers(gameSession);
        spyGame = Mockito.spy(gameSession);
        obs = new BoardObserver(gameSession);
        spyObs = Mockito.spy(obs);
        deck = DOMCardsParser.buildDeck();
    }


    @Test
    public void handleBuildBlock() throws IOException, InterruptedException, GameEndedException, ClassNotFoundException {
        doNothing().when(spyGame).sendMessage(any(), any());

        Player currentPlayer = spyGame.getPlayersHandler().getPlayer(0);

        for (AbstractGodCard c : deck) {
            if (c.getGodName().equals("Demeter")) {
                spyGame.getCardsHandler().setCardToPlayer(currentPlayer.getNickname(), c.getGodName());
                currentPlayer.setAbstractGodCard(c);
            }
        }

        Integer[] workers = currentPlayer.getWorkersIdsArray();
        Worker workerToCheck;
        int i=0;
        do {
            workerToCheck = spyGame.getWorkersHandler().getWorker(workers[i]);
            i++;
        } while (i<workers.length && !(workerToCheck.getCurrentPosition().equals(new Coord(4, 2))));

        Coord secondBuild = new Coord(3, 2);
        doReturn(new ResponseMessage(true),
                new ActionResponse(workerToCheck.getCurrentPosition(), secondBuild)).when(spyGame).sendRequest(any(), any(), any());

        Coord firstBuild = new Coord(3, 3);
        currentPlayer.getAbstractGodCard().buildBlock(new DataToBuild(spyGame, currentPlayer, workerToCheck, firstBuild, Boolean.FALSE));
        assertEquals(1, spyGame.getCellsHandler().getCell(secondBuild).getHeight());
        assertEquals(1, spyGame.getCellsHandler().getCell(firstBuild).getHeight());
    }
}