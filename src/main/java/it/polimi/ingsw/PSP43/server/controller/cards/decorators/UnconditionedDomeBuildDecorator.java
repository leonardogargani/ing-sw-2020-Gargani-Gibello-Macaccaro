package it.polimi.ingsw.PSP43.server.controller.cards.decorators;

import it.polimi.ingsw.PSP43.server.controller.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.controller.cards.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.CellsHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class UnconditionedDomeBuildDecorator extends PowerGodDecorator {
    private static final long serialVersionUID = 8289108530021462717L;

    public UnconditionedDomeBuildDecorator(AbstractGodCard godComponent) {
        super(godComponent);
    }

    // TODO it could be not necessary if I always call the findpositions on the most external abstractgodcard
    /*public void initBuild(GameSession gameSession) throws GameEndedException {
        Player currentPlayer = gameSession.getCurrentPlayer();

        HashMap<Coord, ArrayList<Coord>> availablePositionsToBuildDome = this.findAvailablePositionsToBuildDome(gameSession);
        HashMap<Coord, ArrayList<Coord>> availablePositionsToBuildBlock = this.findAvailablePositionsToBuildBlock(gameSession);

        ResponseMessage responseMessage = new ResponseMessage(false);
        if (availablePositionsToBuildDome.size() != 0) {
            RequestMessage requestMessage = new RequestMessage("Do you want to build a dome? Otherwise you will build a block.");
            responseMessage = gameSession.sendRequest(requestMessage, currentPlayer.getNickname(), ResponseMessage.class);
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
    }*/

    public HashMap<Coord, ArrayList<Coord>> findAvailablePositionsToBuildDome(GameSession gameSession) {
        CellsHandler cellsHandler = gameSession.getCellsHandler();

        HashMap<Coord, ArrayList<Coord>> availablePositions = cellsHandler.findWorkersNeighbouringCoordsExclude(gameSession.getCurrentPlayer());
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

    public AbstractGodCard cleanFromEffects(String nameOfEffect) {
        AbstractGodCard component = super.getGodComponent().cleanFromEffects(nameOfEffect);
        Class<?> c = null;
        try {
            c = Class.forName(nameOfEffect);
        } catch (ClassNotFoundException e) { e.printStackTrace(); }

        assert c != null;
        if (!c.isInstance(this))
            return new UnconditionedDomeBuildDecorator(component);
        else return component;
    }
}
