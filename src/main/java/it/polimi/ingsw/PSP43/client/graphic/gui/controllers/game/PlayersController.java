package it.polimi.ingsw.PSP43.client.graphic.gui.controllers.game;

import it.polimi.ingsw.PSP43.client.graphic.gui.controllers.AbstractController;
import it.polimi.ingsw.PSP43.server.model.Color;
import it.polimi.ingsw.PSP43.server.controller.cards.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.network.networkMessages.PlayersListMessage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class PlayersController {
    private Label player1Nick;
    private Label player2Nick;
    private Label player3Nick;
    private Label player1CardDescription;
    private ImageView player1Card;
    private ImageView player2Card;
    private ImageView player3Card;
    private ImageView player1Worker;
    private ImageView player2Worker;
    private ImageView player3Worker;
    private String ownNick;
    private ArrayList<PlayerInformation> players;

    /**
     * Not default constructor for the PlayersController class.
     * @param labels is an array containing the references of all the board's labels
     * @param images is an array containing the references of ImageViews helpful to show players' information
     * @param nick is a string content the nickname of the player
     */
    public PlayersController(Label[] labels, ImageView[] images, String nick) {
        //I can pass these parameters like a List of Label and a List of ImageView
        this.player1Nick = labels[0];
        this.ownNick = nick;
        this.player2Nick = labels[1];
        this.player3Nick = labels[2];
        this.player1CardDescription = labels[3];
        player1CardDescription.setTextAlignment(TextAlignment.LEFT);
        this.player1Card = images[0];
        this.player2Card = images[1];
        this.player3Card = images[2];
        this.player1Worker = images[3];
        this.player2Worker = images[4];
        this.player3Worker = images[5];
        players = new ArrayList<>();
        player3Nick.setVisible(false);
    }

    /**
     * This method updates information shown
     * @param playersListMessage contains information to show
     */
    public void showUpdate(PlayersListMessage playersListMessage) {
        //search for a nick equal to ownNick and show his information
        clearImages();
        if (players.size() > 0) {
            players.clear();
        }
        Map<Player, AbstractGodCard> cards = playersListMessage.getPlayers();
        Map<Player, Color> colors = playersListMessage.getColor();
        //take Player with equal name
        for (Player player : cards.keySet()) {
            AbstractGodCard card = cards.get(player);
            Color color = colors.get(player);
            if (player.getNickname().equals(ownNick))
                players.add(0, new PlayerInformation(player, card, color));
            else {
                players.add(new PlayerInformation(player, card, color));
            }
        }

        ownNick = AbstractController.getNick();
        player1Nick.setText(ownNick);
        player1Card.setImage(new Image(getClass().getResource("/images/cards/" + players.get(0).getGod().getGodName() + ".png").toExternalForm()));
        player1Worker.setImage(new Image(getClass().getResource("/images/workers/worker_" + players.get(0).getColorName() + "_1.png").toExternalForm()));
        player1CardDescription.setText(players.get(0).getGod().getDescription() + "\n\n" + players.get(0).getGod().getPower());

        player2Nick.setText(players.get(1).getPlayer().getNickname());
        player2Card.setImage(new Image(getClass().getResource("/images/cards/" + players.get(1).getGod().getGodName() + ".png").toExternalForm()));
        player2Worker.setImage(new Image(getClass().getResource("/images/workers/worker_" + players.get(1).getColorName() + "_1.png").toExternalForm()));

        if (players.size() == 3) {
            player3Nick.setVisible(true);
            player3Nick.setText(players.get(2).getPlayer().getNickname());
            player3Card.setImage(new Image(getClass().getResource("/images/cards/" + players.get(2).getGod().getGodName() + ".png").toExternalForm()));
            player3Worker.setImage(new Image(getClass().getResource("/images/workers/worker_" + players.get(2).getColorName() + "_1.png").toExternalForm()));
        }
    }

    /**
     * This method is called when the player clicks on an opponent's god card to show in another window the information
     * about the card
     * @param source is the god card selected
     */
    public void showGod(ImageView source) {
        Stage godStage = new Stage();
        AnchorPane pane = new AnchorPane();

        if (source == player2Card) {
            ImageView view = new ImageView(new Image(getClass().getResourceAsStream("/images/card_descriptions/"+players.get(1).getGod().getGodName()+"_description.png")));
            pane.getChildren().add(view);
        } else if(players.size() == 3 & source == player3Card){
            ImageView view = new ImageView(new Image(getClass().getResourceAsStream("/images/card_descriptions/"+players.get(2).getGod().getGodName()+"_description.png")));
            pane.getChildren().add(view);
        }

        godStage.setScene(new Scene(pane,800,400));
        godStage.setResizable(false);
        godStage.show();
        godStage.setAlwaysOnTop(true);
        godStage.centerOnScreen();
    }

    /**
     * This method is called when the player clicks on the helper icons, it shows in another window the rules of the game
     */
    public void showRules(){
        Stage rules = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/FXML/game/rules.fxml"));
        try {
            rules.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        rules.setWidth(1000);
        rules.setHeight(650);
        rules.setResizable(false);
        rules.setAlwaysOnTop(true);
        rules.show();
    }

    /**
     * This method is called during the match before showing of information arrived in a PlayersListMessage, it removes
     * old information
     */
    public void clearImages() {
        player2Nick.setText("");
        player2Card.setImage(null);
        player2Worker.setImage(null);
        player3Nick.setText("");
        player3Card.setImage(null);
        player3Worker.setImage(null);
    }

}
