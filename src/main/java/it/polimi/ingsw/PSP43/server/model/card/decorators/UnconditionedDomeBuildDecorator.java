package it.polimi.ingsw.PSP43.server.model.card.decorators;

import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlers.CellsHandler;

import java.util.ArrayList;
import java.util.HashMap;

public class UnconditionedDomeBuildDecorator extends PowerGodDecorator {
    private static final long serialVersionUID = 8289108530021462717L;

    public UnconditionedDomeBuildDecorator() {
    }

    public UnconditionedDomeBuildDecorator(AbstractGodCard godComponent) {
        super(godComponent);
    }

    @Override
    public HashMap<Coord, ArrayList<Coord>> findAvailablePositionsToBuildDome(CellsHandler handler, ArrayList<Worker> workers) {
        HashMap<Coord, ArrayList<Coord>> availablePositions = handler.findNeighbouringCoords(workers);
        for (Coord c : availablePositions.keySet()) {
            for (Coord c1 : availablePositions.get(c)) {
                Cell cellToBuild = handler.getCell(c1);
                if (cellToBuild.getOccupiedByDome() || cellToBuild.getOccupiedByWorker()) {
                    availablePositions.get(c).remove(c1);
                }
            }
        }
        return availablePositions;
    }

    public AbstractGodCard cleanFromEffects(String nameOfEffect) throws ClassNotFoundException {
        AbstractGodCard component = super.getGodComponent().cleanFromEffects(nameOfEffect);
        Class<?> c = Class.forName(nameOfEffect);
        if (!c.isInstance(this))
            return new UnconditionedDomeBuildDecorator(component);
        else return component;
    }
}
