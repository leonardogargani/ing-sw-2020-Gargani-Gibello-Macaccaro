package it.polimi.ingsw.PSP43.server.controller.cards.decorators;

import it.polimi.ingsw.PSP43.server.controller.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.controller.cards.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.CellsHandler;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.PlayersHandler;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.WorkersHandler;

import java.util.Map;

/**
 * This decorator is used to give to the player the opportunity to win if his worker moves down two or more levels in a move
 */
public class WinTwoFloorsFallDecorator extends PowerGodDecorator {
    private static final long serialVersionUID = -9171126719022096338L;

    public WinTwoFloorsFallDecorator(AbstractGodCard godComponent) {
        super(godComponent);
    }

    /**
     * This method is used to check if the player has won the game. An additional case is when is worker moves down two or more levels.
     * @param gameSession This is a reference to the main access to the game database.
     * @return True if the player has won, false otherwise.
     */
    public boolean checkConditionsToWin(GameSession gameSession) {
        WorkersHandler workersHandler = gameSession.getWorkersHandler();
        PlayersHandler playersHandler = gameSession.getPlayersHandler();

        String godName = super.getGodName();
        Map<String, AbstractGodCard> mapPlayerCard = gameSession.getCardsHandler().getMapOwnersCard();
        Player currentPlayer = null;

        for (String s : mapPlayerCard.keySet()) {
            AbstractGodCard abstractGodCard = mapPlayerCard.get(s);
            if (abstractGodCard.getGodName().equals(godName)) {
                currentPlayer = playersHandler.getPlayer(s);
            }
        }

        assert currentPlayer != null;
        Integer[] wIds = currentPlayer.getWorkersIdsArray();
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
            return new WinTwoFloorsFallDecorator(component);
        else return component;
    }
}
