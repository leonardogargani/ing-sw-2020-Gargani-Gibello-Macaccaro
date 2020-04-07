package it.polimi.ingsw.PSP43.server.modelHandlersException;

public class CellAlreadyOccupiedExeption extends Exception {
    public CellAlreadyOccupiedExeption() {};
    public CellAlreadyOccupiedExeption(String message){
        super(message);
    }
}
