package it.polimi.ingsw.PSP43.client.gui.controllers.game_end;

import it.polimi.ingsw.PSP43.client.gui.controllers.AbstractController;
import it.polimi.ingsw.PSP43.server.networkMessages.EndGameMessage;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class EndController extends AbstractController {
    @FXML private ImageView homeButton;
    @FXML private Label cloudLabel;
    @FXML private Label homeButtonLabel;
    @FXML private ImageView centralImage;
    @FXML private AnchorPane anchor;

    private boolean buttonActive = true;

    @FXML
    private void initialize() {
        cloudLabel.setId("cloud-label");
        homeButtonLabel.setId("home-label");
        homeButton.setId("home-image");
    }

    /**
     * This method handles homeButton, it restarts the game
     */
    public void onHomeButtonClicked() {
        if(homeButton.isVisible()){
        super.handleExit();}
    }

    /**
     * This method shows the explication of the home button when you move on it
     */
    public void showHomeButtonLabel() {
        if(homeButton.isVisible())
            homeButtonLabel.setText("Go back home and start a new match!");
    }

    /**
     * This method hides the explication of the home button when you aren't on it
     */
    public void hideHomeButtonLabel() {
        if(homeButton.isVisible())
            homeButtonLabel.setText("");
    }

    /**
     * This method shows the end message arrived
     */
    public void setEndMessage(EndGameMessage message) {
        if(message.getEndGameHeader() == EndGameMessage.EndGameHeader.WINNER) {
            centralImage.setImage(new Image(getClass().getResource("/images/others/winner.png").toExternalForm()));
            cloudLabel.setText("You won the game!");
        }
        else if(message.getEndGameHeader() == EndGameMessage.EndGameHeader.LOSER){
            centralImage.setImage(new Image(getClass().getResource("/images/others/loser.png").toExternalForm()));
            cloudLabel.setText("You lost the game!");
        }
        else if(message.getEndGameHeader() == EndGameMessage.EndGameHeader.SERVER_CRASHED){
            homeButton.setVisible(false);
            homeButton.setDisable(true);
            homeButtonLabel.setVisible(false);

            centralImage.setImage(new Image(getClass().getResource("/images/others/server_down.png").toExternalForm()));
            cloudLabel.setText("The server is down!");
        }
        else{
            centralImage.setImage(new Image(getClass().getResource("/images/others/no_connection.png").toExternalForm()));
            cloudLabel.setText("You disconnected from the game!");
        }
    }
}
