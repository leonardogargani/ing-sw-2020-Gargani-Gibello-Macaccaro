package it.polimi.ingsw.PSP43.client.gui.controllers.game_init;

import it.polimi.ingsw.PSP43.client.gui.GuiStarter;
import it.polimi.ingsw.PSP43.client.gui.controllers.AbstractController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import java.io.IOException;


public class ServerIPChoiceController extends AbstractController {

    @FXML private Button confirmButton;
    @FXML private Label buttonPressedLabel;
    @FXML private TextField serverIPField;


    /**
     * Method called as soon as the controlled fxml file gets loaded, here used to set css ids and classes.
     */
    @FXML
    private void initialize() {
        confirmButton.getStyleClass().add("confirm-button");

        serverIPField.setOnKeyPressed(ke -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                handleConfirmButton();
            }
        });
    }


    /**
     * Method that handles an event performed on the button to confirm the server IP.
     */
    @FXML
    private void handleConfirmButton() {

        confirmButton.setDisable(true);

        if (serverIPField.getText().isEmpty()) {
            buttonPressedLabel.setText("You must write the IP address!");
            return;
        }

        String serverIP = serverIPField.getText();
        getClientBG().setServerIP(serverIP);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/FXML/game_init/nicknameChoice.fxml"));

        try {

            Stage stage = GuiStarter.getPrimaryStage();
            Scene scene = new Scene(loader.load());

            NicknameChoiceController controller = loader.getController();
            controller.customInit();

            scene.getStylesheets().add(getClass().getResource("/CSS/game_init/style.css").toExternalForm());
            stage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
