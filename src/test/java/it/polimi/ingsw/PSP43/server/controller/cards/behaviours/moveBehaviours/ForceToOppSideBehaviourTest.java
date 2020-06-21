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
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.WorkersHandler;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException.GameLostException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class ForceToOppSideBehaviourTest {
    GameSessionForTest gameSessionForTest;
    GameSessionForTest spyGame;
    AbstractGodCard abstractGodCard;
    Player currentPlayer;
    Worker forcer;
    Worker forced;

    @Before
    public void setUp() throws Exception {
        gameSessionForTest = new GameSessionForTest(9);
        GameInitialiser.initialisePlayers(gameSessionForTest);
        GameInitialiser.initialiseWorkers(gameSessionForTest);
        GameInitialiser.initialiseCards(gameSessionForTest);
        spyGame = spy(gameSessionForTest);

        currentPlayer = spyGame.getPlayersHandler().getPlayer(0);
        spyGame.setCurrentPlayer(currentPlayer);

        abstractGodCard = new BasicGodCard("", "", "", new ForceToOppSideBehaviour(), new BasicBuildBehaviour());
        spyGame.getCardsHandler().setCardToPlayer(currentPlayer.getNickname(), abstractGodCard);

        forcer = spyGame.getWorkersHandler().getWorker(new Coord(4,3));
        forced = spyGame.getWorkersHandler().getWorker(new Coord(4, 2));
    }

    @Test
    public void initMoveTest() throws GameEndedException, GameLostException {
        WorkersHandler workersHandler = spyGame.getWorkersHandler();
        // here I only check if the method does the forcing properly

        doReturn(  new ResponseMessage(true),
                   new ActionResponse(forcer.getCurrentPosition(), forced.getCurrentPosition())).
                when(spyGame).sendRequest(any(), any(), any());

        abstractGodCard.initMove(spyGame);  // here the forcer is placed in the forcer position because
                                            // I didn't supply another ActionResponse

        forced = workersHandler.getWorker(new Coord(4, 4));
        assertNotNull(forced);

        // here I check the situation in which the player responds that he doesn't want to force anyone
        // or if he cannot force anyone

        Worker firstForcer = workersHandler.getWorker(new Coord(4, 2));
        Worker secondForcer = workersHandler.getWorker(new Coord(1, 1));
        workersHandler.changePosition(firstForcer, new Coord(4, 0));
        workersHandler.changePosition(secondForcer, new Coord(0, 4));

        Worker firstForced = workersHandler.getWorker(new Coord(0, 0));
        Worker secondForced = workersHandler.getWorker(new Coord(4, 4));
        workersHandler.changePosition(firstForced, new Coord(3, 0));
        workersHandler.changePosition(secondForced, new Coord(1, 4));

        ForceToOppSideBehaviour forceToOppSideBehaviour = new ForceToOppSideBehaviour();
        assertNull(forceToOppSideBehaviour.askIfWantToForce(spyGame));

        doReturn(new ResponseMessage(false)).when(spyGame).sendRequest(any(), any(), any());

        assertNull(forceToOppSideBehaviour.askIfWantToForce(spyGame));
    }

    @Test
    public void selectPositionsWorkersToForceTest() {
        // here I see if the method really deletes from the available workers to force the ones that have no positions where
        // to be forced

        Worker wNotPossibleToForce = spyGame.getWorkersHandler().getWorker(new Coord(4, 2));
        spyGame.getWorkersHandler().changePosition(wNotPossibleToForce, new Coord(3, 3));

        ForceToOppSideBehaviour forceToOppSideBehaviour = new ForceToOppSideBehaviour();
        HashMap<Coord, ArrayList<Coord>> effectiveForcers = forceToOppSideBehaviour.selectPositionsWorkersToForce(spyGame.getCellsHandler(), currentPlayer);

        assertFalse(effectiveForcers.containsKey(wNotPossibleToForce.getCurrentPosition()));

        Worker forcerAllowed =  spyGame.getWorkersHandler().getWorker(new Coord(1,1));
        assertTrue(effectiveForcers.get(forcerAllowed.getCurrentPosition()).contains(new Coord(0, 0)) &&
                effectiveForcers.get(forcerAllowed.getCurrentPosition()).size() == 1);
    }
}