package it.polimi.ingsw.PSP43.server.model.card.behaviours.buildBehaviours;

import it.polimi.ingsw.PSP43.client.networkMessages.ResponseMessage;
import it.polimi.ingsw.PSP43.server.DataToBuild;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.card.BasicGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.networkMessages.RequestMessage;

import java.io.IOException;

public class DoubleSameSpaceBehaviour extends BasicBuildBehaviour {
    private static final long serialVersionUID = -834139120755922247L;

    public void handleInitBuild(GameSession gameSession) throws IOException, ClassNotFoundException, InterruptedException, GameEndedException {
        Player currentPlayer = gameSession.getCurrentPlayer();
        DataToBuild dataToBuild = genericAskForBuild(gameSession);

        if (dataToBuild.getBuildDome()) buildDome(dataToBuild);
        else {
            buildBlock(dataToBuild);

            Cell cellBuilt = gameSession.getCellsHandler().getCell(dataToBuild.getNewPosition());
            if (cellBuilt.getHeight() < 3) {
                RequestMessage requestMessage = new RequestMessage("Do you want to build another block on the same space?");
                ResponseMessage responseMessage = gameSession.sendRequest(requestMessage, currentPlayer.getNickname(), new ResponseMessage());

                if (responseMessage.isResponse()) {
                    buildBlock(dataToBuild);
                }
            }
        }
    }
}
