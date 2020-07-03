package it.polimi.ingsw.PSP43.client.graphic.gui.controllers.game_init;

import it.polimi.ingsw.PSP43.client.graphic.gui.controllers.AbstractController;
import it.polimi.ingsw.PSP43.client.network.networkMessages.RegistrationMessage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;


public class NicknameChoiceController extends AbstractController {

    @FXML private TextField nicknameField;
    @FXML private Button confirmButton;
    @FXML private Label buttonPressedLabel;
    @FXML private ImageView exitImage;


    /**
     * Method called as soon as the controlled fxml file gets loaded, here used to set css ids and classes.
     */
    @FXML
    private void initialize() {
        confirmButton.getStyleClass().add("confirm-button");
        exitImage.setPickOnBounds(false);
        exitImage.getStyleClass().add("exit-image");

        nicknameField.getStyleClass().add("textField");

        nicknameField.setOnKeyPressed(ke -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                handleConfirmButton();
            }
        });
    }


    /**
     * Method that suggest the already in use nickname (if present) to the player,
     * compiling the nicknameField with that.
     */
    public void customInit() {
        if (getNick() != null) {
            nicknameField.setText(getNick());
        }
    }


    /**
     * Method that handles an event performed on the button to confirm the nickname.
     */
    @FXML
    private void handleConfirmButton() {

        confirmButton.setDisable(true);

        if (nicknameField.getText().isEmpty()) {
            buttonPressedLabel.setText("You must choose a nickname!");
            confirmButton.setDisable(false);
            return;
        }

        String nickname = nicknameField.getText();
        //MatchController.setNick(nickname);
        AbstractController.setNick(nickname);
        getClientBG().sendMessage(new RegistrationMessage(nickname));

    }


    /**
     * Method that sets the content of the label under the confirm button, used to notify the player
     * if some errors have occurred.
     * @param textToDisplay text to be displayed inside the label under the button
     */
    public void setLabelText(String textToDisplay) {
        buttonPressedLabel.setText(textToDisplay);
    }


    /**
     * Method that handles an event performed on the image to exit and return back to the home screen.
     */
    @FXML
    private void handleExitImage() {
        super.handleExit();
    }

}
