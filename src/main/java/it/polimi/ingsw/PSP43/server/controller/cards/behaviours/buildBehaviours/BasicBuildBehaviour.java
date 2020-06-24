package it.polimi.ingsw.PSP43.server.controller.cards.behaviours.buildBehaviours;

import it.polimi.ingsw.PSP43.client.network.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.client.network.networkMessages.ResponseMessage;
import it.polimi.ingsw.PSP43.server.model.DataToBuild;
import it.polimi.ingsw.PSP43.server.controller.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.controller.cards.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.controller.cards.BasicGodCard;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.CardsHandler;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.WorkersHandler;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.network.networkMessages.ActionRequest;
import it.polimi.ingsw.PSP43.server.network.networkMessages.RequestMessage;
import it.polimi.ingsw.PSP43.server.network.networkMessages.TextMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * This is a basic build behaviour. Having this as build behaviour means that the card
 * will be able only to build blocks and domes according to the basic rules of a worker.
 */
public class BasicBuildBehaviour extends BasicGodCard implements BuildBehaviour {
    private static final long serialVersionUID = 4158958110907103295L;

    /**
     * This method is called for a generic ask to build. So it will be possible for the player to build either a dome or a block (according to rules).
     * @param gameSession This is a reference to the main access to the game database.
     * @return The data representing the choice where to build of the player. They are used by the controller to change the model.
     * @throws GameEndedException   if the player that was asked to build something exits the game. This is treated as an exception because it should be a
     *                              not-expected behaviour from the player.
     */
    public DataToBuild genericAskForBuild(GameSession gameSession) throws GameEndedException {
        Player currentPlayer = gameSession.getCurrentPlayer();
        WorkersHandler workersHandler = gameSession.getWorkersHandler();
        CardsHandler cardsHandler = gameSession.getCardsHandler();
        AbstractGodCard abstractGodCard = cardsHandler.getPlayerCard(currentPlayer.getNickname());

        HashMap<Coord, ArrayList<Coord>> availablePositionsBuildBlock = abstractGodCard.findAvailablePositionsToBuildBlock(gameSession);
        HashMap<Coord, ArrayList<Coord>> availablePositionsBuildDome = abstractGodCard.findAvailablePositionsToBuildDome(gameSession);



        ResponseMessage response = new ResponseMessage(false);
        if (availablePositionsBuildDome.size() != 0) {
            RequestMessage request = new RequestMessage("Do you want to build a dome? Otherwise you will " +
                    "select a cell where to build a block.");
            response = gameSession.sendRequest(request, currentPlayer.getNickname(), ResponseMessage.class);
        }

        ActionResponse actionResponse;
        Worker workerToBuild;
        if (response.isResponse()) {
            String message = "Choose a position where to build your dome.";
            actionResponse = askForBuild(gameSession, availablePositionsBuildDome, message);
            workerToBuild = workersHandler.getWorker(actionResponse.getWorkerPosition());
            return new DataToBuild(gameSession, currentPlayer, workerToBuild, actionResponse.getPosition(), Boolean.TRUE);
        } else {
            if (availablePositionsBuildBlock.size() != 0) {
                String message = "Choose a position where to build your block.";
                actionResponse = askForBuild(gameSession, availablePositionsBuildBlock, message);
                workerToBuild = workersHandler.getWorker(actionResponse.getWorkerPosition());
                return new DataToBuild(gameSession, currentPlayer, workerToBuild, actionResponse.getPosition(), Boolean.FALSE);
            }
            else {
                TextMessage textMessage = new TextMessage("We are sorry, but you can't build a block anywhere.", TextMessage.PositionInScreen.LOW_CENTER);
                gameSession.sendMessage(textMessage, currentPlayer.getNickname());
                return null;
            }
        }
    }

    /**
     * This method asks to the player where he wants to build, choosing within the cells passed as parameter of the method.
     * @param gameSession This is a reference to the main access to the game database.
     * @param availablePositionsBuildBlock The positions where the workers can build.
     * @param message The message sent to the client and used to ask him where he wants to build.
     * @return The response arrived from the client with the position where to build.
     * @throws GameEndedException if the player decides to leave the game during his turn.
     */
    public ActionResponse askForBuild(GameSession gameSession, HashMap<Coord, ArrayList<Coord>> availablePositionsBuildBlock, String message) throws GameEndedException {
        Player currentPlayer = gameSession.getCurrentPlayer();

        ActionRequest actionRequest = new ActionRequest(message, Collections.unmodifiableMap(new HashMap<>(availablePositionsBuildBlock)));

        return gameSession.sendRequest(actionRequest, currentPlayer.getNickname(), ActionResponse.class);
    }

    /**
     * This method is used for a basic build behaviour, that is the one without use of god cards.
     * @param gameSession This is a reference to the main access to the game database.
     * @throws GameEndedException if the player decides to leave the game during his turn.
     */
    public void handleInitBuild(GameSession gameSession) throws GameEndedException {
        DataToBuild dataToBuild = genericAskForBuild(gameSession);
        if (dataToBuild == null) return;

        Coord nextPositionChosen = dataToBuild.getNewPosition();
        Worker workerMoved = dataToBuild.getWorker();

        if (dataToBuild.getBuildDome())
            build(new DataToBuild(gameSession, gameSession.getCurrentPlayer(), workerMoved, nextPositionChosen, Boolean.TRUE));
        else
            build(new DataToBuild(gameSession, gameSession.getCurrentPlayer(), workerMoved, nextPositionChosen, Boolean.FALSE));
    }
}
