package it.polimi.ingsw.PSP43.server.modelHandlers;

import it.polimi.ingsw.PSP43.server.GraphicObserver;
import it.polimi.ingsw.PSP43.server.Observable;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.*;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlersException.*;

import java.util.ArrayList;

/**
 * This is a class intended to handle all the requests to store informations about players in game
 * (such as nickname) and the cards they own during the game;
 */
public class PlayersHandler extends Observable {
    private ArrayList<Player> gamePlayers;
    private GraphicObserver graphicObserver;

    public PlayersHandler(GameSession gameSession) {
        this.graphicObserver = gameSession.getGraphicObserver();
        this.gamePlayers = new ArrayList<Player>();
    }

    /**
     * This method adds, if possible, a player's data (nickname) in the list of game players' data
     * @param nick The identification of a player during the game
     * @throws NicknameAlreadyInUseException if the nickname chosen by the player is already in use
     */
    public void createNewPlayer(String nick) throws NicknameAlreadyInUseException {
        for (Player p : gamePlayers)
            if (p.getNickname().equals(nick)) throw new NicknameAlreadyInUseException("We are sorry, " + nick + " is already in use.");
        gamePlayers.add(new Player(nick));
        // TODO call here notifyMenuChange() once it is implemented
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
     * This method returns the data of the player in the position specified in the argument of the method
     * @param pos The position from which the data of a player are taken
     * @return The data of the player in the position specified
     */
    public Player getPlayer(int pos) {
        return gamePlayers.get(pos);
    }

    /**
     * This method returns the player following in order the one specified in the parameter of the method
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
     * This method returns the number of players in the game
     * @return The number of players in the game
     */
    public int getNumOfPlayers() {
        return gamePlayers.size();
    }

    /**
     * This method delete the data of a player identified by a nickname from the list of players' data
     * @param nick The identifier of a player during the game
     */
    public void deletePlayer(String nick) {
        gamePlayers.removeIf(p -> p.getNickname().equals(nick));
        // TODO call here notifyMenuChange() once it is implemented
    }

    /**
     *
     * @param player The data stored about a player into the list of players
     * @param abstractGodCardChosen The data of the card chosen by a player during the game
     * @throws CardAlreadyInUseException
     */
    public void setCardToPlayer(Player player, AbstractGodCard abstractGodCardChosen) throws CardAlreadyInUseException {
        for (Player p : gamePlayers)
            if (p.getAbstractGodCard()!=null && p.getAbstractGodCard().getGodName().equals(abstractGodCardChosen.getGodName())) throw new CardAlreadyInUseException("We are sorry, but the card " + abstractGodCardChosen.getGodName() + " is already in use.");
        player.setAbstractGodCard(abstractGodCardChosen);
    }


}