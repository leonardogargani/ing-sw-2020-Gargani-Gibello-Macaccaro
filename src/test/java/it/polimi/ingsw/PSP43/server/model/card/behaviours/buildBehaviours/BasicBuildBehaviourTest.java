package it.polimi.ingsw.PSP43.server.model.card.behaviours.buildBehaviours;

import it.polimi.ingsw.PSP43.client.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.client.networkMessages.ResponseMessage;
import it.polimi.ingsw.PSP43.server.model.DataToBuild;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.initialisers.DOMCardsParser;
import it.polimi.ingsw.PSP43.server.initialisers.GameInitialiser;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameEndedException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class BasicBuildBehaviourTest {
    GameSession gameSession;
    GameSession spyGame;
    ArrayList<AbstractGodCard> deck;
    Player currentPlayer;

    @Before
    public void setUp() throws Exception {
        gameSession = GameInitialiser.initialiseGame();
        GameInitialiser.initialisePlayers(gameSession);
        GameInitialiser.initialiseWorkers(gameSession);
        spyGame = spy(gameSession);

        deck = DOMCardsParser.buildDeck();

        currentPlayer = gameSession.getPlayersHandler().getPlayer("Gibi");
        spyGame.setCurrentPlayer(currentPlayer);
    }

    @Test
    public void genericAskForBuildWithBlockOption() throws InterruptedException, ClassNotFoundException, GameEndedException, IOException {
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
    public void genericAskForBuildWithDomeOption() throws InterruptedException, ClassNotFoundException, GameEndedException, IOException {
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
    public void handleInitBuildWithDome() throws IOException, InterruptedException, GameEndedException, ClassNotFoundException {
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
    public void handleInitBuildWithBlock() throws ClassNotFoundException, GameEndedException, InterruptedException, IOException {
        Coord coordWhereToBuild = new Coord(3, 3);

        BasicBuildBehaviour basicBuildBehaviour = new BasicBuildBehaviour();

        Worker workerToBuild = spyGame.getWorkersHandler().getWorker(new Coord(4, 3));
        doReturn(new ActionResponse(workerToBuild.getCurrentPosition(), coordWhereToBuild)).when(spyGame).sendRequest(any(), any(), any());

        basicBuildBehaviour.handleInitBuild(spyGame);

        assertEquals(1, spyGame.getCellsHandler().getCell(coordWhereToBuild).getHeight());
    }
}