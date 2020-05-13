package it.polimi.ingsw.PSP43.client.gui.controllers;

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

    @FXML
    private void handleConfirmButton(ActionEvent event) {

        if (serverIPField.getText().isEmpty()) {
            buttonPressedLabel.setText("You must write the IP address!");
            return;
        }

        String serverIP = serverIPField.getText();

        // TODO create a connection to the chosen IP address


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