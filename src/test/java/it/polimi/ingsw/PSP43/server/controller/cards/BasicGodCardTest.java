package it.polimi.ingsw.PSP43.server.controller.cards;

import it.polimi.ingsw.PSP43.server.controller.cards.behaviours.buildBehaviours.BasicBuildBehaviour;
import it.polimi.ingsw.PSP43.server.controller.cards.behaviours.moveBehaviours.BasicMoveBehaviour;
import it.polimi.ingsw.PSP43.server.controller.gameStates.GameSessionForTest;
import it.polimi.ingsw.PSP43.server.model.initialisers.GameInitialiser;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.CellsHandler;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.WorkersHandler;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.spy;

public class BasicGodCardTest {
    GameSessionForTest gameSession;
    GameSessionForTest spyGame;
    AbstractGodCard abstractGodCard;
    Player currentPlayer;

    @Before
    public void setUp() throws Exception {
        gameSession = new GameSessionForTest(9);
        GameInitialiser.initialisePlayers(gameSession);
        GameInitialiser.initialiseWorkers(gameSession);
        GameInitialiser.initialiseCards(gameSession);
        spyGame = spy(gameSession);

        currentPlayer = spyGame.getPlayersHandler().getPlayer("Gibi");
        spyGame.setCurrentPlayer(currentPlayer);

        abstractGodCard = new BasicGodCard("", "", "", new BasicMoveBehaviour(), new BasicBuildBehaviour());
        spyGame.getCardsHandler().setCardToPlayer(currentPlayer.getNickname(), abstractGodCard);
    }

    @Test
    public void checkConditionsToWin() {
        CellsHandler cellsHandler = spyGame.getCellsHandler();
        WorkersHandler workersHandler = spyGame.getWorkersHandler();
        Worker workerToMove = spyGame.getWorkersHandler().getWorker(new Coord(4, 3));
        workerToMove.setLatestMoved(true);

        workersHandler.changePosition(workerToMove, new Coord(3,3));

        assertFalse(abstractGodCard.checkConditionsToWin(spyGame));

        Coord coordThirdLevel = new Coord(2,3);
        Cell cellThirdLevel = cellsHandler.getCell(coordThirdLevel);
        cellThirdLevel.setHeight(3);
        cellsHandler.changeStateOfCell(cellThirdLevel, coordThirdLevel);

        Cell workerCell = cellsHandler.getCell(workerToMove.getCurrentPosition());
        workerCell.setHeight(2);
        cellsHandler.changeStateOfCell(workerCell, workerToMove.getCurrentPosition());

        workersHandler.changePosition(workerToMove, coordThirdLevel);

        assertTrue(abstractGodCard.checkConditionsToWin(spyGame));
    }
}