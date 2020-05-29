package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.client.Screens;
import it.polimi.ingsw.PSP43.client.networkMessages.ChosenCardResponse;
import it.polimi.ingsw.PSP43.client.networkMessages.ChosenCardsResponse;
import it.polimi.ingsw.PSP43.server.controllers.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.modelHandlers.CardsHandler;
import it.polimi.ingsw.PSP43.server.modelHandlers.PlayersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.networkMessages.CardRequest;
import it.polimi.ingsw.PSP43.server.networkMessages.InitialCardsRequest;
import it.polimi.ingsw.PSP43.server.networkMessages.StartGameMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * This is the State of the game where the players are asked to choose the cards
 * they will own during the game.
 */
public class ChooseCardState extends TurnState {
    protected ArrayList<AbstractGodCard> cardsAvailable;
    private static final int FIRSTPOSITION = 0;

    public ChooseCardState(GameSession gameSession) {
        super(gameSession, TurnName.CHOOSE_CARD_STATE);
    }

    /**
     * This method initialises the god-like player, it asks him to pick up some cards
     * to use during the game and sends a message to all the other players telling them to wait
     * several minutes to choose the card they will own.
     */
    public void initState() {
        GameSession game = super.getGameSession();
        PlayersHandler playersHandler = game.getPlayersHandler();

        game.setCurrentPlayer(playersHandler.getPlayer(FIRSTPOSITION));
        Player current = game.getCurrentPlayer();

        StartGameMessage openingMessage = new StartGameMessage(Screens.CHOOSING_A_CARD.toString());
        game.sendBroadCast(openingMessage, current.getNickname());

        List<AbstractGodCard> available = game.getCardsHandler().getDeckOfAbstractGodCards();
        InitialCardsRequest cardsRequest = new InitialCardsRequest(available, game.getListenersHashMap().size());
        ChosenCardsResponse responseCardMessage;
        do {
            try {
                responseCardMessage = game.sendRequest(cardsRequest, game.getCurrentPlayer().getNickname(), ChosenCardsResponse.class);
            } catch (GameEndedException e) {
                game.setActive();
                return;
            }
        } while (responseCardMessage==null);
        cardsAvailable = responseCardMessage.getCardsName();

        StartGameMessage startGameMessage = new StartGameMessage("\nNow other players are choosing a card within the ones you selected.\n");
        game.sendMessage(startGameMessage, current.getNickname());

        this.executeState();
    }

    /**
     * This method picks up the next player to the god-like in the list where they are saved
     * and calls a method that, one at a time, asks to all the players which card they want to
     * use during the game. The latest card will be assigned to the god-like player.
     */
    public void executeState() {
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
    private void askForCards(GameSession gameSession) {
        PlayersHandler playersHandler = gameSession.getPlayersHandler();
        String nicknameGodLikePlayer = playersHandler.getPlayer(FIRSTPOSITION).getNickname();
        String nicknameCurrentPlayer;
        CardsHandler cardsHandler = gameSession.getCardsHandler();

        do {
            Player current = gameSession.getCurrentPlayer();
            nicknameCurrentPlayer = current.getNickname();

            CardRequest request = new CardRequest(Collections.unmodifiableList(cardsAvailable));
            ChosenCardResponse response;
            do {
                try {
                    response = gameSession.sendRequest(request, nicknameCurrentPlayer, ChosenCardResponse.class);
                } catch (GameEndedException e) {
                    gameSession.setActive();
                    return;
                }
            } while (response == null);

            cardsHandler.setCardToPlayer(nicknameCurrentPlayer, response.getCard().getGodName());

            for (Iterator<AbstractGodCard> cardsIterator = cardsAvailable.iterator(); cardsIterator.hasNext(); ) {
                AbstractGodCard card = cardsIterator.next();
                if (card.getGodName().equals(response.getCard().getGodName())) cardsIterator.remove();
            }

            if (!nicknameGodLikePlayer.equals(nicknameCurrentPlayer))
                gameSession.sendMessage(new StartGameMessage("\nPlease, wait until all the players will have chosen their card.\n"), current.getNickname());

            gameSession.setCurrentPlayer(playersHandler.getNextPlayer(nicknameCurrentPlayer));
        } while (!nicknameGodLikePlayer.equals(nicknameCurrentPlayer));
    }

    /**
     * This method finds the next turn of the game (saving it into a variable in the GameSession database),
     * which will be always a ChooseWorkerState.
     */
    public void findNextState() {
        GameSession game = super.getGameSession();

        for (TurnState t : game.getTurnStateMap()) {
            if (t.getTurnName() == TurnName.CHOOSE_WORKER_STATE)
                game.setNextState(t);
        }
    }
}