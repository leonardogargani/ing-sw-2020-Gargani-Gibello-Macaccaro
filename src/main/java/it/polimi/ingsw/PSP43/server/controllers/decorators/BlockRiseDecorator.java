package it.polimi.ingsw.PSP43.server.controllers.decorators;

import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.controllers.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlers.CellsHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BlockRiseDecorator extends PowerGodDecorator {
    private static final long serialVersionUID = -5029682300766417371L;

    public BlockRiseDecorator(AbstractGodCard godComponent) {
        super(godComponent);
    }

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

    public AbstractGodCard cleanFromEffects(String nameOfEffect) {
        return super.getGodComponent().cleanFromEffects(nameOfEffect);
    }
}
