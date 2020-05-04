package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.client.networkMessages.ChosenCardResponse;
import it.polimi.ingsw.PSP43.client.networkMessages.ChosenCardsResponse;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.modelHandlers.CardsHandler;
import it.polimi.ingsw.PSP43.server.modelHandlers.PlayersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;
import it.polimi.ingsw.PSP43.server.networkMessages.CardRequest;
import it.polimi.ingsw.PSP43.server.networkMessages.InitialCardsRequest;
import it.polimi.ingsw.PSP43.server.networkMessages.StartGameMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * This is the State of the game where the players are asked to choose the cards
 * they will own during the game.
 */
public class ChooseCardState extends TurnState {
    protected ArrayList<AbstractGodCard> cardsAvailable;
    private static final int FIRSTPOSITION = 0;

    public ChooseCardState(GameSession gameSession) {
        super(gameSession);
    }

    /**
     * This method initialises the god-like player, it asks him to pick up some cards
     * to use during the game and sends a message to all the other players telling them to wait
     * several minutes to choose the card they will own.
     */
    public void initState() throws IOException, ClassNotFoundException, WinnerCaughtException, InterruptedException {
        GameSession game = super.getGameSession();
        PlayersHandler playersHandler = game.getPlayersHandler();

        game.setCurrentPlayer(playersHandler.getPlayer(FIRSTPOSITION));
        Player current = game.getCurrentPlayer();

        StartGameMessage openingMessage = new StartGameMessage("You are going to choose a God Card to use during the game in some minutes!");
        ArrayList<String> nicksExcluded = new ArrayList<>();
        nicksExcluded.add(current.getNickname());
        game.sendBroadCast(openingMessage, nicksExcluded);

        ArrayList<AbstractGodCard> available = game.getCardsHandler().getDeckOfAbstractGodCards();
        InitialCardsRequest cardsRequest = new InitialCardsRequest("Choose " + playersHandler.getNumOfPlayers() + " cards. You " +
                "will receive the latest not chosen by other players.", available, game.getListenersHashMap().size());
        ChosenCardsResponse responseCardMessage;
        do {
            responseCardMessage = game.sendRequest(cardsRequest, game.getCurrentPlayer().getNickname(), ChosenCardsResponse.class);
        } while (responseCardMessage==null);
        cardsAvailable = responseCardMessage.getCardsName();
        this.executeState();
    }

    /**
     * This method picks up the next player to the god-like in the list where they are saved
     * and calls a method that, one at a time, asks to all the players which card they want to
     * use during the game. The latest card will be assigned to the god-like player.
     */
    public void executeState() throws IOException, ClassNotFoundException, WinnerCaughtException, InterruptedException {
        GameSession game = super.getGameSession();
        PlayersHandler playersHandler = game.getPlayersHandler();

        game.setCurrentPlayer(playersHandler.getPlayer(FIRSTPOSITION + 1));

        askForCards(game);

        this.findNextState();
    }

    /**
     * This method asks to all the participants to the game to choose a card they want to use during
     * the game.
     * @param gameSession This is a reference to the center of the gameSession database.
     */
    private void askForCards(GameSession gameSession) throws IOException, ClassNotFoundException, InterruptedException {
        PlayersHandler playersHandler = gameSession.getPlayersHandler();
        String nicknameGodLikePlayer = playersHandler.getPlayer(FIRSTPOSITION).getNickname();
        String nicknameCurrentPlayer;
        CardsHandler cardsHandler = gameSession.getCardsHandler();

        do {
            Player current = gameSession.getCurrentPlayer();
            nicknameCurrentPlayer = current.getNickname();

            CardRequest request = new CardRequest("Choose a card:", cardsAvailable);
            ChosenCardResponse response;
            do {
                response = gameSession.sendRequest(request, nicknameCurrentPlayer, ChosenCardResponse.class);
            } while (response == null);

            current.setAbstractGodCard(response.getCard());
            cardsHandler.setCardToPlayer(nicknameCurrentPlayer, response.getCard().getGodName());

            for (Iterator<AbstractGodCard> cardsIterator = cardsAvailable.iterator(); cardsIterator.hasNext(); ) {
                AbstractGodCard card = cardsIterator.next();
                if (card.getGodName().equals(response.getCard().getGodName())) cardsIterator.remove();
            }

            gameSession.setCurrentPlayer(playersHandler.getNextPlayer(nicknameCurrentPlayer));
        } while (!nicknameGodLikePlayer.equals(nicknameCurrentPlayer));
    }

    /**
     * Finds the next state for the game, saving it in a variable in GameSession, and calls on the
     * instance of GameSession the method to transit to the next state of play.
     */
    public void findNextState() throws IOException, ClassNotFoundException, WinnerCaughtException, InterruptedException {
        GameSession game = super.getGameSession();
        int indexCurrentState;
        indexCurrentState = game.getTurnMap().indexOf(game.getCurrentState());
        game.setNextState(game.getTurnMap().get(indexCurrentState + 1));

        game.transitToNextState();
    }
}
