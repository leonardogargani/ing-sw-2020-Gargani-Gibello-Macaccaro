package it.polimi.ingsw.PSP43.client.gui.controllers;

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

    private ClientBG clientBG;


    /**
     * Method that sets the ClientBG attribute of the controller, it will be invoked inside
     * the GuiGraphicHandler constructor so that the controller will have already the attribute set
     * once it will be utilized.
     * @param clientBG clientBG of the current client
     */
    public void setClientBG(ClientBG clientBG) {
        this.clientBG = clientBG;
    }


    @FXML
    private void handleConfirmButton(ActionEvent event) {

        if (nicknameField.getText().isEmpty()) {
            buttonPressedLabel.setText("You must choose a nickname!");
            return;
        }

        String nickname = nicknameField.getText();

        buttonPressedLabel.setText("work in progress");

        // TODO insert here:    clientBG.sendMessage(new RegistrationMessage(nickname));



        /*

        Dovrò fare, in ordine, le seguenti operazioni:
        - invio il RegistrationMessage   [fatto a riga 44]
        - scrivi nella label "wait..."
        - termina il metodo: verrà poi eseguita un update che notificherà l'arrivo di uno dei due seguenti casi
        - ChangeNickRequest -> nick non valido, richiedi cambiamento (sono rimasto nella schermata  di inserimento nel frattempo)
        - StartGameMessage -> stampa messaggio a tutto schermo in una nuova Scene con icona di caricamento

         */



        /*

        Per passare un oggetto ad un controller:
        - dopo aver fatto la load()...      [se non funziona scomponi  Scene scene = new Scene(loader.load());  in due istruzioni]
        - MyControllerType controller = loader.getController();
        - controller.setMyAttribute(myValue);
        --> nota: la classe MyController deve avere come metodo setMyAttribute(...) che setterà  MyAttributeClass myAttribute

         */



    }

}
