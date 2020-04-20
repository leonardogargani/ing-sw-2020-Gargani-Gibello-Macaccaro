package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.server.ClientListener;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.modelHandlers.CardsHandler;
import it.polimi.ingsw.PSP43.server.modelHandlers.PlayersHandler;

import java.util.ArrayList;

public class ChooseCardState extends TurnState {
    private ArrayList<AbstractGodCard> cardsAvailable;
    private static final int FIRSTPOSITION = 0;

    public ChooseCardState(GameSession gameSession) {
        super(gameSession);
    }

    public void initState() {
        GameSession game = super.getGameSession();
        PlayersHandler playersHandler = game.getPlayersHandler();

        game.setCurrentPlayer(playersHandler.getPlayer(FIRSTPOSITION));
        Player current = game.getCurrentPlayer();

        AbstractGodCard[] deckOfCards = (AbstractGodCard[]) game.getCardsHandler().getDeckOfAbstractGodCards().toArray();
        NoticeMessage message = new NoticeMessage(game.getIdGame(), "Choose " + playersHandler.getNumOfPlayers() + " cards. You " +
                "will receive the latest not chosen by other players.");
        ClientListener receiver = game.getListenersHashMap().get(current.getNickname());
        // TODO : SEND TO THE PLAYER THE LIST OF CARDS TO CHOOSE AND PUT THEM INTO THE FIELD AVAILABLECARDS
        this.executeState();
    }

    public void executeState() {
        // TODO : SEND TO ALL THE PLAYERS THAT WE ARE BEGINNING TO CHOOSE THE CARDS
        GameSession game = super.getGameSession();
        PlayersHandler playersHandler = game.getPlayersHandler();
        CardsHandler cardsHandler = game.getCardsHandler();

        int latestPosition = playersHandler.getNumOfPlayers()-1;
        game.setCurrentPlayer(playersHandler.getPlayer(FIRSTPOSITION + 1));

        String nicknameGodLikePlayer = playersHandler.getPlayer(FIRSTPOSITION).getNickname();
        String nicknameCurrentPlayer;

        do {
            Player current = game.getCurrentPlayer();
            nicknameCurrentPlayer = current.getNickname();

            String message = "Choose a card:";
            ArrayList<AbstractGodCard> deckOfAvailableCards = cardsAvailable;
            ClientListener receiver = game.getListenersHashMap().get(current.getNickname());
            // TODO : SEND TO THE CLIENT
            ChosenCardMessage receivedMessage = null;
            // TODO : RECEIVE FROM THE CLIENT

            cardsHandler.setCardToPlayer(nicknameCurrentPlayer, receivedMessage.getNameCardChosen());
            cardsAvailable.remove(cardsHandler.getCardOwned(nicknameCurrentPlayer));

            game.setCurrentPlayer(playersHandler.getNextPlayer(nicknameCurrentPlayer));
        } while (!nicknameGodLikePlayer.equals(nicknameCurrentPlayer));

        findNextState();
    }

    public void findNextState() {
        GameSession game = super.getGameSession();
        int indexCurrentState;
        indexCurrentState = game.getTurnMap().indexOf(game.getCurrentState());
        game.setNextState(game.getTurnMap().get(indexCurrentState + 1 ));

        game.transitToNextState();
    }

    private ArrayList<AbstractGodCard> getCardsAvailable() {
        return cardsAvailable;
    }

    private void setCardsAvailable(ArrayList<AbstractGodCard> cardsAvailable) {
        this.cardsAvailable = cardsAvailable;
    }
}
