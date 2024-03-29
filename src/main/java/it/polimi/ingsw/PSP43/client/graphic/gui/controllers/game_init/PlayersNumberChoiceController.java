package it.polimi.ingsw.PSP43.client.graphic.gui.controllers.game_init;

import it.polimi.ingsw.PSP43.client.graphic.Screens;
import it.polimi.ingsw.PSP43.client.graphic.gui.GuiStarter;
import it.polimi.ingsw.PSP43.client.graphic.gui.controllers.AbstractController;
import it.polimi.ingsw.PSP43.client.network.networkMessages.LeaveGameMessage;
import it.polimi.ingsw.PSP43.client.network.networkMessages.PlayersNumberResponse;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.io.IOException;


public class PlayersNumberChoiceController extends AbstractController {

    @FXML private Button confirmButton;
    @FXML private ToggleGroup toggleGroup;
    @FXML private RadioButton twoPlayersButton;
    @FXML private RadioButton threePlayersButton;
    @FXML private Label buttonPressedLabel;
    @FXML private ImageView exitImage;


    /**
     * Method called as soon as the controlled fxml file gets loaded, here used to set css ids and classes
     */
    @FXML
    private void initialize() {
        confirmButton.getStyleClass().add("confirm-button");
        exitImage.setPickOnBounds(false);
        exitImage.getStyleClass().add("exit-image");
        twoPlayersButton.getStyleClass().add("choice-button");
        threePlayersButton.getStyleClass().add("choice-button");
    }


    /**
     * Method that handles an event performed on the button to confirm the number of players.
     */
    @FXML
    private void handleConfirmButton() {

        confirmButton.setDisable(true);

        int chosenNumber;

        if (toggleGroup.getSelectedToggle() == null) {
            buttonPressedLabel.setText("You must choose a number!");
            confirmButton.setDisable(false);
            return;
        }

        RadioButton selectedButton = (RadioButton) toggleGroup.getSelectedToggle();

        if (twoPlayersButton.equals(selectedButton)) {
            chosenNumber = 2;
        } else {
            chosenNumber = 3;
        }

        PlayersNumberResponse response = new PlayersNumberResponse(chosenNumber);
        getClientBG().sendMessage(response);

        // display a waiting screen while other players are connecting
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/FXML/miscellaneous/wait.fxml"));
        try {
            Stage stage = GuiStarter.getPrimaryStage();
            Scene scene = new Scene(loader.load());

            WaitController controller = loader.getController();
            controller.setLabelText(Screens.CONNECTING_WITH_OTHERS.toString());

            scene.getStylesheets().add(getClass().getResource("/CSS/game_init/style.css").toExternalForm());
            stage.setScene(scene);

            // set minimum sizes of the stage (can be either before and after loader.load())
            stage.setMinHeight(700);
            stage.setMinWidth(1000);

            // set the stage to the center of the screen (must be after loader.load())
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Method that handles an event performed on the image to exit and return back to the home screen.
     */
    @FXML
    private void handleExitImage() {
        getClientBG().sendMessage(new LeaveGameMessage());
        super.handleExit();
    }

}
