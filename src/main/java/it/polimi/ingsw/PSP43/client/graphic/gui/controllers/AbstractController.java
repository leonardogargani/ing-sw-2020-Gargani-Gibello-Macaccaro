package it.polimi.ingsw.PSP43.client.graphic.gui.controllers;

import it.polimi.ingsw.PSP43.client.graphic.gui.GuiStarter;
import it.polimi.ingsw.PSP43.client.graphic.gui.controllers.game_end.EndController;
import it.polimi.ingsw.PSP43.client.network.ClientBG;
import it.polimi.ingsw.PSP43.server.network.networkMessages.EndGameMessage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public abstract class AbstractController {
    private static ClientBG clientBG;
    private static String nick = null;

    /**
     * Static setter method for the clientBg.
     * @param clientBG is the network handler for this player
     */
    public static void setClientBG(ClientBG clientBG) {
        AbstractController.clientBG = clientBG;
    }

    /**
     * Static getter method for the clientBg.
     * @return the clientBg of this player
     */
    public static ClientBG getClientBG() { return clientBG; }


    /**
     * Static setter method for save the nickname of a player.
     * @param nick is the String variable containing the nickname of this player
     */
    public static void setNick(String nick) {
        AbstractController.nick = nick;
    }

    /**
     * Static getter method for the nickname.
     * @return the nickname of the player
     */
    public static String getNick() {
        return nick;
    }

    /**
     * Event catcher method called when during the match the player clicks the exit button.
     */
    public void handleExit() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/FXML/game_init/home.fxml"));

        Stage stage;
        try {
            stage = GuiStarter.getPrimaryStage();
            Scene scene = new Scene(loader.load());

            Stage newStage = new Stage();
            GuiStarter.setPrimaryStage(newStage);

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

    /**
     * This method is used to set a specific size to the stage.
     * @param fxml The fxml to load.
     * @param stylesheet The stylesheet to load.
     * @param width The width of the window.
     * @param height The height of the window.
     * @param message The message used to fill optional fields in some cases.
     */
    public static void setSizeStage(String fxml, String stylesheet, int width, int height, EndGameMessage message) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AbstractController.class.getResource(fxml));

        Stage stage;
        try {
            stage = GuiStarter.getPrimaryStage();
            Scene scene = new Scene(loader.load());

            Stage newStage = new Stage();
            GuiStarter.setPrimaryStage(newStage);

            newStage.setMinHeight(height);
            newStage.setMinWidth(width);
            newStage.setResizable(true);
            newStage.centerOnScreen();

            newStage.setScene(scene);
            scene.getStylesheets().add(AbstractController.class.getResource(stylesheet).toExternalForm());
            stage.close();
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (message!=null && loader.getController() instanceof EndController) {
            EndController endController = loader.getController();
            endController.setEndMessage(message);
        }
    }
}
