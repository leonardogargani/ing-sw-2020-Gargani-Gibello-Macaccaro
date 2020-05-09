package it.polimi.ingsw.PSP43.server.model.card.behaviours;

import it.polimi.ingsw.PSP43.server.BoardObserver;
import it.polimi.ingsw.PSP43.server.DataToAction;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.initialisers.DOMCardsParser;
import it.polimi.ingsw.PSP43.server.initialisers.GameInitialiser;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.model.card.decorators.BlockRiseDecorator;
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

public class BlockOpponentRiseBehaviourTest {
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
    public void handleMoveWithNoBlocking() throws IOException, WinnerCaughtException, InterruptedException, ClassNotFoundException {
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
        DataToAction data = new DataToAction(spyGame, currentPlayer, athenasWorker, newCoord);
        spyAthenaBehaviour.handleMove(data);

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
    public void handleMoveWithBlocking() throws IOException, WinnerCaughtException, InterruptedException, ClassNotFoundException {
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
        DataToAction data = new DataToAction(spyGame, currentPlayer, athenasWorker, newCoord);
        spyAthenaBehaviour.handleMove(data);

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