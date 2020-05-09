package it.polimi.ingsw.PSP43.server.model.card.decorators;

import it.polimi.ingsw.PSP43.server.DataToAction;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlers.CellsHandler;
import it.polimi.ingsw.PSP43.server.modelHandlers.WorkersHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class SwapMoveDecorator extends PowerGodDecorator {
    private static final long serialVersionUID = 1282873326963180012L;

    public SwapMoveDecorator() {
        super();
    }

    public SwapMoveDecorator(AbstractGodCard godComponent) {
        super(godComponent);
    }

    @Override
    public void move(DataToAction dataToAction) throws IOException {
        WorkersHandler handler = dataToAction.getGameSession().getWorkersHandler();
        Worker opponentWorker = handler.getWorker(dataToAction.getNewPosition());
        swapWorkers(dataToAction.getWorker(), opponentWorker);
    }

    @Override
    public HashMap<Coord, ArrayList<Coord>> findAvailablePositionsToMove(CellsHandler handler, ArrayList<Worker> workers) {
        HashMap<Coord, ArrayList<Coord>> availablePositions = handler.findWorkersNeighbouringCoords(workers);
        for (Coord actualCell : availablePositions.keySet()) {
            availablePositions.get(actualCell).removeIf(cellToMove -> handler.getCell(cellToMove).getOccupiedByDome());
        }
        return availablePositions;
    }

    private void swapWorkers(Worker worker1, Worker worker2) throws IOException {
        worker1.setCurrentPosition(worker2.getCurrentPosition());
        worker2.setCurrentPosition(worker1.getPreviousPosition());
    }

    public AbstractGodCard cleanFromEffects(String nameOfEffect) throws ClassNotFoundException {
        AbstractGodCard component = super.getGodComponent().cleanFromEffects(nameOfEffect);
        Class<?> c = Class.forName(nameOfEffect);
        if (!c.isInstance(this))
            return new SwapMoveDecorator(component);
        else return component;
    }
}
