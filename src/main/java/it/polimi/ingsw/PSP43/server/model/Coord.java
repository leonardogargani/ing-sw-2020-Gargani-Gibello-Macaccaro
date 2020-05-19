package it.polimi.ingsw.PSP43.server.model;

import java.io.Serializable;


public class Coord implements Serializable {

    private static final long serialVersionUID = 82606752933927860L;

    private final int xPosition;
    private final int yPosition;


    /**
     * Not default constructor to create coordinates.
     * @param x is the x coordinate
     * @param y is the y coordinate
     */
    public Coord(int x, int y){
        this.xPosition = x;
        this.yPosition = y;
    }


    /**
     * Method that returns the x coordinate.
     * @return the x coordinate
     */
    public int getX(){return xPosition;}


    /**
     * Method that returns the y coordinate.
     * @return the y coordinate
     */
    public int getY(){return yPosition;}


    /**
     * Method that compares a coordinate to the passed object.
     * @param obj object that needs to be compared to the coordinate
     * @return true if two coordinates are equal, false otherwise
     */
    public boolean equals(Object obj) {

        if (obj instanceof Coord) {
            Coord coordCheckEquality = (Coord) obj;
            return coordCheckEquality.getX() == xPosition && coordCheckEquality.getY() == yPosition;
        }
        else {
            return false;
        }

    }


    /**
     * This method clones a coordinate so that it can be used instead of the real one.
     * @return copy of the coordinate
     */
    public Coord clone() {
        return new Coord(this.getX(), this.getY());
    }

}
