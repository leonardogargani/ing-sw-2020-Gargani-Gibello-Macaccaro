package it.polimi.ingsw.PSP43.server.controllers.behaviours.moveBehaviours;

import it.polimi.ingsw.PSP43.client.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.server.model.DataToMove;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.controllers.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.controllers.BasicGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlers.CardsHandler;
import it.polimi.ingsw.PSP43.server.modelHandlers.WorkersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameLostException;
import it.polimi.ingsw.PSP43.server.networkMessages.ActionRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class BasicMoveBehaviour extends BasicGodCard implements MoveBehavior {
    private static final long serialVersionUID = -198685635604926007L;

    public ActionResponse askForMove(GameSession gameSession) throws GameEndedException, GameLostException {
        CardsHandler cardsHandler = gameSession.getCardsHandler();
        AbstractGodCard abstractGodCard = cardsHandler.getPlayerCard(gameSession.getCurrentPlayer().getNickname());

        HashMap<Coord, ArrayList<Coord>> availablePositions = abstractGodCard.findAvailablePositionsToMove(gameSession);

        if (availablePositions.size() == 0) throw new GameLostException();

        return askForMove(gameSession, availablePositions);
    }

    public ActionResponse askForMove(GameSession gameSession, HashMap<Coord, ArrayList<Coord>> availablePositions) throws GameEndedException {
        ActionRequest message = new ActionRequest(  "Choose a position where to place your worker next.",
                                                            Collections.unmodifiableMap(new HashMap<>(availablePositions)));
        ActionResponse response;
        do {
            response = gameSession.sendRequest(message, gameSession.getCurrentPlayer().getNickname(), ActionResponse.class);
        } while (response == null);

        return response;
    }

    public void handleInitMove(GameSession gameSession) throws GameEndedException, GameLostException {
        WorkersHandler workersHandler = gameSession.getWorkersHandler();

        ActionResponse response = askForMove(gameSession);

        Coord nextPositionChosen = response.getPosition();
        Worker workerMoved = workersHandler.getWorker(response.getWorkerPosition());

        move(new DataToMove(gameSession, gameSession.getCurrentPlayer(), workerMoved, nextPositionChosen));
    }
}
