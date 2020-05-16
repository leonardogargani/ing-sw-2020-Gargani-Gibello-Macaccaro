package it.polimi.ingsw.PSP43.server.model.card.behaviours.moveBehaviours;

import it.polimi.ingsw.PSP43.client.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.server.model.DataToMove;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.model.card.decorators.BlockRiseDecorator;
import it.polimi.ingsw.PSP43.server.modelHandlers.CardsHandler;
import it.polimi.ingsw.PSP43.server.modelHandlers.WorkersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;

import java.io.IOException;
import java.util.HashMap;

/**
 * This class is made to give run-time the possibility to a card
 * to block the opponents to rise when the owner's worker rise
 * in a turn.
 */
public class BlockOpponentRiseBehaviour extends BasicMoveBehaviour {
    private static final long serialVersionUID = 409949207020680510L;


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
     * @param gameSession The main access to the database of the game.
     * @throws WinnerCaughtException if a worker is moved on the third level.
     */
    public void handleInitMove(GameSession gameSession) throws ClassNotFoundException, IOException, WinnerCaughtException, InterruptedException, GameEndedException {
        disablePowers(gameSession);

        WorkersHandler workersHandler = gameSession.getWorkersHandler();
        ActionResponse response = askForMove(gameSession);

        Coord nextPositionChosen = response.getPosition();
        Worker workerMoved = workersHandler.getWorker(response.getWorkerPosition());

        move(new DataToMove(gameSession, gameSession.getCurrentPlayer(), workerMoved, nextPositionChosen));

        Coord newCoord = workerMoved.getCurrentPosition();
        Cell newCell = gameSession.getCellsHandler().getCell(newCoord);
        Coord oldCoord = workerMoved.getPreviousPosition();
        Cell oldCell = gameSession.getCellsHandler().getCell(oldCoord);

        if (newCell.getHeight() - oldCell.getHeight() > 0) {
            enablePowers(gameSession);
        }
    }
}
