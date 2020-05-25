package it.polimi.ingsw.PSP43.server.controllers.behaviours.moveBehaviours;

import it.polimi.ingsw.PSP43.client.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.server.model.DataToMove;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.controllers.BlockRiseFactory;
import it.polimi.ingsw.PSP43.server.modelHandlers.CardsHandler;
import it.polimi.ingsw.PSP43.server.modelHandlers.WorkersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameLostException;

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
    private void disablePowers(GameSession gameSession) {
        gameSession.getCardsHandler().removeDecorator(this.getGodName(), "it.polimi.ingsw.PSP43.server.model.card.decorators.BlockRiseDecorator");
    }

    /**
     * Each turn this method is called if the worker's player rises of one level to block
     * the opponents' workers to rise to one higher level in their move.
     * @param gameSession The reference to the active game database.
     */
    private void enablePowers(GameSession gameSession) {
        CardsHandler cardsHandler = gameSession.getCardsHandler();
        cardsHandler.addDecorator(this.getGodName(), new BlockRiseFactory());
    }

    /**
     * This method handles the move of a worker and at the end, if he has risen of one level,
     * calls the method to block the other workers to move up in the next move.
     * @param gameSession The main access to the database of the game.
     */
    public void handleInitMove(GameSession gameSession) throws GameEndedException, GameLostException {
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
