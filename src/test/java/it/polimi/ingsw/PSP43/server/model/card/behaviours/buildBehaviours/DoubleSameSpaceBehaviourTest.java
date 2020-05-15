package it.polimi.ingsw.PSP43.server.model.card.behaviours.buildBehaviours;

import it.polimi.ingsw.PSP43.client.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.client.networkMessages.ResponseMessage;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.initialisers.DOMCardsParser;
import it.polimi.ingsw.PSP43.server.initialisers.GameInitialiser;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.model.card.BasicGodCard;
import it.polimi.ingsw.PSP43.server.model.card.behaviours.moveBehaviours.BasicMoveBehaviour;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameEndedException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

public class DoubleSameSpaceBehaviourTest {
    GameSession gameSession;
    GameSession spyGame;
    ArrayList<AbstractGodCard> deck;
    Player currentPlayer;

    @Before
    public void setUp() throws Exception {
        gameSession = GameInitialiser.initialiseGame();
        GameInitialiser.initialisePlayers(gameSession);
        GameInitialiser.initialiseWorkers(gameSession);
        spyGame = Mockito.spy(gameSession);
        deck = DOMCardsParser.buildDeck();
        currentPlayer = spyGame.getPlayersHandler().getPlayer("Gibi");
        spyGame.setCurrentPlayer(currentPlayer);
    }

    @Test
    public void handleInitBuild() throws ClassNotFoundException, GameEndedException, InterruptedException, IOException {
        currentPlayer.setAbstractGodCard(new BasicGodCard("", "", "", new BasicMoveBehaviour(), new DoubleSameSpaceBehaviour()));

        Integer[] workers = currentPlayer.getWorkersIdsArray();
        Worker workerToBuildTwice;
        int i = 0;
        do {
            workerToBuildTwice = spyGame.getWorkersHandler().getWorker(workers[i]);
            i++;
        } while (i < workers.length && !(workerToBuildTwice.getCurrentPosition().equals(new Coord(4, 3))));

        Coord coordToBuildTwice = new Coord(3, 3);

        doReturn(new ActionResponse(workerToBuildTwice.getCurrentPosition(), coordToBuildTwice),
                new ResponseMessage(true)).when(spyGame).sendRequest(any(), any(), any());

        currentPlayer.getAbstractGodCard().initBuild(spyGame);

        assertEquals(2, spyGame.getCellsHandler().getCell(coordToBuildTwice).getHeight());
    }
}