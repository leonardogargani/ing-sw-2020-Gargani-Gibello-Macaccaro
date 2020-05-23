package it.polimi.ingsw.PSP43.server.model.card.decorators;

import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;

import java.util.ArrayList;
import java.util.HashMap;

public class BuildUnderFeetDecorator extends PowerGodDecorator {
    private static final long serialVersionUID = 3848762574012185257L;

    public BuildUnderFeetDecorator(AbstractGodCard godComponent) {
        super(godComponent);
    }

    public HashMap<Coord, ArrayList<Coord>> findAvailablePositionsToBuildBlock(GameSession gameSession) {
        HashMap<Coord, ArrayList<Coord>> availablePositionsToBuild = super.findAvailablePositionsToBuildBlock(gameSession);

        for (Coord c : availablePositionsToBuild.keySet()) {
            ArrayList<Coord> actualPositions = availablePositionsToBuild.get(c);
            actualPositions.add(c.clone());
        }

        return availablePositionsToBuild;
    }
}
