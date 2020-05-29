package it.polimi.ingsw.PSP43.client.gui.controllers.game;

import it.polimi.ingsw.PSP43.Color;
import it.polimi.ingsw.PSP43.client.gui.controllers.AbstractController;
import it.polimi.ingsw.PSP43.server.controllers.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.networkMessages.PlayersListMessage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Map;

public class PlayersController {
    //I can pass these parameters like a List of Label and a List of Imageview
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
    private static String ownNick;
    private ArrayList<PlayerInformation> players;

    public PlayersController(Label[] labels, ImageView[] images) {
        this.player1Nick = labels[0];
        this.player2Nick = labels[1];
        this.player3Nick = labels[2];
        this.player1CardDescription = labels[3];
        this.player1Card = images[0];
        this.player2Card = images[1];
        this.player3Card = images[2];
        this.player1Worker = images[3];
        this.player2Worker = images[4];
        this.player3Worker = images[5];
    }


    public void showUpdate(PlayersListMessage playersListMessage) {
        //search for a nick equal to ownNick and show his information
        clearImages();
        players.clear();
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

        player1Card.setImage(new Image(getClass().getResource("/images/gods/" + players.get(0).getGod().getGodName() + ".png").toExternalForm()));
        player1Worker.setImage(new Image(getClass().getResource("/images/workers/worker_" + players.get(0).getColor() + "_1.png").toExternalForm()));
        player1CardDescription.setText(players.get(0).getGod().getDescription());

        player2Nick.setText(players.get(1).getPlayer().getNickname());
        player2Card.setImage(new Image(getClass().getResource("/images/gods/" + players.get(1).getGod().getGodName() + ".png").toExternalForm()));
        player2Worker.setImage(new Image(getClass().getResource("/images/workers/worker_" + players.get(1).getColor() + "_1.png").toExternalForm()));

        if (players.size() == 3) {
            player3Nick.setText(players.get(2).getPlayer().getNickname());
            player3Card.setImage(new Image(getClass().getResource("/images/gods/" + players.get(2).getGod().getGodName() + ".png").toExternalForm()));
            player3Worker.setImage(new Image(getClass().getResource("/images/workers/worker_" + players.get(2).getColor() + "_1.png").toExternalForm()));
        }
    }

    public void showGod(ImageView source) {
        Stage godStage = new Stage();
        AnchorPane pane = new AnchorPane();
        pane.getStyleClass().add("/CSS/game/god_descriptions.css");
        godStage.setScene(new Scene(pane));
        if (source == player2Card) {
            pane.setId(players.get(1).getGod().getGodName() + "-pane");
        } else if(players.size() == 3 & source == player3Card){
            pane.setId(players.get(2).getGod().getGodName() + "-pane");
        }

        godStage.setWidth(800);
        godStage.setHeight(400);
        godStage.setResizable(false);
        godStage.show();
        godStage.setAlwaysOnTop(true);
        godStage.centerOnScreen();
    }

    public void clearImages() {
        player2Nick.setText("");
        player2Card.setImage(null);
        player2Worker.setImage(null);
        player3Nick.setText("");
        player3Card.setImage(null);
        player3Worker.setImage(null);
    }

    public void setOwnNick(String ownNick) {
        PlayersController.ownNick = ownNick;
        player1Nick.setText(ownNick);
    }
}
