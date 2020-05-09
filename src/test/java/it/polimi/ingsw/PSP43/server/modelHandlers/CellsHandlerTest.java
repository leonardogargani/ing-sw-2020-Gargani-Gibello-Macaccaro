package it.polimi.ingsw.PSP43.server.modelHandlers;

import it.polimi.ingsw.PSP43.Color;
import it.polimi.ingsw.PSP43.server.BoardObserver;
import it.polimi.ingsw.PSP43.server.initialisers.DOMCardsParser;
import it.polimi.ingsw.PSP43.server.initialisers.GameInitialiser;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlersException.NicknameAlreadyInUseException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import static org.junit.Assert.*;

public class CellsHandlerTest {
    GameSession gameSession;
    GameSession spyGame;
    BoardObserver obs;
    BoardObserver spyObs;
    ArrayList<AbstractGodCard> deck;

    @Before
    public void setUp() throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException, NicknameAlreadyInUseException {
        gameSession = GameInitialiser.initialiseGame();
        deck = DOMCardsParser.buildDeck();
        GameInitialiser.initialisePlayers(gameSession);
        GameInitialiser.initialiseCards(gameSession);
        GameInitialiser.initialiseWorkers(gameSession);
        spyGame = Mockito.spy(gameSession);
        obs = new BoardObserver(gameSession);
        spyObs = Mockito.spy(obs);
    }

    @Test
    public void changeStateOfCell() throws IOException {
        CellsHandler handler = gameSession.getCellsHandler();

        Coord coordOccupiedByDome = new Coord(0, 3);
        Cell cellOccupiedByDome = new Cell(coordOccupiedByDome, gameSession.getBoardObserver());
        cellOccupiedByDome.setOccupiedByDome(true);
        handler.changeStateOfCell(cellOccupiedByDome, coordOccupiedByDome);
        assertTrue(handler.getCell(coordOccupiedByDome).getOccupiedByDome());

        Coord coordOccupiedByWorker = new Coord(2, 3);
        Cell cellOccupiedByWorker = new Cell(coordOccupiedByWorker, gameSession.getBoardObserver());
        cellOccupiedByWorker.setOccupiedByWorker(true);
        handler.changeStateOfCell(cellOccupiedByWorker, coordOccupiedByWorker);
        assertTrue(cellOccupiedByWorker.getOccupiedByWorker());

        Coord coordHeightChanged = new Coord(0, 4);
        Cell cellHeightChanged = new Cell(coordHeightChanged, gameSession.getBoardObserver());
        cellHeightChanged.setHeight(3);
        handler.changeStateOfCell(cellHeightChanged, coordHeightChanged);
        assertEquals(3, handler.getCell(coordHeightChanged).getHeight());
    }

    @Test
    public void findAllCoordsFree() {
        ArrayList<Coord> coords;
        coords = gameSession.getCellsHandler().findAllFreeCoords();

        boolean right = true;
        if (coords.size() != 21) right = false;

        for (Coord c : coords) {
            if (c.equals(new Coord(0, 0)) || c.equals(new Coord(1, 1)) ||
            c.equals(new Coord(4, 2)) || c.equals(new Coord(4, 3))) {
                right = false;
                break;
            }
        }

        assertTrue(right);
    }

    @Test
    public void findNeighbouringCoordsLowerBound() throws IOException {
        Worker workerToCheckNeighbours = new Worker(0, Color.ANSI_RED, gameSession.getBoardObserver());
        workerToCheckNeighbours.setCurrentPosition(new Coord(2, 4));
        ArrayList<Worker> workersToFindNeighbours = new ArrayList<>();
        workersToFindNeighbours.add(workerToCheckNeighbours);
        HashMap<Coord, ArrayList<Coord>> neighbouringCoords = gameSession.getCellsHandler().findWorkersNeighbouringCoords(workersToFindNeighbours);

        ArrayList<Coord> coordsToCheck = new ArrayList<>();
        coordsToCheck.add(new Coord(1,4));
        coordsToCheck.add(new Coord(1,3));
        coordsToCheck.add(new Coord(2,3));
        coordsToCheck.add(new Coord(3,3));
        coordsToCheck.add(new Coord(3,4));

        boolean right = true;
        for (Coord key : neighbouringCoords.keySet()) {
            if (neighbouringCoords.get(key).size() != coordsToCheck.size()) right = false;
        }

        for (Coord c : neighbouringCoords.keySet()) {
            if (c.equals(workerToCheckNeighbours.getCurrentPosition())) {
                ArrayList<Coord> effectiveCoords = neighbouringCoords.get(c);
                for (Coord c1 : effectiveCoords) {
                    Iterator<Coord> iterator = coordsToCheck.iterator();
                    while (true) {
                        if (iterator.next().equals(c1)) break;
                        else if (!iterator.hasNext()) {
                            right = false;
                            break;
                        }
                    }
                }
            }
            else {
                right = false;
                break;
            }
        }

        assertTrue(right);
    }

