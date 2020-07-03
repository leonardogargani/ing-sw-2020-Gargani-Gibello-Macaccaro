package it.polimi.ingsw.PSP43.client.graphic.gui.controllers.game_init;

import it.polimi.ingsw.PSP43.client.graphic.gui.GuiStarter;
import it.polimi.ingsw.PSP43.client.graphic.gui.controllers.AbstractController;
import it.polimi.ingsw.PSP43.server.model.Color;
import it.polimi.ingsw.PSP43.client.graphic.Screens;
import it.polimi.ingsw.PSP43.client.network.networkMessages.LeaveGameMessage;
import it.polimi.ingsw.PSP43.client.network.networkMessages.WorkersColorResponse;
import it.polimi.ingsw.PSP43.server.network.networkMessages.WorkersColorRequest;
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

    /**
     * This method handles the arrival of a worker color request by setting up the window of the color choice.
     * @param workersColorRequest The request containing the colors of the workers available for choice.
     */
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
    @FXML
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
     */
    @FXML
    private void handleConfirmImage() {
        WorkersColorResponse response = new WorkersColorResponse(actualColorDisplayed);
        AbstractController.getClientBG().sendMessage(response);
        handleTransitToBoard();

    }

    /**
     * This method is used to transit to the board, setting minimum width and size for a comfortable user experience.
     */
    public void handleTransitToBoard() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AbstractController.class.getResource("/FXML/game/board.fxml"));
        Stage stage;
        try {
            stage = GuiStarter.getPrimaryStage();
            Scene scene = new Scene(loader.load());

            Stage newStage = new Stage();
            GuiStarter.setPrimaryStage(newStage);

            newStage.setMinHeight(700.0);
            newStage.setMinWidth(1200.0);
            newStage.setResizable(true);
            stage.centerOnScreen();

            newStage.setScene(scene);
            scene.getStylesheets().add(AbstractController.class.getResource("/CSS/game/game.css").toExternalForm());

            stage.close();
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method handles the click done by the player on the exit button of the window.
     */
    @FXML
    private void handleExitClick() {
        getClientBG().sendMessage(new LeaveGameMessage());
        super.handleExit();
    }
}
