package it.polimi.ingsw.PSP43.server.model.card.behaviours;

import it.polimi.ingsw.PSP43.client.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.client.networkMessages.ResponseMessage;
import it.polimi.ingsw.PSP43.server.DataToAction;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;
import it.polimi.ingsw.PSP43.server.networkMessages.ActionRequest;
import it.polimi.ingsw.PSP43.server.networkMessages.RequestMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class DoubleMoveBehaviour extends AbstractGodCard implements MoveBehavior {
    public void handleMove(DataToAction dataToAction) throws IOException, ClassNotFoundException, WinnerCaughtException, InterruptedException {
        super.move(dataToAction);
        RequestMessage requestMessage = new RequestMessage("Do you want to move another time?");
        ResponseMessage responseMessage;
        do {
            responseMessage = dataToAction.getGameSession().sendRequest(  requestMessage,
                                                                    dataToAction.getPlayer().getNickname(),
                                                                    ResponseMessage.class);
        } while (responseMessage == null);

        boolean response  = responseMessage.isResponse();
        if (response) {
            Worker [] workersToMove = new Worker[1];
            workersToMove[0] = dataToAction.getWorker();
            HashMap<Coord, ArrayList<Coord>> availablePositions =  super.findAvailablePositionsToMove(dataToAction.getGameSession().getCellsHandler(), workersToMove);
            for (Coord c : availablePositions.keySet()) {
                availablePositions.get(c).removeIf(c1
                        -> c1.getY() == dataToAction.getNewPosition().getY() && c1.getX() == dataToAction.getNewPosition().getX());
            }
            ActionRequest request = new ActionRequest("Choose a cell where to move.", availablePositions);
            ActionResponse actionResponse;
            do {
                actionResponse = dataToAction.getGameSession().sendRequest(request, dataToAction.getPlayer().getNickname(), ActionResponse.class);
            } while (actionResponse == null);

            Coord coordChosen = actionResponse.getPosition();
            super.move(new DataToAction(dataToAction.getGameSession(), dataToAction.getPlayer(), dataToAction.getWorker(), coordChosen));
        }
    }
}
