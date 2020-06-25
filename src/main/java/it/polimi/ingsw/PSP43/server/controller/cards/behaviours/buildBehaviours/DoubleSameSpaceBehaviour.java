package it.polimi.ingsw.PSP43.server.controller.cards.behaviours.buildBehaviours;

import it.polimi.ingsw.PSP43.client.network.networkMessages.ResponseMessage;
import it.polimi.ingsw.PSP43.server.model.DataToBuild;
import it.polimi.ingsw.PSP43.server.controller.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.network.networkMessages.RequestMessage;

/**
 * This class implements Hephaestus's behaviour, that is building on the same space of the first build.
 * It is only allowed if it not implies the build of a dome.
 */
public class DoubleSameSpaceBehaviour extends BasicBuildBehaviour {
    private static final long serialVersionUID = -834139120755922247L;

    /**
     * This method handles the build action according to the powers of Hephestus. So it allows the player
     * to build a second time on the same place of the previous one.
     * @param gameSession This is a reference to the main access to the game database.
     * @throws GameEndedException if the player decides to leave the game during his turn.
     */
    public void handleInitBuild(GameSession gameSession) throws GameEndedException {
        Player currentPlayer = gameSession.getCurrentPlayer();
        DataToBuild dataToBuild = genericAskForBuild(gameSession);
        if (dataToBuild == null) return;

        if (dataToBuild.getBuildDome()) build(dataToBuild);
        else {
            build(dataToBuild);

            Cell cellBuilt = gameSession.getCellsHandler().getCell(dataToBuild.getNewPosition());
            if (cellBuilt.getHeight() < 3) {
                RequestMessage requestMessage = new RequestMessage("Do you want to build another block on the same space?");
                ResponseMessage responseMessage = gameSession.sendRequest(requestMessage, currentPlayer.getNickname(), ResponseMessage.class);

                if (responseMessage.isResponse()) {
                    build(dataToBuild);
                }
            }
        }
    }
}
