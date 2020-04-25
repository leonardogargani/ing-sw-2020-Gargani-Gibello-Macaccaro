package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.client.networkMessages.ChoseCardMessage;
import it.polimi.ingsw.PSP43.server.ClientListener;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.modelHandlers.CardsHandler;
import it.polimi.ingsw.PSP43.server.modelHandlers.PlayersHandler;
import it.polimi.ingsw.PSP43.server.networkMessages.CardsMessage;
import it.polimi.ingsw.PSP43.server.networkMessages.TextMessage;

import java.io.IOException;
import java.util.ArrayList;

public class ChooseCardState extends TurnState {
    private ArrayList<AbstractGodCard> cardsAvailable;
    private static final int FIRSTPOSITION = 0;

    public ChooseCardState(GameSession gameSession) {
        super(gameSession);
    }

    public void initState() throws IOException, ClassNotFoundException {
        GameSession game = super.getGameSession();
        PlayersHandler playersHandler = game.getPlayersHandler();

        game.setCurrentPlayer(playersHandler.getPlayer(FIRSTPOSITION));
        Player current = game.getCurrentPlayer();

        TextMessage openingMessage = new TextMessage("You are going to choose a God Card to use during the game!");
        ArrayList<String> nicksExcluded = new ArrayList<>();
        nicksExcluded.add(current.getNickname());
        game.sendBroadCast(openingMessage, nicksExcluded);

        ArrayList<AbstractGodCard> available = game.getCardsHandler().getDeckOfAbstractGodCards();
        CardsMessage cardsRequest = new CardsMessage("Choose " + playersHandler.getNumOfPlayers() + " cards. You " +
                "will receive the latest not chosen by other players.", available);
        // TODO : I do not have a type response for this yet
        cardsAvailable = available;
        this.executeState();
    }

    public void executeState() throws IOException, ClassNotFoundException {
        GameSession game = super.getGameSession();
        PlayersHandler playersHandler = game.getPlayersHandler();
        CardsHandler cardsHandler = game.getCardsHandler();

        //int latestPosition = playersHandler.getNumOfPlayers()-1;
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
            ChoseCardMessage receivedMessage = null;
            // TODO : RECEIVE FROM THE CLIENT

            cardsHandler.setCardToPlayer(nicknameCurrentPlayer, receivedMessage.getCardName());
            cardsAvailable.remove(cardsHandler.getCardOwned(nicknameCurrentPlayer));

            game.setCurrentPlayer(playersHandler.getNextPlayer(nicknameCurrentPlayer));
        } while (!nicknameGodLikePlayer.equals(nicknameCurrentPlayer));

        findNextState();
    }

    public void findNextState() throws IOException, ClassNotFoundException {
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
