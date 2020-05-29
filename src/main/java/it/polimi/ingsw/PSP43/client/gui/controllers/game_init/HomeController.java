package it.polimi.ingsw.PSP43.client.gui.controllers.game_init;

import it.polimi.ingsw.PSP43.client.gui.GuiStarter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.io.IOException;


public class HomeController {

    @FXML private ImageView playImage;


    /**
     * Method called as soon as the controlled fxml file gets loaded, here used to set css ids and classes.
     */
    @FXML
    private void initialize() {
        playImage.setId("play-image");
        playImage.setPickOnBounds(false);
    }


    /**
     * Method that handles a mouse event performed on the image to play a game.
     */
    @FXML
    private void handlePlayImage() {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/FXML/game_init/serverIPChoice.fxml"));

        try {

            Stage stage = GuiStarter.getPrimaryStage();
            Scene scene = new Scene(loader.load());

            scene.getStylesheets().add(getClass().getResource("/CSS/game_init/style.css").toExternalForm());

            stage.setMinHeight(400);
            stage.setMinWidth(600);
            stage.setResizable(true);
            stage.setScene(scene);
            stage.centerOnScreen();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
