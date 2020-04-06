package it.polimi.ingsw.PSP43.modelHandlers;

import it.polimi.ingsw.PSP43.model.Cell;
import it.polimi.ingsw.PSP43.model.Coord;
import it.polimi.ingsw.PSP43.model.GameSession;
import it.polimi.ingsw.PSP43.model.Worker;
import java.util.ArrayList;


/**
 *WorkersHandler has the tasks of keeping track of all the workers, adding new ones
 * and changing their position.
 * Their methods are called from the GameSession object, which has a workersHandler attribute.
 */
public class WorkersHandler {

    private ArrayList<Worker> workers = new ArrayList<>();
    private GameSession gameSession;


    /**
     * Non-default constructor, it initializes a WorkersHandler with the GameSession
     * that owns this object.
     * @param gameSession game session which has this handler as an attribute
     */
    public WorkersHandler(GameSession gameSession) {
        this.gameSession = gameSession;
    }


    /**
     * This method adds a new worker to the existing ones.
     * It invokes the constructor of the worker with the color chosen by its owner and with
     * an incremental id based on the number of already existing workers.
     * @param color color of the worker
     */
    public void addNewWorker(String color) {
        int workerId = workers.size();
        Worker newWorker = new Worker(workerId, color);
        workers.add(newWorker);
    }


    /**
     * This method changes the position of a worker, checking if the move is possible and
     * throwing an exception if not possible
     * @param worker worker whose position is wanted to be changed
     * @param position position the player wants to move the worker to
     */
    public void changePosition(Worker worker, Coord position) {
        Cell cellToMove = gameSession.getCellsHandler().getCell(position);
        // TODO implement control if change is possible (cell already occupied -> exception)
        worker.setCurrentPosition(position);
    }


    /**
     * This method returns the ArrayList containing all the workers.
     * @return ArrayList containing all the workers
     */
    public ArrayList<Worker> getWorkers()  {
        return workers;
    }


    // TODO add getGameSession() (or maybe not because it is not needed from outside the class...)

}
