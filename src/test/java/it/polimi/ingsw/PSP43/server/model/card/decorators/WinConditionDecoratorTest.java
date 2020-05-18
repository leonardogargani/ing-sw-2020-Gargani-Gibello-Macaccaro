package it.polimi.ingsw.PSP43.server.model.card.decorators;

import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.initialisers.DOMCardsParser;
import it.polimi.ingsw.PSP43.server.initialisers.GameInitialiser;
import it.polimi.ingsw.PSP43.server.model.*;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.model.card.BasicGodCard;
import it.polimi.ingsw.PSP43.server.model.card.behaviours.buildBehaviours.BasicBuildBehaviour;
import it.polimi.ingsw.PSP43.server.model.card.behaviours.moveBehaviours.BasicMoveBehaviour;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;

public class WinConditionDecoratorTest {
    GameSession gameSession;
    GameSession spyGame;
    ArrayList<AbstractGodCard> deck;
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
        deck = DOMCardsParser.buildDeck();

        currentPlayer = spyGame.getPlayersHandler().getPlayer(0);
        spyGame.setCurrentPlayer(currentPlayer);

        abstractGodCard = new WinConditionDecorator(new BasicGodCard("", "", "", new BasicMoveBehaviour(), new BasicBuildBehaviour()));

        workerToMove = spyGame.getWorkersHandler().getWorker(new Coord(4, 3));
    }

    @Test
    public void move() throws IOException, InterruptedException, ClassNotFoundException {
        Coord workerCoord = workerToMove.getCurrentPosition();
        Cell workerCell = spyGame.getCellsHandler().getCell(workerCoord);
        workerCell.setHeight(3);
        spyGame.getCellsHandler().changeStateOfCell(workerCell, workerCoord);

        Coord coordToMove = new Coord(3, 3);
        Cell cellToMove = spyGame.getCellsHandler().getCell(coordToMove);
        cellToMove.setHeight(0);
        spyGame.getCellsHandler().changeStateOfCell(cellToMove, coordToMove);

        boolean catched = false;

        try {
            abstractGodCard.move(new DataToMove(spyGame, currentPlayer, workerToMove, coordToMove));
        } catch (WinnerCaughtException e) {
            catched = true;
        }

        assertTrue(catched);
    }

    @Test
    public void cleanFromEffects() throws ClassNotFoundException {
        abstractGodCard.cleanFromEffects("it.polimi.ingsw.PSP43.server.model.card.decorators.BlockRiseDecorator");

        boolean verified = true;
        while (true) {
            if (!(abstractGodCard instanceof WinConditionDecorator) && !(abstractGodCard instanceof BasicGodCard)) {
                verified = false;
            }
            if (abstractGodCard instanceof BasicGodCard) break;
            else abstractGodCard = ((PowerGodDecorator) abstractGodCard).getGodComponent();
        }

        assertTrue(verified);
    }
}