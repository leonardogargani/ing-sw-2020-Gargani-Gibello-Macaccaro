package it.polimi.ingsw.PSP43.server.model.card.decorators;

import it.polimi.ingsw.PSP43.client.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.server.DataToMove;
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

    public void initMove(GameSession gameSession) throws ClassNotFoundException, WinnerCaughtException, InterruptedException, IOException, GameEndedException {
        ActionResponse actionResponse = askForMove(gameSession, findAvailablePositionsToMove(gameSession));

        Worker workerMoved = gameSession.getWorkersHandler().getWorker(actionResponse.getWorkerPosition());

        Cell cellToMove = gameSession.getCellsHandler().getCell(actionResponse.getPosition());

        ActionResponse response = new ActionResponse();
        if (cellToMove.getOccupiedByWorker()) {
            CellsHandler cellsHandler = gameSession.getCellsHandler();

            Coord currentWorkerPosition = workerMoved.getCurrentPosition();

            HashMap<Coord, ArrayList<Coord>> availablePositionsToForce = new HashMap<>();
            availablePositionsToForce.put(new Coord(0, 0),
                    directionAvailableCoords(cellsHandler, currentWorkerPosition, actionResponse.getPosition()));
            ActionRequest request = new ActionRequest("Choose a position where to force your opponent.", availablePositionsToForce);
            response = gameSession.sendRequest(request, gameSession.getCurrentPlayer().getNickname(), new ActionResponse());
        }

        Coord coordToForce = response.getPosition();

        move(new DataToMove(gameSession, gameSession.getCurrentPlayer(), workerMoved, actionResponse.getPosition()), coordToForce);
    }

    public void move(DataToMove dataToMove, Coord coordToForce) throws IOException, ClassNotFoundException, WinnerCaughtException, InterruptedException {
        GameSession gameSession = dataToMove.getGameSession();

        Coord coordToMove = dataToMove.getNewPosition();
        Cell cellToMove = gameSession.getCellsHandler().getCell(coordToMove);

        if (!cellToMove.getOccupiedByWorker()) super.move(dataToMove);
        else {
            WorkersHandler workersHandler = gameSession.getWorkersHandler();
            Worker playerWorker = dataToMove.getWorker();
            Worker opponentWorker = workersHandler.getWorker(coordToForce);

            workersHandler.changePosition(playerWorker, coordToForce);
            workersHandler.changePosition(opponentWorker, coordToForce);
        }
    }

    public HashMap<Coord, ArrayList<Coord>> findAvailablePositionsToMove(GameSession gameSession) {
        CellsHandler cellsHandler = gameSession.getCellsHandler();
        HashMap<Coord, ArrayList<Coord>> availablePositions = super.findAvailablePositionsToMove(gameSession);

        for (Coord actualCell : availablePositions.keySet()) {
            for (Coord cellToMove : availablePositions.get(actualCell)) {
                if (cellsHandler.getCell(cellToMove).getOccupiedByWorker()) {
                    ArrayList<Cell> availablePositionsOnDirection = directionAvailableCells(cellsHandler, actualCell, cellToMove);
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
