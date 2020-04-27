package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.client.networkMessages.ChosenCardResponse;
import it.polimi.ingsw.PSP43.client.networkMessages.ChosenCardsResponse;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.modelHandlers.CardsHandler;
import it.polimi.ingsw.PSP43.server.modelHandlers.PlayersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;
import it.polimi.ingsw.PSP43.server.networkMessages.CardRequest;
import it.polimi.ingsw.PSP43.server.networkMessages.TextMessage;

import java.io.IOException;
import java.util.ArrayList;

public class ChooseCardState extends TurnState {
    private ArrayList<AbstractGodCard> cardsAvailable;
    private static final int FIRSTPOSITION = 0;

    public ChooseCardState(GameSession gameSession) {
        super(gameSession);
    }

    public void initState() throws IOException, ClassNotFoundException, WinnerCaughtException {
        GameSession game = super.getGameSession();
        PlayersHandler playersHandler = game.getPlayersHandler();

        game.setCurrentPlayer(playersHandler.getPlayer(FIRSTPOSITION));
        Player current = game.getCurrentPlayer();

        TextMessage openingMessage = new TextMessage("You are going to choose a God Card to use during the game in some minutes!");
        ArrayList<String> nicksExcluded = new ArrayList<>();
        nicksExcluded.add(current.getNickname());
        game.sendBroadCast(openingMessage, nicksExcluded);

        ArrayList<AbstractGodCard> available = game.getCardsHandler().getDeckOfAbstractGodCards();
        CardRequest cardsRequest = new CardRequest("Choose " + playersHandler.getNumOfPlayers() + " cards. You " +
                "will receive the latest not chosen by other players.", available);
        ChosenCardsResponse responseCardMessage = null;
        boolean delivered;
        do {
            delivered = game.sendRequest(cardsRequest, game.getCurrentPlayer().getNickname(), responseCardMessage);
        } while (!delivered);
        // TODO : message class not yet well implemented
        cardsAvailable = responseCardMessage.getCardsName();
        this.executeState();
    }

    public void executeState() throws IOException, ClassNotFoundException, WinnerCaughtException {
        GameSession game = super.getGameSession();
        PlayersHandler playersHandler = game.getPlayersHandler();
        CardsHandler cardsHandler = game.getCardsHandler();

        game.setCurrentPlayer(playersHandler.getPlayer(FIRSTPOSITION + 1));

        String nicknameGodLikePlayer = playersHandler.getPlayer(FIRSTPOSITION).getNickname();
        String nicknameCurrentPlayer;

        do {
            Player current = game.getCurrentPlayer();
            nicknameCurrentPlayer = current.getNickname();

            CardRequest request = new CardRequest("Choose a card:", cardsAvailable);
            ChosenCardResponse response = null;
            boolean delivered;
            do {
                delivered = game.sendRequest(request, nicknameCurrentPlayer, response);
            } while (!delivered);

            cardsHandler.setCardToPlayer(nicknameCurrentPlayer, response.getCard().getGodName());
            cardsAvailable.remove(response.getCard());

            game.setCurrentPlayer(playersHandler.getNextPlayer(nicknameCurrentPlayer));
        } while (!nicknameGodLikePlayer.equals(nicknameCurrentPlayer));

        findNextState();
    }

    public void findNextState() throws IOException, ClassNotFoundException, WinnerCaughtException {
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
