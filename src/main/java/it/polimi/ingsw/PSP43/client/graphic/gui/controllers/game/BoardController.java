package it.polimi.ingsw.PSP43.client.graphic.gui.controllers.game;

import it.polimi.ingsw.PSP43.server.model.Color;
import it.polimi.ingsw.PSP43.client.network.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.network.networkMessages.CellMessage;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Map;

public class BoardController {
    private ImageView[][] board;
    private ArrayList<Coord> cellsAvailable;
    private DropShadow effect;

    /**
     * Not default constructor for the BoardController class.
     * @param board is an array of ImageViews that are the board's cells
     */
    public BoardController(ImageView[][] board) {
        this.board = board;
        cellsAvailable = new ArrayList<>();
        effect = new DropShadow(BlurType.THREE_PASS_BOX, javafx.scene.paint.Color.YELLOW, 10, 0.50, 0, 0);
    }

    /**
     * This method updates cells when a CellMessage arrives.
     * @param cellMessage containing coordinates, high and occupied states of the cell to be updated
     */
    public void updateCell(CellMessage cellMessage) {
        Cell cell = cellMessage.getCell();

        ImageView imageView = board[cellMessage.getCell().getCoord().getX()][cellMessage.getCell().getCoord().getY()];

        if (cell.getOccupiedByWorker()) {
            switch (cell.getHeight()) {
                case 0: {
                    if (cell.getColor() == Color.ANSI_BLUE)
                        imageView.setImage(new Image(getClass().getResource("/images/workers/worker_blue_1.png").toExternalForm()));
                    else if (cell.getColor() == Color.ANSI_GREEN)
                        imageView.setImage(new Image(getClass().getResource("/images/workers/worker_green_1.png").toExternalForm()));
                    else if (cell.getColor() == Color.ANSI_RED)
                        imageView.setImage(new Image(getClass().getResource("/images/workers/worker_red_1.png").toExternalForm()));
                    break;
                }
                case 1: {
                    if (cell.getColor() == Color.ANSI_GREEN)
                        imageView.setImage(new Image(getClass().getResource("/images/workers/cell_1_green.png").toExternalForm()));
                    else if (cell.getColor() == Color.ANSI_BLUE)
                        imageView.setImage(new Image(getClass().getResource("/images/workers/cell_1_blue.png").toExternalForm()));
                    else if (cell.getColor() == Color.ANSI_RED)
                        imageView.setImage(new Image(getClass().getResource("/images/workers/cell_1_red.png").toExternalForm()));
                    break;
                }
                case 2: {
                    if (cell.getColor() == Color.ANSI_RED)
                        imageView.setImage(new Image(getClass().getResource("/images/workers/cell_2_red.png").toExternalForm()));
                    else if (cell.getColor() == Color.ANSI_GREEN)
                        imageView.setImage(new Image(getClass().getResource("/images/workers/cell_2_green.png").toExternalForm()));
                    else if (cell.getColor() == Color.ANSI_BLUE)
                        imageView.setImage(new Image(getClass().getResource("/images/workers/cell_2_blue.png").toExternalForm()));
                    break;
                }
                case 3: {
                    if (cell.getColor() == Color.ANSI_BLUE)
                        imageView.setImage(new Image(getClass().getResource("/images/workers/cell_3_blue.png").toExternalForm()));
                    else if (cell.getColor() == Color.ANSI_RED)
                        imageView.setImage(new Image(getClass().getResource("/images/workers/cell_3_red.png").toExternalForm()));
                    else if (cell.getColor() == Color.ANSI_GREEN)
                        imageView.setImage(new Image(getClass().getResource("/images/workers/cell_3_green.png").toExternalForm()));
                    break;
                }
            }
        } else {
            if (cell.getHeight() == 0)
                imageView.setImage(null);
            else if (cell.getHeight() == 1)
                imageView.setImage(new Image(getClass().getResource("/images/workers/cell_1.png").toExternalForm()));
            else if (cell.getHeight() == 2)
                imageView.setImage(new Image(getClass().getResource("/images/workers/cell_2.png").toExternalForm()));
            else if (cell.getHeight() == 3)
                imageView.setImage(new Image(getClass().getResource("/images/workers/cell_3.png").toExternalForm()));
            else imageView.setImage(new Image(getClass().getResource("/images/workers/dome.png").toExternalForm()));
        }
    }

    /**
     * This method checks that you select one of your workers for the action.
     * @param map containing your worker's coordinates like keys and their possible future positions like values
     * @param source is the source of the cell clicked for the move/build
     * @return coordinates of the selected cell for the move/build if it is possible null otherwise
     */
    public Coord checkWorkerChosen(Map<Coord, ArrayList<Coord>> map, ImageView source){
        Coord chosenPosition = findCell(source);
        boolean workerOk = false;

        for (Map.Entry<Coord, ArrayList<Coord>> entry : map.entrySet()){
            assert chosenPosition != null;
            if (entry.getKey().getX() == chosenPosition.getX() && entry.getKey().getY() == chosenPosition.getY()) {
                workerOk = true;
                break;
            }
        }
        if (workerOk){
            return chosenPosition;
        }
        else {
            return null;
        }
    }

