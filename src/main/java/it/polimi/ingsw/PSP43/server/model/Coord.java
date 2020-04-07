package it.polimi.ingsw.PSP43.server.model;

public class Coord {
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
}
