package it.polimi.ingsw.PSP43.server.controllers.decorators;

import it.polimi.ingsw.PSP43.client.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.server.model.DataToMove;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.controllers.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlers.CellsHandler;
import it.polimi.ingsw.PSP43.server.modelHandlers.WorkersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameLostException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SwapMoveDecorator extends PowerGodDecorator {
    private static final long serialVersionUID = 1282873326963180012L;

    public SwapMoveDecorator(AbstractGodCard godComponent) {
        super(godComponent);
    }

    public void initMove(GameSession gameSession) throws GameEndedException, GameLostException {
        HashMap<Coord, ArrayList<Coord>> availablePositions = findAvailablePositionsToMove(gameSession);

        if (availablePositions.size() == 0) throw new GameLostException();

        ActionResponse actionResponse = askForMove(gameSession, availablePositions);

        Worker workerMoved = gameSession.getWorkersHandler().getWorker(actionResponse.getWorkerPosition());
        move(new DataToMove(gameSession, gameSession.getCurrentPlayer(), workerMoved, actionResponse.getPosition()));
    }

    public void move(DataToMove dataToMove) {
        CellsHandler cellsHandler = dataToMove.getGameSession().getCellsHandler();
        Coord newCoord = dataToMove.getNewPosition();

        if (!cellsHandler.getCell(newCoord).getOccupiedByWorker())
            super.move(dataToMove);
        else {
            WorkersHandler handler = dataToMove.getGameSession().getWorkersHandler();
            Worker opponentWorker = handler.getWorker(dataToMove.getNewPosition());
            swapWorkers(handler, dataToMove.getWorker(), opponentWorker);
        }
    }

    public HashMap<Coord, ArrayList<Coord>> findAvailablePositionsToMove(GameSession gameSession) {
        CellsHandler cellsHandler = gameSession.getCellsHandler();

        HashMap<Coord, ArrayList<Coord>> availablePositions = cellsHandler.findWorkersNeighbouringCoords(gameSession.getCurrentPlayer());

        for (Iterator<Map.Entry<Coord, ArrayList<Coord>>> keyIterator = availablePositions.entrySet().iterator(); keyIterator.hasNext(); ) {
            Map.Entry<Coord, ArrayList<Coord>> currentKey = keyIterator.next();
            Cell actualCell = cellsHandler.getCell(currentKey.getKey());
            ArrayList<Coord> neighbouringPositions = currentKey.getValue();
            for (Iterator<Coord> coordIterator = neighbouringPositions.iterator(); coordIterator.hasNext(); ) {
                Coord coordToCheck = coordIterator.next();
                Cell cellToCheck = cellsHandler.getCell(coordToCheck);
                if (cellToCheck.getOccupiedByDome() || (cellToCheck.getHeight() - actualCell.getHeight() > 1))
                    coordIterator.remove();
            }

            if (neighbouringPositions.size() == 0) keyIterator.remove();
        }
        return availablePositions;
    }

    private void swapWorkers(WorkersHandler workersHandler, Worker worker1, Worker worker2) {
        workersHandler.swapPositions(worker1, worker2);
    }

    public AbstractGodCard cleanFromEffects(String nameOfEffect) {
        AbstractGodCard component = super.getGodComponent().cleanFromEffects(nameOfEffect);
        Class<?> c = null;
        try {
            c = Class.forName(nameOfEffect);
        } catch (ClassNotFoundException e) { e.printStackTrace(); }

        assert c != null;
        if (!c.isInstance(this))
            return new SwapMoveDecorator(component);
        else return component;
    }
}
