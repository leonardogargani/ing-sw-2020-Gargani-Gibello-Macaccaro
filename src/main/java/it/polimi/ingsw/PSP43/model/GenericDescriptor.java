package it.polimi.ingsw.PSP43.model;


/**
 * a generic description arriving from the net that is to be sent to the model
 */
public class GenericDescriptor {
    private GameSession gameIdentifier;

    /**
     * @param gameIdentifier the identification of the object GameSession to which the message is to be sent
     */
    public GenericDescriptor(GameSession gameIdentifier) {
        this.gameIdentifier = gameIdentifier;
    }

    /**
     * @return the reference to the GameSession to which the message is sent
     */
    public GameSession getGameIdentifier() {
        return gameIdentifier;
    }
}
