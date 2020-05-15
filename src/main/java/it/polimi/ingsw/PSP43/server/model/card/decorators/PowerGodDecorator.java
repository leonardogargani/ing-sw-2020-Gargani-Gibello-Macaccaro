package it.polimi.ingsw.PSP43.server.model.card.decorators;

import it.polimi.ingsw.PSP43.client.networkMessages.ClientMessage;
import it.polimi.ingsw.PSP43.server.DataToBuild;
import it.polimi.ingsw.PSP43.server.DataToMove;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This decoration is made to add dinamically features to the cards, in order to give powers to the players' Gods and
 * to limit their possibilities because of the opponents' Gods
 */
public abstract class PowerGodDecorator extends AbstractGodCard {
    private static final long serialVersionUID = -1361536654497716050L;
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

    public void move(DataToMove dataToMove) throws IOException, ClassNotFoundException, WinnerCaughtException, InterruptedException {
        godComponent.move(dataToMove);
    }

    public void buildBlock(DataToBuild dataToBuild) throws IOException, ClassNotFoundException, InterruptedException {
        godComponent.buildBlock(dataToBuild);
    }

    public void buildDome(DataToBuild dataToBuild) throws IOException {
        godComponent.buildDome(dataToBuild);
    }

    public HashMap<Coord, ArrayList<Coord>> findAvailablePositionsToMove(GameSession gameSession) {
        return godComponent.findAvailablePositionsToMove(gameSession);
    }

    public HashMap<Coord, ArrayList<Coord>> findAvailablePositionsToBuildBlock(GameSession gameSession) {
        return godComponent.findAvailablePositionsToBuildBlock(gameSession);
    }

    public HashMap<Coord, ArrayList<Coord>> findAvailablePositionsToBuildDome(GameSession gameSession) {
        return godComponent.findAvailablePositionsToBuildDome(gameSession);
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

    public void initMove(GameSession gameSession) throws ClassNotFoundException, WinnerCaughtException, InterruptedException, IOException, GameEndedException {
        godComponent.initMove(gameSession);
    }

    public void initBuild(GameSession gameSession) throws GameEndedException, IOException, InterruptedException, ClassNotFoundException {
        godComponent.initBuild(gameSession);
    }

    public <T extends ClientMessage> T askForMove(GameSession gameSession) throws GameEndedException {
        return godComponent.askForMove(gameSession);
    }

    public <T extends ClientMessage> T askForMove(GameSession gameSession, HashMap<Coord, ArrayList<Coord>> availablePositions) throws GameEndedException {
        return godComponent.askForMove(gameSession, availablePositions);
    }

    public DataToBuild genericAskForBuild(GameSession gameSession) throws GameEndedException, InterruptedException, IOException, ClassNotFoundException {
        return godComponent.genericAskForBuild(gameSession);
    }

    public <T extends ClientMessage> T askForBuild(GameSession gameSession, HashMap<Coord, ArrayList<Coord>> availablePositionsBuildBlock, String message) throws GameEndedException, InterruptedException, IOException, ClassNotFoundException {
        return godComponent.askForBuild(gameSession, availablePositionsBuildBlock, message);
    }

    public void print() {
        godComponent.print();
    }
}
