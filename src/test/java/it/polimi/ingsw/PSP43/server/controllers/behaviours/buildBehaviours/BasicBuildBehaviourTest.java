package it.polimi.ingsw.PSP43.server.controllers.behaviours.buildBehaviours;

import it.polimi.ingsw.PSP43.client.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.client.networkMessages.ResponseMessage;
import it.polimi.ingsw.PSP43.server.gameStates.GameSessionForTest;
import it.polimi.ingsw.PSP43.server.model.DataToBuild;
import it.polimi.ingsw.PSP43.server.initialisers.GameInitialiser;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.controllers.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.controllers.BasicGodCard;
import it.polimi.ingsw.PSP43.server.controllers.behaviours.moveBehaviours.BasicMoveBehaviour;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameEndedException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class BasicBuildBehaviourTest {
    GameSessionForTest gameSession;
    GameSessionForTest spyGame;
    AbstractGodCard abstractGodCard;
    Player currentPlayer;

    @Before
    public void setUp() throws Exception {
        gameSession = GameInitialiser.initialiseGame();
        GameInitialiser.initialisePlayers(gameSession);
        GameInitialiser.initialiseWorkers(gameSession);
        spyGame = spy(gameSession);

        abstractGodCard = new BasicGodCard("", "", "", new BasicMoveBehaviour(), new BasicBuildBehaviour());

        currentPlayer = gameSession.getPlayersHandler().getPlayer("Gibi");
        spyGame.setCurrentPlayer(currentPlayer);

        spyGame.getCardsHandler().setCardToPlayer(currentPlayer.getNickname(), abstractGodCard);
    }

    @Test
    public void genericAskForBuildWithBlockOption() throws GameEndedException {
        BasicBuildBehaviour basicBuildBehaviour = new BasicBuildBehaviour();

        Worker workerToBuild = spyGame.getWorkersHandler().getWorker(new Coord(4, 3));
        Coord coordWhereToBuild = new Coord(3, 3);
        doReturn(new ActionResponse(workerToBuild.getCurrentPosition(), coordWhereToBuild)).when(spyGame).sendRequest(any(), any(), any());
        DataToBuild expectedDataToBuild = new DataToBuild(null, null, workerToBuild, coordWhereToBuild, Boolean.FALSE);

        DataToBuild effectiveDataToBuild = basicBuildBehaviour.genericAskForBuild(spyGame);

        assertEquals(expectedDataToBuild.getWorker().getId(), effectiveDataToBuild.getWorker().getId());
        assertEquals(expectedDataToBuild.getBuildDome(), effectiveDataToBuild.getBuildDome());
        assertEquals(expectedDataToBuild.getNewPosition(), effectiveDataToBuild.getNewPosition());
    }

    @Test
    public void genericAskForBuildWithDomeOption() throws GameEndedException {
        Coord coordWhereToBuild = new Coord(3, 3);
        Cell cellWhereToBuild = spyGame.getCellsHandler().getCell(coordWhereToBuild);
        cellWhereToBuild.setHeight(3);
        spyGame.getCellsHandler().changeStateOfCell(cellWhereToBuild, coordWhereToBuild);

        BasicBuildBehaviour basicBuildBehaviour = new BasicBuildBehaviour();

        Worker workerToBuild = spyGame.getWorkersHandler().getWorker(new Coord(4, 3));
        doReturn(new ResponseMessage(true),
                new ActionResponse(workerToBuild.getCurrentPosition(), coordWhereToBuild)).when(spyGame).sendRequest(any(), any(), any());
        DataToBuild expectedDataToBuild = new DataToBuild(null, null, workerToBuild, coordWhereToBuild, Boolean.TRUE);

        DataToBuild effectiveDataToBuild = basicBuildBehaviour.genericAskForBuild(spyGame);

        assertEquals(expectedDataToBuild.getWorker().getId(), effectiveDataToBuild.getWorker().getId());
        assertEquals(expectedDataToBuild.getBuildDome(), effectiveDataToBuild.getBuildDome());
        assertEquals(expectedDataToBuild.getNewPosition(), effectiveDataToBuild.getNewPosition());
    }

    @Test
    public void handleInitBuildWithDome() throws GameEndedException {
        Coord coordWhereToBuild = new Coord(3, 3);
        Cell cellWhereToBuild = spyGame.getCellsHandler().getCell(coordWhereToBuild);
        cellWhereToBuild.setHeight(3);
        spyGame.getCellsHandler().changeStateOfCell(cellWhereToBuild, coordWhereToBuild);

        BasicBuildBehaviour basicBuildBehaviour = new BasicBuildBehaviour();

        Worker workerToBuild = spyGame.getWorkersHandler().getWorker(new Coord(4, 3));
        doReturn(new ResponseMessage(true),
                new ActionResponse(workerToBuild.getCurrentPosition(), coordWhereToBuild)).when(spyGame).sendRequest(any(), any(), any());

        basicBuildBehaviour.handleInitBuild(spyGame);

        assertEquals(4, spyGame.getCellsHandler().getCell(coordWhereToBuild).getHeight());
    }

    @Test
    public void handleInitBuildWithBlock() throws GameEndedException {
        Coord coordWhereToBuild = new Coord(3, 3);

        BasicBuildBehaviour basicBuildBehaviour = new BasicBuildBehaviour();

        Worker workerToBuild = spyGame.getWorkersHandler().getWorker(new Coord(4, 3));
        doReturn(new ActionResponse(workerToBuild.getCurrentPosition(), coordWhereToBuild)).when(spyGame).sendRequest(any(), any(), any());

        basicBuildBehaviour.handleInitBuild(spyGame);

        assertEquals(1, spyGame.getCellsHandler().getCell(coordWhereToBuild).getHeight());
    }
}