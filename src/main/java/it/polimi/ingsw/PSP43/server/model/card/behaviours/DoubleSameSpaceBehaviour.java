package it.polimi.ingsw.PSP43.server.model.card.behaviours;

import it.polimi.ingsw.PSP43.client.networkMessages.ResponseMessage;
import it.polimi.ingsw.PSP43.server.DataToAction;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.networkMessages.RequestMessage;

import java.io.IOException;

public class DoubleSameSpaceBehaviour extends AbstractGodCard implements BuildBlockBehaviour {
    public void handleBuildBlock(DataToAction dataToAction) throws IOException, ClassNotFoundException {
        super.buildBlock(dataToAction);
        RequestMessage requestMessage = new RequestMessage("Do you want to build another time on the same space?");
        ResponseMessage responseMessage = null;
        boolean delivered;
        do {
            delivered = dataToAction.getGameSession().sendRequest(  requestMessage,
                                                                    dataToAction.getPlayer().getNickname(),
                                                                    responseMessage);
        } while (!delivered);

        boolean response = responseMessage.isResponse();
        if (response) {
            super.buildDome(dataToAction);
        }
    }
}
