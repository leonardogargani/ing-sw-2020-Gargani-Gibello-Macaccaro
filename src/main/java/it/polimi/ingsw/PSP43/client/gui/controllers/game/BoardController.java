package it.polimi.ingsw.PSP43.client.gui.controllers.game;

import it.polimi.ingsw.PSP43.Color;
import it.polimi.ingsw.PSP43.client.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.networkMessages.ActionRequest;
import it.polimi.ingsw.PSP43.server.networkMessages.CellMessage;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BoardController {
    private ImageView[][] board;
    private Label bottomMenu;
    private MatchController.Decision decision;
    private ArrayList<Coord> cellsAvailable;

    public BoardController(ImageView[][] board, Label bottomMenu, MatchController.Decision decision) {
        this.board = board;
        this.bottomMenu = bottomMenu;
        this.decision = decision;
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


    public ActionResponse checkAction(Map<Coord, ArrayList<Coord>> cellsAvailable, ImageView img) {
        ArrayList<Coord> possibleStartPositions = new ArrayList<>();
        Coord chosenPosition = null;

        int i, j;
        for (i = 0; i < 5; i++)
            for (j = 0; j < 5; j++) {
                if (img == board[i][j]) {
                    chosenPosition = new Coord(i, j);
                }
            }
        for (Coord startPosition : cellsAvailable.keySet()) {
            ArrayList<Coord> list = cellsAvailable.get(startPosition);
            for (Coord coord : list) {
                assert chosenPosition != null;
                if (coord.getX() == chosenPosition.getX() && coord.getY() == chosenPosition.getY()) {
                    possibleStartPositions.add(startPosition);
                }
            }
        }
        if (possibleStartPositions.size() == 0) {
            return null;
        } else if (possibleStartPositions.size() == 1) {
            return new ActionResponse(possibleStartPositions.get(0), chosenPosition);
        } else {
            bottomMenu.setText("Choose the worker you want to make the action perform: \n" +
                    "-for worker in " + possibleStartPositions.get(0).getX() + "," + possibleStartPositions.get(0).getY() +
                    " press V" +
                    "\n-for worker in " + possibleStartPositions.get(1).getX() + "," + possibleStartPositions.get(1).getY() +
                    " press X");
            decision = MatchController.Decision.NOT_DECIDED;
            while (decision == MatchController.Decision.NOT_DECIDED) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
        if (decision == MatchController.Decision.YES) {
            return new ActionResponse(possibleStartPositions.get(0), chosenPosition);
        } else {
            return new ActionResponse(possibleStartPositions.get(1), chosenPosition);
        }

    }

    public void underlineAvailableCells(ActionRequest actionRequest) {
        cellsAvailable.clear();
        Map<Coord, ArrayList<Coord>> cells = actionRequest.getCellsAvailable();
        for (Map.Entry<Coord, ArrayList<Coord>> entry : cells.entrySet()) {
            cellsAvailable.addAll(entry.getValue());
            for (Coord coord : cellsAvailable) board[coord.getX()][coord.getY()].setId("cellFree-image");
        }
    }

    public void removeUnderline() {
        for (Coord coord : cellsAvailable) board[coord.getX()][coord.getY()].setId(null);
        cellsAvailable.clear();
    }
}




