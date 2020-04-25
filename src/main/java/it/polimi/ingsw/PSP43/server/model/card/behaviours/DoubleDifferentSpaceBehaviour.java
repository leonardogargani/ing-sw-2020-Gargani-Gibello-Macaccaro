package it.polimi.ingsw.PSP43.server.model.card.behaviours;

import it.polimi.ingsw.PSP43.server.DataToAction;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlers.CellsHandler;
import it.polimi.ingsw.PSP43.server.networkMessages.TextMessage;

import java.util.ArrayList;
import java.util.HashMap;

public class DoubleDifferentSpaceBehaviour extends AbstractGodCard implements BuildBlockBehaviour {
    public void handleBuildBlock(DataToAction dataToAction) {
        super.buildBlock(dataToAction);
        TextMessage text = new TextMessage("Do you want to build another time on a different space?");
        // TODO : send and receive the answer from the client
        boolean response = true;
        if (response) {
            GameSession game = dataToAction.getGameSession();
            CellsHandler handler = game.getCellsHandler();
            HashMap<Coord, ArrayList<Coord>> availablePositions = game.getCellsHandler().findNeighbouringCoords(new Worker[]{dataToAction.getWorker()});
            Coord oldCoordsBuilt = dataToAction.getNewPosition();
            for (Coord c : availablePositions.keySet()) {
                availablePositions.get(c).removeIf(c1 -> c1.getY() == oldCoordsBuilt.getY() && c1.getX() == oldCoordsBuilt.getY());
            }
            // TODO : send the position to the client to make him choose a position
            Coord coordChosen = null;
            super.buildDome(new DataToAction(dataToAction.getGameSession(), dataToAction.getPlayer(), dataToAction.getWorker(), coordChosen));
        }
    }
}
