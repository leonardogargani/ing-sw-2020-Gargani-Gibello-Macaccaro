package it.polimi.ingsw.PSP43.server.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CoordTest {

    private Coord c;
    private int cX= 3;
    private int cY= 4;

    @Before
    public void setUp(){
        c=new Coord(cX,cY);

    }
    @Test
    public void getX() {
        assertEquals(cX,c.getX());
    }

    @Test
    public void getY() {
        assertEquals(cY,c.getY());
    }
}