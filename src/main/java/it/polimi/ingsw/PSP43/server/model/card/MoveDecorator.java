package it.polimi.ingsw.PSP43.server.model.card;

import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.GameSession;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.modelHandlersException.CellAlreadyOccupiedExeption;
import it.polimi.ingsw.PSP43.server.modelHandlersException.CellHeightException;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;

public class MoveDecorator extends PowerGodDecorator {
    private MoveBehavior moveBehavior;

    public MoveDecorator() {
    }

    public MoveDecorator(AbstractGodCard godComponent, MoveBehavior moveBehavior) {
        super(godComponent);
        this.moveBehavior = moveBehavior;
    }

    @Override
    public void move(GameSession gameSession, Player player, Worker workerToMove, Coord newPosition) throws CellHeightException, CellAlreadyOccupiedExeption, WinnerCaughtException {
        moveBehavior.handleMove(gameSession, player, workerToMove, newPosition);
    }

    /**
     *
     *
     * @param gameSession The session of the game, from which it is possible to retrieve the ClientListener
     * @param player The player who did the build
     * @param worker The worker that is going to build a block
     * @param position The position in which the player's worker is going to build the block
     * @throws CellHeightException if it is not possible to build anymore on the cell because level three is already reached
     * @throws CellAlreadyOccupiedExeption if it is not possible to build in newPosition because the cell is already occupied by another worker or dome
     */
    public void buildBlock(GameSession gameSession, Player player, Worker worker, Coord position) throws CellHeightException, CellAlreadyOccupiedExeption {
        super.buildBlock(gameSession, player, worker, position);
    }

    /**
     *
     *
     * @param gameSession The session of the game, from which it is possible to retrieve the ClientListener
     * @param player The player who did the build
     * @param worker The worker that is going to build a dome
     * @param position The position in which the player's worker is going to build the dome
     * @throws CellHeightException
     * @throws CellAlreadyOccupiedExeption if it is not possible to build in newPosition because the cell is already occupied by another worker or dome
     */
    public void buildDome(GameSession gameSession, Player player, Worker worker, Coord position) throws CellHeightException, CellAlreadyOccupiedExeption {
        super.buildDome(gameSession, player, worker, position);
    }
}
