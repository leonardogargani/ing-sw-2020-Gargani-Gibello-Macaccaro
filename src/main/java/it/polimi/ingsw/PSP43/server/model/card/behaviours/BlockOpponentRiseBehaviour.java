package it.polimi.ingsw.PSP43.server.model.card.behaviours;

import it.polimi.ingsw.PSP43.server.DataToAction;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.model.card.decorators.BlockRiseDecorator;
import it.polimi.ingsw.PSP43.server.modelHandlers.CardsHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;

import java.io.IOException;
import java.util.HashMap;

/**
 * This class is made to give run-time the possibility to a card
 * to block the opponents to rise when the owner's worker rise
 * in a turn.
 */
public class BlockOpponentRiseBehaviour extends AbstractGodCard implements MoveBehavior {
    private static final long serialVersionUID = 409949207020680510L;

    public BlockOpponentRiseBehaviour() {
    }

    /**
     * Each turn, before the player moves his worker, this method is called to remove from all
     * the opponents' cards the block to rise that was imposed the previous turn.
     * @param gameSession The reference to the active game database.
     */
    private void disablePowers(GameSession gameSession) throws ClassNotFoundException {
        HashMap<String, AbstractGodCard> mapCardsOwned = gameSession.getCardsHandler().getMapOwnersCard();
        String godName;
        AbstractGodCard card, newCard;

        for (String s : mapCardsOwned.keySet()) {
            godName = mapCardsOwned.get(s).getGodName();
            if (!godName.equals("Athena")) {
                card = mapCardsOwned.get(s);
                newCard = card.cleanFromEffects("it.polimi.ingsw.PSP43.server.model.card.decorators.BlockRiseDecorator");
                mapCardsOwned.put(s, newCard);
            }
        }

    }

    /**
     * Each turn this method is called if the worker's player rises of one level to block
     * the opponents' workers to rise to one higher level in their move.
     * @param gameSession The reference to the active game database.
     */
    private void enablePowers(GameSession gameSession) {
        CardsHandler cardsHandler = gameSession.getCardsHandler();
        AbstractGodCard cardOwned;
        for (String s : cardsHandler.getMapOwnersCard().keySet()) {
            cardOwned = cardsHandler.getMapOwnersCard().get(s);
            if (!cardOwned.getGodName().equals("Athena")) {
                cardsHandler.getMapOwnersCard().put(s, new BlockRiseDecorator(cardOwned));
            }
        }
    }

    /**
     * This method handles the move of a worker and at the end, if he has risen of one level,
     * calls the method to block the other workers to move up in the next move.
     * @param dataToAction The data required to handle a move and to change properly the data stored in the model.
     * @throws WinnerCaughtException if a worker is moved on the third level.
     */
    public void handleMove(DataToAction dataToAction) throws ClassNotFoundException, IOException, WinnerCaughtException, InterruptedException {
        disablePowers(dataToAction.getGameSession());
        super.move(dataToAction);
        Coord newCoord = dataToAction.getWorker().getCurrentPosition();
        Cell newCell = dataToAction.getGameSession().getCellsHandler().getCell(newCoord);
        Coord oldCoord = dataToAction.getWorker().getPreviousPosition();
        Cell oldCell = dataToAction.getGameSession().getCellsHandler().getCell(oldCoord);

        if (newCell.getHeight() - oldCell.getHeight() > 0) {
            enablePowers(dataToAction.getGameSession());
        }
    }
}
