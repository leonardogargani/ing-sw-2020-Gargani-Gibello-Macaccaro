package it.polimi.ingsw.PSP43.server.model.card.behaviours.moveBehaviours;

import it.polimi.ingsw.PSP43.client.networkMessages.ActionResponse;
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
import it.polimi.ingsw.PSP43.server.model.card.BlockRiseFactory;
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
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BlockOpponentRiseBehaviourTest {
    GameSession gameSession;
    GameSession spyGame;
    ArrayList<AbstractGodCard> deck;
    Player currentPlayer;

    @Before
    public void setUp() throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException, NicknameAlreadyInUseException {
        gameSession = GameInitialiser.initialiseGame();
        GameInitialiser.initialisePlayers(gameSession);
        GameInitialiser.initialiseWorkers(gameSession);
        GameInitialiser.initialiseCards(gameSession);
        spyGame = Mockito.spy(gameSession);
        deck = DOMCardsParser.buildDeck();

        currentPlayer = spyGame.getPlayersHandler().getPlayer("Gibi");
    }

    @Test
    public void handleInitMoveWithNoBlocking() throws IOException, WinnerCaughtException, InterruptedException, ClassNotFoundException, GameEndedException {
        Map<String, AbstractGodCard> map = gameSession.getCardsHandler().getMapOwnersCard();

        spyGame.setCurrentPlayer(currentPlayer);

        currentPlayer.setAbstractGodCard(new BasicGodCard("", "", "", new BlockOpponentRiseBehaviour(), new BasicBuildBehaviour()));
        spyGame.getCardsHandler().setCardToPlayer("Gibi", "Athena");

        Worker athenasWorker = gameSession.getWorkersHandler().getWorker(new Coord(4, 3));

        Coord coordToMove = new Coord(3, 3);

        doReturn(new ActionResponse(athenasWorker.getCurrentPosition(), coordToMove)).when(spyGame).sendRequest(any(), any(), any());

        for (String s : map.keySet()) {
            AbstractGodCard c = map.get(s);
            if (!(c.getGodName().equals("Athena"))) {
                gameSession.getCardsHandler().addDecorator("Athena", new BlockRiseFactory());
            }
        }

        currentPlayer.getAbstractGodCard().initMove(spyGame);

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
    public void handleMoveWithBlocking() throws IOException, WinnerCaughtException, InterruptedException, ClassNotFoundException, GameEndedException {
        Map<String, AbstractGodCard> map = gameSession.getCardsHandler().getMapOwnersCard();

        spyGame.setCurrentPlayer(currentPlayer);

        currentPlayer.setAbstractGodCard(new BasicGodCard("", "", "", new BlockOpponentRiseBehaviour(), new BasicBuildBehaviour()));
        spyGame.getCardsHandler().setCardToPlayer("Gibi", "Athena");

        Worker athenasWorker = gameSession.getWorkersHandler().getWorker(new Coord(4, 3));
        Cell workerCell = spyGame.getCellsHandler().getCell(athenasWorker.getCurrentPosition());

        Coord coordToMove = new Coord(3, 3);
        Cell newCell = gameSession.getCellsHandler().getCell(coordToMove);
        newCell.setHeight(workerCell.getHeight() + 1);
        spyGame.getCellsHandler().changeStateOfCell(newCell, coordToMove);

        doReturn(new ActionResponse(athenasWorker.getCurrentPosition(), coordToMove)).when(spyGame).sendRequest(any(), any(), any());

        currentPlayer.getAbstractGodCard().initMove(spyGame);

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