package it.polimi.ingsw.PSP43.server;

import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;

public class DataToBuild extends DataToMove {
    private Boolean buildDome;

    public DataToBuild(GameSession gameSession, Player player, Worker worker, Coord newPosition, Boolean buildDome) {
        super(gameSession, player, worker, newPosition);
        this.buildDome = buildDome;
    }

    public Boolean getBuildDome() {
        return buildDome;
    }
}
