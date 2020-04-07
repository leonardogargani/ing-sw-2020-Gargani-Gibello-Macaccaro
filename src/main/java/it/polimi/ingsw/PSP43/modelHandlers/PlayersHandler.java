package it.polimi.ingsw.PSP43.modelHandlers;

import it.polimi.ingsw.PSP43.model.*;
import it.polimi.ingsw.PSP43.modelHandlersException.*;

import java.util.ArrayList;

/**
 * This is a class intended to handle all the requests to store informations about players in game
 * (such as nickname) and the cards they own during the game;
 */
public class PlayersHandler {
    private ArrayList<Player> gamePlayers;

    /**
     * This method adds, if possible, a player's data (nickname) in the list of game players' data
     * @param nick The identification of a player during the game
     * @throws NicknameAlreadyInUseException if the nickname chosen by the player is already in use
     */
    public void createNewPlayer(String nick) throws NicknameAlreadyInUseException {
        for (Player p : gamePlayers)
            if (p.getNickname().equals(nick)) throw new NicknameAlreadyInUseException("We are sorry, " + nick + " is already in use.");
        gamePlayers.add(new Player(nick));
    }

    /**
     * This method returns player's data searched from the list of players' data
     * @param nick The identifier of a player during the game
     * @return The data of the player searched
     */
    public Player getPlayer(String nick) {
        for (Player p : gamePlayers) {
            if (p.getNickname().equals(nick)) return p;
        }
        return null;
    }

    /**
     * This method delete the data of a player identified by a nickname from the list of players' data
     * @param nick The identifier of a player during the game
     */
    public void deletePlayer(String nick) {
        gamePlayers.removeIf(p -> p.getNickname().equals(nick));
    }

    /**
     *
     * @param player The data stored about a player into the list of players
     * @param cardChosen The data of the card chosen by a player during the game
     * @throws CardAlreadyInUseException
     */
    public void setCardToPlayer(Player player, Card cardChosen) throws CardAlreadyInUseException {
        for (Player p : gamePlayers)
            if (p.getCard()!=null && p.getCard().getGodName().equals(cardChosen.getGodName())) throw new CardAlreadyInUseException("We are sorry, but the card " + cardChosen.getGodName() + " is already in use.");
        player.setCard(cardChosen);
    }


}
