package it.polimi.ingsw.PSP43.server.model.card.decorators;

import it.polimi.ingsw.PSP43.server.DataToAction;
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
import java.util.Iterator;

public class SwapMoveDecorator extends PowerGodDecorator {
    private static final long serialVersionUID = 1282873326963180012L;

    public SwapMoveDecorator() {
        super();
    }

    public SwapMoveDecorator(AbstractGodCard godComponent) {
        super(godComponent);
    }

    @Override
    public void move(DataToAction dataToAction) throws IOException, WinnerCaughtException, InterruptedException, ClassNotFoundException {
        CellsHandler cellsHandler = dataToAction.getGameSession().getCellsHandler();
        Coord newCoord = dataToAction.getNewPosition();

        if (!cellsHandler.getCell(newCoord).getOccupiedByWorker())
            super.move(dataToAction);
        else {
            WorkersHandler handler = dataToAction.getGameSession().getWorkersHandler();
            Worker opponentWorker = handler.getWorker(dataToAction.getNewPosition());
            swapWorkers(handler, dataToAction.getWorker(), opponentWorker);
        }
    }

    @Override
    public HashMap<Coord, ArrayList<Coord>> findAvailablePositionsToMove(CellsHandler handler, ArrayList<Worker> workers) {
        HashMap<Coord, ArrayList<Coord>> availablePositions = handler.findWorkersNeighbouringCoords(workers);
        for (Coord actualCoord : availablePositions.keySet()) {
            Cell actualCell = handler.getCell(actualCoord);
            ArrayList<Coord> neighbouringPositions = availablePositions.get(actualCoord);
            for (Iterator<Coord> coordIterator = neighbouringPositions.iterator(); coordIterator.hasNext(); ) {
                Coord coordToCheck = coordIterator.next();
                Cell cellToCheck = handler.getCell(coordToCheck);
                if (cellToCheck.getOccupiedByDome() || (cellToCheck.getHeight() - actualCell.getHeight() > 1))
                    coordIterator.remove();
            }
        }
        return availablePositions;
    }

    private void swapWorkers(WorkersHandler workersHandler, Worker worker1, Worker worker2) throws IOException {
        workersHandler.swapPositions(worker1, worker2);
    }

    public AbstractGodCard cleanFromEffects(String nameOfEffect) throws ClassNotFoundException {
        AbstractGodCard component = super.getGodComponent().cleanFromEffects(nameOfEffect);
        Class<?> c = Class.forName(nameOfEffect);
        if (!c.isInstance(this))
            return new SwapMoveDecorator(component);
        else return component;
    }
}
