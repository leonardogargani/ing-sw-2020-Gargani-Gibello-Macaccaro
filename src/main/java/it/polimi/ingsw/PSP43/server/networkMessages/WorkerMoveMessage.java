package it.polimi.ingsw.PSP43.server.networkMessages;

import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Worker;

public class WorkerMoveMessage extends ResMessage {
    private static final long SerialVersionUID=111111123456789012L;
    private Worker worker;
    private Coord previousPosition;
    private Coord newPosition;

    public WorkerMoveMessage(int idGame, Worker worker, Coord previousPosition, Coord newPosition){
        super(idGame);
        this.worker = worker;
        this.previousPosition = previousPosition;
        this.newPosition = newPosition;
    }

    public Worker getWorker() {
        return worker;
    }

    public Coord getPreviousPosition() {
        return previousPosition;
    }

    public Coord getNewPosition() {
        return newPosition;
    }
}
