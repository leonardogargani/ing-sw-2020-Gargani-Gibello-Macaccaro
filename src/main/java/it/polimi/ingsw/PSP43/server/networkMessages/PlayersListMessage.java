package it.polimi.ingsw.PSP43.server.networkMessages;

import it.polimi.ingsw.PSP43.Color;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;

import java.util.HashMap;

public class PlayersListMessage extends TextMessage {
    private static final long SerialVersionUID=776776776776776776L;
    private HashMap<Player, AbstractGodCard> players;
    private HashMap<Player, Color> color;

    /**
     * Not default constructor for PlayersListMessage
     * @param message is the string that will be shown to the recipient
     * @param players is an HashMap with players and theirs associated god card
     * @param color is an HashMap with players and their associated color chosen
     */
    public PlayersListMessage(String message, HashMap<Player, AbstractGodCard> players, HashMap<Player, Color> color){
        super(message);
        this.players = players;
        this.color = color;
    }

    /**
     * Getter method for the string message
     * @return message
     */
    @Override
    public String getMessage() {
        return super.getMessage();
    }

    /**
     * Getter method for the HashMap players
     * @return players
     */
    public HashMap<Player, AbstractGodCard> getPlayers() {
        return players;
    }

    /**
     * Getter method for the HashMap color
     * @return color
     */
    public HashMap<Player, Color> getColor() {
        return color;
    }
}
