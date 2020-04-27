package it.polimi.ingsw.PSP43.server.model.card.behaviours;

import it.polimi.ingsw.PSP43.server.ClientListener;
import it.polimi.ingsw.PSP43.server.DataToAction;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;
import it.polimi.ingsw.PSP43.server.networkMessages.RequestMessage;
import it.polimi.ingsw.PSP43.server.networkMessages.TextMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class BuildBeforeMoveBehaviour extends AbstractGodCard implements MoveBehavior {
    public void handleMove(DataToAction dataToAction) throws IOException, ClassNotFoundException, WinnerCaughtException {
        GameSession gameSession = dataToAction.getGameSession();
        Player player = dataToAction.getPlayer();
        Worker worker = dataToAction.getWorker();
        Cell oldCell = gameSession.getCellsHandler().getCell(worker.getCurrentPosition());
        Cell newCell = gameSession.getCellsHandler().getCell(dataToAction.getNewPosition());
        if (newCell.getHeight() - oldCell.getHeight() == 0) {
            RequestMessage message = new RequestMessage("Do you want to build before moving?");
            ClientListener receiver = gameSession.getListenersHashMap().get(player.getNickname());
            // TODO : how do I send the message????
            boolean response = true; // set only not to have an error because I cannot receive for now from the net;
            if (response) {
                Worker[] workers = new Worker[1];
                workers[0] = worker;
                HashMap<Coord, ArrayList<Coord>> availablePositions = gameSession.getCellsHandler().findNeighbouringCoords(workers);
                for (Coord c : availablePositions.keySet()) {
                    availablePositions.get(c).removeIf(c1 -> c1.getY() == dataToAction.getNewPosition().getY() && c1.getX() == dataToAction.getNewPosition().getX());
                }
                // TODO : send the available positions to build and save it into coordToBuild
                Coord coordToBuild = null;
                DataToAction dataBuild = new DataToAction(gameSession, player, worker, coordToBuild);
            }
        }
        else super.move(dataToAction);
    }
}
