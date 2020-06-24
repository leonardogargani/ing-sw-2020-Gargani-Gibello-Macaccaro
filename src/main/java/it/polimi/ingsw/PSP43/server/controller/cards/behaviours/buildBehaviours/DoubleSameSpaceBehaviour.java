package it.polimi.ingsw.PSP43.server.controller.cards.behaviours.buildBehaviours;

import it.polimi.ingsw.PSP43.client.network.networkMessages.ResponseMessage;
import it.polimi.ingsw.PSP43.server.model.DataToBuild;
import it.polimi.ingsw.PSP43.server.controller.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.network.networkMessages.RequestMessage;

public class DoubleSameSpaceBehaviour extends BasicBuildBehaviour {
    private static final long serialVersionUID = -834139120755922247L;

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
