package it.polimi.ingsw.PSP43.client.gui.controllers;

import it.polimi.ingsw.PSP43.client.ClientBG;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;


public class ServerIPChoiceController {

    public Button confirmButton;
    public Label buttonPressedLabel;
    public TextField serverIPField;

    private static ClientBG clientBG;


    /**
     * Method that sets the ClientBG attribute of the controller, it will be invoked inside
     * the GuiGraphicHandler constructor so that the controller will have already the attribute set
     * once it will be utilized.
     * @param clientBG clientBG of the current client
     */
    public void setClientBG(ClientBG clientBG) {
        ServerIPChoiceController.clientBG = clientBG;
    }


    /**
     * Method that handles an event performed on the button to confirm the server IP.
     * @param event event performed on the button
     */
    @FXML
    private void handleConfirmButton(ActionEvent event) {

        if (serverIPField.getText().isEmpty()) {
            buttonPressedLabel.setText("You must write the IP address!");
            return;
        }

        String serverIP = serverIPField.getText();

        clientBG.setServerIP(serverIP);

        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/FXML/nicknameChoice.fxml"));

        try {
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
