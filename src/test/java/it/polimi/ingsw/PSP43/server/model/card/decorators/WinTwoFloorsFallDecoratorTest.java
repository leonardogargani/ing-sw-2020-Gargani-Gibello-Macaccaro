package it.polimi.ingsw.PSP43.server.model.card.decorators;

import it.polimi.ingsw.PSP43.client.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.initialisers.DOMCardsParser;
import it.polimi.ingsw.PSP43.server.initialisers.GameInitialiser;
import it.polimi.ingsw.PSP43.server.model.*;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.model.card.BasicGodCard;
import it.polimi.ingsw.PSP43.server.model.card.behaviours.buildBehaviours.BasicBuildBehaviour;
import it.polimi.ingsw.PSP43.server.model.card.behaviours.moveBehaviours.BasicMoveBehaviour;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameLostException;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class WinTwoFloorsFallDecoratorTest {
    GameSession gameSession;
    GameSession spyGame;
    Player currentPlayer;
    AbstractGodCard abstractGodCard;
    Worker workerToMove;

    @Before
    public void setUp() throws Exception {
        gameSession = new GameSession(9);
        GameInitialiser.initialisePlayers(gameSession);
        GameInitialiser.initialiseWorkers(gameSession);
        GameInitialiser.initialiseCards(gameSession);
        spyGame = spy(gameSession);

        currentPlayer = spyGame.getPlayersHandler().getPlayer(0);

        abstractGodCard = new WinTwoFloorsFallDecorator(new BasicGodCard("", "", "", new BasicMoveBehaviour(), new BasicBuildBehaviour()));
        currentPlayer.setAbstractGodCard(abstractGodCard);
        spyGame.setCurrentPlayer(currentPlayer);

        workerToMove = spyGame.getWorkersHandler().getWorker(new Coord(4, 3));
    }

    @Test
    public void checkConditionsOfWinTest() throws GameEndedException {
        Coord workerCoord = workerToMove.getCurrentPosition();
        Cell workerCell = spyGame.getCellsHandler().getCell(workerCoord);
        workerCell.setHeight(3);
        spyGame.getCellsHandler().changeStateOfCell(workerCell, workerCoord);

        Coord coordToMove = new Coord(3, 3);
        Cell cellToMove = spyGame.getCellsHandler().getCell(coordToMove);
        cellToMove.setHeight(0);
        spyGame.getCellsHandler().changeStateOfCell(cellToMove, coordToMove);

        doReturn(new ActionResponse(workerToMove.getCurrentPosition(), coordToMove)).when(spyGame).sendRequest(any(), any(), any());

        try {
            abstractGodCard.initMove(spyGame);
        } catch (GameLostException | GameEndedException e) { e.printStackTrace(); }

        assertTrue(abstractGodCard.checkConditionsToWin(spyGame));
    }

    @Test
    public void cleanFromEffects() {
        abstractGodCard.cleanFromEffects("it.polimi.ingsw.PSP43.server.model.card.decorators.BlockRiseDecorator");

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