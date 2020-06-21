package it.polimi.ingsw.PSP43.client.network.networkMessages;


public class LeaveGameMessage extends ClientMessage {
    private static final long serialVersionUID = -3467670547741290155L;

    // IRREVERSIBLE_DISCONNECTION = the client has "killed" the program, so he won't have any further interaction
    // with the game server. The only way to do that it's to re-run the client application;
    // GAME_DISCONNECTION = the client has tapped on the "home button" (feature only available in GUI). Here all the sockets
    // are left opened to give him the possibility to join another game.
    public enum TypeDisconnectionHeader {
        IRREVERSIBLE_DISCONNECTION,
        GAME_DISCONNECTION
    }

    private TypeDisconnectionHeader typeDisconnectionHeader;

    public LeaveGameMessage() {
    }

    /**
     * LeaveGameMessage constructor.
     */
    public LeaveGameMessage(TypeDisconnectionHeader typeDisconnectionHeader) {
        this.typeDisconnectionHeader = typeDisconnectionHeader;
    }

    public TypeDisconnectionHeader getTypeDisconnectionHeader() {
        return typeDisconnectionHeader;
    }

    public void setTypeDisconnectionHeader(TypeDisconnectionHeader typeDisconnectionHeader) {
        this.typeDisconnectionHeader = typeDisconnectionHeader;
    }
}
