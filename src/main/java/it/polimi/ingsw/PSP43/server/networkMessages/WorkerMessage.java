package it.polimi.ingsw.PSP43.server.networkMessages;

import it.polimi.ingsw.PSP43.server.model.Worker;

public class WorkerMessage extends ServerMessage {
    private static final long SerialVersionUID=8989676745452323010L;
    private Worker worker;

    public WorkerMessage(Worker worker){
        this.worker = worker;
    }

    public Worker getWorker() {
        return worker;
    }
}
