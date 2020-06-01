package it.polimi.ingsw.PSP43.client.gui.controllers.game_init;

import it.polimi.ingsw.PSP43.client.gui.GuiStarter;
import it.polimi.ingsw.PSP43.client.gui.controllers.AbstractController;
import it.polimi.ingsw.PSP43.client.networkMessages.LeaveGameMessage;
import it.polimi.ingsw.PSP43.client.networkMessages.StarterResponse;
import it.polimi.ingsw.PSP43.server.networkMessages.ChooseStarterMessage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;


public class StarterChoiceController extends AbstractController {

    @FXML private Button confirmButton;
    @FXML private ToggleGroup toggleGroup;
    @FXML private RadioButton player1Button;
    @FXML private RadioButton player2Button;
    @FXML private Label buttonPressedLabel;
    @FXML private ImageView exitImage;

    private List<String> playersList;


    /**
     * Method called as soon as the controlled fxml file gets loaded, here used to set css ids and classes
     */
    @FXML
    private void initialize() {
        confirmButton.getStyleClass().add("confirm-button");
        exitImage.setPickOnBounds(false);
        exitImage.getStyleClass().add("exit-image");
    }


    /**
     * Method that, given a message from the server requesting the choice of the player who will be the first
     * in the game, displays the names of the two players it is possible to choose between.
     * @param request request message sent by the server
     */
    public void customInit(ChooseStarterMessage request) {
        setPlayersList(request.getPlayersList());
        player1Button.setText(playersList.get(0));
        player2Button.setText(playersList.get(1));
    }


    /**
     * Method that sets the attribute of the controller containing the list of all the opponents that
     * it is possible to choose between as the first player in the game.
     * @param playersList list of all the names of the opponents
     */
    private void setPlayersList(List<String> playersList) {
        this.playersList = playersList;
    }


    /**
     * Method that handles an event performed on the button to confirm the number of players.
     */
    @FXML
    private void handleConfirmButton() {

        confirmButton.setDisable(true);

        String chosenPlayerName;

        if (toggleGroup.getSelectedToggle() == null) {
            buttonPressedLabel.setText("You must choose a player!");
            confirmButton.setDisable(false);
            return;
        }

        RadioButton selectedButton = (RadioButton) toggleGroup.getSelectedToggle();

        if (player1Button.equals(selectedButton)) {
            chosenPlayerName = player1Button.getText();
        } else {
            chosenPlayerName = player2Button.getText();
        }

        StarterResponse response = new StarterResponse(chosenPlayerName);
        getClientBG().sendMessage(response);

        // display a waiting screen while other players are connecting
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/FXML/miscellaneous/wait.fxml"));
        try {
            Stage stage = GuiStarter.getPrimaryStage();
            Scene scene = new Scene(loader.load());

            WaitController controller = loader.getController();
            controller.setLabelText("");

            scene.getStylesheets().add(getClass().getResource("/CSS/game_init/style.css").toExternalForm());
            stage.setScene(scene);

            // set minimum sizes of the stage (can be either before and after loader.load())
            stage.setMinHeight(700);
            stage.setMinWidth(1000);

            // set the stage to the center of the screen (must be after loader.load())
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Method that handles an event performed on the image to exit and return back to the home screen.
     */
    @FXML
    private void handleExitImage() {
        getClientBG().sendMessage(new LeaveGameMessage());
        super.handleExit();
    }


}
