package it.polimi.ingsw.PSP43.server.model.card;

import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.modelHandlers.CellsHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * This decorator is used to give to the player the opportunity to win if his worker moves down two or more levels in a move
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class WinConditionDecorator extends PowerGodDecorator {

    public WinConditionDecorator() {
    }

    public WinConditionDecorator(AbstractGodCard godComponent) {
        super(godComponent);
    }

    /**
     *
     *
     * @param gameSession The session of the game, from which it is possible to retrieve the ClientListener
     * @param player The player who did the move
     * @param workerToMove The worker that is moving
     * @param newPosition The position in which the player is going to place his worker
     * @throws WinnerCaughtException if the player's worker moves down two or more levels in a move
     */
    @Override
    public void move(GameSession gameSession, Player player, Worker workerToMove, Coord newPosition) throws WinnerCaughtException {
        CellsHandler handler = gameSession.getCellsHandler();
        super.getGodComponent().move(gameSession, player, workerToMove, newPosition);
        Coord oldPosition = workerToMove.getPreviousPosition();
        int heightOldPosition = handler.getCell(oldPosition).getHeight();
        int heightNewPosition = handler.getCell(newPosition).getHeight();
        if (heightOldPosition - heightNewPosition >= 2) {
            throw new WinnerCaughtException();
        }
    }

    /**
     *
     *
     * @param gameSession The session of the game, from which it is possible to retrieve the ClientListener
     * @param player The player who did the build
     * @param worker The worker that is going to build a block
     * @param position The position in which the player's worker is going to build the block
     * */
    @Override
    public void buildBlock(GameSession gameSession, Player player, Worker worker, Coord position) {
        super.buildBlock(gameSession, player, worker, position);
    }

    /**
     *
     *
     * @param gameSession The session of the game, from which it is possible to retrieve the ClientListener
     * @param player The player who did the build
     * @param worker The worker that is going to build a dome
     * @param position The position in which the player's worker is going to build the dome
     */
    @Override
    public void buildDome(GameSession gameSession, Player player, Worker worker, Coord position) {
        super.buildDome(gameSession, player, worker, position);
    }
}
