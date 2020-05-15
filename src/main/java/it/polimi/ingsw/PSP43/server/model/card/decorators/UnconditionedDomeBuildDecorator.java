package it.polimi.ingsw.PSP43.server.model.card.decorators;

import it.polimi.ingsw.PSP43.client.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.client.networkMessages.ResponseMessage;
import it.polimi.ingsw.PSP43.server.DataToBuild;
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

public class UnconditionedDomeBuildDecorator extends PowerGodDecorator {
    private static final long serialVersionUID = 8289108530021462717L;

    public UnconditionedDomeBuildDecorator() {
    }

    public UnconditionedDomeBuildDecorator(AbstractGodCard godComponent) {
        super(godComponent);
    }

    public void initBuild(GameSession gameSession) throws GameEndedException, IOException, InterruptedException, ClassNotFoundException {
        Player currentPlayer = gameSession.getCurrentPlayer();

        HashMap<Coord, ArrayList<Coord>> availablePositionsToBuildDome = findAvailablePositionsToBuildDome(gameSession);
        HashMap<Coord, ArrayList<Coord>> availablePositionsToBuildBlock = findAvailablePositionsToBuildBlock(gameSession);

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
            buildDome(new DataToBuild(gameSession, currentPlayer, workerToBuild, actionResponse.getPosition(), Boolean.TRUE));
        }
        else {
            message = "Choose a position where to place a block.";
            ActionResponse actionResponse = askForBuild(gameSession, availablePositionsToBuildBlock, message);
            Worker workerToBuild = gameSession.getWorkersHandler().getWorker(actionResponse.getWorkerPosition());
            buildBlock(new DataToBuild(gameSession, currentPlayer, workerToBuild, actionResponse.getPosition(), Boolean.FALSE));
        }
    }

    public HashMap<Coord, ArrayList<Coord>> findAvailablePositionsToBuildDome(GameSession gameSession) {
        CellsHandler cellsHandler = gameSession.getCellsHandler();

        Integer[] workersIds = gameSession.getCurrentPlayer().getWorkersIdsArray();
        ArrayList<Worker> workers = gameSession.getWorkersHandler().getWorkers(workersIds);

        HashMap<Coord, ArrayList<Coord>> availablePositions = cellsHandler.findWorkersNeighbouringCoords(workers);
        for (Coord c : availablePositions.keySet()) {
            for (Coord c1 : availablePositions.get(c)) {
                Cell cellToBuild = cellsHandler.getCell(c1);
                if (cellToBuild.getOccupiedByDome() || cellToBuild.getOccupiedByWorker()) {
                    availablePositions.get(c).remove(c1);
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
