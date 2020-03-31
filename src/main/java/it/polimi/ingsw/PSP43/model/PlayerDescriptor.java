package it.polimi.ingsw.PSP43.model;

/**
 * Player description arriving from the net and traslated into a message for a specified GameSession
 */
public class PlayerDescriptor extends GenericDescriptor {
    private Player playerReference;
    private boolean quitPlayerAccess;
    private String nick;
    private Card godCard;

    /**
     *
     * @param gameIdentifier Identification of the object GameSession to which the message is to be sent and handled
     * @param playerReference Reference to the player data to modify
     * @param nick Nickname that identifies a player during the game
     * @param godCard Card chosen by the player during the game
     */
    public PlayerDescriptor(GameSession gameIdentifier, Player playerReference, boolean quit , String nick, Card godCard) {
        super(gameIdentifier);
        this.playerReference = playerReference;
        this.quitPlayerAccess = quit;
        this.nick = nick;
        this.godCard = godCard;
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
    public Card getCard() {
        return godCard;
    }
}
