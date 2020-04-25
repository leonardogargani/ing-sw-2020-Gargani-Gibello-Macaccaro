package it.polimi.ingsw.PSP43.server.model.card.behaviours;

import it.polimi.ingsw.PSP43.client.networkMessages.ResponseMessage;
import it.polimi.ingsw.PSP43.server.DataToAction;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;
import it.polimi.ingsw.PSP43.server.networkMessages.RequestMessage;
import it.polimi.ingsw.PSP43.server.networkMessages.TextMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class DoubleMoveBehaviour extends AbstractGodCard implements MoveBehavior {
    public void handleMove(DataToAction dataToAction) throws IOException, ClassNotFoundException, WinnerCaughtException {
        super.move(dataToAction);
        RequestMessage message = new RequestMessage("Do you want to move another time?");
        //ResponseMessage response = dataToAction.getGameSession().sen
        // TODO : ask for the response of the client
        boolean response  = true;
        if (response) {
            Worker [] workersToMove = new Worker[1];
            workersToMove[0] = dataToAction.getWorker();
            HashMap<Coord, ArrayList<Coord>> availablePositions =  super.findAvailablePositionsToMove(dataToAction.getGameSession().getCellsHandler(), workersToMove);
            for (Coord c : availablePositions.keySet()) {
                availablePositions.get(c).removeIf(c1
                        -> c1.getY() == dataToAction.getNewPosition().getY() && c1.getX() == dataToAction.getNewPosition().getX());
            }
            // TODO : receive the coord from client
            Coord coordChosen = null;
            super.move(new DataToAction(dataToAction.getGameSession(), dataToAction.getPlayer(), dataToAction.getWorker(), coordChosen));
        }
    }
}
