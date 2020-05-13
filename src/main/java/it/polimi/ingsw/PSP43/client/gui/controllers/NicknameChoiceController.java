package it.polimi.ingsw.PSP43.client.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class NicknameChoiceController {

    public TextField nicknameField;
    public Button confirmButton;
    public Label buttonPressedLabel;

    @FXML
    private void handleConfirmButton(ActionEvent event) {

        if (nicknameField.getText().isEmpty()) {
            buttonPressedLabel.setText("You must choose a nickname!");
            return;
        }

        String nickname = nicknameField.getText();

        buttonPressedLabel.setText("ok");


        // TODO send a RegistrationMessage and look at the initialization as done in the cli


    }

}
