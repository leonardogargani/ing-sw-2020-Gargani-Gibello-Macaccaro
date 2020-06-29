package it.polimi.ingsw.PSP43.server.controller.cards.decorators;

import it.polimi.ingsw.PSP43.client.network.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.server.model.*;
import it.polimi.ingsw.PSP43.server.controller.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.controller.cards.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.CellsHandler;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.WorkersHandler;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException.GameLostException;
import it.polimi.ingsw.PSP43.server.network.networkMessages.ActionRequest;

import java.util.*;

/**
 * This class is used to add the functionality to Minotaur's card to make possible to a worker
 * to move on the cell of another worker, pushing him on another cell of the same height and on the
 * same direction.
 */
public class SwapIfPossibleDecorator extends PowerGodDecorator {
    private static final long serialVersionUID = 8250965157977039866L;

    public SwapIfPossibleDecorator(AbstractGodCard godComponent) {
        super(godComponent);
    }

    /**
     * This method handles the move of the player giving him the possibility to move on a cell already
     * occupied by another opponents' worker.
     * @param gameSession This is a reference to the main access to the game database.
     * @throws GameEndedException if the player decides to leave the game during his turn.
     * @throws GameLostException if the player can't do any move.
     */
    public void initMove(GameSession gameSession) throws GameEndedException, GameLostException {
        String currentNickname = gameSession.getCurrentPlayer().getNickname();
        AbstractGodCard card = gameSession.getCardsHandler().getPlayerCard(currentNickname);
        HashMap<Coord, ArrayList<Coord>> availablePositions = card.findAvailablePositionsToMove(gameSession);

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
                    Map.of(actionResponse.getPosition(), directionAvailableCoords(cellsHandler, currentWorkerPosition, actionResponse.getPosition())));
            response = gameSession.sendRequest(request, gameSession.getCurrentPlayer().getNickname(), ActionResponse.class);
            coordToForce = response.getPosition();
        }

        move(new DataToMove(gameSession, gameSession.getCurrentPlayer(), workerMoved, actionResponse.getPosition()), coordToForce);
    }

    /**
     * This method handles the move of a worker by calling the basic move if the cell in which he is moving
     * is free, or calling the proper functions to do the "switch" between workers if necessary.
     * @param dataToMove The data used to change the model and obtained by the interaction with the client.
     * @param coordToForce The coord where to force an opponent (null if the player decided to move on a free cell).
     */
    public void move(DataToMove dataToMove, Coord coordToForce) {
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

    /**
     * This method finds all the available positions in which a worker can move, included the ones of opponents'
     * workers (according to the rules).
     * @param gameSession This is a reference to the main access to the game database.
     * @return An HashMap containing all the coordinates where the two workers owned by the player can move.
     */
    public HashMap<Coord, ArrayList<Coord>> findAvailablePositionsToMove(GameSession gameSession) {
        CellsHandler cellsHandler = gameSession.getCellsHandler();
        Player currentPlayer = gameSession.getCurrentPlayer();
        List<Worker> actualWorkers = gameSession.getWorkersHandler().getWorkers(currentPlayer.getWorkersIdsArray());

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

            for (Worker w : actualWorkers) {
                availableCurrentPositions.remove(w.getCurrentPosition());
            }

            if (availableCurrentPositions.size() == 0) keyIterator.remove();
        }
        return availablePositions;
    }

    /**
     * This method finds all the cells available on a determined direction on the board.
     * @param handler The handler of the data of the cells contained in the model.
     * @param oldPosition The position from where it's necessary to finds all the cells in a direction.
     * @param newPosition The position used to determine a direction to find all the cells on the board on the same direction.
     * @return All the cells that follows the ones as parameters in the same direction.
     */
    private ArrayList<Cell> directionAvailableCells(CellsHandler handler, Coord oldPosition, Coord newPosition) {
        ArrayList<Coord> coordsInDirection = handler.findSameDirectionCoords(oldPosition, newPosition);
        ArrayList<Cell> cellsInDirection = handler.getCells(coordsInDirection);
        Cell cellOpponent = handler.getCell(newPosition);

        cellsInDirection.removeIf(c -> c.getHeight() != cellOpponent.getHeight() || c.getOccupiedByWorker());

        return cellsInDirection;
    }

    /**
     * This method finds all the coordinates available on a determined direction on the board.
     * @param handler The handler of the data of the cells contained in the model.
     * @param oldPosition The position from where it's necessary to finds all the coords in a direction.
     * @param newPosition The position used to determine a direction to find all the coords on the board on the same direction.
     * @return All the coords that follows the ones as parameters in the same direction.
     */
    private ArrayList<Coord> directionAvailableCoords(CellsHandler handler, Coord oldPosition, Coord newPosition) {
        ArrayList<Coord> coordsInDirection = handler.findSameDirectionCoords(oldPosition, newPosition);
        int heightOpponent = handler.getCell(newPosition).getHeight();

        coordsInDirection.removeIf(coord -> handler.getCell(coord).getHeight() != heightOpponent || handler.getCell(coord).getOccupiedByWorker());

        return coordsInDirection;
    }

    /**
     * This method is used to clean the card from possible decorators which could block some functionalities.
     * It is called when the blocker begins a new turn.
     * @param nameOfEffect The effect that the blocker has activated by doing a determined action.
     * @return The card cleaned by the blocking decorator passed as parameter.
     */
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
