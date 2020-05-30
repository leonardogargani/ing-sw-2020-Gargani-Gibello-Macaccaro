package it.polimi.ingsw.PSP43.client.gui.controllers.game;

import it.polimi.ingsw.PSP43.Color;
import it.polimi.ingsw.PSP43.client.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.networkMessages.ActionRequest;
import it.polimi.ingsw.PSP43.server.networkMessages.CellMessage;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Map;

public class BoardController {
    private ImageView[][] board;
    private Label bottomLabel;
    private MatchController.Decision decision;
    private ArrayList<Coord> cellsAvailable;

    public BoardController(ImageView[][] board, Label bottomMenu, MatchController.Decision decision) {
        this.board = board;
        this.bottomLabel = bottomMenu;
        this.decision = decision;
        cellsAvailable = new ArrayList<>();
    }


    public void updateCell(CellMessage cellMessage) {
        Cell cell = cellMessage.getCell();
        int i, j;
        i = cell.getCoord().getX();
        j = cell.getCoord().getY();
        ImageView imageView = board[i][j];

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


    public Coord checkWorkerChosen(Map<Coord, ArrayList<Coord>> map, ImageView source){
        Coord chosenPosition = findCell(source);
        boolean workerOk = false;

        for (Map.Entry<Coord, ArrayList<Coord>> entry : map.entrySet()){
            assert chosenPosition != null;
            if (entry.getKey().getX() == chosenPosition.getX() & entry.getKey().getY() == chosenPosition.getY())
            {
                workerOk = true;
            }
        }
        if (workerOk){
            return chosenPosition;
        }
        else {
            return null;
        }
    }

    public ActionResponse checkAvailability(Map<Coord, ArrayList<Coord>> map, ImageView source) {
        Coord chosenPosition = findCell(source);
        boolean found = false;
        //check if it is available on the hashMap

        for (Map.Entry<Coord, ArrayList<Coord>> entry : map.entrySet()) {
            for(int i = 0; i<entry.getValue().size();i++){
                if (entry.getValue().get(i).getX() == chosenPosition.getX() & entry.getValue().get(i).getY() == chosenPosition.getY()) {
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

    public Coord checkAction(Map<Coord, ArrayList<Coord>> cellsAvailable, ImageView source, Coord starterPosition) {
        Coord chosenPosition = findCell(source);
        boolean moveOk = false;

        for (Map.Entry<Coord, ArrayList<Coord>> entry : cellsAvailable.entrySet()) {
            if (entry.getKey().getX() == starterPosition.getX() & entry.getKey().getY() == starterPosition.getY())
                for (int c = 0; c < entry.getValue().size(); c++) {
                    assert chosenPosition != null;
                    if (entry.getValue().get(c).getX() == chosenPosition.getX() & entry.getValue().get(c).getY() == chosenPosition.getY()) {
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

    public void underlineAvailableCells(ActionRequest actionRequest) {
        if (cellsAvailable.size() > 0) {
            cellsAvailable.clear();
        }
        Map<Coord, ArrayList<Coord>> cells = actionRequest.getCellsAvailable();
        for (Map.Entry<Coord, ArrayList<Coord>> entry : cells.entrySet()) {
            cellsAvailable.addAll(entry.getValue());
            for (Coord coord : cells.keySet()) board[coord.getX()][coord.getY()].setEffect(new DropShadow(BlurType.THREE_PASS_BOX, javafx.scene.paint.Color.DARKORANGE, 10, 0.65, 10, 10));
        }
    }

    public void removeUnderline(ActionRequest actionRequest) {
        Map<Coord, ArrayList<Coord>> cells = actionRequest.getCellsAvailable();
        for (Coord coord : cells.keySet()) board[coord.getX()][coord.getY()].setEffect(null);
        cellsAvailable.clear();
    }
}




