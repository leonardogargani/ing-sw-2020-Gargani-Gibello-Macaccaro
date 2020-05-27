package it.polimi.ingsw.PSP43.client.gui.controllers.game_init;

import it.polimi.ingsw.PSP43.client.ClientBG;
import it.polimi.ingsw.PSP43.client.networkMessages.RegistrationMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class NicknameChoiceController {

    public TextField nicknameField;
    public Button confirmButton;
    public Label buttonPressedLabel;

    private static ClientBG clientBG;


    /**
     * Method called as soon as the controlled fxml file gets loaded, here used to set css ids and classes.
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
    public static void setClientBG(ClientBG clientBG) {
        NicknameChoiceController.clientBG = clientBG;
    }


    /**
     * Method that handles an event performed on the button to confirm the nickname.
     * @param event event performed on the button
     */
    @FXML
    public void handleConfirmButton(ActionEvent event) {

        if (nicknameField.getText().isEmpty()) {
            buttonPressedLabel.setText("You must choose a nickname!");
            return;
        }

        String nickname = nicknameField.getText();

        clientBG.sendMessage(new RegistrationMessage(nickname));

        //buttonPressedLabel.setText("nickname not in use (work in progress)");


        /*
        Dovrò fare, in ordine, le seguenti operazioni:
        - invio il RegistrationMessage   [fatto a riga 44]
        - scrivi nella label "wait..."
        - termina il metodo: verrà poi eseguita un update che notificherà l'arrivo di uno dei due seguenti casi
        - ChangeNickRequest -> nick non valido, richiedi cambiamento (sono rimasto nella schermata  di inserimento nel frattempo)
        - StartGameMessage -> stampa messaggio a tutto schermo in una nuova Scene con icona di caricamento
         */

    }


    /**
     * Method that sets the content of the label under the confirm button, used to notify the player
     * if some errors have occurred.
     * @param textToDisplay text to be displayed inside the label under the button
     */
    public void setLabelText(String textToDisplay) {
        buttonPressedLabel.setText(textToDisplay);
    }

}
