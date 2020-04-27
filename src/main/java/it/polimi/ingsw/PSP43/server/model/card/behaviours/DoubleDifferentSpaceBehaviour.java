package it.polimi.ingsw.PSP43.server.model.card.behaviours;

import it.polimi.ingsw.PSP43.client.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.client.networkMessages.ResponseMessage;
import it.polimi.ingsw.PSP43.server.DataToAction;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlers.CellsHandler;
import it.polimi.ingsw.PSP43.server.networkMessages.ActionRequest;
import it.polimi.ingsw.PSP43.server.networkMessages.RequestMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class DoubleDifferentSpaceBehaviour extends AbstractGodCard implements BuildBlockBehaviour {
    public void handleBuildBlock(DataToAction dataToAction) throws IOException, ClassNotFoundException {
        super.buildBlock(dataToAction);
        RequestMessage requestMessage = new RequestMessage("Do you want to build another time on a different space?");
        ResponseMessage responseMessage = null;
        boolean delivered;
        do {
            delivered = dataToAction.getGameSession().sendRequest(  requestMessage,
                                                                    dataToAction.getPlayer().getNickname(),
                                                                    responseMessage);
        } while (!delivered);

        boolean response = responseMessage.isResponse();
        if (response) {
            GameSession game = dataToAction.getGameSession();
            CellsHandler handler = game.getCellsHandler();
            HashMap<Coord, ArrayList<Coord>> availablePositions = game.getCellsHandler().findNeighbouringCoords(new Worker[]{dataToAction.getWorker()});
            Coord oldCoordsBuilt = dataToAction.getNewPosition();
            for (Coord c : availablePositions.keySet()) {
                availablePositions.get(c).removeIf(c1 -> c1.getY() == oldCoordsBuilt.getY() && c1.getX() == oldCoordsBuilt.getY());
            }
            ActionRequest request = new ActionRequest("Choose a position where to build a dome.", availablePositions);
            ActionResponse actionResponse = null;
            do {
                delivered = dataToAction.getGameSession().sendRequest(  requestMessage,
                                                                        dataToAction.getPlayer().getNickname(),
                                                                        actionResponse);
            } while (!delivered);

            Coord coordChosen = actionResponse.getPosition();
            super.buildDome(new DataToAction(dataToAction.getGameSession(), dataToAction.getPlayer(), dataToAction.getWorker(), coordChosen));
        }
    }
}
