package it.polimi.ingsw.PSP43.server;

import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;

public class DataToMove {
    private final GameSession gameSession;
    private final Player player;
    private final Worker worker;
    private final Coord newPosition;

    public DataToMove(GameSession gameSession, Player player, Worker worker, Coord newPosition) {
        this.gameSession = gameSession;
        this.player = player;
        this.worker = worker;
        this.newPosition = newPosition;
    }

    public GameSession getGameSession() {
        return gameSession;
    }

    public Player getPlayer() {
        return player;
    }

    public Worker getWorker() {
        return worker;
    }

    public Coord getNewPosition() {
        return newPosition;
    }
}
