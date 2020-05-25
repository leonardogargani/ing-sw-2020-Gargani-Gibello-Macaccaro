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
        else return checkConditionsToWin(gameSession);
    }

    public AbstractGodCard cleanFromEffects(String nameOfEffect) {
        return super.cleanFromEffects(nameOfEffect);
    }
}
