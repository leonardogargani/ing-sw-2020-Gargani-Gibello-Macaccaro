package it.polimi.ingsw.PSP43.client.gui.controllers.game;

import it.polimi.ingsw.PSP43.Color;
import it.polimi.ingsw.PSP43.server.controllers.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.networkMessages.PlayersListMessage;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;
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
    private String ownNick;
    private ArrayList<PlayerInformation> players;
    private int ownPlayer;

    public PlayersController(Label[] labels, ImageView[] images){
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


    public void showUpdate(PlayersListMessage playersListMessage){
        //search for a nick equal to ownNick and show his information
        players.clear();
        Map<Player, AbstractGodCard> cards = playersListMessage.getPlayers();
        Map<Player, Color> colors = playersListMessage.getColor();
        //take Player with equal name
        for(Player player : cards.keySet()){
            AbstractGodCard card = cards.get(player);
            Color color = colors.get(player);
            players.add(new PlayerInformation(player,card,color));
        }
        //TODO check if gods images are .png or .jpg (probably .png)
        //TODO rename workers images
        boolean player2BoxFree = true;
        for(int i = 0; i<players.size(); i++){
            if(players.get(i).getPlayer().getNickname().equals(ownNick))
            {
                //player1Nick already shown
                player1Card.setImage(new Image(getClass().getResource("/images/gods/"+ players.get(i).getGod().getGodName() +".png").toExternalForm()));
                player1Worker.setImage(new Image(getClass().getResource("/images/workers/"+ players.get(i).getColor() +".png").toExternalForm()));
                player1CardDescription.setText(players.get(i).getGod().getDescription());
                ownPlayer = i;
            }
            else if(player2BoxFree){
                player2Nick.setText(players.get(i).getPlayer().getNickname());
                player2Card.setImage(new Image(getClass().getResource("/images/gods/"+ players.get(i).getGod().getGodName() +".png").toExternalForm()));
                player2Worker.setImage(new Image(getClass().getResource("/images/workers/"+ players.get(i).getColor() +".png").toExternalForm()));
                player2BoxFree = false;
            }
            else {
                player3Nick.setText(players.get(i).getPlayer().getNickname());
                player3Card.setImage(new Image(getClass().getResource("/images/gods/"+ players.get(i).getGod().getGodName() +".png").toExternalForm()));
                player3Worker.setImage(new Image(getClass().getResource("/images/workers/"+ players.get(i).getColor() +".png").toExternalForm()));
            }
        }
    }

    public void setOwnNick(String ownNick) {
        this.ownNick = ownNick;
    }
}
