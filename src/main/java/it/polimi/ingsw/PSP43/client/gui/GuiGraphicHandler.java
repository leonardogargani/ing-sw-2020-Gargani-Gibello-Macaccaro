package it.polimi.ingsw.PSP43.client.gui;

import it.polimi.ingsw.PSP43.client.ClientBG;
import it.polimi.ingsw.PSP43.client.GraphicHandler;
import it.polimi.ingsw.PSP43.client.gui.controllers.game_end.EndController;
import it.polimi.ingsw.PSP43.client.gui.controllers.game_init.NicknameChoiceController;
import it.polimi.ingsw.PSP43.client.gui.controllers.game_init.PlayersNumberChoiceController;
import it.polimi.ingsw.PSP43.client.gui.controllers.game_init.ServerIPChoiceController;
import it.polimi.ingsw.PSP43.client.gui.controllers.game_init.WaitController;
import it.polimi.ingsw.PSP43.server.networkMessages.*;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class GuiGraphicHandler extends GraphicHandler {

    // this attribute is needed since an update method has to change the label of the controller
    private NicknameChoiceController nicknameChoiceController;


    /**
     * Non default constructor that sets the ClientBG attribute both in this class and
     * in the ServerIPChoiceController class.
     * @param clientBG clientBG of the current client
     */
    public GuiGraphicHandler(ClientBG clientBG) {
        super(clientBG);

        // Platform.startup(()->{}) starts the JavaFX runtime as soon as the GuiGraphicHandler constructor is invoked:
        // I need that runtime to be running before the GuiStarter class (which extends Application) is launched
        // (and the GuiGraphicHandler constructor is invoked before the Application.launch(GuiStarter.class))
        Platform.startup(() ->
        {
            try {

                // setting ClientBG attribute in the ServerIPChoiceController
                FXMLLoader loader1 = new FXMLLoader();
                loader1.setLocation(getClass().getResource("/FXML/game_init/serverIPChoice.fxml"));
                loader1.load();
                ServerIPChoiceController controller1 = loader1.getController();
                controller1.setClientBG(clientBG);

                // setting ClientBG attribute in the NicknameChoiceController
                FXMLLoader loader2 = new FXMLLoader();
                loader2.setLocation(getClass().getResource("/FXML/game_init/nicknameChoice.fxml"));
                loader2.load();
                NicknameChoiceController controller2 = loader2.getController();
                nicknameChoiceController = controller2;
                controller2.setClientBG(clientBG);

                // setting ClientBG attribute in the PlayersNumberChoiceController
                FXMLLoader loader3 = new FXMLLoader();
                loader3.setLocation(getClass().getResource("/FXML/game_init/playersNumberChoice.fxml"));
                loader3.load();
                PlayersNumberChoiceController controller3 = loader3.getController();
                controller3.setClientBG(clientBG);

                //TODO add settings for BoardController
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

    }


    /**
     * Method that accepts an fxml path and a variable number of css path so that they can be used
     * to set a new scene to the primary stage. This method must be used only when a message arrives
     * (because of the Platform.runLater() statement) and there is no need to change or set the
     * value/content of a JavaFX element (e.g.: arrival of a ChangeNickRequest message).
     * @param fxml path of the fxml file to be loaded
     * @param cssStylesheets paths of an arbitrary number of css files that contain the styles to be
     *                       applied to the fxml
     */
    private void loadToPrimaryStage(String fxml, String... cssStylesheets) {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxml));

        try {

            Stage stage = GuiStarter.getPrimaryStage();
            Scene scene = new Scene(loader.load());

            for (String stylesheet : cssStylesheets) {
                scene.getStylesheets().add(getClass().getResource(stylesheet).toExternalForm());
            }

            // run the specified Runnable on the JavaFX Application Thread at some unspecified time in the future
            Platform.runLater(() -> stage.setScene(scene));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * This method updates the cli board changing the symbol of a cell, based
     * on the fact that a worker can build on a cell.
     * @param cellMessage message containing the cell that has changed (a worker has built on it)
     */
    @Override
    public void updateBoardChange(CellMessage cellMessage) {
        // TODO implementation with JavaFX
    }


    /**
     * This method updates the graphics of the client displaying the message of the players number
     * request, waiting and sending the response through the ClientBG object.
     * @param request message containing the request for the number of the players
     */
    @Override
    public void updateMenuChange(PlayersNumberRequest request) {

        loadToPrimaryStage("/FXML/game_init/playersNumberChoice.fxml", "/CSS/game_init/style.css");

    }


    /**
     * This method updates the graphics of the client displaying the message of the initial cards
     * request, waiting and sending the response through the ClientBG object.
     * @param request message containing the request for the cards chosen for this game
     */
    @Override
    public void updateMenuChange(InitialCardsRequest request) {

        loadToPrimaryStage("/FXML/game_init/cardsChoice.fxml");


        // TODO display the gods contained in the request

    }


    /**
     * This method updates the graphics of the client displaying the message of the single card
     * request, waiting and sending the response through the ClientBG object.
     * @param request message containing the request for the card chosen by a player
     */
    @Override
    public void updateMenuChange(CardRequest request) {
        // TODO implementation with JavaFX
    }


    /**
     * This method updates the graphics of the client displaying the message of the workers color
     * request, waiting and sending the response through the ClientBG object.
     * @param request message containing the request for the color of player's workers
     */
    @Override
    public void updateMenuChange(WorkersColorRequest request) {
        // TODO implementation with JavaFX
    }


    /**
     * This method updates the graphics of the client displaying the message of the action
     * request, waiting and sending the response through the ClientBG object.
     * @param request message containing the request for the action a player wants to make
     */
    @Override
    public void updateMenuChange(ActionRequest request) {
        // TODO implementation with JavaFX
    }


    /**
     * This method updates the graphics of the client displaying the message of the generic request
     * that needs a boolean as a response, waiting and sending the response through the ClientBG object.
     * @param request message containing the generic boolean request
     */
    @Override
    public void updateMenuChange(RequestMessage request) {
        // TODO implementation with JavaFX
    }


    /**
     * This method updates the graphics of the client displaying the message of the end of the game.
     * @param message message that notifies that the client the game has ended
     */
    @Override
    public void updateMenuChange(EndGameMessage message) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/FXML/game_end/end.fxml"));

        try {
            Stage stage = GuiStarter.getPrimaryStage();
            Scene scene = new Scene(loader.load());
            EndController controller = loader.getController();
            controller.setEndMessage(message);

            scene.getStylesheets().add(getClass().getResource("/CSS/game_end/endStyle.css").toExternalForm());

            Platform.runLater(() -> {
                stage.setScene(scene);
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * This method updates the graphics of the client displaying the message of the request
     * for a change of the nick, since the chosen one is already in use.
     * @param request message that notifies the client that the nick he has just chosen
     *                is already taken
     */
    @Override
    public void updateMenuChange(ChangeNickRequest request) {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/FXML/game_init/nicknameChoice.fxml"));

        try {
            Stage stage = GuiStarter.getPrimaryStage();
            Scene scene = new Scene(loader.load());
            NicknameChoiceController controller = loader.getController();

            // since it is needed to set the text of the label, the method loadToPrimaryStage() can't be used
            controller.setLabelText("already in use, choose another nickname");

            scene.getStylesheets().add(getClass().getResource("/CSS/game_init/style.css").toExternalForm());

            Platform.runLater(() -> {
                stage.centerOnScreen();
                stage.setScene(scene);
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * This method updates the graphics of the client displaying the message at the top
     * of the screen (used to write that it's someone else's turn).
     * @param message message to be displayed at the top of the screen
     */
    @Override
    public void updateMenuChange(TextMessage message) {
        // TODO implementation with JavaFX
    }


    /**
     * This method updates the graphics of the client displaying players' nicknames,
     * the Gods they've chosen and their workers' color.
     * @param message message containing workers, their colors and the chosen gods
     */
    @Override
    public void updateMenuChange(PlayersListMessage message) {
        // TODO implementation with JavaFX
    }

    /**
     * This method updates the graphics of the client displaying, at the beginning of
     * the game, some useful information about the state of the game preparation.
     * @param message message to be displayed
     */
    @Override
    public void updateMenuChange(StartGameMessage message) {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/FXML/miscellaneous/wait.fxml"));
        try {
            Stage stage = GuiStarter.getPrimaryStage();
            Scene scene = new Scene(loader.load());

            WaitController controller = loader.getController();
            controller.setLabelText(message.getMessage());
            scene.getStylesheets().add(getClass().getResource("/CSS/game_init/style.css").toExternalForm());

            Platform.runLater(() -> {
                stage.setMinHeight(700);
                stage.setMinWidth(1000);
                stage.centerOnScreen();
                stage.setScene(scene);
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
