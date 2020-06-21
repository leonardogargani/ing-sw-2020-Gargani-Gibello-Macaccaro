package it.polimi.ingsw.PSP43.client.network.networkMessages;

import it.polimi.ingsw.PSP43.server.model.Color;


public class WorkersColorResponse extends ClientMessage {
    private static final long serialVersionUID = -7968705884426214270L;
    private Color color;


    /**
     * Not default constructor for WorkersColorResponse message
     * @param color is the chosen color for the sender's workers
     */
    public WorkersColorResponse(Color color){
        this.color = color;
    }


    /**
     * WorkersColorResponse constructor without parameters
     */
    public WorkersColorResponse() {
    }


    /**
     * Getter method for the variable color
     * @return color
     */
    public Color getColor() {
        return color;
    }

}
