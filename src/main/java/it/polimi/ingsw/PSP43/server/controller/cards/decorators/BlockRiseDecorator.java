package it.polimi.ingsw.PSP43.server.controller.cards.decorators;

import it.polimi.ingsw.PSP43.server.controller.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.controller.cards.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.CellsHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This class represents the decorator used to block the rise of the workers when Athena goes up of one level.
 */
public class BlockRiseDecorator extends PowerGodDecorator {
    private static final long serialVersionUID = -5029682300766417371L;

    public BlockRiseDecorator(AbstractGodCard godComponent) {
        super(godComponent);
    }

    /**
     * This method is used to filter positions to move, removing the ones that imply a rise of one level.
     * @param gameSession This is a reference to the main access to the game database.
     * @return All the positions where a player can move his workers.
     */
    public HashMap<Coord, ArrayList<Coord>> findAvailablePositionsToMove(GameSession gameSession) {
        CellsHandler cellsHandler = gameSession.getCellsHandler();
        HashMap<Coord, ArrayList<Coord>> availablePositionsToMove = super.findAvailablePositionsToMove(gameSession);
        Iterator<Map.Entry<Coord,ArrayList<Coord>>> iter = availablePositionsToMove.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Coord,ArrayList<Coord>> entry = iter.next();
            ArrayList<Coord> currentPositions = entry.getValue();
            for(Iterator<Coord> coordIterator = currentPositions.iterator(); coordIterator.hasNext(); ) {
                Cell newCell = cellsHandler.getCell(coordIterator.next());
                Cell oldCell = cellsHandler.getCell(entry.getKey());
                if (newCell.getHeight() > oldCell.getHeight()) coordIterator.remove();
            }
            if (currentPositions.size() == 0) iter.remove();
        }
        return availablePositionsToMove;
    }

    /**
     * This method eliminates the block from the card not wrapping up it into this blocker.
     * @param nameOfEffect The effect that the blocker has activated by doing a determined action.
     * @return The card cleaned from the blocker.
     */
    public AbstractGodCard cleanFromEffects(String nameOfEffect) {
        return super.getGodComponent().cleanFromEffects(nameOfEffect);
    }
}
