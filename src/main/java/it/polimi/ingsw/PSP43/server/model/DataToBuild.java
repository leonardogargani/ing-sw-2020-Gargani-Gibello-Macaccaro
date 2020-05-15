package it.polimi.ingsw.PSP43.server.model;

import it.polimi.ingsw.PSP43.server.gameStates.GameSession;

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
