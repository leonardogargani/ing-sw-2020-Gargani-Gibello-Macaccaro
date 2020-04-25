package it.polimi.ingsw.PSP43.server.model.card.behaviours;

import it.polimi.ingsw.PSP43.server.DataToAction;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.networkMessages.TextMessage;

public class DoubleSameSpaceBehaviour extends AbstractGodCard implements BuildBlockBehaviour {
    public void handleBuildBlock(DataToAction dataToAction) {
        super.buildBlock(dataToAction);
        TextMessage text = new TextMessage("Do you want to build another time on the same space?");
        // TODO : send and receive the answer from the client
        boolean response = true;
        if (response) {
            super.buildDome(dataToAction);
        }
    }
}
