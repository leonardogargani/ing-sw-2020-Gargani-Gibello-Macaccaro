package it.polimi.ingsw.PSP43.client.gui.controllers.game_init;

import it.polimi.ingsw.PSP43.Color;
import it.polimi.ingsw.PSP43.client.ClientBG;
import it.polimi.ingsw.PSP43.client.Screens;
import it.polimi.ingsw.PSP43.client.gui.controllers.AbstractController;
import it.polimi.ingsw.PSP43.client.networkMessages.LeaveGameMessage;
import it.polimi.ingsw.PSP43.client.networkMessages.WorkersColorResponse;
import it.polimi.ingsw.PSP43.server.controllers.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.networkMessages.WorkersColorRequest;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class WorkerColorChoiceController extends AbstractController {
    @FXML
    private Label messageLabel;
    @FXML
    private ImageView confirmButton;
    @FXML
    private ImageView leftArrow;
    @FXML
    private ImageView rightArrow;
    @FXML
    private ImageView workerImage;
    @FXML
    private ImageView exitButton;

    private List<Color> colorsAvailable;
    private Color actualColorDisplayed;

    /**
     * Method called as soon as the controlled fxml file gets loaded, here used to set css ids and classes.
     */
    @FXML
    private void initialize() {
        rightArrow.getStyleClass().add("arrow");
        leftArrow.getStyleClass().add("arrow");
        confirmButton.setId("confirm-button");
        messageLabel.setId("advise-label");
        confirmButton.getStyleClass().add("confirm-button");
        messageLabel.getStyleClass().add("advise-label");
        workerImage.setId("worker-image");
        workerImage.getStyleClass().add("worker-image");
        exitButton.setId("exit-button");
        exitButton.getStyleClass().add("exit-button");
    }

    public void handleChoiceOfWorkerColor(WorkersColorRequest workersColorRequest) {
        messageLabel.setText(Screens.WORKERS_COLOR_REQUEST.toString());
        messageLabel.setAlignment(Pos.CENTER);

        colorsAvailable = workersColorRequest.getColorsAvailable();
        actualColorDisplayed = colorsAvailable.get(0);
        displayWorker();
    }

    /**
     * This method displays the next color of the worker, that is saved in the private field of the
     * controller.
     */
    private void displayWorker() {
        switch (actualColorDisplayed) {
            case ANSI_GREEN:
                workerImage.setImage(new Image(getClass().getResource("/images/workers/worker_green_1.png").toExternalForm()));
                break;
            case ANSI_RED:
                workerImage.setImage(new Image(getClass().getResource("/images/workers/worker_red_1.png").toExternalForm()));
                break;
            case ANSI_BLUE:
                workerImage.setImage(new Image(getClass().getResource("/images/workers/worker_blue_1.png").toExternalForm()));
                break;
        }
    }

    /**
     * Method that handles a mouse event performed on the left arrow image to show the previous
     * color of the worker, checking if the currently displayed one is the latest color or not.
     */
    @FXML
    private void handleRightArrowImage() {
        for (Iterator<Color> colorIterator = colorsAvailable.iterator(); colorIterator.hasNext(); ) {
            Color color = colorIterator.next();
            if (actualColorDisplayed == color) {
                if (colorsAvailable.indexOf(color) == colorsAvailable.size() - 1) {
                    actualColorDisplayed = colorsAvailable.get(0);
                    break;
                } else {
                    actualColorDisplayed = colorIterator.next();
                    break;
                }
            }
        }
        displayWorker();
    }


    /**
     * Method that handles a mouse event performed on the left arrow image to show the previous
     * color of the worker, checking if the currently displayed one is the first color or not.
     */
    @FXML
    private void handleLeftArrowImage() {
        for (Color c : colorsAvailable) {
            if (actualColorDisplayed == c) {
                if (colorsAvailable.indexOf(c) == 0) {
                    actualColorDisplayed = colorsAvailable.get(colorsAvailable.size() - 1);
                    break;
                } else {
                    int indexCurrentColor = colorsAvailable.indexOf(c);
                    actualColorDisplayed = colorsAvailable.get(indexCurrentColor - 1);
                    break;
                }
            }
        }
        displayWorker();
    }

    /**
     * Method that handles a mouse event performed on the image to confirm that the current color
     * will be the one chosen by the player for this game session.
     *
     * @param event mouse event performed on the image
     */
    @FXML
    private void handleConfirmImage(MouseEvent event) {
        WorkersColorResponse response = new WorkersColorResponse(actualColorDisplayed);
        AbstractController.getClientBG().sendMessage(response);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/FXML/game/board.fxml"));
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(loader.load());

            scene.getStylesheets().add(getClass().getResource("/CSS/game/game.css").toExternalForm());
            stage.setScene(scene);

            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleExitClick(MouseEvent event) {
        getClientBG().sendMessage(new LeaveGameMessage());
        super.handleExit();
    }
}
