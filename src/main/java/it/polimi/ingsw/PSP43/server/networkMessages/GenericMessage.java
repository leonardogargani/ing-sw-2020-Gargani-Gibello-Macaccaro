package it.polimi.ingsw.PSP43.server.networkMessages;

public abstract class GenericMessage {
    int idGameSession;

    public GenericMessage(int idGameSession) {
        this.idGameSession = idGameSession;
    }

    public int getIdGameSession() {
        return idGameSession;
    }
}