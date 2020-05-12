package it.polimi.ingsw.PSP43.client.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;


public class ModalityChoiceController {

    public RadioButton cliButton;
    public RadioButton guiButton;
    public Label buttonPressedLabel;
    public ToggleGroup toggleGroup;
    public Button confirmButton;

    @FXML
    private void handleConfirmButton(ActionEvent event) {

        /*
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/myexamples/FXML/sequence/colorChoice.fxml"));

        try {
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }

         */


        if (toggleGroup.getSelectedToggle() == null) {
            buttonPressedLabel.setText("You must choose a modality!");
        } else {
            RadioButton selectedButton = (RadioButton) toggleGroup.getSelectedToggle();
            if (cliButton.equals(selectedButton)) {
                buttonPressedLabel.setText(selectedButton.getText());
                // TODO look at the initialization as done in the cli
            } else if (guiButton.equals(selectedButton)) {
                buttonPressedLabel.setText(selectedButton.getText());
                // TODO look at the initialization as done in the cli
            } else {
                System.out.println("An error has occurred");
            }
        }


    }

}
