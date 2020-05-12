package it.polimi.ingsw.PSP43.client.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/FXML/modalityChoice.fxml"));

        try {
            primaryStage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(600);
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }

}
