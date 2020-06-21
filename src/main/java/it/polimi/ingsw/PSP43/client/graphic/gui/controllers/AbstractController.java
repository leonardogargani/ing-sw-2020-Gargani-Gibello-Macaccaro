package it.polimi.ingsw.PSP43.client.graphic.gui.controllers;

import it.polimi.ingsw.PSP43.client.graphic.gui.GuiStarter;
import it.polimi.ingsw.PSP43.client.network.ClientBG;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public abstract class AbstractController {
    private static ClientBG clientBG;
    private static String nick = null;

    public static void setClientBG(ClientBG clientBG) {
        AbstractController.clientBG = clientBG;
    }

    public static ClientBG getClientBG() { return clientBG; }

    public static void setNick(String nick) {
        AbstractController.nick = nick;
    }

    public static String getNick() {
        return nick;
    }

    public void handleExit() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/FXML/game_init/home.fxml"));

        Stage stage;
        try {
            stage = GuiStarter.getPrimaryStage();
            Scene scene = new Scene(loader.load());

            Stage newStage = new Stage();
            GuiStarter.setPrimaryStage(newStage);

            //newStage.setHeight(650);
            //newStage.setWidth(650);
            newStage.setMinHeight(650);
            newStage.setMinWidth(650);
            newStage.setResizable(false);
            newStage.centerOnScreen();

            newStage.setScene(scene);
            scene.getStylesheets().add(getClass().getResource("/CSS/game_init/home.css").toExternalForm());
            stage.close();
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
