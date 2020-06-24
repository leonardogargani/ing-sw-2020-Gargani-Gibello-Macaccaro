package it.polimi.ingsw.PSP43.server.controller.cards.behaviours.buildBehaviours;

import it.polimi.ingsw.PSP43.client.network.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.client.network.networkMessages.ResponseMessage;
import it.polimi.ingsw.PSP43.server.controller.gameStates.GameSessionForTest;
import it.polimi.ingsw.PSP43.server.model.initialisers.DOMCardsParser;
import it.polimi.ingsw.PSP43.server.model.initialisers.GameInitialiser;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.controller.cards.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.controller.cards.BasicGodCard;
import it.polimi.ingsw.PSP43.server.controller.cards.behaviours.moveBehaviours.BasicMoveBehaviour;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException.GameEndedException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

public class DoubleSameSpaceBehaviourTest {
    GameSessionForTest gameSession;
    GameSessionForTest spyGame;
    ArrayList<AbstractGodCard> deck;
    Player currentPlayer;
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

        abstractGodCard = new BasicGodCard("", "", "", new BasicMoveBehaviour(), new DoubleSameSpaceBehaviour());
        spyGame.getCardsHandler().setCardToPlayer(currentPlayer.getNickname(), abstractGodCard);
    }

    @Test
    public void handleInitBuild() throws GameEndedException {

        Integer[] workers = currentPlayer.getWorkersIdsArray();
        Worker workerToBuildTwice;
        int i = 0;
        do {
            workerToBuildTwice = spyGame.getWorkersHandler().getWorker(workers[i]);
            i++;
        } while (i < workers.length && !(workerToBuildTwice.getCurrentPosition().equals(new Coord(4, 3))));

        workerToBuildTwice.setLatestMoved(true);

        Coord coordToBuildTwice = new Coord(3, 3);

        doReturn(new ActionResponse(workerToBuildTwice.getCurrentPosition(), coordToBuildTwice),
                new ResponseMessage(true)).when(spyGame).sendRequest(any(), any(), any());

        spyGame.getCardsHandler().getPlayerCard(currentPlayer.getNickname()).initBuild(spyGame);

        assertEquals(2, spyGame.getCellsHandler().getCell(coordToBuildTwice).getHeight());
    }
}