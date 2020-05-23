package it.polimi.ingsw.PSP43.server.model.card.decorators;

import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlers.CellsHandler;
import it.polimi.ingsw.PSP43.server.modelHandlers.WorkersHandler;

/**
 * This decorator is used to give to the player the opportunity to win if his worker moves down two or more levels in a move
 */
public class WinTwoFloorsFallDecorator extends PowerGodDecorator {
    private static final long serialVersionUID = -9171126719022096338L;

    public WinTwoFloorsFallDecorator(AbstractGodCard godComponent) {
        super(godComponent);
    }

    public boolean checkConditionsToWin(GameSession gameSession) {
        WorkersHandler workersHandler = gameSession.getWorkersHandler();
        Integer[] wIds = gameSession.getCurrentPlayer().getWorkersIdsArray();
        for (Integer i : wIds) {
            Worker worker = workersHandler.getWorker(i);
            if (worker.isLatestMoved()) {
                CellsHandler cellsHandler = gameSession.getCellsHandler();
                Coord oldPosition = worker.getPreviousPosition();
                int heightOldPosition = cellsHandler.getCell(oldPosition).getHeight();
                int heightNewPosition = cellsHandler.getCell(worker.getCurrentPosition()).getHeight();
                if (heightOldPosition - heightNewPosition >= 2) {
                    return true;
                }
            }
        }
        return super.checkConditionsToWin(gameSession);
    }

    public AbstractGodCard cleanFromEffects(String nameOfEffect) {
        AbstractGodCard component = super.getGodComponent().cleanFromEffects(nameOfEffect);
        Class<?> c = null;
        try {
            c = Class.forName(nameOfEffect);
        } catch (ClassNotFoundException e) { e.printStackTrace(); }

        assert c != null;
        if (!c.isInstance(this))
            return new WinTwoFloorsFallDecorator(component);
        else return component;
    }
}
