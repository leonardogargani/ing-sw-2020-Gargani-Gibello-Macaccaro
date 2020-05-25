package it.polimi.ingsw.PSP43.client.gui.controllers.game_init;

import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class WaitController {

    public Label labelToDisplay;


    /**
     * Method called as soon as the controlled fxml file gets loaded, here used to set
     * a generic waiting message to the label.
     */
    @FXML
    private void initialize() {
        // this is the default text, used if not overwritten with the setLabelText() method
        labelToDisplay.setText("Wait...");
    }


    /**
     * Method that sets the content of the main label at the top of the window to display
     * some information about the cause of the waiting.
     * @param textToDisplay text to be displayed inside the label at the top of the window
     */
    public void setLabelText(String textToDisplay) {
        labelToDisplay.setText(textToDisplay);
    }

}
