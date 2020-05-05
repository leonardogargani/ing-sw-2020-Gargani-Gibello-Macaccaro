package it.polimi.ingsw.PSP43.server.model.card.decorators;

import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlers.CellsHandler;

import java.util.ArrayList;
import java.util.HashMap;

public class BlockRiseDecorator extends PowerGodDecorator {
    public BlockRiseDecorator() {
        super();
    }

    public BlockRiseDecorator(AbstractGodCard godComponent) {
        super(godComponent);
    }

    public HashMap<Coord, ArrayList<Coord>> findAvailablePositionsToMove(CellsHandler handler, ArrayList<Worker> workers) {
        HashMap<Coord, ArrayList<Coord>> positions = super.findAvailablePositionsToMove(handler, workers);
        for (Coord c : positions.keySet()) {
            for (Coord c1 : positions.get(c)) {
                Cell newCell = handler.getCell(c1);
                Cell oldCell = handler.getCell(c);
                if (newCell.getHeight() > oldCell.getHeight()) positions.get(c).remove(c1);
            }
        }
        return super.findAvailablePositionsToMove(handler, workers);
    }

    public AbstractGodCard cleanFromEffects(String nameOfEffect) throws ClassNotFoundException {
        return super.getGodComponent().cleanFromEffects(nameOfEffect);
    }
}
