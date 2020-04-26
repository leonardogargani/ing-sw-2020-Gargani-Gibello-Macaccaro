package it.polimi.ingsw.PSP43.client.networkMessages;

public class ResponseMessage extends ClientMessage {
    private static final long SerialVersionUID=123123123123123123L;
    private boolean response;

    public ResponseMessage(boolean response){
        this.response = response;
    }

    public boolean isResponse() {
        return response;
    }
}
