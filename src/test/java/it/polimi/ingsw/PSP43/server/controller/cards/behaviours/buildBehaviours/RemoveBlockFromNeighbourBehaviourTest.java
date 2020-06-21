package it.polimi.ingsw.PSP43.server.controller.cards.behaviours.buildBehaviours;

import it.polimi.ingsw.PSP43.client.network.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.client.network.networkMessages.ResponseMessage;
import it.polimi.ingsw.PSP43.server.controller.cards.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.controller.cards.BasicGodCard;
import it.polimi.ingsw.PSP43.server.controller.cards.behaviours.moveBehaviours.BasicMoveBehaviour;
import it.polimi.ingsw.PSP43.server.controller.gameStates.GameSessionForTest;
import it.polimi.ingsw.PSP43.server.model.initialisers.GameInitialiser;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.CellsHandler;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException.GameEndedException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class RemoveBlockFromNeighbourBehaviourTest {
    GameSessionForTest gameSessionForTest;
    GameSessionForTest spyGame;
    AbstractGodCard abstractGodCard;
    Player currentPlayer;
    Worker firstWorker;
    Worker secondWorker;

    @Before
    public void setUp() throws Exception {
        gameSessionForTest = new GameSessionForTest(9);
        GameInitialiser.initialisePlayers(gameSessionForTest);
        GameInitialiser.initialiseCards(gameSessionForTest);
        GameInitialiser.initialiseWorkers(gameSessionForTest);
        spyGame = spy(gameSessionForTest);

        currentPlayer = spyGame.getPlayersHandler().getPlayer(0);
        spyGame.setCurrentPlayer(currentPlayer);

        abstractGodCard = new BasicGodCard("","","",new BasicMoveBehaviour(), new RemoveBlockFromNeighbourBehaviour());
        spyGame.getCardsHandler().setCardToPlayer(currentPlayer.getNickname(), abstractGodCard);

        firstWorker = spyGame.getWorkersHandler().getWorker(new Coord(4, 3));
        secondWorker = spyGame.getWorkersHandler().getWorker(new Coord(1, 1));
    }

    @Test
    public void initBuild() throws GameEndedException {
        Coord coordBuildAndRemove = new Coord(3, 3);
        doReturn(new ActionResponse(firstWorker.getCurrentPosition(), coordBuildAndRemove), new ResponseMessage(true),
                new ActionResponse(firstWorker.getCurrentPosition(), coordBuildAndRemove))
                .when(spyGame).sendRequest(any(), any(), any());

        abstractGodCard.initBuild(spyGame);

        Cell cellWithBlockRemoved = spyGame.getCellsHandler().getCell(coordBuildAndRemove);
        assertEquals(0, cellWithBlockRemoved.getHeight());
    }

    @Test
    public void findPositionsToRemoveBlock() {
        firstWorker.setLatestMoved(false);
        secondWorker.setLatestMoved(false);
        HashMap<Coord, ArrayList<Coord>> arrived;

        CellsHandler cellsHandler = spyGame.getCellsHandler();

        Coord coordFirstBlock = new Coord(0, 1);
        Cell cellFirstBlock = cellsHandler.getCell(coordFirstBlock);
        cellFirstBlock.setHeight(1);
        cellsHandler.changeStateOfCell(cellFirstBlock, coordFirstBlock);

        Coord coordSecondBlock = new Coord(2, 2);
        Cell cellSecondBlock = cellsHandler.getCell(coordSecondBlock);
        cellSecondBlock.setHeight(2);
        cellsHandler.changeStateOfCell(cellSecondBlock, coordSecondBlock);

        Coord coordThirdBlock = new Coord(3, 3);
        Cell cellThirdBlock = cellsHandler.getCell(coordThirdBlock);
        cellThirdBlock.setHeight(3);
        cellsHandler.changeStateOfCell(cellThirdBlock, coordThirdBlock);

        Coord coordFourthBlock = new Coord(4, 4);
        Cell cellFourthBlock = cellsHandler.getCell(coordFourthBlock);
        cellFourthBlock.setOccupiedByDome(true);
        cellsHandler.changeStateOfCell(cellFourthBlock, coordFourthBlock);

        RemoveBlockFromNeighbourBehaviour removeBlockFromNeighbourBehaviour = new RemoveBlockFromNeighbourBehaviour();
        arrived = removeBlockFromNeighbourBehaviour.findPositionsToRemoveBlock(spyGame);

        for (Coord c : arrived.keySet()) {
            if (c.equals(firstWorker.getCurrentPosition())) {
                ArrayList<Coord> positions = arrived.get(c);
                assertTrue(!(positions.contains(coordFourthBlock))
                        && positions.contains(coordThirdBlock) && positions.size() == 1);
            }
            else if (c.equals(secondWorker.getCurrentPosition())){
                ArrayList<Coord> positions = arrived.get(c);
                assertTrue(positions.contains(coordFirstBlock)
                        && positions.contains(coordSecondBlock) && positions.size() == 2);
            }
        }

        assertEquals(2, arrived.size());
    }
}