    @Test
    public void findNeighbouringCoordsUpperBound() throws IOException {
        Worker workerToCheckNeighbours = new Worker(0, Color.ANSI_RED, gameSession.getBoardObserver());
        workerToCheckNeighbours.setCurrentPosition(new Coord(3, 0));
        ArrayList<Worker> workersToFindNeighbours = new ArrayList<>();
        workersToFindNeighbours.add(workerToCheckNeighbours);
        HashMap<Coord, ArrayList<Coord>> neighbouringCoords = gameSession.getCellsHandler().findWorkersNeighbouringCoords(workersToFindNeighbours);

        ArrayList<Coord> coordsToCheck = new ArrayList<>();
        coordsToCheck.add(new Coord(2,0));
        coordsToCheck.add(new Coord(2,1));
        coordsToCheck.add(new Coord(3,1));
        coordsToCheck.add(new Coord(4,1));
        coordsToCheck.add(new Coord(4,0));

        boolean right = true;
        for (Coord key : neighbouringCoords.keySet()) {
            if (neighbouringCoords.get(key).size() != coordsToCheck.size()) right = false;
        }

        for (Coord c : neighbouringCoords.keySet()) {
            if (c.equals(workerToCheckNeighbours.getCurrentPosition())) {
                ArrayList<Coord> effectiveCoords = neighbouringCoords.get(c);
                for (Coord c1 : effectiveCoords) {
                    Iterator<Coord> iterator = coordsToCheck.iterator();
                    while (true) {
                        if (iterator.next().equals(c1)) break;
                        else if (!iterator.hasNext()) {
                            right = false;
                            break;
                        }
                    }
                }
            }
            else {
                right = false;
                break;
            }
        }

        assertTrue(right);
    }

    @Test
    public void findNeighbouringCoordsRightBound() throws IOException {
        Worker workerToCheckNeighbours = new Worker(0, Color.ANSI_RED, gameSession.getBoardObserver());
        workerToCheckNeighbours.setCurrentPosition(new Coord(4, 2));
        ArrayList<Worker> workersToFindNeighbours = new ArrayList<>();
        workersToFindNeighbours.add(workerToCheckNeighbours);
        HashMap<Coord, ArrayList<Coord>> neighbouringCoords = gameSession.getCellsHandler().findWorkersNeighbouringCoords(workersToFindNeighbours);

        ArrayList<Coord> coordsToCheck = new ArrayList<>();
        coordsToCheck.add(new Coord(4,1));
        coordsToCheck.add(new Coord(3,1));
        coordsToCheck.add(new Coord(3,2));
        coordsToCheck.add(new Coord(3,3));
        coordsToCheck.add(new Coord(4,3));

        boolean right = true;
        for (Coord key : neighbouringCoords.keySet()) {
            if (neighbouringCoords.get(key).size() != coordsToCheck.size()) right = false;
        }

        for (Coord c : neighbouringCoords.keySet()) {
            if (c.equals(workerToCheckNeighbours.getCurrentPosition())) {
                ArrayList<Coord> effectiveCoords = neighbouringCoords.get(c);
                for (Coord c1 : effectiveCoords) {
                    Iterator<Coord> iterator = coordsToCheck.iterator();
                    while (true) {
                        if (iterator.next().equals(c1)) break;
                        else if (!iterator.hasNext()) {
                            right = false;
                            break;
                        }
                    }
                }
            }
            else {
                right = false;
                break;
            }
        }

        assertTrue(right);
    }

