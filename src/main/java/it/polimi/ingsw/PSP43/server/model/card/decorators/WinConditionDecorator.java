package it.polimi.ingsw.PSP43.server.model.card.decorators;

import it.polimi.ingsw.PSP43.server.DataToAction;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlers.CellsHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;

import java.io.IOException;

/**
 * This decorator is used to give to the player the opportunity to win if his worker moves down two or more levels in a move
 */
public class WinConditionDecorator extends PowerGodDecorator {

    public WinConditionDecorator() {
    }

    public WinConditionDecorator(AbstractGodCard godComponent) {
        super(godComponent);
    }

    /**
     *
     *
     * @param dataToAction
     * @throws WinnerCaughtException if the player's worker moves down two or more levels in a move
     */
    @Override
    public void move(DataToAction dataToAction) throws IOException, ClassNotFoundException, WinnerCaughtException {
        super.move(dataToAction);
        CellsHandler handler = dataToAction.getGameSession().getCellsHandler();
        Coord oldPosition = dataToAction.getWorker().getPreviousPosition();
        int heightOldPosition = handler.getCell(oldPosition).getHeight();
        int heightNewPosition = handler.getCell(dataToAction.getNewPosition()).getHeight();
        if (heightOldPosition - heightNewPosition >= 2) {
            throw new WinnerCaughtException();
        }
    }

    public AbstractGodCard cleanFromEffects(String nameOfEffect) throws ClassNotFoundException {
        AbstractGodCard newCard;
        AbstractGodCard component = super.getGodComponent().cleanFromEffects(nameOfEffect);
        Class c = Class.forName(nameOfEffect);
        if (!c.isInstance(this))
            return newCard = new WinConditionDecorator(component);
        else return component;
    }
}
