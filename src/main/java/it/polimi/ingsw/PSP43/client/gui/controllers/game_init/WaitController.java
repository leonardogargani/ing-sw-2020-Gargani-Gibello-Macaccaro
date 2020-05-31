package it.polimi.ingsw.PSP43.client.gui.controllers.game_init;

import it.polimi.ingsw.PSP43.client.gui.controllers.AbstractController;
import it.polimi.ingsw.PSP43.client.networkMessages.LeaveGameMessage;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;


public class WaitController extends AbstractController {

    @FXML private Label labelToDisplay;
    @FXML private ImageView exitImage;


    /**
     * Method called as soon as the controlled fxml file gets loaded, here used to set
     * a generic waiting message to the label.
     */
    @FXML
    private void initialize() {
        // this is the default text, used if not overwritten with the setLabelText() method
        labelToDisplay.setText("Wait...");
        exitImage.setPickOnBounds(false);
        exitImage.getStyleClass().add("exit-image");
    }


    /**
     * Method that sets the content of the main label at the top of the window to display
     * some information about the cause of the waiting.
     * @param textToDisplay text to be displayed inside the label at the top of the window
     */
    public void setLabelText(String textToDisplay) {
        labelToDisplay.setText(textToDisplay);
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
