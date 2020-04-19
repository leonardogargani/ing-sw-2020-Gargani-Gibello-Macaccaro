package it.polimi.ingsw.PSP43.server.modelHandlers;

import it.polimi.ingsw.PSP43.Color;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.modelHandlersException.CellAlreadyOccupiedExeption;
import it.polimi.ingsw.PSP43.server.modelHandlersException.CellHeightException;

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


    public void removeWorkers(int[] workersIds) {
        CellsHandler cellsHandler = gameSession.getCellsHandler();
        Worker workerToRemove;
        Cell cellOccupiedByWorker;
        Coord workerPosition;

        for (int i : workersIds) {
            for (Worker w : workers) {
                if (w.getId() == i) {
                    workerToRemove = w;
                    workerPosition = workerToRemove.getCurrentPosition();
                    cellOccupiedByWorker = cellsHandler.getCell(workerPosition);
                    cellOccupiedByWorker.setOccupiedByWorker(false);
                    cellsHandler.changeStateOfCell(cellOccupiedByWorker, workerPosition);
                    workers.remove(w);
                }
            }
        }
    }

    /**
     * This method adds a new worker to the existing ones.
     * It invokes the constructor of the worker with the color chosen by its owner and with
     * an incremental id based on the number of already existing workers.
     * @param color color of the worker
     */
    public int addNewWorker(Color color) {
        int workerId = workers.size();
        Worker newWorker = new Worker(workerId, color);
        workers.add(newWorker);
        return workerId;
    }


    /**
     * This method changes the position of a worker, checking if the move is possible and
     * throwing an exception if not possible.
     * It also sets the occupation boolean flags of the two involved cells.
     * @param worker worker whose position is wanted to be changed
     * @param position position the player wants to move the worker to
     */
    public void changePosition(Worker worker, Coord position) {
        Coord coordBeforeMove = worker.getCurrentPosition();
        Cell cellBeforeMove = gameSession.getCellsHandler().getCell(coordBeforeMove);
        Cell cellAfterMove = gameSession.getCellsHandler().getCell(position);


        worker.setCurrentPosition(position);
        cellBeforeMove.setOccupiedByWorker(false);
        cellAfterMove.setOccupiedByWorker(true);
    }


    /**
     * This method returns the ArrayList containing all the workers.
     * @return ArrayList containing all the workers
     */
    public ArrayList<Worker> getWorkers()  {
        return workers;
    }

    public Worker getWorker(Coord c) {
        Coord position;
        for (Worker w : workers) {
            position = w.getCurrentPosition();
            if (position.getX() == c.getX() && position.getY() == c.getY()) return w;
        }
        return null;
    }

    public Worker getWorker(int id) {
        int currentId;
        for (Worker w : workers) {
            currentId = w.getId();
            if (id == currentId) return w;
        }
        return null;
    }

    // TODO add getGameSession() (or maybe not because it is not needed from outside the class...)

}