    @Test
    public void findNeighbouringCoordsLeftBound() throws IOException {
        Worker workerToCheckNeighbours = new Worker(0, Color.ANSI_RED, gameSession.getBoardObserver());
        workerToCheckNeighbours.setCurrentPosition(new Coord(0 , 2));
        ArrayList<Worker> workersToFindNeighbours = new ArrayList<>();
        workersToFindNeighbours.add(workerToCheckNeighbours);
        HashMap<Coord, ArrayList<Coord>> neighbouringCoords = gameSession.getCellsHandler().findWorkersNeighbouringCoords(workersToFindNeighbours);

        ArrayList<Coord> coordsToCheck = new ArrayList<>();
        coordsToCheck.add(new Coord(0,1));
        coordsToCheck.add(new Coord(1,1));
        coordsToCheck.add(new Coord(1,2));
        coordsToCheck.add(new Coord(1,3));
        coordsToCheck.add(new Coord(0,3));

        boolean right = true;
        for (Coord key : neighbouringCoords.keySet()) {
            if (neighbouringCoords.get(key).size() != coordsToCheck.size()) right = false;
        }

        for (Coord c : neighbouringCoords.keySet()) {
            if (c.equals(workerToCheckNeighbours.getCurrentPosition())) {
                ArrayList<Coord> effectiveCoords = neighbouringCoords.get(c);
                for (Coord c1 : effectiveCoords) {
                    Iterator<Coord> iterator = coordsToCheck.iterator();
                    while (true) {
                        if (iterator.next().equals(c1)) break;
                        else if (!iterator.hasNext()) {
                            right = false;
                            break;
                        }
                    }
                }
            }
            else {
                right = false;
                break;
            }
        }

        assertTrue(right);
    }

    @Test
    public void simpleLeftRightDirectionIterator() {
        Coord origin = new Coord(0,0);
        Coord destination = new Coord(1,0);
        ArrayList<Coord> coordsExpected = new ArrayList<>();

        coordsExpected.add(new Coord(2, 0));
        coordsExpected.add(new Coord(3, 0));
        coordsExpected.add(new Coord(4, 0));

        ArrayList<Coord> coordsToCheck;
        coordsToCheck = gameSession.getCellsHandler().findSameDirectionCoords(origin, destination);

        boolean right = true;
        if (coordsExpected.size() != coordsToCheck.size()) right = false;

        for (Coord c1 : coordsToCheck) {
            Iterator<Coord> iterator = coordsExpected.iterator();
            while (true) {
                if (!iterator.hasNext()) {
                    right = false;
                    break;
                }
                if (iterator.next().equals(c1)) break;
            }
        }

        assertTrue(right);
    }

    @Test
    public void simpleRightLeftDirectionIterator() {
        Coord origin = new Coord(4,2);
        Coord destination = new Coord(3,2);
        ArrayList<Coord> coordsExpected = new ArrayList<>();

        coordsExpected.add(new Coord(0, 2));
        coordsExpected.add(new Coord(1, 2));
        coordsExpected.add(new Coord(2, 2));

        ArrayList<Coord> coordsToCheck;
        coordsToCheck = gameSession.getCellsHandler().findSameDirectionCoords(origin, destination);

        boolean right = true;
        if (coordsExpected.size() != coordsToCheck.size()) right = false;

        for (Coord c1 : coordsToCheck) {
            Iterator<Coord> iterator = coordsExpected.iterator();
            while (true) {
                if (!iterator.hasNext()) {
                    right = false;
                    break;
                }
                if (iterator.next().equals(c1)) break;
            }
        }

        assertTrue(right);
    }

