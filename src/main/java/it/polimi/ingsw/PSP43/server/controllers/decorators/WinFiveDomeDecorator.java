package it.polimi.ingsw.PSP43.server.controllers.decorators;

import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.controllers.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlers.CellsHandler;

public class WinFiveDomeDecorator extends PowerGodDecorator {
    private static final long serialVersionUID = 4504668968733552582L;

    public WinFiveDomeDecorator(AbstractGodCard godComponent) { super(godComponent); }

    public boolean checkConditionsToWin(GameSession gameSession) {
        CellsHandler cellsHandler = gameSession.getCellsHandler();

        if (cellsHandler.findNumberOfDomes() == 5) return true;
        else return super.checkConditionsToWin(gameSession);
    }

    public AbstractGodCard cleanFromEffects(String nameOfEffect) {
        AbstractGodCard component = super.getGodComponent().cleanFromEffects(nameOfEffect);
        Class<?> c = null;
        try {
            c = Class.forName(nameOfEffect);
        } catch (ClassNotFoundException e) { e.printStackTrace(); }

        assert c != null;
        if (!c.isInstance(this))
            return new WinFiveDomeDecorator(component);
        else return component;
    }
}
