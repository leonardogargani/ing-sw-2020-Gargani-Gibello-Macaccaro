package it.polimi.ingsw.PSP43.server.networkMessages;

public class TextMessage extends ResMessage {
    private static final long SerialVersionUID=111234567890123456L;
    private String message;

    public TextMessage(int idGame, String message){
        super(idGame);
        this.message = message;
    }

    public static long getSerialVersionUID() {
        return SerialVersionUID;
    }

    public String getMessage() {
        return message;
    }
}
