package it.polimi.ingsw.PSP43.server.controller.cards.behaviours.moveBehaviours;

import it.polimi.ingsw.PSP43.client.network.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.client.network.networkMessages.ResponseMessage;
import it.polimi.ingsw.PSP43.server.controller.cards.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.controller.cards.BasicGodCard;
import it.polimi.ingsw.PSP43.server.controller.cards.behaviours.buildBehaviours.BasicBuildBehaviour;
import it.polimi.ingsw.PSP43.server.controller.gameStates.GameSessionForTest;
import it.polimi.ingsw.PSP43.server.model.initialisers.GameInitialiser;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException.GameLostException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class InfiniteMovesOnPerimeterBehaviourTest {
    GameSessionForTest gameSessionForTest;
    GameSessionForTest spyGame;
    AbstractGodCard abstractGodCard;
    Player currentPlayer;

    @Before
    public void setUp() throws Exception {
        gameSessionForTest = new GameSessionForTest(9);
        GameInitialiser.initialisePlayers(gameSessionForTest);
        GameInitialiser.initialiseWorkers(gameSessionForTest);
        GameInitialiser.initialiseCards(gameSessionForTest);
        spyGame = spy(gameSessionForTest);

        currentPlayer = spyGame.getPlayersHandler().getPlayer(0);
        spyGame.setCurrentPlayer(currentPlayer);

        abstractGodCard = new BasicGodCard("", "", "", new InfiniteMovesOnPerimeterBehaviour(), new BasicBuildBehaviour());
        spyGame.getCardsHandler().setCardToPlayer(currentPlayer.getNickname(), abstractGodCard);
    }

    @Test
    public void initMove() throws GameEndedException, GameLostException {
        Worker workerMoved = spyGame.getWorkersHandler().getWorker(new Coord(4, 3));
        Coord coordFirstMove = new Coord(4,4);
        Coord coordSecondMove = new Coord(3, 2);

        doReturn(new ActionResponse(workerMoved.getCurrentPosition(), coordFirstMove),
                new ResponseMessage(true),
                new ActionResponse(coordFirstMove, coordSecondMove)).when(spyGame).
            sendRequest(any(), any(), any());

        abstractGodCard.initMove(spyGame);

        workerMoved = spyGame.getWorkersHandler().getWorker(coordSecondMove);
        assertEquals(workerMoved.getCurrentPosition(), coordSecondMove);
    }
}