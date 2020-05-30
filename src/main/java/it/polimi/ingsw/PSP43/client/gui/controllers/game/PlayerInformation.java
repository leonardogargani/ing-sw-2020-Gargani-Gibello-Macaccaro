package it.polimi.ingsw.PSP43.client.gui.controllers.game;

import it.polimi.ingsw.PSP43.Color;
import it.polimi.ingsw.PSP43.server.controllers.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.model.Player;

public class PlayerInformation {
    private Player player;
    private AbstractGodCard god;
    private Color color;

    /**
     * Not default constructor for PlayerInformation
     * @param player is a Player
     * @param god is his god card
     * @param color is his chosen color
     */
    public PlayerInformation(Player player, AbstractGodCard god, Color color){
        this.player = player;
        this.god = god;
        this.color = color;
    }

    /**
     * Getter method for player variable
     * @return player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Getter method for god card variable
     * @return god
     */
    public AbstractGodCard getGod() {
        return god;
    }

    /**
     * Getter method for color
     * @return color
     */
    //useless
    public Color getColor() {
        return color;
    }

    public String getColorName(){
        if(color == Color.ANSI_RED)
            return "red";
        else if(color == Color.ANSI_BLUE)
            return "blue";
        else if(color == Color.ANSI_GREEN)
            return "green";
        else
            return null;
    }
}
