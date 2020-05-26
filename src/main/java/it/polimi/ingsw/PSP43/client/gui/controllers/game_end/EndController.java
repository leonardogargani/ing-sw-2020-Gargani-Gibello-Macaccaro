package it.polimi.ingsw.PSP43.client.gui.controllers.game_end;

import it.polimi.ingsw.PSP43.client.ClientManager;
import it.polimi.ingsw.PSP43.client.gui.GuiStarter;
import it.polimi.ingsw.PSP43.server.networkMessages.EndGameMessage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class EndController {
    @FXML
    ImageView homeButton;
    @FXML
    Label cloudLabel;
    @FXML
    Label homeButtonLabel;
    @FXML
    ImageView centralImage;

    @FXML
    private void initialize() {
        cloudLabel.getStyleClass().add("cloud-label");
        homeButtonLabel.getStyleClass().add("homeButton-label");
        homeButton.getStyleClass().add("home-ImageView");
    }

    /**
     * This method handles homeButton, it restarts the game
     */
    public void onHomeButtonClicked() {
        ClientManager clientManager = new ClientManager(2,false);
        Thread clientManagerThread = new Thread(clientManager);
        clientManagerThread.start();
        //load home.fxml
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/FXML/home.fxml"));

        try {

            Stage stage = GuiStarter.getPrimaryStage();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            //set dimension of the stage and set it to not resizable
            stage.setHeight(650);
            stage.setWidth(650);
            stage.setResizable(false);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method shows the explication of the home button when you move on it
     */
    public void showHomeButtonLabel() {
        homeButtonLabel.setText("Go back home and start a new match!");
    }

    /**
     * This method hides the explication of the home button when you aren't on it
     */
    public void hideHomeButtonLabel() {
        homeButtonLabel.setText("");
    }

    /**
     * This method shows the end message arrived
     */
    public void setEndMessage(EndGameMessage message) {
        if(message.getEndGameHeader() == EndGameMessage.EndGameHeader.WINNER) {
            centralImage.setImage(new Image(getClass().getResourceAsStream("winner.png")));
            cloudLabel.setText("You won the game!");
        }
        else if(message.getEndGameHeader() == EndGameMessage.EndGameHeader.LOSER){
            centralImage.setImage(new Image(getClass().getResourceAsStream("loser.png")));
            cloudLabel.setText("You lost the game!");
        }
        else{
            centralImage.setImage(new Image(getClass().getResourceAsStream("no_connection.png")));
            cloudLabel.setText("You disconnected from the game!");
        }
    }
}
