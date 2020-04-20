package it.polimi.ingsw.PSP43.client.networkMessages;

public class ResponseMessage extends ClientMessage {
    private static final long SerialVersionUID=123123123123123123L;
    private String message;
    private boolean response;

    public ResponseMessage(String message, boolean response){
        this.message = message;
        this.response = response;
    }

    public String getMessage() {
        return message;
    }

    public boolean isResponse() {
        return response;
    }
}