    /**
     * This method checks the availability of a cell chosen for place a worker at the start of the game.
     * @param map contains available positions
     * @param source clicked to place there the worker
     * @return an ActionResponse if the selected cell is ok, null otherwise
     */
    public ActionResponse checkAvailability(Map<Coord, ArrayList<Coord>> map, ImageView source) {
        Coord chosenPosition = findCell(source);
        boolean found = false;
        //check if it is available on the hashMap

        for (Map.Entry<Coord, ArrayList<Coord>> entry : map.entrySet()) {
            for(int i = 0; i<entry.getValue().size();i++){
                if (entry.getValue().get(i).getX() == chosenPosition.getX() && entry.getValue().get(i).getY() == chosenPosition.getY()) {
                    found = true;
                    break;
                }
            }
        }
        if (found) {
            return new ActionResponse(chosenPosition, chosenPosition);
        } else {
            return null;
        }
    }

    /**
     * This method checks the clicked cell for the move/build.
     * @param cellsAvailable contains available cells
     * @param source is the clicked cell
     * @param starterPosition is the cell where your selected worker is before moving/building
     * @return coordinates of the selected cell for the move/build if it is possible null otherwise
     */
    public Coord checkAction(Map<Coord, ArrayList<Coord>> cellsAvailable, ImageView source, Coord starterPosition) {
        Coord chosenPosition = findCell(source);
        boolean moveOk = false;

        for (Map.Entry<Coord, ArrayList<Coord>> entry : cellsAvailable.entrySet()) {
            if (entry.getKey().getX() == starterPosition.getX() && entry.getKey().getY() == starterPosition.getY())
                for (int c = 0; c < entry.getValue().size(); c++) {
                    assert chosenPosition != null;
                    if (entry.getValue().get(c).getX() == chosenPosition.getX() && entry.getValue().get(c).getY() == chosenPosition.getY()) {
                        moveOk = true;
                        break;
                    }
                }
        }
        if (moveOk) {
            return chosenPosition;
        } else {
            return null;
        }
    }

    /**
     * This method finds a cell from his source, in the board's array of the cells.
     * @param source is the clicked cell
     * @return coordinates of the source
     */
    public Coord findCell(ImageView source){
        Coord chosenPosition = null;
        int i, j;
        for (i = 0; i < 5; i++)
            for (j = 0; j < 5; j++) {
                if (source == board[i][j]) {
                    chosenPosition = new Coord(i, j);
                }
            }
        return chosenPosition;
    }

    /**
     * This method set a DropShadow effect in the cell where you have your workers.
     * @param cells is the HashMap content in the ActionRequest message
     */
    public void underlineWorkers(Map<Coord,ArrayList<Coord>> cells) {
        if (cellsAvailable.size() > 0) {
            cellsAvailable.clear();
        }
        for (Map.Entry<Coord, ArrayList<Coord>> entry : cells.entrySet()) {
            cellsAvailable.addAll(entry.getValue());
            for (Coord coord : cells.keySet()) board[coord.getX()][coord.getY()].setEffect(effect);
        }
    }

    /**
     * This method removes the DropShadow effect from your workers' cells.
     * @param cells is the HashMap content in the ActionRequest message
     */
    public void removeUnderlineWorkers(Map<Coord, ArrayList<Coord>> cells) {
        for (Coord coord : cells.keySet()) {
            board[coord.getX()][coord.getY()].setEffect(null);
        }
        cellsAvailable.clear();
    }

    /**
     * This method set a DropShadow effect in the cells where you can move/build with your workers.
     * @param cells is the HashMap content in the ActionRequest message
     * @param startPosition is the cell where your selected worker is before moving/building
     */
    public void underlineMoves(Map<Coord, ArrayList<Coord>> cells, Coord startPosition) {
        Image image = new Image(getClass().getResource("/images/workers/cell_empty.png").toExternalForm());
        for (Map.Entry<Coord, ArrayList<Coord>> entry : cells.entrySet()) {
            if (entry.getKey().getX() == startPosition.getX() && entry.getKey().getY() == startPosition.getY()) {
                for (int i = 0; i < entry.getValue().size(); i++) {
                    if (board[entry.getValue().get(i).getX()][entry.getValue().get(i).getY()].getImage() == null) {
                        board[entry.getValue().get(i).getX()][entry.getValue().get(i).getY()].setImage(image);
                        cellsAvailable.add(new Coord(entry.getValue().get(i).getX(),entry.getValue().get(i).getY()));
                    }
                    board[entry.getValue().get(i).getX()][entry.getValue().get(i).getY()].setEffect(effect);
                }
            }
        }
    }

    /**
     * This method removes the DropShadow effect from your available cells for the move/build after the choice.
     * @param cells is the HashMap content in the ActionRequest message
     * @param startPosition is the cell where your selected worker is before moving/building
     */
    public void removeUnderlineMoves(Map<Coord, ArrayList<Coord>> cells, Coord startPosition) {
        for (Map.Entry<Coord, ArrayList<Coord>> entry : cells.entrySet()) {
            if (entry.getKey().getX() == startPosition.getX() & entry.getKey().getY() == startPosition.getY()) {
                for (int i = 0; i < entry.getValue().size(); i++) {
                    board[entry.getValue().get(i).getX()][entry.getValue().get(i).getY()].setEffect(null);
                    for (Coord coord : cellsAvailable) {
                        if (entry.getValue().get(i).getX() == coord.getX() && entry.getValue().get(i).getY() == coord.getY()) {
                            board[entry.getValue().get(i).getX()][entry.getValue().get(i).getY()].setImage(null);
                            break;
                        }
                    }
                }
            }
        }
    }
}