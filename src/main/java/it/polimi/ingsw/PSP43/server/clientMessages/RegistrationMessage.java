package it.polimi.ingsw.PSP43.server.clientMessages;

public class RegistrationMessage extends GenericMessage {
    String nickPlayerId;
    boolean unregistration;

    public RegistrationMessage(int idGameSession, String nickPlayerId, boolean value) {
        super(idGameSession);
        this.nickPlayerId = nickPlayerId;
        this.unregistration = value;
    }

    public String getNickPlayerId() {
        return nickPlayerId;
    }

    public boolean isUnregistration() {
        return unregistration;
    }
}