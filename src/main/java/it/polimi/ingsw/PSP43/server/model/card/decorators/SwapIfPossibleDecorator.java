package it.polimi.ingsw.PSP43.server.model.card.decorators;

import it.polimi.ingsw.PSP43.client.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.server.model.DataToMove;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlers.CellsHandler;
import it.polimi.ingsw.PSP43.server.modelHandlers.WorkersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameLostException;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;
import it.polimi.ingsw.PSP43.server.networkMessages.ActionRequest;

import java.util.*;

public class SwapIfPossibleDecorator extends PowerGodDecorator {
    private static final long serialVersionUID = 8250965157977039866L;

    public SwapIfPossibleDecorator() {
        super();
    }

    public SwapIfPossibleDecorator(AbstractGodCard godComponent) {
        super(godComponent);
    }

    public void initMove(GameSession gameSession) throws WinnerCaughtException, GameEndedException, GameLostException {
        HashMap<Coord, ArrayList<Coord>> availablePositions = findAvailablePositionsToMove(gameSession);

        if (availablePositions.size() == 0) throw new GameLostException();

        ActionResponse actionResponse = askForMove(gameSession, availablePositions);

        Worker workerMoved = gameSession.getWorkersHandler().getWorker(actionResponse.getWorkerPosition());

        Cell cellToMove = gameSession.getCellsHandler().getCell(actionResponse.getPosition());

        ActionResponse response;
        Coord coordToForce = null;
        if (cellToMove.getOccupiedByWorker()) {
            CellsHandler cellsHandler = gameSession.getCellsHandler();

            Coord currentWorkerPosition = workerMoved.getCurrentPosition();

            ActionRequest request = new ActionRequest("Choose a position where to force your opponent.",
                    Map.of(new Coord(0, 0), directionAvailableCoords(cellsHandler, currentWorkerPosition, actionResponse.getPosition())));
            response = gameSession.sendRequest(request, gameSession.getCurrentPlayer().getNickname(), ActionResponse.class);
            coordToForce = response.getPosition();
        }

        move(new DataToMove(gameSession, gameSession.getCurrentPlayer(), workerMoved, actionResponse.getPosition()), coordToForce);
    }

    public void move(DataToMove dataToMove, Coord coordToForce) throws WinnerCaughtException {
        GameSession gameSession = dataToMove.getGameSession();

        Coord coordToMove = dataToMove.getNewPosition();
        Cell cellToMove = gameSession.getCellsHandler().getCell(coordToMove);

        if (!cellToMove.getOccupiedByWorker()) super.move(dataToMove);
        else {
            WorkersHandler workersHandler = gameSession.getWorkersHandler();
            Worker playerWorker = dataToMove.getWorker();
            Worker opponentWorker = workersHandler.getWorker(dataToMove.getNewPosition());

            workersHandler.changePosition(opponentWorker, coordToForce);
            workersHandler.changePosition(playerWorker, coordToMove);
        }
    }

    public HashMap<Coord, ArrayList<Coord>> findAvailablePositionsToMove(GameSession gameSession) {
        CellsHandler cellsHandler = gameSession.getCellsHandler();
        HashMap<Coord, ArrayList<Coord>> availablePositions = super.findAvailablePositionsToMove(gameSession);

        for (Iterator<Map.Entry<Coord, ArrayList<Coord>>> keyIterator = availablePositions.entrySet().iterator(); keyIterator.hasNext(); ) {
            Map.Entry<Coord, ArrayList<Coord>> currentKey = keyIterator.next();
            ArrayList<Coord> availableCurrentPositions = currentKey.getValue();
            for (Iterator<Coord> coordIterator = availableCurrentPositions.iterator(); coordIterator.hasNext(); ) {
                Coord arrivalCoord = coordIterator.next();
                if (cellsHandler.getCell(arrivalCoord).getOccupiedByWorker()) {
                    ArrayList<Cell> availablePositionsOnDirection = directionAvailableCells(cellsHandler, currentKey.getKey(), arrivalCoord);
                    if (availablePositionsOnDirection.size() == 0) coordIterator.remove();
                }
            }
            if (availableCurrentPositions.size() == 0) keyIterator.remove();
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

        coordsInDirection.removeIf(coord -> handler.getCell(coord).getHeight() != heightOpponent);

        return coordsInDirection;
    }

    public AbstractGodCard cleanFromEffects(String nameOfEffect) {
        AbstractGodCard component = super.getGodComponent().cleanFromEffects(nameOfEffect);
        Class<?> c = null;
        try {
            c = Class.forName(nameOfEffect);
        } catch (ClassNotFoundException e) { e.printStackTrace(); }

        assert c != null;
        if (!c.isInstance(this))
            return new SwapIfPossibleDecorator(component);
        else return component;
    }
}
