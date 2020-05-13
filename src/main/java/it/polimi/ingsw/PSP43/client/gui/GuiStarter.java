package it.polimi.ingsw.PSP43.client.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;


public class GuiStarter extends Application {


    /**
     * First method to be executed, it loads the FXML file with the choice between cli and gui.
     * @param primaryStage stage automatically generated from JavaFX
     */
    @Override
    public void start(Stage primaryStage) {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/FXML/serverIPChoice.fxml"));

        try {
            Scene scene = new Scene(loader.load());
            //scene.getStylesheets().add(getClass().getResource("/CSS/style.css").toExternalForm());
            primaryStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // set the minimum height and width so that the window cannot be resized to lower values
        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(600);

        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }

}
