package it.polimi.ingsw.PSP43.client.gui.controllers.game_init;

import it.polimi.ingsw.PSP43.client.ClientBG;
import it.polimi.ingsw.PSP43.client.networkMessages.PlayersNumberResponse;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;


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
     * Method that sets the ClientBG attribute of the controller, it will be invoked inside
     * the GuiGraphicHandler constructor so that the controller will have already the attribute set
     * once it will be utilized.
     * @param clientBG clientBG of the current client
     */
    public void setClientBG(ClientBG clientBG) {
        PlayersNumberChoiceController.clientBG = clientBG;
    }


    /**
     * Method that handles an event performed on the button to confirm the number of players.
     * @param event event performed on the button
     */
    @FXML
    private void handleConfirmButton(ActionEvent event) {

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

    }

}
