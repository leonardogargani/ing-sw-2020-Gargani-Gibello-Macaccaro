package it.polimi.ingsw.PSP43.server.model.card.behaviours.moveBehaviours;

import it.polimi.ingsw.PSP43.client.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.client.networkMessages.ClientMessage;
import it.polimi.ingsw.PSP43.server.DataToMove;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.model.card.BasicGodCard;
import it.polimi.ingsw.PSP43.server.model.card.behaviours.buildBehaviours.BasicBuildBehaviour;
import it.polimi.ingsw.PSP43.server.modelHandlers.WorkersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;
import it.polimi.ingsw.PSP43.server.networkMessages.ActionRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class BasicMoveBehaviour extends BasicGodCard implements MoveBehavior {
    private static final long serialVersionUID = -198685635604926007L;

    public <T extends ClientMessage> T askForMove(GameSession gameSession) throws GameEndedException {
        HashMap<Coord, ArrayList<Coord>> availablePositions = findAvailablePositionsToMove(gameSession);

        return askForMove(gameSession, availablePositions);
    }

    public <T extends ClientMessage> T askForMove(GameSession gameSession, HashMap<Coord, ArrayList<Coord>> availablePositions) throws GameEndedException {
        ActionRequest message = new ActionRequest("Choose a position where to place your worker next.", availablePositions);
        ActionResponse response = null;
        do {
            try {
                response = gameSession.sendRequest(message, gameSession.getCurrentPlayer().getNickname(), new ActionResponse());
            } catch (InterruptedException | IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } while (response == null);

        return (T) response;
    }

    public void handleInitMove(GameSession gameSession) throws IOException, ClassNotFoundException, WinnerCaughtException, InterruptedException, GameEndedException {
        WorkersHandler workersHandler = gameSession.getWorkersHandler();

        ActionResponse response = askForMove(gameSession);

        Coord nextPositionChosen = response.getPosition();
        Worker workerMoved = workersHandler.getWorker(response.getWorkerPosition());

        move(new DataToMove(gameSession, gameSession.getCurrentPlayer(), workerMoved, nextPositionChosen));
    }

    public BasicBuildBehaviour getBuildBehaviour() {
        return super.getBuildBehaviour();
    }
}
