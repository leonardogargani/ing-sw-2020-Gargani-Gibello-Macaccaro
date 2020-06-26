package it.polimi.ingsw.PSP43.server.controller.cards.behaviours.moveBehaviours;

import it.polimi.ingsw.PSP43.client.network.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.server.model.DataToMove;
import it.polimi.ingsw.PSP43.server.controller.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.controller.cards.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.controller.cards.BasicGodCard;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.CardsHandler;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.WorkersHandler;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException.GameLostException;
import it.polimi.ingsw.PSP43.server.network.networkMessages.ActionRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * This class is the basic one to handle moves of workers.
 */
public class BasicMoveBehaviour extends BasicGodCard implements MoveBehavior {
    private static final long serialVersionUID = -198685635604926007L;

    /**
     * This method asks to the client to move a worker.
     * @param gameSession This is a reference to the main access to the game database.
     * @return The response containing the choice of the player.
     * @throws GameEndedException if the player decides to leave the game during his turn.
     * @throws GameLostException if the player can't do any move.
     */
    public ActionResponse askForMove(GameSession gameSession) throws GameEndedException, GameLostException {
        CardsHandler cardsHandler = gameSession.getCardsHandler();
        AbstractGodCard abstractGodCard = cardsHandler.getPlayerCard(gameSession.getCurrentPlayer().getNickname());

        HashMap<Coord, ArrayList<Coord>> availablePositions = abstractGodCard.findAvailablePositionsToMove(gameSession);

        if (availablePositions.size() == 0) throw new GameLostException();

        return askForMove(gameSession, availablePositions);
    }

    /**
     * This method asks to the player for a move, between some positions already selected.
     * @param gameSession This is a reference to the main access to the game database.
     * @param availablePositions The positions among which the player can choose where to move a worker.
     * @return The response from the player about where to move a worker.
     * @throws GameEndedException if the player decides to leave the game during his turn.
     */
    public ActionResponse askForMove(GameSession gameSession, HashMap<Coord, ArrayList<Coord>> availablePositions) throws GameEndedException {
        ActionRequest message = new ActionRequest(  "Choose a position where to place your worker next.",
                                                            Collections.unmodifiableMap(new HashMap<>(availablePositions)));
        ActionResponse response;
        do {
            response = gameSession.sendRequest(message, gameSession.getCurrentPlayer().getNickname(), ActionResponse.class);
        } while (response == null);

        return response;
    }

    /**
     * This method handles a basic move, where the player is asked where to move his worker.
     * @param gameSession This is a reference to the main access to the game database.
     * @throws GameEndedException if the player decides to leave the game during his turn.
     * @throws GameLostException if the player can't do any move.
     */
    public void handleInitMove(GameSession gameSession) throws GameEndedException, GameLostException {
        WorkersHandler workersHandler = gameSession.getWorkersHandler();

        ActionResponse response = askForMove(gameSession);

        Coord nextPositionChosen = response.getPosition();
        Worker workerMoved = workersHandler.getWorker(response.getWorkerPosition());

        move(new DataToMove(gameSession, gameSession.getCurrentPlayer(), workerMoved, nextPositionChosen));
    }
}
