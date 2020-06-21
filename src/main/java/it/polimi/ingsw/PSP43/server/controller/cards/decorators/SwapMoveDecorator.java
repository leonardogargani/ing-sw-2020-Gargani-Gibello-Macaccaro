package it.polimi.ingsw.PSP43.server.controller.cards.decorators;

import it.polimi.ingsw.PSP43.client.network.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.server.model.*;
import it.polimi.ingsw.PSP43.server.controller.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.controller.cards.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.CellsHandler;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.WorkersHandler;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException.GameLostException;

import java.util.*;

/**
 * This class is used to add the functionality to the card of Apollo to allow
 * the player to swap his worker with an opponent's one.
 */
public class SwapMoveDecorator extends PowerGodDecorator {
    private static final long serialVersionUID = 1282873326963180012L;

    public SwapMoveDecorator(AbstractGodCard godComponent) {
        super(godComponent);
    }

    /**
     * This method permits to a player to move one of his workers in a different way compared to a basic
     * card. It gives the possibility to move a worker also on the cell occupied by another worker.
     * @param gameSession This is a reference to the main access to the game database.
     * @throws GameEndedException if the player decides to leave the game during his turn.
     * @throws GameLostException if the player can't do any move.
     */
    public void initMove(GameSession gameSession) throws GameEndedException, GameLostException {
        HashMap<Coord, ArrayList<Coord>> availablePositions = findAvailablePositionsToMove(gameSession);

        if (availablePositions.size() == 0) throw new GameLostException();

        ActionResponse actionResponse = askForMove(gameSession, availablePositions);

        Worker workerMoved = gameSession.getWorkersHandler().getWorker(actionResponse.getWorkerPosition());
        move(new DataToMove(gameSession, gameSession.getCurrentPlayer(), workerMoved, actionResponse.getPosition()));
    }

    /**
     * This method handles the move of the player's worker by swapping his worker with an opponent's one
     * if he is moving on a cell already occupied.
     * @param dataToMove The data used to change the model and obtained by the interaction with the client.
     */
    public void move(DataToMove dataToMove) {
        CellsHandler cellsHandler = dataToMove.getGameSession().getCellsHandler();
        Coord newCoord = dataToMove.getNewPosition();

        if (!cellsHandler.getCell(newCoord).getOccupiedByWorker())
            super.move(dataToMove);
        else {
            WorkersHandler handler = dataToMove.getGameSession().getWorkersHandler();
            Worker opponentWorker = handler.getWorker(dataToMove.getNewPosition());
            handler.swapPositions(dataToMove.getWorker(), opponentWorker);
        }
    }

    /**
     * This method finds all the available positions where a player's worker can move, included the ones
     * occupied by the opponents' workers. (the cells occupied by the player's workers are excluded)
     * @param gameSession This is a reference to the main access to the game database.
     * @return An HashMap containing all the coordinates where the two workers owned by the player can move.
     */
    public HashMap<Coord, ArrayList<Coord>> findAvailablePositionsToMove(GameSession gameSession) {
        CellsHandler cellsHandler = gameSession.getCellsHandler();
        Player currentPlayer = gameSession.getCurrentPlayer();
        List<Worker> actualWorkers = gameSession.getWorkersHandler().getWorkers(currentPlayer.getWorkersIdsArray());

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

            for (Worker w : actualWorkers) {
                neighbouringPositions.remove(w.getCurrentPosition());
            }

            if (neighbouringPositions.size() == 0) keyIterator.remove();
        }
        return availablePositions;
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
            return new SwapMoveDecorator(component);
        else return component;
    }
}
