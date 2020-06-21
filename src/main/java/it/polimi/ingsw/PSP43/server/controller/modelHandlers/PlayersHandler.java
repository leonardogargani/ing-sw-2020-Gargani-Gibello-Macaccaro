package it.polimi.ingsw.PSP43.server.controller.modelHandlers;

import it.polimi.ingsw.PSP43.server.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This is a class intended to handle all the requests to store informations about players in game
 * (such as nickname) and the cards they own during the game;
 */
public class PlayersHandler {

    private final ArrayList<Player> gamePlayers;


    /**
     * Non default constructor that instantiates the gamePlayers attribute.
     */
    public PlayersHandler() {
        this.gamePlayers = new ArrayList<>();
    }


    /**
     * This method adds, if possible, a player's data (nickname) in the list of game players' data.
     * @param nick The identification of a player during the game
     */
    public void createNewPlayer(String nick) {
        gamePlayers.add(new Player(nick));
    }


    /**
     * This method returns player's data searched from the list of players' data.
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
     * This method returns the data of the player in the position specified in the argument of the method.
     * @param pos The position from which the data of a player are taken
     * @return The data of the player in the position specified
     */
    public Player getPlayer(int pos) {
        return gamePlayers.get(pos);
    }


    /**
     * This method returns the player following in order the one specified in the parameter of the method.
     * @param currentPlayer The string that represents the name of the current player
     * @return The data of the player following the current one
     */
    public Player getNextPlayer(String currentPlayer) {
        int pos = 0;
        for (Player p : gamePlayers) if (currentPlayer.equals(p.getNickname())) pos = gamePlayers.indexOf(p);
        if (pos < gamePlayers.size() - 1) return gamePlayers.get(pos+1);
        else return gamePlayers.get(0);
    }


    /**
     * This method returns the number of players in the game.
     * @return The number of players in the game
     */
    public int getNumOfPlayers() {
        return gamePlayers.size();
    }


    /**
     * This method delete the data of a player identified by a nickname from the list of players' data.
     * @param nick The identifier of a player during the game
     */
    public void deletePlayer(String nick) {
        gamePlayers.removeIf(p -> p.getNickname().equals(nick));
    }

    public List<String> getNickNames(String nickExcluded) {
        ArrayList<String> nicks = new ArrayList<>();

        for (int i = 0; i < getNumOfPlayers(); i++) {
            String currentNickName = getPlayer(i).getNickname();
            if (!(currentNickName.equals(nickExcluded))) {
                nicks.add(currentNickName);
            }
        }
        return Collections.unmodifiableList(new ArrayList<>(nicks));
    }
}
