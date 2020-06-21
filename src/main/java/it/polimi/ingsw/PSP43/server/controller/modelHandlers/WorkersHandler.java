package it.polimi.ingsw.PSP43.server.controller.modelHandlers;

import it.polimi.ingsw.PSP43.server.model.Color;
import it.polimi.ingsw.PSP43.server.controller.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Worker;
import java.util.*;


/**
 * WorkersHandler has the tasks of keeping track of all the workers, adding new ones
 * and changing their position.
 * Their methods are called from the GameSession object, which has a workersHandler attribute.
 */
public class WorkersHandler {

    private final List<Worker> workers = new ArrayList<>();
    private final GameSession gameSession;
    private final CellsHandler cellsHandler;


    /**
     * Non-default constructor, it initializes a WorkersHandler with the GameSession
     * that owns this object.
     * @param gameSession game session which has this handler as an attribute
     */
    public WorkersHandler(GameSession gameSession) {
        this.gameSession = gameSession;
        this.cellsHandler = gameSession.getCellsHandler();
    }


    /**
     * Method that removes the workers (identified by their id) from the list of all the workers.
     * @param workersIds array containing the ids of the workers to be removed
     */
    public void removeWorkers(Integer[] workersIds) {

        CellsHandler cellsHandler = gameSession.getCellsHandler();

        for (int i : workersIds) {

            for (Iterator<Worker> workerIterator = workers.iterator(); workerIterator.hasNext(); ) {
                Worker currentWorker = workerIterator.next();

                if (currentWorker.getId() == i) {
                    Coord workerPosition = currentWorker.getCurrentPosition();
                    Cell cellOccupiedByWorker = cellsHandler.getCell(workerPosition);
                    cellOccupiedByWorker.setOccupiedByWorker(false);
                    cellsHandler.changeStateOfCell(cellOccupiedByWorker, workerPosition);
                    workerIterator.remove();
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
     * This method changes the coordAfterMove of a worker, checking if the move is possible and
     * throwing an exception if not possible.
     * It also sets the occupation boolean flags of the two involved cells.
     * @param worker worker whose coordAfterMove is wanted to be changed
     * @param coordAfterMove coordAfterMove the player wants to move the worker to
     */
    public void changePosition(Worker worker, Coord coordAfterMove) {
        Coord coordBeforeMove = worker.getCurrentPosition();
        Cell cellBeforeMove = gameSession.getCellsHandler().getCell(coordBeforeMove);
        Cell cellAfterMove = gameSession.getCellsHandler().getCell(coordAfterMove);

        cellBeforeMove.setOccupiedByWorker(false);
        cellBeforeMove.setColor(Color.ANSI_WHITE);
        cellAfterMove.setOccupiedByWorker(true);
        cellAfterMove.setColor(worker.getColor());
        cellsHandler.changeStateOfCell(cellAfterMove, coordAfterMove);
        cellsHandler.changeStateOfCell(cellBeforeMove, coordBeforeMove);


        worker.setCurrentPosition(coordAfterMove);
        worker.setLatestMoved(true);
    }


    /**
     * Method that swap the position of two workers.
     * @param currentPlayerWorker worker owned by the player who is making the move
     * @param opponentWorker worker owned by an opponent and that will be used for the swap
     */
    public void swapPositions(Worker currentPlayerWorker, Worker opponentWorker) {
        currentPlayerWorker.setCurrentPosition(opponentWorker.getCurrentPosition());
        currentPlayerWorker.setLatestMoved(true);
        opponentWorker.setCurrentPosition(currentPlayerWorker.getPreviousPosition());


        Coord coordFirstWorker = currentPlayerWorker.getCurrentPosition();
        Cell cellFirstWorker = cellsHandler.getCell(coordFirstWorker);
        cellFirstWorker.setColor(currentPlayerWorker.getColor());
        cellsHandler.changeStateOfCell(cellFirstWorker, coordFirstWorker);

        Coord coordSecondWorker = opponentWorker.getCurrentPosition();
        Cell cellSecondWorker = cellsHandler.getCell(coordSecondWorker);
        cellSecondWorker.setColor(opponentWorker.getColor());
        cellsHandler.changeStateOfCell(cellSecondWorker, coordSecondWorker);
    }


    /**
     * This method returns the ArrayList containing all the workers.
     * @return ArrayList containing all the workers
     */
    public List<Worker> getWorkers()  {
        return Collections.unmodifiableList(workers);
    }


    /**
     * Method that return the workers given by their ids.
     * @param ids ids of the worker that are wanted to be returned
     * @return ArrayList of requested workers
     */
    public List<Worker> getWorkers(Integer[] ids) {
        ArrayList<Worker> workers = new ArrayList<>();
        for (Integer i : ids) {
            workers.add(getWorker(i));
        }
        return Collections.unmodifiableList(new ArrayList<>(workers));
    }


    /**
     * Method that returns a worker given its coordinate.
     * @param c coordinate of the worker
     * @return worker at the given coordinate (null if the cell at that coordinate is empty)
     */
    public Worker getWorker(Coord c) {
        Coord position;
        for (Worker w : workers) {
            position = w.getCurrentPosition();
            if (position.getX() == c.getX() && position.getY() == c.getY()) return w;
        }
        return null;
    }


    /**
     * Method that returns a worker given its id.
     * @param id id of the worker
     * @return worker with the given id (null if there is no worker with the given id)
     */
    public Worker getWorker(int id) {
        int currentId;
        for (Worker w : workers) {
            currentId = w.getId();
            if (id == currentId) return w;
        }
        return null;
    }


    /**
     * Method that sets the initial position of a worker.
     * @param idWorker id of the worker that needs to be placed on the board
     * @param initialCoord initial coordinate of the worker on the board
     */
    public void setInitialPosition(int idWorker, Coord initialCoord) {
        for (Worker w : workers) {
            if (w.getId() == idWorker) {
                Cell initialCell = cellsHandler.getCell(initialCoord);
                initialCell.setColor(w.getColor());
                initialCell.setOccupiedByWorker(true);
                cellsHandler.changeStateOfCell(initialCell, initialCoord);

                w.setCurrentPosition(initialCoord);
            }
        }
    }

    public boolean isOnPerimetralCell(Worker worker) {
        Coord currentPosition = worker.getCurrentPosition();

        return cellsHandler.isPerimetral(currentPosition);
    }

}