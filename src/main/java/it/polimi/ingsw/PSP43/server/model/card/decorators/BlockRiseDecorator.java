package it.polimi.ingsw.PSP43.server.model.card.decorators;

import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlers.CellsHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BlockRiseDecorator extends PowerGodDecorator {
    private static final long serialVersionUID = -5029682300766417371L;

    public BlockRiseDecorator() {
        super();
    }

    public BlockRiseDecorator(AbstractGodCard godComponent) {
        super(godComponent);
    }

    // TODO useless if I call the findpositions on the external abs
    /*public void initMove(GameSession gameSession) throws WinnerCaughtException, GameEndedException, GameLostException {
        HashMap<Coord, ArrayList<Coord>> availablePositionsAfterBlocked = findAvailablePositionsToMove(gameSession);

        if (availablePositionsAfterBlocked.size() == 0) throw new GameLostException();

        ActionResponse actionResponse = askForMove(gameSession, availablePositionsAfterBlocked);
        Worker workerMoved = gameSession.getWorkersHandler().getWorker(actionResponse.getWorkerPosition());

        move(new DataToMove(gameSession, gameSession.getCurrentPlayer(), workerMoved, actionResponse.getPosition()));
    }*/

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
