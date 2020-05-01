package it.polimi.ingsw.PSP43.server.model.card.behaviours;

import it.polimi.ingsw.PSP43.server.DataToAction;

import java.io.IOException;

public interface BuildBlockBehaviour {
    void handleBuildBlock(DataToAction dataToAction) throws IOException, ClassNotFoundException, InterruptedException;
}
