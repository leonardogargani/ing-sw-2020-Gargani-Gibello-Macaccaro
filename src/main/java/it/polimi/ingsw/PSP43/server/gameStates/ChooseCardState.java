package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.server.ClientListener;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.modelHandlers.CardsHandler;
import it.polimi.ingsw.PSP43.server.modelHandlers.PlayersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.CardAlreadyInUseException;
import it.polimi.ingsw.PSP43.server.networkMessages.ChosenCardMessage;
import it.polimi.ingsw.PSP43.server.networkMessages.NoticeMessage;

import java.util.ArrayList;

public class ChooseCardState extends TurnState {

    public ChooseCardState(GameSession gameSession) {
        super(gameSession);
    }

    public void initState() {
        // TODO : SEND TO ALL THE PLAYERS THAT WE ARE BEGINNING TO CHOOSE THE CARDS

        GameSession game = super.getGameSession();

        PlayersHandler playersHandler = game.getPlayersHandler();
        int latestPosition = playersHandler.getNumOfPlayers()-1;
        String nicknameLatestPlayer = playersHandler.getPlayer(latestPosition).getNickname();

        Player current = game.getCurrentPlayer();
        String nicknameCurrentPlayer = current.getNickname();

        CardsHandler cardsHandler = game.getCardsHandler();

        String str = "Choose a card:";
        ArrayList<AbstractGodCard> deckOfCards = game.getCardsHandler().getDeckOfAbstractGodCards();
        ClientListener receiver = game.getListenersHashMap().get(current.getNickname());
        // TODO : SEND TO THE CLIENT
        ChosenCardMessage receivedMessage = null;
        // TODO : RECEIVE FROM THE CLIENT

        cardsHandler.setCardToPlayer(nicknameCurrentPlayer, receivedMessage.getNameCardChosen());

        if (nicknameLatestPlayer.equals(nicknameCurrentPlayer)) {
            game.setCurrentState(game.getTurnMap().get(2));
            game.setCurrentPlayer(playersHandler.getPlayer(0));
            game.initNextState();
        }
        else {
            game.setCurrentPlayer(playersHandler.getNextPlayer(nicknameCurrentPlayer));
        }
        game.initNextState();
    }
}
