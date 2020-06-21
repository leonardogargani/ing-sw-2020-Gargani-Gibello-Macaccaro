package it.polimi.ingsw.PSP43.server.controller.cards.decorators;

import it.polimi.ingsw.PSP43.server.controller.cards.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.controller.cards.BasicGodCard;
import it.polimi.ingsw.PSP43.server.controller.cards.behaviours.buildBehaviours.BasicBuildBehaviour;
import it.polimi.ingsw.PSP43.server.controller.cards.behaviours.moveBehaviours.BasicMoveBehaviour;
import it.polimi.ingsw.PSP43.server.controller.gameStates.GameSessionForTest;
import it.polimi.ingsw.PSP43.server.model.initialisers.GameInitialiser;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.CellsHandler;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.spy;

public class WinFiveDomeDecoratorTest {
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

        abstractGodCard = new WinFiveDomeDecorator(new BasicGodCard("", "", "", new BasicMoveBehaviour(), new BasicBuildBehaviour()));
        spyGame.getCardsHandler().setCardToPlayer(currentPlayer.getNickname(), abstractGodCard);
    }

    @Test
    public void checkConditionsToWin() {
        Worker workerMoved = spyGame.getWorkersHandler().getWorker(0);
        Coord coordAfterMove = new Coord(3, 3);
        Coord coordBeforeMove = workerMoved.getCurrentPosition();
        spyGame.getWorkersHandler().changePosition(workerMoved, coordAfterMove);

        CellsHandler cellsHandler = spyGame.getCellsHandler();
        Cell cellBeforeMove = cellsHandler.getCell(coordBeforeMove);
        cellBeforeMove.setHeight(2);
        cellsHandler.changeStateOfCell(cellBeforeMove, coordBeforeMove);

        Cell cellAfterMove = cellsHandler.getCell(coordAfterMove);
        cellAfterMove.setHeight(3);
        cellsHandler.changeStateOfCell(cellAfterMove, coordAfterMove);

        assertTrue(abstractGodCard.checkConditionsToWin(spyGame));

        Coord zeroCoordDome = new Coord(2, 1);
        Cell zeroCellDome = cellsHandler.getCell(zeroCoordDome);
        zeroCellDome.setOccupiedByDome(true);
        cellsHandler.changeStateOfCell(zeroCellDome, zeroCoordDome);
        Coord firstCoordDome = new Coord(2, 2);
        Cell firstCellDome = cellsHandler.getCell(firstCoordDome);
        firstCellDome.setOccupiedByDome(true);
        cellsHandler.changeStateOfCell(firstCellDome, firstCoordDome);
        Coord secondCoordDome = new Coord(2, 3);
        Cell secondCellDome = cellsHandler.getCell(secondCoordDome);
        secondCellDome.setOccupiedByDome(true);
        cellsHandler.changeStateOfCell(secondCellDome, secondCoordDome);
        Coord thirdCoordDome = new Coord(2, 4);
        Cell thirdCellDome = cellsHandler.getCell(thirdCoordDome);
        thirdCellDome.setOccupiedByDome(true);
        cellsHandler.changeStateOfCell(thirdCellDome, thirdCoordDome);
        Coord fourthCoordDome = new Coord(3, 0);
        Cell fourthCellDome = cellsHandler.getCell(fourthCoordDome);
        fourthCellDome.setOccupiedByDome(true);
        cellsHandler.changeStateOfCell(fourthCellDome, fourthCoordDome);

        assertTrue(abstractGodCard.checkConditionsToWin(spyGame));
    }

    @Test
    public void cleanFromEffects() {
        abstractGodCard.cleanFromEffects("it.polimi.ingsw.PSP43.server.controller.cards.decorators.BlockRiseDecorator");

        boolean verified = true;
        while (true) {
            if (!(abstractGodCard instanceof WinFiveDomeDecorator) && !(abstractGodCard instanceof BasicGodCard)) {
                verified = false;
            }
            if (abstractGodCard instanceof BasicGodCard) break;
            else abstractGodCard = ((PowerGodDecorator) abstractGodCard).getGodComponent();
        }

        assertTrue(verified);
    }
}