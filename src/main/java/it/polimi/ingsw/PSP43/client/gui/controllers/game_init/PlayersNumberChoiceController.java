package it.polimi.ingsw.PSP43.client.gui.controllers.game_init;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import java.io.IOException;


public class PlayersNumberChoiceController {

    public Button confirmButton;
    public ToggleGroup toggleGroup;
    public Button twoPlayersButton;
    public Button threePlayersButton;


    @FXML
    private void handleConfirmButton(ActionEvent event) {

        /*
        if (toggleGroup.getSelectedToggle() == null) {
            buttonPressedLabel.setText("You must choose a number!");
            return;
        }

        RadioButton selectedButton = (RadioButton) toggleGroup.getSelectedToggle();

        if (cliButton.equals(selectedButton)) {

            buttonPressedLabel.setText("work in progress...");

            // TODO look at the initialization as done in the cli (e.g.: create a CliGraphicHandler)

        } else if (guiButton.equals(selectedButton)) {

            // TODO look at the initialization as done in the cli (e.g.: create a GuiGraphicHandler)

            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/FXML/serverIPChoice.fxml"));

            try {
                stage.setScene(new Scene(loader.load()));
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {

            System.out.println("An error has occurred");

        }

         */

    }

}
