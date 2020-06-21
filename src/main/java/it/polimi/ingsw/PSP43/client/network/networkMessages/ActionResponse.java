package it.polimi.ingsw.PSP43.client.network.networkMessages;

import it.polimi.ingsw.PSP43.server.model.Coord;


public class ActionResponse extends ClientMessage {
    private static final long serialVersionUID = -3850574828879543142L;
    private Coord workerPosition;
    private Coord position;


    /**
     * Not default constructor for ActionResponse message.
     * @param workerPosition is the starter position of a chosen worker
     * @param position is the position where the chosen worker will build or move himself
     */
    public ActionResponse(Coord workerPosition, Coord position){
        this.workerPosition = workerPosition;
        this.position = position;
    }


    /**
     * ActionResponse constructor without parameters.
     */
    public ActionResponse() {
    }


    /**
     * Getter method for the starter workerPosition.
     * @return workerPosition
     */
    public Coord getWorkerPosition() {
        return workerPosition;
    }


    /**
     * Getter method for the action position
     * @return position
     */
    public Coord getPosition() {
        return position;
    }

}
