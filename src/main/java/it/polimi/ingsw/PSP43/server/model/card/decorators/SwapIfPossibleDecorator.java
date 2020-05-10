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
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;
import it.polimi.ingsw.PSP43.server.networkMessages.ActionRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class SwapIfPossibleDecorator extends PowerGodDecorator {
    private static final long serialVersionUID = 8250965157977039866L;

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
            Worker playerWorker = dataToAction.getWorker();
            Worker opponentWorker = workersHandler.getWorker(dataToAction.getNewPosition());

            HashMap<Coord, ArrayList<Coord>> availablePositionsToForce = new HashMap<>();
            availablePositionsToForce.put(new Coord(0, 0),
                    directionAvailableCoords(cellsHandler, currentWorkerPosition, dataToAction.getNewPosition()));
            ActionRequest request = new ActionRequest("Choose a position where to force your opponent.", availablePositionsToForce);
            ActionResponse response;
            do {
                try {
                    response = gameSession.sendRequest(request, dataToAction.getPlayer().getNickname(), new ActionResponse());
                } catch (GameEndedException e) {
                    gameSession.setActive();
                    return;
                }
            } while (response == null);

            Coord coordToForce = response.getPosition();

            workersHandler.changePosition(playerWorker, coordToMove);
            workersHandler.changePosition(opponentWorker, coordToForce);
        }
    }

    @Override
    public HashMap<Coord, ArrayList<Coord>> findAvailablePositionsToMove(CellsHandler handler, ArrayList<Worker> workers) {
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
        ArrayList<Coord> coordsInDirection = handler.findSameDirectionCoords(oldPosition, newPosition);
        ArrayList<Cell> cellsInDirection = handler.getCells(coordsInDirection);
        Cell cellOpponent = handler.getCell(newPosition);

        cellsInDirection.removeIf(c -> c.getHeight() != cellOpponent.getHeight());

        return cellsInDirection;
    }

    private ArrayList<Coord> directionAvailableCoords(CellsHandler handler, Coord oldPosition, Coord newPosition) {
        ArrayList<Coord> coordsInDirection = handler.findSameDirectionCoords(oldPosition, newPosition);
        int heightOpponent = handler.getCell(newPosition).getHeight();

        coordsInDirection.removeIf(coord -> handler.getCell(coord).getHeight() == heightOpponent);

        return coordsInDirection;
    }

    public AbstractGodCard cleanFromEffects(String nameOfEffect) throws ClassNotFoundException {
        AbstractGodCard component = super.getGodComponent().cleanFromEffects(nameOfEffect);
        Class<?> c = Class.forName(nameOfEffect);
        if (!c.isInstance(this))
            return new SwapIfPossibleDecorator(component);
        else return component;
    }
}
