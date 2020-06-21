package it.polimi.ingsw.PSP43.server.controller.cards.decorators;

import it.polimi.ingsw.PSP43.client.network.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.server.controller.gameStates.GameSessionForTest;
import it.polimi.ingsw.PSP43.server.model.initialisers.GameInitialiser;
import it.polimi.ingsw.PSP43.server.model.*;
import it.polimi.ingsw.PSP43.server.controller.cards.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.controller.cards.BasicGodCard;
import it.polimi.ingsw.PSP43.server.controller.cards.behaviours.buildBehaviours.BasicBuildBehaviour;
import it.polimi.ingsw.PSP43.server.controller.cards.behaviours.moveBehaviours.BasicMoveBehaviour;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException.GameLostException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class WinTwoFloorsFallDecoratorTest {
    GameSessionForTest gameSession;
    GameSessionForTest spyGame;
    Player currentPlayer;
    AbstractGodCard abstractGodCard;
    Worker workerToMove;

    @Before
    public void setUp() throws Exception {
        gameSession = new GameSessionForTest(9);
        GameInitialiser.initialisePlayers(gameSession);
        GameInitialiser.initialiseWorkers(gameSession);
        GameInitialiser.initialiseCards(gameSession);
        spyGame = spy(gameSession);

        currentPlayer = spyGame.getPlayersHandler().getPlayer(0);
        spyGame.setCurrentPlayer(currentPlayer);

        abstractGodCard = new WinTwoFloorsFallDecorator(new BasicGodCard("", "", "", new BasicMoveBehaviour(), new BasicBuildBehaviour()));
        spyGame.getCardsHandler().setCardToPlayer(currentPlayer.getNickname(), abstractGodCard);

        workerToMove = spyGame.getWorkersHandler().getWorker(new Coord(4, 3));
    }

    @Test
    public void checkConditionsOfWinTest() throws GameEndedException {
        Coord initialWorkerCoord = workerToMove.getCurrentPosition();
        Cell initialWorkerCell = spyGame.getCellsHandler().getCell(initialWorkerCoord);
        initialWorkerCell.setHeight(3);
        spyGame.getCellsHandler().changeStateOfCell(initialWorkerCell, initialWorkerCoord);

        Coord coordToMove = new Coord(3, 3);
        Cell cellToMove = spyGame.getCellsHandler().getCell(coordToMove);
        cellToMove.setHeight(0);
        spyGame.getCellsHandler().changeStateOfCell(cellToMove, coordToMove);

        doReturn(new ActionResponse(workerToMove.getCurrentPosition(), coordToMove)).when(spyGame).sendRequest(any(), any(), any());

        try {
            abstractGodCard.initMove(spyGame);
        } catch (GameLostException | GameEndedException e) { e.printStackTrace(); }

        assertTrue(abstractGodCard.checkConditionsToWin(spyGame));

        initialWorkerCoord = workerToMove.getCurrentPosition();
        initialWorkerCell = spyGame.getCellsHandler().getCell(initialWorkerCoord);
        initialWorkerCell.setHeight(2);
        spyGame.getCellsHandler().changeStateOfCell(initialWorkerCell, initialWorkerCoord);

        coordToMove = new Coord(4, 3);
        cellToMove = spyGame.getCellsHandler().getCell(coordToMove);
        cellToMove.setHeight(3);
        spyGame.getCellsHandler().changeStateOfCell(cellToMove, coordToMove);

        doReturn(new ActionResponse(workerToMove.getCurrentPosition(), coordToMove)).when(spyGame).sendRequest(any(), any(), any());

        try {
            abstractGodCard.initMove(spyGame);
        } catch (GameLostException | GameEndedException e) { e.printStackTrace(); }

        assertTrue(abstractGodCard.checkConditionsToWin(spyGame));
    }

    @Test
    public void cleanFromEffects() {
        abstractGodCard.cleanFromEffects("it.polimi.ingsw.PSP43.server.controller.cards.decorators.BlockRiseDecorator");

        boolean verified = true;
        while (true) {
            if (!(abstractGodCard instanceof WinTwoFloorsFallDecorator) && !(abstractGodCard instanceof BasicGodCard)) {
                verified = false;
            }
            if (abstractGodCard instanceof BasicGodCard) break;
            else abstractGodCard = ((PowerGodDecorator) abstractGodCard).getGodComponent();
        }

        assertTrue(verified);
    }
}