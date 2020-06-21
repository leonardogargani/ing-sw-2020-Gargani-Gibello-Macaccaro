package it.polimi.ingsw.PSP43.server.controller.cards.behaviours.moveBehaviours;

import it.polimi.ingsw.PSP43.client.network.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.server.controller.gameStates.GameSessionForTest;
import it.polimi.ingsw.PSP43.server.model.initialisers.DOMCardsParser;
import it.polimi.ingsw.PSP43.server.model.initialisers.GameInitialiser;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.controller.cards.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.controller.cards.BasicGodCard;
import it.polimi.ingsw.PSP43.server.controller.cards.behaviours.buildBehaviours.BasicBuildBehaviour;
import it.polimi.ingsw.PSP43.server.controller.cards.decorators.BlockRiseDecorator;
import it.polimi.ingsw.PSP43.server.controller.cards.BlockRiseFactory;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException.GameLostException;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException.NicknameAlreadyInUseException;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException.WinnerCaughtException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BlockOpponentRiseBehaviourTest {
    GameSessionForTest gameSession;
    GameSessionForTest spyGame;
    ArrayList<AbstractGodCard> deck;
    Player currentPlayer;
    AbstractGodCard abstractGodCard;

    @Before
    public void setUp() throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException, NicknameAlreadyInUseException {
        gameSession = GameInitialiser.initialiseGame();
        GameInitialiser.initialisePlayers(gameSession);
        GameInitialiser.initialiseWorkers(gameSession);
        GameInitialiser.initialiseCards(gameSession);
        spyGame = Mockito.spy(gameSession);
        deck = DOMCardsParser.buildDeck();

        currentPlayer = spyGame.getPlayersHandler().getPlayer("Gibi");
        spyGame.setCurrentPlayer(currentPlayer);

        abstractGodCard = new BasicGodCard("", "", "", new BlockOpponentRiseBehaviour(), new BasicBuildBehaviour());
        spyGame.getCardsHandler().setCardToPlayer(currentPlayer.getNickname(), abstractGodCard);
    }

    @Test
    public void handleInitMoveWithNoBlocking() throws WinnerCaughtException, GameEndedException, GameLostException {
        Map<String, AbstractGodCard> map = gameSession.getCardsHandler().getMapOwnersCard();

        Worker athenasWorker = gameSession.getWorkersHandler().getWorker(new Coord(4, 3));

        Coord coordToMove = new Coord(3, 3);

        doReturn(new ActionResponse(athenasWorker.getCurrentPosition(), coordToMove)).when(spyGame).sendRequest(any(), any(), any());

        for (String s : map.keySet()) {
            AbstractGodCard c = map.get(s);
            if (!(c.getGodName().equals("Athena"))) {
                gameSession.getCardsHandler().addDecorator("Athena", new BlockRiseFactory());
            }
        }

        abstractGodCard.initMove(spyGame);

        boolean right = true;
        for (String s : map.keySet()) {
            AbstractGodCard c = map.get(s);
            if (!(c.getGodName().equals("Athena"))) {
                if ((c instanceof BlockRiseDecorator))
                    right = false;
            }
        }

        assertTrue(right);
    }

    @Test
    public void handleMoveWithBlocking() throws GameEndedException, GameLostException {
        spyGame.setCurrentPlayer(currentPlayer);

        Worker athenasWorker = gameSession.getWorkersHandler().getWorker(new Coord(4, 3));
        Cell workerCell = spyGame.getCellsHandler().getCell(athenasWorker.getCurrentPosition());

        Coord coordToMove = new Coord(3, 3);
        Cell newCell = gameSession.getCellsHandler().getCell(coordToMove);
        newCell.setHeight(workerCell.getHeight() + 1);
        spyGame.getCellsHandler().changeStateOfCell(newCell, coordToMove);

        doReturn(new ActionResponse(athenasWorker.getCurrentPosition(), coordToMove)).when(spyGame).sendRequest(any(), any(), any());

        abstractGodCard.initMove(spyGame);

        Map<String, AbstractGodCard> map = gameSession.getCardsHandler().getMapOwnersCard();
        boolean right = true;
        for (String s : map.keySet()) {
            AbstractGodCard c = map.get(s);
            if (!(c.getGodName().equals("Athena"))) {
                if (!(c instanceof BlockRiseDecorator))
                    right = false;
            }
        }

        assertTrue(right);
    }
}