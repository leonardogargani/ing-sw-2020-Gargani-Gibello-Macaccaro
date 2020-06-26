package it.polimi.ingsw.PSP43.server.controller.cards.decorators;

import it.polimi.ingsw.PSP43.server.controller.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.controller.cards.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.CellsHandler;

/**
 * This class represents Chronus' behaviour.
 */
public class WinFiveDomeDecorator extends PowerGodDecorator {
    private static final long serialVersionUID = 4504668968733552582L;

    public WinFiveDomeDecorator(AbstractGodCard godComponent) { super(godComponent); }

    /**
     * This method checks if the player has won the game, according Chronus' powers. So another case in which the player wins a game is when there are five complete towers on the board.
     * @param gameSession This is a reference to the main access to the game database.
     * @return True if the player won the game, false otherwise.
     */
    public boolean checkConditionsToWin(GameSession gameSession) {
        CellsHandler cellsHandler = gameSession.getCellsHandler();

        if (cellsHandler.findNumberOfDomes() == 5) return true;
        else return super.checkConditionsToWin(gameSession);
    }

    /**
     * This method is used to clean the card from possible decorators which could block some functionalities.
     * It is called when the blocker begins a new turn.
     * @param nameOfEffect The effect that the blocker has activated by doing a determined action.
     * @return The card cleaned by the blocking decorator passed as parameter.
     */
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
