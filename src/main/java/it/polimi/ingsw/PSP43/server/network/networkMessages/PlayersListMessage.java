package it.polimi.ingsw.PSP43.server.network.networkMessages;

import it.polimi.ingsw.PSP43.server.model.Color;
import it.polimi.ingsw.PSP43.server.controller.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.controller.cards.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.CardsHandler;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.PlayersHandler;

import java.util.HashMap;
import java.util.Map;


public class PlayersListMessage extends TextMessage {

    private static final long serialVersionUID = 5488819383659184710L;
    private final Map<Player, AbstractGodCard> players;
    private final Map<Player, Color> color;


    /**
     * Not default constructor for PlayersListMessage.
     * @param message is the string that will be shown to the recipient
     * @param gameSession
     */
    public PlayersListMessage(String message, GameSession gameSession){
        super(message, TextMessage.PositionInScreen.LOW_CENTER);

        PlayersHandler playersHandler = gameSession.getPlayersHandler();
        CardsHandler cardsHandler = gameSession.getCardsHandler();
        HashMap<Player, AbstractGodCard> playerCardMap = new HashMap<>();

        for (int i = 0; i < playersHandler.getNumOfPlayers(); i++) {
            Player actualPlayer = playersHandler.getPlayer(i);
            AbstractGodCard actualCard = cardsHandler.getPlayerCard(actualPlayer.getNickname());
            playerCardMap.put(actualPlayer, actualCard);
        }

        HashMap<Player, Color> playerColorMap = new HashMap<>();
        for (int i = 0; i < playersHandler.getNumOfPlayers(); i++) {
            Player actualPlayer = playersHandler.getPlayer(i);
            Integer[] ids = actualPlayer.getWorkersIdsArray();
            Worker actualWorker = gameSession.getWorkersHandler().getWorker(ids[0]);
            playerColorMap.put(actualPlayer, actualWorker.getColor());
        }

        this.players = playerCardMap;
        this.color = playerColorMap;
    }


    /**
     * Getter method for the string message.
     * @return message
     */
    @Override
    public String getMessage() {
        return super.getMessage();
    }


    /**
     * Getter method for the Map players.
     * @return players
     */
    public Map<Player, AbstractGodCard> getPlayers() {
        return players;
    }


    /**
     * Getter method for the Map color.
     * @return color
     */
    public Map<Player, Color> getColor() {
        return color;
    }

}