    @Test
    public void simpleBottomUpDirectionIterator() {
        Coord origin = new Coord(0,4);
        Coord destination = new Coord(0,3);
        ArrayList<Coord> coordsExpected = new ArrayList<>();

        coordsExpected.add(new Coord(0, 2));
        coordsExpected.add(new Coord(0, 1));
        coordsExpected.add(new Coord(0, 0));

        ArrayList<Coord> coordsToCheck;
        coordsToCheck = gameSession.getCellsHandler().findSameDirectionCoords(origin, destination);

        boolean right = true;
        if (coordsExpected.size() != coordsToCheck.size()) right = false;

        for (Coord c1 : coordsToCheck) {
            Iterator<Coord> iterator = coordsExpected.iterator();
            while (true) {
                if (!iterator.hasNext()) {
                    right = false;
                    break;
                }
                if (iterator.next().equals(c1)) break;
            }
        }

        assertTrue(right);
    }

    @Test
    public void simpleUpBottomDirectionIterator() {
        Coord origin = new Coord(3,0);
        Coord destination = new Coord(3,1);
        ArrayList<Coord> coordsExpected = new ArrayList<>();

        coordsExpected.add(new Coord(3, 2));
        coordsExpected.add(new Coord(3, 3));
        coordsExpected.add(new Coord(3, 4));

        ArrayList<Coord> coordsToCheck;
        coordsToCheck = gameSession.getCellsHandler().findSameDirectionCoords(origin, destination);

        boolean right = true;
        if (coordsExpected.size() != coordsToCheck.size()) right = false;

        for (Coord c1 : coordsToCheck) {
            Iterator<Coord> iterator = coordsExpected.iterator();
            while (true) {
                if (!iterator.hasNext()) {
                    right = false;
                    break;
                }
                if (iterator.next().equals(c1)) break;
            }
        }

        assertTrue(right);
    }

    @Test
    public void slantingUpBottomDirectionIterator() {
        Coord origin = new Coord(3,0);
        Coord destination = new Coord(2,1);
        ArrayList<Coord> coordsExpected = new ArrayList<>();

        coordsExpected.add(new Coord(1, 2));
        coordsExpected.add(new Coord(0, 3));

        ArrayList<Coord> coordsToCheck;
        coordsToCheck = gameSession.getCellsHandler().findSameDirectionCoords(origin, destination);

        boolean right = true;
        if (coordsExpected.size() != coordsToCheck.size()) right = false;

        for (Coord c1 : coordsToCheck) {
            Iterator<Coord> iterator = coordsExpected.iterator();
            while (true) {
                if (!iterator.hasNext()) {
                    right = false;
                    break;
                }
                if (iterator.next().equals(c1)) break;
            }
        }

        assertTrue(right);
    }

    @Test
    public void slantingBottomUpDirectionIterator() {
        Coord origin = new Coord(0,4);
        Coord destination = new Coord(1,3);
        ArrayList<Coord> coordsExpected = new ArrayList<>();

        coordsExpected.add(new Coord(2, 2));
        coordsExpected.add(new Coord(3, 1));
        coordsExpected.add(new Coord(4, 0));

        ArrayList<Coord> coordsToCheck;
        coordsToCheck = gameSession.getCellsHandler().findSameDirectionCoords(origin, destination);

        boolean right = true;
        if (coordsExpected.size() != coordsToCheck.size()) right = false;

        for (Coord c1 : coordsToCheck) {
            Iterator<Coord> iterator = coordsExpected.iterator();
            while (true) {
                if (!iterator.hasNext()) {
                    right = false;
                    break;
                }
                if (iterator.next().equals(c1)) break;
            }
        }

        assertTrue(right);
    }

    @Test
    public void getCells() {
        ArrayList<Coord> coordsToFind = new ArrayList<>();
        coordsToFind.add(new Coord(0, 0));
        coordsToFind.add(new Coord(1, 3));
        coordsToFind.add(new Coord(2, 2));

        ArrayList<Cell> cellsMatched = gameSession.getCellsHandler().getCells(coordsToFind);

        boolean equal = true;
        if (cellsMatched.size() != coordsToFind.size()) equal = false;

        for (Cell c : cellsMatched) {
            Iterator<Coord> coordIterator = coordsToFind.iterator();
            while (true) {
                if (!coordIterator.hasNext()) {
                    equal = false;
                    break;
                }

                Coord actualCoord = coordIterator.next();
                if (actualCoord.getY() == c.getCoord().getY() && actualCoord.getX() == actualCoord.getX()) {
                    break;
                }
            }
        }

        assertTrue(equal);
    }
}