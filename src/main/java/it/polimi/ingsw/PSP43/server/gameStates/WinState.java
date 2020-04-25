package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.server.ClientListener;
import it.polimi.ingsw.PSP43.server.networkMessages.EndGameMessage;

import java.io.IOException;
import java.util.HashMap;

public class WinState extends TurnState {
    String winner;

    public WinState(GameSession gameSession) {
        super(gameSession);
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public void executeState() throws IOException {
        EndGameMessage message = new EndGameMessage(winner + " win the play!");
        HashMap<String, ClientListener> map = getGameSession().getListenersHashMap();
        for (String s : map.keySet()) {
            map.get(s).sendMessage(message);
        }
        //map.get(winner).removeGameSession(getGameSession());
    }
}
