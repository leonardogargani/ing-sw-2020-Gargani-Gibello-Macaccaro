package it.polimi.ingsw.PSP43.server.controllers.decorators;

import it.polimi.ingsw.PSP43.client.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.server.model.DataToMove;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.controllers.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameLostException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This decoration is made to add dinamically features to the cards, in order to give powers to the players' Gods and
 * to limit their possibilities because of the opponents' Gods
 */
public abstract class PowerGodDecorator extends AbstractGodCard {
    private static final long serialVersionUID = -1361536654497716050L;
    private final AbstractGodCard godComponent;

    public PowerGodDecorator(AbstractGodCard godComponent) {
        this.godComponent = godComponent;
    }

    public AbstractGodCard getGodComponent() {
        return godComponent;
    }

    public void move(DataToMove dataToMove) {
        godComponent.move(dataToMove);
    }

    public HashMap<Coord, ArrayList<Coord>> findAvailablePositionsToMove(GameSession gameSession) {
        return godComponent.findAvailablePositionsToMove(gameSession);
    }

    public HashMap<Coord, ArrayList<Coord>> findAvailablePositionsToBuildBlock(GameSession gameSession) {
        return godComponent.findAvailablePositionsToBuildBlock(gameSession);
    }

    public String getGodName() {
        return godComponent.getGodName();
    }

    public String getDescription() {
        return godComponent.getDescription();
    }

    public String getPower() {
        return godComponent.getPower();
    }

    public void initMove(GameSession gameSession) throws GameEndedException, GameLostException {
        godComponent.initMove(gameSession);
    }

    public void initBuild(GameSession gameSession) throws GameEndedException {
        godComponent.initBuild(gameSession);
    }

    public ActionResponse askForMove(GameSession gameSession, HashMap<Coord, ArrayList<Coord>> availablePositions) throws GameEndedException {
        return godComponent.askForMove(gameSession, availablePositions);
    }

    public boolean checkConditionsToWin(GameSession gameSession) {
        return godComponent.checkConditionsToWin(gameSession);
    }

    public void print() {
        godComponent.print();
    }
}
