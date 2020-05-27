package it.polimi.ingsw.PSP43.server.controllers.decorators;

import it.polimi.ingsw.PSP43.server.controllers.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.controllers.BasicGodCard;
import it.polimi.ingsw.PSP43.server.controllers.behaviours.buildBehaviours.BasicBuildBehaviour;
import it.polimi.ingsw.PSP43.server.controllers.behaviours.moveBehaviours.BasicMoveBehaviour;
import it.polimi.ingsw.PSP43.server.gameStates.GameSessionForTest;
import it.polimi.ingsw.PSP43.server.initialisers.GameInitialiser;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.modelHandlers.WorkersHandler;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class BuildUnderFeetDecoratorTest {
    GameSessionForTest gameSessionForTest;
    AbstractGodCard abstractGodCard;
    Player currentPlayer;

    @Before
    public void setUp() throws Exception {
        gameSessionForTest = GameInitialiser.initialiseGame();
        GameInitialiser.initialisePlayers(gameSessionForTest);
        GameInitialiser.initialiseWorkers(gameSessionForTest);
        GameInitialiser.initialiseCards(gameSessionForTest);

        currentPlayer = gameSessionForTest.getPlayersHandler().getPlayer(0);
        gameSessionForTest.setCurrentPlayer(currentPlayer);

        abstractGodCard = new BuildUnderFeetDecorator(new BasicGodCard("","","", new BasicMoveBehaviour(), new BasicBuildBehaviour()));
        gameSessionForTest.getCardsHandler().setCardToPlayer(currentPlayer.getNickname(), abstractGodCard);
    }

    @Test
    public void findAvailablePositionsToBuildBlock() {
        WorkersHandler workersHandler = gameSessionForTest.getWorkersHandler();
        Integer[] wIds = currentPlayer.getWorkersIdsArray();
        ArrayList<Coord> workersCoord = new ArrayList<>();
        for (Integer i : wIds) {
            workersHandler.getWorker(i).setLatestMoved(true);
            workersCoord.add(workersHandler.getWorker(i).getCurrentPosition());
        }

        HashMap<Coord, ArrayList<Coord>> effectiveAvailablePositions = abstractGodCard.findAvailablePositionsToBuildBlock(gameSessionForTest);

        for (Coord c : workersCoord) {
            ArrayList<Coord> positionsWhereToBuild = effectiveAvailablePositions.get(c);

            if (positionsWhereToBuild.size() != 0) {
                assertTrue(positionsWhereToBuild.contains(c));
            }
        }
    }
}