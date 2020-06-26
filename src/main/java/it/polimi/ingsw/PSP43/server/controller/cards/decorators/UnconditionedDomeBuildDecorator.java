package it.polimi.ingsw.PSP43.server.controller.cards.decorators;

import it.polimi.ingsw.PSP43.server.controller.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.controller.cards.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.CellsHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * This class is used to implement Atlas behaviour.
 */
public class UnconditionedDomeBuildDecorator extends PowerGodDecorator {
    private static final long serialVersionUID = 8289108530021462717L;

    public UnconditionedDomeBuildDecorator(AbstractGodCard godComponent) {
        super(godComponent);
    }

    /**
     * This method is used to find all the available positions where to build a dome, that is, according to Atlas behaviour, all the neighbouring positions of the workers of the player.
     * @param gameSession This is a reference to the main access to the game database.
     * @return A map of all the coordinates where the workers can build a dome.
     */
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

    /**
     * This method is used to clean the card from possible decorators which could block some functionalities.
     * It is called when the blocker begins a new turn.
     * @param nameOfEffect The effect that the blocker has activated by doing a determined action.
     * @return The card cleaned by the blocking decorator passed as parameter.
     */
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
