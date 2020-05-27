package it.polimi.ingsw.PSP43.client.gui.controllers.game_init;

import it.polimi.ingsw.PSP43.client.ClientBG;
import it.polimi.ingsw.PSP43.client.Screens;
import it.polimi.ingsw.PSP43.client.networkMessages.PlayersNumberResponse;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import java.io.IOException;


public class PlayersNumberChoiceController {

    public Button confirmButton;
    public ToggleGroup toggleGroup;
    public RadioButton twoPlayersButton;
    public RadioButton threePlayersButton;
    public Label buttonPressedLabel;

    private static ClientBG clientBG;


    /**
     * Method called as soon as the controlled fxml file gets loaded, here used to set css ids and classes
     */
    @FXML
    private void initialize() {
        confirmButton.getStyleClass().add("confirm-button");
    }


    /**
     * Method that sets the ClientBG attribute of the controller, it will be invoked inside the GuiGraphicHandler
     * constructor so that the controller will have already the attribute set once it will be utilized.
     * @param clientBG clientBG of the current client
     */
    public static void setClientBG(ClientBG clientBG) {
        PlayersNumberChoiceController.clientBG = clientBG;
    }


    /**
     * Method that handles an event performed on the button to confirm the number of players.
     * @param event event performed on the button
     */
    @FXML
    public void handleConfirmButton(ActionEvent event) {

        int chosenNumber;

        if (toggleGroup.getSelectedToggle() == null) {
            buttonPressedLabel.setText("You must choose a number!");
            return;
        }

        RadioButton selectedButton = (RadioButton) toggleGroup.getSelectedToggle();

        if (twoPlayersButton.equals(selectedButton)) {
            chosenNumber = 2;
        } else {
            chosenNumber = 3;
        }

        PlayersNumberResponse response = new PlayersNumberResponse(chosenNumber);
        clientBG.sendMessage(response);

        // display a waiting screen while other players are connecting
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/FXML/miscellaneous/wait.fxml"));
        try {
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
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


}
