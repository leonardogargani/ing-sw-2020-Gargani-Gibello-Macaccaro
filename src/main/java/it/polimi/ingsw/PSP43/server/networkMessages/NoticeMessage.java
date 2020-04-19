package it.polimi.ingsw.PSP43.server.networkMessages;

public class NoticeMessage extends GenericMessage {
    private String message;

    public NoticeMessage(int idGameSession, String message) {
        super(idGameSession);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}