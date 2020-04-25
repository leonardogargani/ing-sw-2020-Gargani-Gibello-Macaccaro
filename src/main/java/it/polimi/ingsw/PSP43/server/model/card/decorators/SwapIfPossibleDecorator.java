package it.polimi.ingsw.PSP43.server.model.card.decorators;

import it.polimi.ingsw.PSP43.server.ClientListener;
import it.polimi.ingsw.PSP43.server.DataToAction;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlers.CellsHandler;
import it.polimi.ingsw.PSP43.server.modelHandlers.WorkersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class SwapIfPossibleDecorator extends PowerGodDecorator {
    public SwapIfPossibleDecorator() {
        super();
    }

    public SwapIfPossibleDecorator(AbstractGodCard godComponent) {
        super(godComponent);
    }

    @Override
    public void move(DataToAction dataToAction) throws IOException, ClassNotFoundException, WinnerCaughtException {
        GameSession gameSession = dataToAction.getGameSession();
        Coord coordToMove = dataToAction.getNewPosition();
        Cell cellToMove = gameSession.getCellsHandler().getCell(coordToMove);
        if (!cellToMove.getOccupiedByWorker()) super.move(dataToAction);
        else {
            Coord currentWorkerPosition = dataToAction.getWorker().getCurrentPosition();
            CellsHandler cellsHandler = gameSession.getCellsHandler();
            WorkersHandler workersHandler = gameSession.getWorkersHandler();
            ArrayList<Cell> availablePositionsToForce = directionAvailablePositions(cellsHandler, currentWorkerPosition, dataToAction.getNewPosition());

            Worker playerWorker = dataToAction.getWorker();
            Worker opponentWorker = workersHandler.getWorker(dataToAction.getNewPosition());
            ClientListener receiver = gameSession.getListenersHashMap().get(dataToAction.getPlayer().getNickname());

            // TODO : coordChosen will be initialised with the cell chosen by the player
            Coord coordChosen = new Coord(0, 0);
            // TODO : send all the possible positions to the player

            Cell oldCell = cellsHandler.getCell(playerWorker.getCurrentPosition());
            oldCell.setOccupiedByWorker(false);
            cellsHandler.changeStateOfCell(oldCell, playerWorker.getCurrentPosition());
            playerWorker.setCurrentPosition(dataToAction.getNewPosition());

            Cell newOpponentCell = cellsHandler.getCell(coordChosen);
            newOpponentCell.setOccupiedByWorker(true);
            cellsHandler.changeStateOfCell(newOpponentCell, coordChosen);
            opponentWorker.setCurrentPosition(coordChosen);
        }
    }

    @Override
    public HashMap<Coord, ArrayList<Coord>> findAvailablePositionsToMove(CellsHandler handler, Worker[] workers) {
        HashMap<Coord, ArrayList<Coord>> availablePositions = super.findAvailablePositionsToMove(handler, workers);
        for (Coord actualCell : availablePositions.keySet()) {
            for (Coord cellToMove : availablePositions.get(actualCell)) {
                if (handler.getCell(cellToMove).getOccupiedByWorker()) {
                    ArrayList<Cell> availablePositionsOnDirection = directionAvailablePositions(handler, actualCell, cellToMove);
                    if (availablePositionsOnDirection.size() == 0) availablePositions.get(actualCell).remove(cellToMove);
                }
            }
        }
        return availablePositions;
    }

    private ArrayList<Cell> directionAvailablePositions (CellsHandler handler, Coord oldPosition, Coord newPosition) {
        CellsHandler.AbstractIterator iterator = handler.directionIterator(oldPosition, newPosition);
        ArrayList<Cell> availablePositionsOnDirection = new ArrayList<>();
        Cell cellToCopy;
        Cell cellOfOpponent = handler.getCell(newPosition);
        while ((cellToCopy = iterator.next()) != null) {
            if (cellToCopy.getHeight() == cellOfOpponent.getHeight())
                availablePositionsOnDirection.add(cellToCopy);
        }
        return availablePositionsOnDirection;
    }

    public AbstractGodCard cleanFromEffects(String nameOfEffect) throws ClassNotFoundException {
        AbstractGodCard newCard;
        AbstractGodCard component = super.getGodComponent().cleanFromEffects(nameOfEffect);
        Class c = Class.forName(nameOfEffect);
        if (!c.isInstance(this))
            return newCard = new SwapIfPossibleDecorator(component);
        else return component;
    }
}
