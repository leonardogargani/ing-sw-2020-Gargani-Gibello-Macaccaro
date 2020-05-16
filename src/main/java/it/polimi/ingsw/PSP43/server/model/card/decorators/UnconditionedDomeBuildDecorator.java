package it.polimi.ingsw.PSP43.server.model.card.decorators;

import it.polimi.ingsw.PSP43.client.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.client.networkMessages.ResponseMessage;
import it.polimi.ingsw.PSP43.server.model.DataToBuild;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlers.CellsHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.networkMessages.RequestMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class UnconditionedDomeBuildDecorator extends PowerGodDecorator {
    private static final long serialVersionUID = 8289108530021462717L;

    public UnconditionedDomeBuildDecorator() {
    }

    public UnconditionedDomeBuildDecorator(AbstractGodCard godComponent) {
        super(godComponent);
    }

    public void initBuild(GameSession gameSession) throws GameEndedException, IOException, InterruptedException, ClassNotFoundException {
        Player currentPlayer = gameSession.getCurrentPlayer();

        HashMap<Coord, ArrayList<Coord>> availablePositionsToBuildDome = this.findAvailablePositionsToBuildDome(gameSession);
        HashMap<Coord, ArrayList<Coord>> availablePositionsToBuildBlock = this.findAvailablePositionsToBuildBlock(gameSession);

        ResponseMessage responseMessage = new ResponseMessage(false);
        if (availablePositionsToBuildDome.size() != 0) {
            RequestMessage requestMessage = new RequestMessage("Do you want to build a dome? Otherwise you will build a block.");
            responseMessage = gameSession.sendRequest(requestMessage, currentPlayer.getNickname(), new ResponseMessage());
        }

        String message;
        if (responseMessage.isResponse()) {
            message = "Choose a position where to place a dome.";
            ActionResponse actionResponse = askForBuild(gameSession, availablePositionsToBuildDome, message);
            Worker workerToBuild = gameSession.getWorkersHandler().getWorker(actionResponse.getWorkerPosition());
            build(new DataToBuild(gameSession, currentPlayer, workerToBuild, actionResponse.getPosition(), Boolean.TRUE));
        }
        else {
            message = "Choose a position where to place a block.";
            ActionResponse actionResponse = askForBuild(gameSession, availablePositionsToBuildBlock, message);
            Worker workerToBuild = gameSession.getWorkersHandler().getWorker(actionResponse.getWorkerPosition());
            build(new DataToBuild(gameSession, currentPlayer, workerToBuild, actionResponse.getPosition(), Boolean.FALSE));
        }
    }

    public HashMap<Coord, ArrayList<Coord>> findAvailablePositionsToBuildDome(GameSession gameSession) {
        CellsHandler cellsHandler = gameSession.getCellsHandler();

        HashMap<Coord, ArrayList<Coord>> availablePositions = cellsHandler.findWorkersNeighbouringCoords(gameSession.getCurrentPlayer());
        for (Coord c : availablePositions.keySet()) {
            for (Iterator<Coord> coordIterator = availablePositions.get(c).iterator(); coordIterator.hasNext(); ) {
                Coord actualCoord = coordIterator.next();
                Cell cellToBuild = cellsHandler.getCell(actualCoord);
                if (cellToBuild.getOccupiedByDome() || cellToBuild.getOccupiedByWorker()) {
                    coordIterator.remove();
                }
            }

        }
        return availablePositions;
    }

    public AbstractGodCard cleanFromEffects(String nameOfEffect) throws ClassNotFoundException {
        AbstractGodCard component = super.getGodComponent().cleanFromEffects(nameOfEffect);
        Class<?> c = Class.forName(nameOfEffect);
        if (!c.isInstance(this))
            return new UnconditionedDomeBuildDecorator(component);
        else return component;
    }
}
