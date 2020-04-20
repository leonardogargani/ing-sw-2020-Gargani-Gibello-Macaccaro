package it.polimi.ingsw.PSP43.server.networkMessages;

public class TextMessage extends ServerMessage {
    private static final long SerialVersionUID=111234567890123456L;
    private String message;

    public TextMessage( String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
