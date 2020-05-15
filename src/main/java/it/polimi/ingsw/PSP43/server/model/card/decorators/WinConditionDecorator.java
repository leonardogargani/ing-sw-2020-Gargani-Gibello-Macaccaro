package it.polimi.ingsw.PSP43.server.model.card.decorators;

import it.polimi.ingsw.PSP43.client.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.server.DataToMove;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlers.CellsHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;

import java.io.IOException;

/**
 * This decorator is used to give to the player the opportunity to win if his worker moves down two or more levels in a move
 */
public class WinConditionDecorator extends PowerGodDecorator {
    private static final long serialVersionUID = -9171126719022096338L;

    public WinConditionDecorator() {
    }

    public WinConditionDecorator(AbstractGodCard godComponent) {
        super(godComponent);
    }

    @Override
    public void initMove(GameSession gameSession) throws ClassNotFoundException, WinnerCaughtException, InterruptedException, IOException, GameEndedException {
        ActionResponse responseMessage = askForMove(gameSession);

        Worker workerMoved = gameSession.getWorkersHandler().getWorker(responseMessage.getWorkerPosition());
        move(new DataToMove(gameSession, gameSession.getCurrentPlayer(), workerMoved, responseMessage.getPosition()));
    }

    public void move(DataToMove dataToMove) throws IOException, ClassNotFoundException, WinnerCaughtException, InterruptedException {
        super.move(dataToMove);
        CellsHandler handler = dataToMove.getGameSession().getCellsHandler();
        Coord oldPosition = dataToMove.getWorker().getPreviousPosition();
        int heightOldPosition = handler.getCell(oldPosition).getHeight();
        int heightNewPosition = handler.getCell(dataToMove.getNewPosition()).getHeight();
        if (heightOldPosition - heightNewPosition >= 2) {
            throw new WinnerCaughtException(dataToMove.getPlayer().getNickname());
        }
    }

    public AbstractGodCard cleanFromEffects(String nameOfEffect) throws ClassNotFoundException {
        AbstractGodCard component = super.getGodComponent().cleanFromEffects(nameOfEffect);
        Class<?> c = Class.forName(nameOfEffect);
        if (!c.isInstance(this))
            return new WinConditionDecorator(component);
        else return component;
    }
}
