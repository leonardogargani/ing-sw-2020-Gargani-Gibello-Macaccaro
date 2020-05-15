package it.polimi.ingsw.PSP43.server.model.card.decorators;

import it.polimi.ingsw.PSP43.client.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.server.model.DataToMove;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlers.CellsHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class BlockRiseDecorator extends PowerGodDecorator {
    private static final long serialVersionUID = -5029682300766417371L;

    public BlockRiseDecorator() {
        super();
    }

    public BlockRiseDecorator(AbstractGodCard godComponent) {
        super(godComponent);
    }

    public HashMap<Coord, ArrayList<Coord>> findAvailablePositionsToMove(GameSession gameSession) {
        CellsHandler cellsHandler = gameSession.getCellsHandler();
        HashMap<Coord, ArrayList<Coord>> availablePositionsToMove = super.findAvailablePositionsToMove(gameSession);
        for (Coord c : availablePositionsToMove.keySet()) {
            for (Coord c1 : availablePositionsToMove.get(c)) {
                Cell newCell = cellsHandler.getCell(c1);
                Cell oldCell = cellsHandler.getCell(c);
                if (newCell.getHeight() > oldCell.getHeight()) availablePositionsToMove.get(c).remove(c1);
            }
        }
        return availablePositionsToMove;
    }

    public AbstractGodCard cleanFromEffects(String nameOfEffect) throws ClassNotFoundException {
        return super.getGodComponent().cleanFromEffects(nameOfEffect);
    }

    public void initMove(GameSession gameSession) throws ClassNotFoundException, WinnerCaughtException, InterruptedException, IOException, GameEndedException {
        HashMap<Coord, ArrayList<Coord>> availablePositionsAfterBlocked = findAvailablePositionsToMove(gameSession);

        ActionResponse actionResponse = askForMove(gameSession, availablePositionsAfterBlocked);
        Worker workerMoved = gameSession.getWorkersHandler().getWorker(actionResponse.getWorkerPosition());

        move(new DataToMove(gameSession, gameSession.getCurrentPlayer(), workerMoved, actionResponse.getPosition()));
    }
}
