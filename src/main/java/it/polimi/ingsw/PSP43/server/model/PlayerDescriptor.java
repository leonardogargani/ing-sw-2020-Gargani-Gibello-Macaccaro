package it.polimi.ingsw.PSP43.server.model;

import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;

/**
 * Player description arriving from the net and traslated into a message for a specified GameSession
 */
public class PlayerDescriptor extends GenericDescriptor {
    private Player playerReference;
    private boolean quitPlayerAccess;
    private String nick;
    private AbstractGodCard godAbstractGodCard;

    /**
     *
     * @param gameIdentifier Identification of the object GameSession to which the message is to be sent and handled
     * @param playerReference Reference to the player data to modify
     * @param nick Nickname that identifies a player during the game
     * @param godAbstractGodCard AbstractGodCard chosen by the player during the game
     */
    public PlayerDescriptor(GameSession gameIdentifier, Player playerReference, boolean quit , String nick, AbstractGodCard godAbstractGodCard) {
        super(gameIdentifier);
        this.playerReference = playerReference;
        this.quitPlayerAccess = quit;
        this.nick = nick;
        this.godAbstractGodCard = godAbstractGodCard;
    }

    /**
     *
     * @return The reference to the data of a player of the game
     */
    public Player getPlayerReference() {
        return playerReference;
    }

    /**
     * @return True if the player who sent the message wants or is forced to quit the game, False otherway
     */
    public boolean isQuitting() {
        return quitPlayerAccess;
    }

    /**
     *
     * @return The nickname that identifies a player during the game
     */
    public String getNick() {
        return nick;
    }

    /**
     *
     * @return The card that is used by a player during the game
     */
    public AbstractGodCard getCard() {
        return godAbstractGodCard;
    }
}
