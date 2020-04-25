package it.polimi.ingsw.PSP43.server.networkMessages;

import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;

import java.util.HashMap;

public class PlayerList extends TextMessage {
    private static final long SerialVersionUID=776776776776776776L;
    private HashMap<Player, AbstractGodCard> players;

    public PlayerList(String message){super(message);}

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public HashMap<Player, AbstractGodCard> getPlayers() {
        return players;
    }
}
