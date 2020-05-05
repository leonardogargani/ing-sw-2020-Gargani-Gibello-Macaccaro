package it.polimi.ingsw.PSP43.server.model.card.behaviours;

import it.polimi.ingsw.PSP43.client.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.client.networkMessages.ResponseMessage;
import it.polimi.ingsw.PSP43.server.DataToAction;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;
import it.polimi.ingsw.PSP43.server.networkMessages.ActionRequest;
import it.polimi.ingsw.PSP43.server.networkMessages.RequestMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class BuildBeforeMoveBehaviour extends AbstractGodCard implements MoveBehavior {
    private static final long serialVersionUID = 2762718994928009618L;

    public void handleMove(DataToAction dataToAction) throws IOException, ClassNotFoundException, WinnerCaughtException, InterruptedException {
        GameSession gameSession = dataToAction.getGameSession();
        Player player = dataToAction.getPlayer();
        Worker worker = dataToAction.getWorker();
        Cell oldCell = gameSession.getCellsHandler().getCell(worker.getCurrentPosition());
        Cell newCell = gameSession.getCellsHandler().getCell(dataToAction.getNewPosition());
        if (newCell.getHeight() - oldCell.getHeight() == 0) {
            RequestMessage message = new RequestMessage("Do you want to build before moving?");
            ResponseMessage responseMessage;
            do {
                responseMessage = gameSession.sendRequest(message, player.getNickname(), ResponseMessage.class);
            } while (responseMessage == null);

            boolean response = responseMessage.isResponse();
            if (response) {
                buildBeforeMove(dataToAction);
            }
        }
        else super.move(dataToAction);
    }

    private void buildBeforeMove(DataToAction oldData) throws InterruptedException, IOException, ClassNotFoundException {
        GameSession gameSession = oldData.getGameSession();
        ArrayList<Worker> workers = new ArrayList<>();
        Worker workerAllowedToBuild = oldData.getWorker();
        workers.add(workerAllowedToBuild);
        HashMap<Coord, ArrayList<Coord>> availablePositions = gameSession.getCellsHandler().findNeighbouringCoords(workers);
        for (Coord c : availablePositions.keySet()) {
            availablePositions.get(c).removeIf(c1 ->
                    (c1.getY() == oldData.getNewPosition().getY() && c1.getX() == oldData.getNewPosition().getX()) ||
                    c1.getY() == workerAllowedToBuild.getCurrentPosition().getY() && c1.getX() == workerAllowedToBuild.getCurrentPosition().getX());
        }
        ActionRequest request = new ActionRequest("Choose a cell where to build.", availablePositions);
        ActionResponse actionResponse;
        do {
            actionResponse = gameSession.sendRequest(request, gameSession.getCurrentPlayer().getNickname(), ActionResponse.class);
        } while (actionResponse == null);

        Coord coordToBuild = actionResponse.getPosition();
        DataToAction dataBuild = new DataToAction(gameSession, oldData.getPlayer(), workerAllowedToBuild, coordToBuild);
        super.buildBlock(dataBuild);
    }
}
