package it.polimi.ingsw.PSP43.server.modelHandlers;

import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.modelHandlersException.CellAlreadyOccupiedExeption;
import it.polimi.ingsw.PSP43.server.modelHandlersException.CellHeightException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CellsHandlerTest {
    private GameSession game;
    private Cell c1;
    private Coord c;
    private int cX=2;
    private int cY=3;

    @Before
    public void setUp() throws CellHeightException, CellAlreadyOccupiedExeption {
        game = new GameSession(4);
        c=new Coord(cX,cY);
        c1=new Cell();
        c1.setHeight(2);
        c1.setOccupiedByWorker(false);
        c1.setOccupiedByDome(true);
        game.getCellsHandler().changeStateOfCell(c1, c);
    }

    @Test
    public void getCell() {
        assertTrue(game.getCellsHandler().getCell(c).getOccupiedByDome());
        assertFalse(game.getCellsHandler().getCell(c).getOccupiedByWorker());
        assertEquals(game.getCellsHandler().getCell(c).getHeight(),2);
    }


    @Test
    public void changeStateOfCell() throws CellAlreadyOccupiedExeption, CellHeightException {

        Cell cell=new Cell();
        cell.setHeight(2);
        cell.setOccupiedByWorker(true);
        cell.setOccupiedByDome(false);
        game.getCellsHandler().changeStateOfCell(cell,c);

        assertEquals(game.getCellsHandler().getCell(c).getHeight(),2);
        assertTrue(game.getCellsHandler().getCell(c).getOccupiedByWorker());
        assertFalse(game.getCellsHandler().getCell(c).getOccupiedByDome());

        cell.setOccupiedByWorker(false);
        cell.setOccupiedByDome(true);
        game.getCellsHandler().changeStateOfCell(cell,c);

        /*assertTrue(game.getCellsHandler().getCell(c).getOccupiedByDome());
        assertFalse(game.getCellsHandler().getCell(c).getOccupiedByWorker());*/
        }


}