package it.polimi.ingsw.PSP43.client.networkMessages;

import it.polimi.ingsw.PSP43.Color;


public class WorkersColorResponse extends ClientMessage {

    private static final long SerialVersionUID=432109876543210987L;
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
