package it.polimi.ingsw.PSP43.server.model.card.decorators;

import it.polimi.ingsw.PSP43.server.DataToAction;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlers.CellsHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@XmlAccessorType(XmlAccessType.FIELD)
/**
 * This decoration is made to add dinamically features to the cards, in order to give powers to the players' Gods and
 * to limit their possibilities because of the opponents' Gods
 */
public abstract class PowerGodDecorator extends AbstractGodCard {
    private AbstractGodCard godComponent;

    public PowerGodDecorator() {}

    public PowerGodDecorator(AbstractGodCard godComponent) {
        this.godComponent = godComponent;
    }

    public AbstractGodCard getGodComponent() {
        return godComponent;
    }

    public void setGodComponent(AbstractGodCard godComponent) {
        this.godComponent = godComponent;
    }

    public void move(DataToAction dataToAction) throws IOException, ClassNotFoundException, WinnerCaughtException {
        godComponent.move(dataToAction);
    }

    public void buildBlock(DataToAction dataToAction) {
        godComponent.buildBlock(dataToAction);
    }

    public void buildDome(DataToAction dataToAction) {
        godComponent.buildDome(dataToAction);
    }

    public HashMap<Coord, ArrayList<Coord>> findAvailablePositionsToMove(CellsHandler handler, Worker[] workers) {
        return godComponent.findAvailablePositionsToMove(handler, workers);
    }

    public HashMap<Coord, ArrayList<Coord>> findAvailablePositionsToBuildBlock(CellsHandler handler, Worker[] workers) {
        return godComponent.findAvailablePositionsToBuildBlock(handler, workers);
    }

    public HashMap<Coord, ArrayList<Coord>> findAvailablePositionsToBuildDome(CellsHandler handler, Worker[] workers) {
        return godComponent.findAvailablePositionsToBuildDome(handler, workers);
    }
}
