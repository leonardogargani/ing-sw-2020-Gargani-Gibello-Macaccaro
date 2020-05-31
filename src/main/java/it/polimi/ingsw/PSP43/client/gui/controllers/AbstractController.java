package it.polimi.ingsw.PSP43.client.gui.controllers;

import it.polimi.ingsw.PSP43.client.ClientBG;
import it.polimi.ingsw.PSP43.client.ClientManager;
import it.polimi.ingsw.PSP43.client.gui.GuiStarter;
import it.polimi.ingsw.PSP43.client.networkMessages.LeaveGameMessage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public abstract class AbstractController {
    private static ClientBG clientBG;
    private static String nick;

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
        try {
            Stage stage = GuiStarter.getPrimaryStage();
            Scene scene = new Scene(loader.load());

            scene.getStylesheets().add(getClass().getResource("/CSS/game_init/home.css").toExternalForm());
            stage.setScene(scene);

            stage.setMinHeight(650);
            stage.setMinWidth(650);
            stage.setResizable(false);

            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
