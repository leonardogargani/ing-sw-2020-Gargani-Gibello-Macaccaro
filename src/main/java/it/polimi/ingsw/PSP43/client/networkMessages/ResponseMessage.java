package it.polimi.ingsw.PSP43.client.networkMessages;


public class ResponseMessage extends ClientMessage {
    private static final long serialVersionUID = 4224430394343432214L;
    private boolean response;


    /**
     * Not default constructor for ResponseMessage.
     * @param response is a boolean variable
     */
    public ResponseMessage(boolean response){
        this.response = response;
    }


    /**
     * Response message constructor without parameters.
     */
    public ResponseMessage() {
    }


    /**
     * Getter method for the boolean variable response.
     * @return response
     */
    public boolean isResponse() {
        return response;
    }

}
