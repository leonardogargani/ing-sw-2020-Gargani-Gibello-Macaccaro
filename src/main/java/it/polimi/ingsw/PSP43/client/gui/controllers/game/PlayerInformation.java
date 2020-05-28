package it.polimi.ingsw.PSP43.client.gui.controllers.game;

import it.polimi.ingsw.PSP43.Color;
import it.polimi.ingsw.PSP43.server.controllers.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.model.Player;

public class PlayerInformation {
    private Player player;
    private AbstractGodCard god;
    private Color color;

    public PlayerInformation(Player player, AbstractGodCard god, Color color){
        this.player = player;
        this.god = god;
        this.color = color;
    }

    public Player getPlayer() {
        return player;
    }

    public AbstractGodCard getGod() {
        return god;
    }

    public Color getColor() {
        return color;
    }
}
