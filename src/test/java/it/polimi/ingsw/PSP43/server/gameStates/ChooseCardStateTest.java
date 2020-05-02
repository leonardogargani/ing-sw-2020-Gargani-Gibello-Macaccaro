package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.client.networkMessages.ChosenCardResponse;
import it.polimi.ingsw.PSP43.client.networkMessages.ChosenCardsResponse;
import it.polimi.ingsw.PSP43.server.BoardObserver;
import it.polimi.ingsw.PSP43.server.initialisers.DOMCardsParser;
import it.polimi.ingsw.PSP43.server.initialisers.GameInitialiser;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;

public class ChooseCardStateTest {
    GameSession gameSession;
    GameSession spyGame;
    BoardObserver obs;
    BoardObserver spyObs;
    ArrayList<AbstractGodCard> deck;
    ChooseCardState state;
    ChooseCardState spyState;

    @Before
    public void setUp() throws Exception {
        gameSession = GameInitialiser.initialiseGame();
        gameSession = GameInitialiser.initialisePlayers(gameSession);
        spyGame = Mockito.spy(gameSession);
        obs = new BoardObserver();
        spyObs = Mockito.spy(obs);
        deck = DOMCardsParser.buildDeck();
        state = new ChooseCardState(spyGame);
        spyState = Mockito.spy(state);
    }

    @Test
    public void initState() throws ClassNotFoundException, IOException, WinnerCaughtException, InterruptedException {
        ChosenCardsResponse response = new ChosenCardsResponse(deck);

        Mockito.doReturn(response).when(spyGame).sendRequest(any(), any(), any());
        Mockito.doNothing().when(spyState).executeState();

        spyState.initState();
        boolean equals = true;
        if (spyState.cardsAvailable.size() == deck.size()) {
            for (int i = 0; i < spyState.cardsAvailable.size() - 1; i++) {
                if (!spyState.cardsAvailable.get(i).getGodName().equals(deck.get(i).getGodName())
                        || !spyState.cardsAvailable.get(i).getDescription().equals(deck.get(i).getDescription())
                        || !spyState.cardsAvailable.get(i).getPower().equals(deck.get(i).getPower())) {
                    equals = false;
                    break;
                }
            }
        } else equals = false;
        assertTrue(equals);
    }

    @Test
    public void executeState() throws ClassNotFoundException, IOException, WinnerCaughtException, InterruptedException {
        ArrayList<AbstractGodCard> initialAvailableCards = new ArrayList<>();
        ArrayList<AbstractGodCard> cardsToCheck = new ArrayList<>();
        initialAvailableCards.add(deck.get(0));
        initialAvailableCards.add(deck.get(1));
        cardsToCheck.add(deck.get(0));
        cardsToCheck.add(deck.get(1));
        spyState.cardsAvailable = initialAvailableCards;

        Mockito.doReturn(new ChosenCardResponse(spyState.cardsAvailable.get(0)))
                .doReturn(new ChosenCardResponse(spyState.cardsAvailable.get(1)))
                .when(spyGame).sendRequest(any(), any(), any());
        Mockito.doNothing().when(spyState).findNextState();

        spyState.executeState();

        boolean equals = true;
        AbstractGodCard godlikePlayerCard = cardsToCheck.get(cardsToCheck.size() - 1);
        AbstractGodCard effectivePlayerCard = spyGame.getPlayersHandler().getPlayer(0).getAbstractGodCard();

        if (!godlikePlayerCard.getGodName().equals(effectivePlayerCard.getGodName()) ||
                !godlikePlayerCard.getDescription().equals(effectivePlayerCard.getDescription()) ||
                !godlikePlayerCard.getPower().equals(effectivePlayerCard.getPower()))
            equals = false;

        for (int i = 1; i < spyGame.getPlayersHandler().getNumOfPlayers(); i++) {
            godlikePlayerCard = cardsToCheck.get(i - 1);
            effectivePlayerCard = spyGame.getPlayersHandler().getPlayer(i).getAbstractGodCard();
            if (!godlikePlayerCard.getGodName().equals(effectivePlayerCard.getGodName()) ||
                    !godlikePlayerCard.getDescription().equals(effectivePlayerCard.getDescription()) ||
                    !godlikePlayerCard.getPower().equals(effectivePlayerCard.getPower()))
                equals = false;
        }
        assertTrue(equals);
    }

    @Test
    public void findNextState() throws ClassNotFoundException, IOException, WinnerCaughtException, InterruptedException {
        spyGame.setCurrentState(spyGame.getTurnMap().get(1));

        Mockito.doNothing().when(spyGame).transitToNextState();

        spyState.findNextState();

        assertTrue(spyGame.getNextState() instanceof ChooseWorkerState);
    }
}