package it.polimi.ingsw.PSP43.server.model;

import java.io.Serializable;

public class Coord implements Serializable {

    private static final long serialVersionUID = 82606752933927860L;

    private final int xPosition;
    private final int yPosition;

    /**
     * Not default constructor to create coordinates
     * @param x is the x coordinate
     * @param y is the y coordinate
     */
    public Coord(int x, int y){
        this.xPosition=x;
        this.yPosition=y;
    }

    /**
     * Method to return the x coordinate
     * @return the x coordinate
     */
    public int getX(){return xPosition;}

    /**
     * Method to return the y coordinate
     * @return the y coordinate
     */
    public int getY(){return yPosition;}

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Coord) {
            Coord coordCheckEquality = (Coord) obj;
            if (coordCheckEquality.getX() == xPosition && coordCheckEquality.getY() == yPosition) return true;
            else return false;
        }
        else {
            return false;
        }
    }

    @Override
    public Coord clone() {
        return new Coord(this.getX(), this.getY());
    }
}
