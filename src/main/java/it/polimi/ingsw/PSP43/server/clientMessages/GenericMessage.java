package it.polimi.ingsw.PSP43.server.clientMessages;

public abstract class GenericMessage {
    int idGameSession;

    public GenericMessage(int idGameSession) {
        this.idGameSession = idGameSession;
    }

    public int getIdGameSession() {
        return idGameSession;
    }
}