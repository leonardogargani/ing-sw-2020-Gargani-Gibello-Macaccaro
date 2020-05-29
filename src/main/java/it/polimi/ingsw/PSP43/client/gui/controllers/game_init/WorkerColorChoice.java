package it.polimi.ingsw.PSP43.client.gui.controllers.game_init;

import it.polimi.ingsw.PSP43.Color;
import it.polimi.ingsw.PSP43.client.ClientBG;
import it.polimi.ingsw.PSP43.client.networkMessages.WorkersColorResponse;
import it.polimi.ingsw.PSP43.server.networkMessages.WorkersColorRequest;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class WorkerColorChoice {
    public ImageView confirmButton;
    public ImageView leftArrow;
    public ImageView rightArrow;
    public ImageView workerImage;
    public ImageView exitButton;

    private static ClientBG clientBG;

    private List<Color> colorsAvailable;
    private Color actualColorDisplayed;

    private void initialise() {
        rightArrow.getStyleClass().add("arrow");
        leftArrow.getStyleClass().add("arrow");
        confirmButton.setId("confirm-button");
    }

    /**
     * Method that sets the ClientBG attribute of the controller, it will be invoked inside
     * the GuiGraphicHandler constructor so that the controller will have already the attribute set
     * once it will be utilized.
     * @param clientBG clientBG of the current client
     */
    public static void setClientBG(ClientBG clientBG) {
        WorkerColorChoice.clientBG = clientBG;
    }

    public void handleChoiceOfWorkerColor(WorkersColorRequest workersColorRequest) {
        colorsAvailable = workersColorRequest.getColorsAvailable();
        actualColorDisplayed = colorsAvailable.get(0);
        displayWorker();
    }

    /**
     * This method displays the next color of the worker, that is saved in the private field of the
     * controller.
     */
    public void displayWorker() {
        try {
            Image image;
            switch (actualColorDisplayed) {
                case ANSI_GREEN:
                    image = new Image(new FileInputStream(String.valueOf(getClass().getResource("images/workers/worker_green_1.png"))));
                    workerImage.setImage(image);
                case ANSI_RED:
                    image = new Image(new FileInputStream(String.valueOf(getClass().getResource("images/workers/worker_red_1.png"))));
                    workerImage.setImage(image);
                case ANSI_BLUE:
                    image = new Image(new FileInputStream(String.valueOf(getClass().getResource("images/workers/worker_blue_1.png"))));
                    workerImage.setImage(image);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method that handles a mouse event performed on the left arrow image to show the previous
     * color of the worker, checking if the currently displayed one is the latest color or not.
     */
    @FXML
    public void handleRightArrowImage() {
        for (Iterator<Color> colorIterator = colorsAvailable.iterator(); colorIterator.hasNext(); ) {
            Color color = colorIterator.next();
            if (actualColorDisplayed == color) {
                if (colorsAvailable.indexOf(color) == colorsAvailable.size()) {
                    actualColorDisplayed = colorsAvailable.get(0);
                }
                else {
                    actualColorDisplayed = colorIterator.next();
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
    public void handleLeftArrowImage() {
        for (Color c : colorsAvailable) {
            if (actualColorDisplayed == c) {
                if (colorsAvailable.indexOf(c) == 0) {
                    actualColorDisplayed = colorsAvailable.get(colorsAvailable.size() - 1);
                }
                else {
                    int indexCurrentColor = colorsAvailable.indexOf(c);
                    actualColorDisplayed = colorsAvailable.get(indexCurrentColor - 1);
                }
            }
        }
        displayWorker();
    }

    /**
     * Method that handles a mouse event performed on the image to confirm that the current color
     * will be the one chosen by the player for this game session.
     * @param event mouse event performed on the image
     */
    @FXML
    public void handleConfirmImage(MouseEvent event) {
        WorkersColorResponse response = new WorkersColorResponse(actualColorDisplayed);
        clientBG.sendMessage(response);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/FXML/miscellaneous/wait.fxml"));
        try {
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(loader.load());

            WaitController controller = loader.getController();
            controller.setLabelText("wait for other players to choose the color of their workers...");

            scene.getStylesheets().add(getClass().getResource("/CSS/game_init/style.css").toExternalForm());
            stage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
