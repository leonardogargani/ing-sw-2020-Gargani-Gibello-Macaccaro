package it.polimi.ingsw.PSP43.server.model.card.behaviours.buildBehaviours;

import it.polimi.ingsw.PSP43.client.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.client.networkMessages.ClientMessage;
import it.polimi.ingsw.PSP43.client.networkMessages.ResponseMessage;
import it.polimi.ingsw.PSP43.server.model.DataToBuild;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.model.card.BasicGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlers.WorkersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.networkMessages.ActionRequest;
import it.polimi.ingsw.PSP43.server.networkMessages.RequestMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class BasicBuildBehaviour extends BasicGodCard implements BuildBehaviour {
    private static final long serialVersionUID = 4158958110907103295L;

    public DataToBuild genericAskForBuild(GameSession gameSession) throws GameEndedException, InterruptedException, IOException, ClassNotFoundException {
        Player currentPlayer = gameSession.getCurrentPlayer();
        WorkersHandler workersHandler = gameSession.getWorkersHandler();

        HashMap<Coord, ArrayList<Coord>> availablePositionsBuildBlock = findAvailablePositionsToBuildBlock(gameSession);
        HashMap<Coord, ArrayList<Coord>> availablePositionsBuildDome = findAvailablePositionsToBuildDome(gameSession);

        ResponseMessage response = new ResponseMessage(false);
        if (availablePositionsBuildDome.size() != 0) {
            RequestMessage request = new RequestMessage("Do you want to build a dome? Otherwise you will " +
                    "select a cell where to build a block.");
            try {
                response = gameSession.sendRequest(request, currentPlayer.getNickname(), new ResponseMessage());
            } catch (InterruptedException | IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        ActionResponse actionResponse;
        Worker workerToBuild;
        if (response.isResponse()) {
            String message = "Choose a position where to build your dome.";
            actionResponse = askForBuild(gameSession, availablePositionsBuildBlock, message);
            workerToBuild = workersHandler.getWorker(actionResponse.getWorkerPosition());
            return new DataToBuild(gameSession, currentPlayer, workerToBuild, actionResponse.getPosition(), Boolean.TRUE);
        } else {
            String message = "Choose a position where to build your block.";
            actionResponse = askForBuild(gameSession, availablePositionsBuildBlock, message);
            workerToBuild = workersHandler.getWorker(actionResponse.getWorkerPosition());
            return new DataToBuild(gameSession, currentPlayer, workerToBuild, actionResponse.getPosition(), Boolean.FALSE);
        }
    }

    public <T extends ClientMessage> T askForBuild(GameSession gameSession, HashMap<Coord, ArrayList<Coord>> availablePositionsBuildBlock, String message) throws GameEndedException, InterruptedException, IOException, ClassNotFoundException {
        Player currentPlayer = gameSession.getCurrentPlayer();

        ActionRequest actionRequest = new ActionRequest(message, availablePositionsBuildBlock);
        ActionResponse actionResponse = gameSession.sendRequest(actionRequest, currentPlayer.getNickname(), new ActionResponse());

        return (T) actionResponse;
    }

    public void handleInitBuild(GameSession gameSession) throws IOException, ClassNotFoundException, InterruptedException, GameEndedException {
        DataToBuild dataToBuild = genericAskForBuild(gameSession);

        Coord nextPositionChosen = dataToBuild.getNewPosition();
        Worker workerMoved = dataToBuild.getWorker();

        if (dataToBuild.getBuildDome())
            build(new DataToBuild(gameSession, gameSession.getCurrentPlayer(), workerMoved, nextPositionChosen, Boolean.TRUE));
        else
            build(new DataToBuild(gameSession, gameSession.getCurrentPlayer(), workerMoved, nextPositionChosen, Boolean.FALSE));
    }
}
