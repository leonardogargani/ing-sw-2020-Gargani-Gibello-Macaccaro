package it.polimi.ingsw.PSP43.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CoordTest {
    Coord c;
    int cX= 3;
    int cY= 4;
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