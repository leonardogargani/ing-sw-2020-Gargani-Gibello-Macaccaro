package it.polimi.ingsw.PSP43.server.model.card.behaviours;

import it.polimi.ingsw.PSP43.client.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.client.networkMessages.ResponseMessage;
import it.polimi.ingsw.PSP43.server.DataToAction;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.networkMessages.ActionRequest;
import it.polimi.ingsw.PSP43.server.networkMessages.RequestMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class DoubleDifferentSpaceBehaviour extends AbstractGodCard implements BuildBlockBehaviour {
    public void handleBuildBlock(DataToAction dataToAction) throws IOException, ClassNotFoundException, InterruptedException {
        super.buildBlock(dataToAction);
        RequestMessage requestMessage = new RequestMessage("Do you want to build another time on a different space?");
        ResponseMessage responseMessage;
        do {
            responseMessage = dataToAction.getGameSession().sendRequest(  requestMessage,
                                                                    dataToAction.getPlayer().getNickname(),
                                                                    ResponseMessage.class);
        } while (responseMessage == null);

        boolean response = responseMessage.isResponse();
        if (response) {
            GameSession game = dataToAction.getGameSession();
            HashMap<Coord, ArrayList<Coord>> availablePositions = game.getCellsHandler().findNeighbouringCoords(new Worker[]{dataToAction.getWorker()});
            Coord oldCoordsBuilt = dataToAction.getNewPosition();
            for (Coord c : availablePositions.keySet()) {
                availablePositions.get(c).removeIf(c1 -> c1.getY() == oldCoordsBuilt.getY() && c1.getX() == oldCoordsBuilt.getY());
            }
            ActionRequest request = new ActionRequest("Choose a position where to build a dome.", availablePositions);
            ActionResponse actionResponse;
            do {
                actionResponse = dataToAction.getGameSession().sendRequest(  request,
                                                                        dataToAction.getPlayer().getNickname(),
                                                                        ActionResponse.class);
            } while (actionResponse == null);

            Coord coordChosen = actionResponse.getPosition();
            super.buildDome(new DataToAction(dataToAction.getGameSession(), dataToAction.getPlayer(), dataToAction.getWorker(), coordChosen));
        }
    }
}
