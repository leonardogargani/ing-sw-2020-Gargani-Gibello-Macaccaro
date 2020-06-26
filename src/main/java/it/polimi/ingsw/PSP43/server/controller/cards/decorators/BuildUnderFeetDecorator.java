package it.polimi.ingsw.PSP43.server.controller.cards.decorators;

import it.polimi.ingsw.PSP43.server.controller.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.controller.cards.AbstractGodCard;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class represent a decorator that gives to a card Zeus's behaviour.
 */
public class BuildUnderFeetDecorator extends PowerGodDecorator {
    private static final long serialVersionUID = 3848762574012185257L;

    public BuildUnderFeetDecorator(AbstractGodCard godComponent) {
        super(godComponent);
    }

    /**
     * This method is used to find all the available positions where a player can build a block, also on the current position of a worker owned.
     * @param gameSession This is a reference to the main access to the game database.
     * @return The map of the available positions where a player can build a block.
     */
    public HashMap<Coord, ArrayList<Coord>> findAvailablePositionsToBuildBlock(GameSession gameSession) {
        HashMap<Coord, ArrayList<Coord>> availablePositionsToBuild = super.findAvailablePositionsToBuildBlock(gameSession);

        for (Coord c : availablePositionsToBuild.keySet()) {
            ArrayList<Coord> actualPositions = availablePositionsToBuild.get(c);
            actualPositions.add(c.clone());
        }

        return availablePositionsToBuild;
    }
}
