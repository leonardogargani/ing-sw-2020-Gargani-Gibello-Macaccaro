package it.polimi.ingsw.PSP43.server.model.card.behaviours.moveBehaviours;

import it.polimi.ingsw.PSP43.client.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.client.networkMessages.ResponseMessage;
import it.polimi.ingsw.PSP43.server.BoardObserver;
import it.polimi.ingsw.PSP43.server.DataToMove;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.initialisers.DOMCardsParser;
import it.polimi.ingsw.PSP43.server.initialisers.GameInitialiser;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.model.card.BasicGodCard;
import it.polimi.ingsw.PSP43.server.model.card.behaviours.buildBehaviours.BasicBuildBehaviour;
import it.polimi.ingsw.PSP43.server.model.card.decorators.BlockRiseDecorator;
import it.polimi.ingsw.PSP43.server.modelHandlers.WorkersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.modelHandlersException.NicknameAlreadyInUseException;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BuildBeforeMoveBehaviourTest {
    GameSession gameSession;
    GameSession spyGame;
    ArrayList<AbstractGodCard> deck;

    @Before
    public void setUp() throws Exception {
        gameSession = new GameSession(9);
        GameInitialiser.initialisePlayers(gameSession);
        GameInitialiser.initialiseWorkers(gameSession);
        GameInitialiser.initialiseCards(gameSession);
        spyGame = spy(gameSession);
        deck = DOMCardsParser.buildDeck();

        Player currentPlayer = spyGame.getPlayersHandler().getPlayer(0);
        spyGame.setCurrentPlayer(currentPlayer);
    }

    /**
     * This test is made to check the private method "buildBeforeMove(DataToMove oldData)" of the class,
     * which is the one that has to give the opportunity at runtime to the player to build before moving (here
     * tested the build of a block),
     * respecting certain conditions (no build in before-move position of the worker and no build in after-move
     * position of the worker). The method doesn't check if the move is successful because it is
     * already checked in the superclass method "move(...)".
     */
    @Test
    public void handleInitMoveWithBuildBlock() throws ClassNotFoundException, WinnerCaughtException, InterruptedException, IOException, GameEndedException {
        Player currentPlayer = spyGame.getPlayersHandler().getPlayer(0);
        Integer[] playerWorkers = currentPlayer.getWorkersIdsArray();
        Worker workerToBuild = null;
        for (Integer playerWorker : playerWorkers) {
            workerToBuild = spyGame.getWorkersHandler().getWorker(playerWorker);
            if (workerToBuild.getCurrentPosition().equals(new Coord(4, 3))) break;
        }

        assert workerToBuild != null;
        doReturn(new ActionResponse(workerToBuild.getCurrentPosition(), new Coord(3, 2)),
                new ResponseMessage(true),
                new ActionResponse(workerToBuild.getCurrentPosition(), new Coord(3, 3))).when(spyGame).sendRequest(any(), any(), any());

        BuildBeforeMoveBehaviour spyBehaviour = spy(new BuildBeforeMoveBehaviour());
        AbstractGodCard spyCard = spy(new BasicGodCard("", "", "", spyBehaviour, new BasicBuildBehaviour()));

        spyGame.getCurrentPlayer().setAbstractGodCard(spyCard);
        doReturn(new HashMap<Coord, ArrayList<Coord>>()).when(spyBehaviour).findAvailablePositionsToBuildBlock(any(), any());
        doReturn(new HashMap<Coord, ArrayList<Coord>>()).when(spyBehaviour).findAvailablePositionsToBuildDome(any(), any());
        doReturn(new HashMap<Coord, ArrayList<Coord>>()).when(spyBehaviour).findAvailablePositionsToMove(any());
        doNothing().when(spyGame).sendBroadCast(any());

        spyCard.initMove(spyGame);

        assertEquals(1, spyGame.getCellsHandler().getCell(new Coord(3, 3)).getHeight());
    }

    /**
     * This test is made to check the private method "buildBeforeMove(DataToMove oldData)" of the class,
     * which is the one that has to give the opportunity at runtime to the player to build before moving (here
     * tested the build of a dome),
     * respecting certain conditions (no build in before-move position of the worker and no build in after-move
     * position of the worker). The method doesn't check if the move is successful because it is
     * already checked in the superclass method "move(...)".
     */
    @Test
    public void handleInitMoveWithBuildDome() throws ClassNotFoundException, WinnerCaughtException, InterruptedException, IOException, GameEndedException {
        Player currentPlayer = spyGame.getPlayersHandler().getPlayer(0);
        Integer[] playerWorkers = currentPlayer.getWorkersIdsArray();
        Worker workerToBuild = null;
        for (Integer playerWorker : playerWorkers) {
            workerToBuild = spyGame.getWorkersHandler().getWorker(playerWorker);
            if (workerToBuild.getCurrentPosition().equals(new Coord(4, 3))) break;
        }

        Coord newThreeLevelCoord = new Coord(3, 3);
        Cell newThreeLevelCell = new Cell(newThreeLevelCoord, gameSession.getBoardObserver());
        newThreeLevelCell.setHeight(3);
        gameSession.getCellsHandler().changeStateOfCell(newThreeLevelCell, newThreeLevelCoord);

        assert workerToBuild != null;
        doReturn(new ActionResponse(workerToBuild.getCurrentPosition(), new Coord(3, 2)),
                new ResponseMessage(true), new ResponseMessage(true),
                new ActionResponse(workerToBuild.getCurrentPosition(), new Coord(3, 3))).when(spyGame).sendRequest(any(), any(), any());

        BuildBeforeMoveBehaviour spyBehaviour = spy(new BuildBeforeMoveBehaviour());
        AbstractGodCard spyCard = spy(new BasicGodCard("", "", "", spyBehaviour, new BasicBuildBehaviour()));

        doReturn(new HashMap<Coord, ArrayList<Coord>>()).when(spyBehaviour).findAvailablePositionsToBuildBlock(any(), any());
        doReturn(new HashMap<Coord, ArrayList<Coord>>()).when(spyBehaviour).findAvailablePositionsToMove(any());

        assert spyCard != null;
        spyCard.initMove(spyGame);

        assertTrue(spyGame.getCellsHandler().getCell(new Coord(3, 3)).getOccupiedByDome());
    }

    @Test
    public void findAvailablePositionsToBuildBlock() {
    }

    @Test
    public void findAvailablePositionsToBuildDome() {
    }

    public static class BlockOpponentRiseBehaviourTest {
        GameSession gameSession;
        GameSession spyGame;
        BoardObserver obs;
        BoardObserver spyObs;
        ArrayList<AbstractGodCard> deck;

        @Before
        public void setUp() throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException, NicknameAlreadyInUseException {
            gameSession = GameInitialiser.initialiseGame();
            GameInitialiser.initialisePlayers(gameSession);
            GameInitialiser.initialiseWorkers(gameSession);
            GameInitialiser.initialiseCards(gameSession);
            spyGame = Mockito.spy(gameSession);
            obs = new BoardObserver(gameSession);
            spyObs = Mockito.spy(obs);
            deck = DOMCardsParser.buildDeck();
        }

        @Test
        public void handleMoveWithNoBlocking() throws IOException, WinnerCaughtException, InterruptedException, ClassNotFoundException, GameEndedException {
            Player currentPlayer = gameSession.getPlayersHandler().getPlayer("Gibi");
            String nickWithAthena = "Gibi";
            gameSession.getCardsHandler().setCardToPlayer(nickWithAthena, "Athena");
            doNothing().when(spyGame).sendMessage(any(), any());

            BlockOpponentRiseBehaviour spyAthenaBehaviour = Mockito.spy(new BlockOpponentRiseBehaviour());

            HashMap<String, AbstractGodCard> map = gameSession.getCardsHandler().getMapOwnersCard();
            for (String s : map.keySet()) {
                if (!(s.equals(nickWithAthena))) {
                    AbstractGodCard card = map.get(s);
                    map.put(s, new BlockRiseDecorator(card));
                }
            }

            doNothing().when(spyAthenaBehaviour).move(any());

            Worker athenasWorker = gameSession.getWorkersHandler().getWorker(new Coord(4, 3));
            athenasWorker.setCurrentPosition(new Coord(2, 2));
            athenasWorker.setCurrentPosition(new Coord(2,3));

            Coord newCoord = new Coord(2, 3);
            Cell newCell = gameSession.getCellsHandler().getCell(newCoord);
            newCell.setHeight(0);
            gameSession.getCellsHandler().changeStateOfCell(newCell, newCoord);
            DataToMove data = new DataToMove(spyGame, currentPlayer, athenasWorker, newCoord);
            spyAthenaBehaviour.handleInitMove(spyGame);

            boolean right = true;
            map = gameSession.getCardsHandler().getMapOwnersCard();
            for (String s : map.keySet()) {
                AbstractGodCard c = map.get(s);
                if ((c.getGodName().equals("Athena"))) {
                    if ((c instanceof BlockRiseDecorator))
                        right = false;
                }
            }

            assertTrue(right);
        }

        @Test
        public void handleMoveWithBlocking() throws IOException, WinnerCaughtException, InterruptedException, ClassNotFoundException, GameEndedException {
            doNothing().when(spyGame).sendMessage(any(), any());

            Player currentPlayer = gameSession.getPlayersHandler().getPlayer("Gibi");
            String nickWithAthena = "Gibi";
            gameSession.getCardsHandler().setCardToPlayer(nickWithAthena, "Athena");

            gameSession.getCardsHandler().setCardToPlayer("Rob", "Minotaur");

            BlockOpponentRiseBehaviour spyAthenaBehaviour = Mockito.spy(new BlockOpponentRiseBehaviour());
            doNothing().when(spyAthenaBehaviour).move(any());

            Worker athenasWorker = gameSession.getWorkersHandler().getWorker(new Coord(4, 3));
            athenasWorker.setCurrentPosition(new Coord(2, 2));
            athenasWorker.setCurrentPosition(new Coord(2,3));

            Coord newCoord = new Coord(2, 3);
            Cell newCell = gameSession.getCellsHandler().getCell(newCoord);
            newCell.setHeight(1);
            gameSession.getCellsHandler().changeStateOfCell(newCell, newCoord);
            DataToMove data = new DataToMove(spyGame, currentPlayer, athenasWorker, newCoord);
            spyAthenaBehaviour.handleInitMove(gameSession);

            boolean right = true;
            HashMap<String, AbstractGodCard> map = gameSession.getCardsHandler().getMapOwnersCard();
            for (String s : map.keySet()) {
                AbstractGodCard c = map.get(s);
                if (!(c.getGodName().equals("Athena"))) {
                    if (!(c instanceof BlockRiseDecorator)) right = false;
                }
            }

            assertTrue(right);
        }
    }

    public static class DoubleMoveBehaviourTest {
        GameSession gameSession;
        GameSession spyGame;
        ArrayList<AbstractGodCard> deck;
        BasicGodCard artemis;
        DoubleMoveBehaviour doubleMoveBehaviour;
        DoubleMoveBehaviour spyMoveBehaviour;
        Worker workerToMoveTwice;
        Player currentPlayer;

        @Before
        public void setUp() throws Exception {
            gameSession = GameInitialiser.initialiseGame();
            GameInitialiser.initialisePlayers(gameSession);
            GameInitialiser.initialiseWorkers(gameSession);
            spyGame = spy(gameSession);

            deck = DOMCardsParser.buildDeck();
            doubleMoveBehaviour = new DoubleMoveBehaviour();
            spyMoveBehaviour = spy(doubleMoveBehaviour);
            artemis = new BasicGodCard("", "", "", spyMoveBehaviour, null);

            currentPlayer = gameSession.getPlayersHandler().getPlayer("Gibi");

            Integer[] workersIds = currentPlayer.getWorkersIdsArray();
            workerToMoveTwice = null;
            for (int workersId : workersIds) {
                Worker actualWorker = gameSession.getWorkersHandler().getWorker(workersId);
                if (actualWorker.getCurrentPosition().equals(new Coord(4, 3)))
                    workerToMoveTwice = actualWorker;
            }
        }

        /**
         * This method tests if the two sub-sequent moves are done in the right way.
         */
        @Test
        public void handleMove() throws ClassNotFoundException, GameEndedException, InterruptedException, IOException, WinnerCaughtException {
            assert workerToMoveTwice != null;
            Coord initialPosition = workerToMoveTwice.getCurrentPosition();
            Coord firstMove = new Coord(3,3);
            Coord secondMove = new Coord(2, 3);

            doReturn(null).when(spyMoveBehaviour).findAvailablePositionsToMove(any());
            doReturn(new ResponseMessage(true),
                    new ActionResponse(firstMove, secondMove)).when(spyGame).sendRequest(any(), any(), any());

            artemis.move(new DataToMove(spyGame, currentPlayer, workerToMoveTwice, firstMove));

            assertFalse(gameSession.getCellsHandler().getCell(initialPosition).getOccupiedByWorker());
            assertFalse(gameSession.getCellsHandler().getCell(firstMove).getOccupiedByWorker());
            assertTrue(gameSession.getCellsHandler().getCell(secondMove).getOccupiedByWorker());
            WorkersHandler workersHandler = gameSession.getWorkersHandler();
            assertEquals(workersHandler.getWorker(workerToMoveTwice.getId()).getCurrentPosition(), secondMove);
            assertEquals(workersHandler.getWorker(workerToMoveTwice.getId()).getPreviousPosition(), firstMove);
        }

        /**
         * This method tests that it is not possible for a player to move his worker back to the previous position.
         */
        @Test
        public void findAvailablePositionsToMove() throws IOException {
            ArrayList<Worker> workers = new ArrayList<>();
            workers.add(workerToMoveTwice);

            gameSession.getWorkersHandler().changePosition(workerToMoveTwice, new Coord(3,3));
            Worker updatedWorkerToMoveTwice = spyGame.getWorkersHandler().getWorker(workerToMoveTwice.getId());
            HashMap<Coord, ArrayList<Coord>> actualHashMap = spyMoveBehaviour.findAvailablePositionsToMove(spyGame);

            for (Coord keyCoord : actualHashMap.keySet()) {
                ArrayList<Coord> positions = actualHashMap.get(keyCoord);
                for (Coord c : positions) assertFalse(c.equals(updatedWorkerToMoveTwice.getPreviousPosition()));
            }
        }
    }
}