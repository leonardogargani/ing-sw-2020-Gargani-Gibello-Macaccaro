package it.polimi.ingsw.PSP43.server.model.card.decorators;

import it.polimi.ingsw.PSP43.client.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.server.DataToAction;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlers.CellsHandler;
import it.polimi.ingsw.PSP43.server.modelHandlers.WorkersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;
import it.polimi.ingsw.PSP43.server.networkMessages.ActionRequest;

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
    public void move(DataToAction dataToAction) throws IOException, ClassNotFoundException, WinnerCaughtException, InterruptedException {
        GameSession gameSession = dataToAction.getGameSession();
        Coord coordToMove = dataToAction.getNewPosition();
        Cell cellToMove = gameSession.getCellsHandler().getCell(coordToMove);
        if (!cellToMove.getOccupiedByWorker()) super.move(dataToAction);
        else {
            Coord currentWorkerPosition = dataToAction.getWorker().getCurrentPosition();
            CellsHandler cellsHandler = gameSession.getCellsHandler();
            WorkersHandler workersHandler = gameSession.getWorkersHandler();
            HashMap<Coord, ArrayList<Coord>> availablePositionsToForce = new HashMap<>();
            availablePositionsToForce.put(new Coord(0, 0),
                    directionAvailableCoords(cellsHandler, currentWorkerPosition, dataToAction.getNewPosition()));
            Worker playerWorker = dataToAction.getWorker();
            Worker opponentWorker = workersHandler.getWorker(dataToAction.getNewPosition());

            ActionRequest request = new ActionRequest("Choose a position where to force your opponent.", availablePositionsToForce);
            ActionResponse response;
            do {
                response = gameSession.sendRequest(request, dataToAction.getPlayer().getNickname(), ActionResponse.class);
            } while (response == null);

            Coord coordChosen = response.getPosition();

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
                    ArrayList<Cell> availablePositionsOnDirection = directionAvailableCells(handler, actualCell, cellToMove);
                    if (availablePositionsOnDirection.size() == 0) availablePositions.get(actualCell).remove(cellToMove);
                }
            }
        }
        return availablePositions;
    }

    private ArrayList<Cell> directionAvailableCells(CellsHandler handler, Coord oldPosition, Coord newPosition) {
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

    private ArrayList<Coord> directionAvailableCoords(CellsHandler handler, Coord oldPosition, Coord newPosition) {
        CellsHandler.AbstractIterator iterator = handler.directionIterator(oldPosition, newPosition);
        ArrayList<Coord> availablePositionsOnDirection = new ArrayList<>();
        Cell cellToCopy;
        Cell cellOfOpponent = handler.getCell(newPosition);
        while ((cellToCopy = iterator.next()) != null) {
            if (cellToCopy.getHeight() == cellOfOpponent.getHeight())
                availablePositionsOnDirection.add(cellToCopy.getCoord());
        }
        return availablePositionsOnDirection;
    }

    public AbstractGodCard cleanFromEffects(String nameOfEffect) throws ClassNotFoundException {
        AbstractGodCard component = super.getGodComponent().cleanFromEffects(nameOfEffect);
        Class<?> c = Class.forName(nameOfEffect);
        if (!c.isInstance(this))
            return new SwapIfPossibleDecorator(component);
        else return component;
    }
}
