package it.polimi.ingsw.PSP43.modelHandlersException;

public class CellAlreadyOccupiedExeption extends Exception {
    public CellAlreadyOccupiedExeption() {};
    public CellAlreadyOccupiedExeption(String message){
        super(message);
    }
}
