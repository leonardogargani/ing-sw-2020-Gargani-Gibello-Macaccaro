package it.polimi.ingsw.PSP43.server.model.card.behaviours;

import it.polimi.ingsw.PSP43.client.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.client.networkMessages.ResponseMessage;
import it.polimi.ingsw.PSP43.server.DataToAction;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.networkMessages.ActionRequest;
import it.polimi.ingsw.PSP43.server.networkMessages.RequestMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is made to give run-time the possibility to a card
 * to build another block, but not in the same position.
 */
public class DoubleDifferentSpaceBehaviour extends AbstractGodCard implements BuildBlockBehaviour {
    private static final long serialVersionUID = 5472654096820247816L;

    /**
     * This method checks if the player wants to build another time
     * @param dataToAction The necessary data used to make a build and also to check the cell where it is not possible to build again.
     */
    public void handleBuildBlock(DataToAction dataToAction) throws IOException, ClassNotFoundException, InterruptedException {
        super.buildBlock(dataToAction);
        RequestMessage requestMessage = new RequestMessage("Do you want to build another time on a different space?");
        // TODO :   it should be possible to generify all the cards' interaction method with the client in the superclass AbstractGodCard.
        //          In that way I shouldn't repeat all the same pattern in all the classes of the cards in which I have to interact with the client. :(
        ResponseMessage responseMessage;
        do {
            try {
                responseMessage = dataToAction.getGameSession().sendRequest(  requestMessage,
                                                                        dataToAction.getPlayer().getNickname(),
                                                                        new ResponseMessage());
            } catch (GameEndedException e) {
                dataToAction.getGameSession().setActive();
                return;
            }
        } while (responseMessage == null);

        boolean response = responseMessage.isResponse();
        if (response) {
            buildAnotherTime(dataToAction);
        }
    }

    /**
     * This method is used to give the possibility to the player to build twice, but not on the same space.
     * @param oldDataToAction The data of the previous build, used to check and not to give the possibility to the player
     *                        to build in the same position of the previous one.
     */
    private void buildAnotherTime(DataToAction oldDataToAction) throws IOException, InterruptedException, ClassNotFoundException {
        GameSession game = oldDataToAction.getGameSession();
        ArrayList<Worker> workersToMove = new ArrayList<>();
        workersToMove.add(oldDataToAction.getWorker());
        HashMap<Coord, ArrayList<Coord>> availablePositions = this.findAvailablePositionsToBuildBlock(game.getCellsHandler(), workersToMove);
        Coord oldCoordsBuilt = oldDataToAction.getNewPosition();
        for (Coord c : availablePositions.keySet()) {
            availablePositions.get(c).removeIf(c1 -> c1.getY() == oldCoordsBuilt.getY() && c1.getX() == oldCoordsBuilt.getY());
        }
        ActionRequest request = new ActionRequest("Choose a position where to build a block.", availablePositions);
        ActionResponse actionResponse;
        do {
            try {
                actionResponse = oldDataToAction.getGameSession().sendRequest(  request,
                        oldDataToAction.getPlayer().getNickname(),
                        new ActionResponse());
            } catch (GameEndedException | IOException | ClassNotFoundException | InterruptedException e) {
                game.setActive();
                return;
            }
        } while (actionResponse == null);

        Coord coordChosen = actionResponse.getPosition();
        super.buildBlock(new DataToAction(oldDataToAction.getGameSession(), oldDataToAction.getPlayer(), oldDataToAction.getWorker(), coordChosen));
    }
}