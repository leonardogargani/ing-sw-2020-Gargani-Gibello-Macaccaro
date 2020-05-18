package it.polimi.ingsw.PSP43.server.model.card.decorators;

import it.polimi.ingsw.PSP43.server.model.DataToMove;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlers.CellsHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;

/**
 * This decorator is used to give to the player the opportunity to win if his worker moves down two or more levels in a move
 */
public class WinConditionDecorator extends PowerGodDecorator {
    private static final long serialVersionUID = -9171126719022096338L;

    public WinConditionDecorator() {}

    public WinConditionDecorator(AbstractGodCard godComponent) {
        super(godComponent);
    }

    public void move(DataToMove dataToMove) throws WinnerCaughtException {
        super.move(dataToMove);
        CellsHandler handler = dataToMove.getGameSession().getCellsHandler();
        Coord oldPosition = dataToMove.getWorker().getPreviousPosition();
        int heightOldPosition = handler.getCell(oldPosition).getHeight();
        int heightNewPosition = handler.getCell(dataToMove.getNewPosition()).getHeight();
        if (heightOldPosition - heightNewPosition >= 2) {
            throw new WinnerCaughtException(dataToMove.getPlayer().getNickname());
        }
    }

    public AbstractGodCard cleanFromEffects(String nameOfEffect) {
        AbstractGodCard component = super.getGodComponent().cleanFromEffects(nameOfEffect);
        Class<?> c = null;
        try {
            c = Class.forName(nameOfEffect);
        } catch (ClassNotFoundException e) { e.printStackTrace(); }

        assert c != null;
        if (!c.isInstance(this))
            return new WinConditionDecorator(component);
        else return component;
    }
}
