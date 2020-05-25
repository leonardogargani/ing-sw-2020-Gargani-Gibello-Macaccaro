package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.client.networkMessages.ChosenCardResponse;
import it.polimi.ingsw.PSP43.client.networkMessages.ChosenCardsResponse;
import it.polimi.ingsw.PSP43.server.initialisers.DOMCardsParser;
import it.polimi.ingsw.PSP43.server.initialisers.GameInitialiser;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.controllers.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameEndedException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;

public class ChooseCardStateTest {
    GameSessionForTest gameSession;
    GameSessionForTest spyGame;
    ArrayList<AbstractGodCard> deck;
    ChooseCardState state;
    ChooseCardState spyState;

    @Before
    public void setUp() throws Exception {
        gameSession = GameInitialiser.initialiseGame();
        GameInitialiser.initialisePlayers(gameSession);
        spyGame = Mockito.spy(gameSession);
        deck = DOMCardsParser.buildDeck();
        state = new ChooseCardState(spyGame);
        spyState = Mockito.spy(state);
    }

    @Test
    public void initState() throws GameEndedException {
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
    public void executeState() throws GameEndedException {
        Player godLikePlayer = spyGame.getPlayersHandler().getPlayer(0);

        ArrayList<AbstractGodCard> initialAvailableCards = new ArrayList<>();
        HashMap<String, String> expectedCorrespondence = new HashMap<>();

        initialAvailableCards.add(deck.get(0));
        initialAvailableCards.add(deck.get(1));
        spyState.cardsAvailable = initialAvailableCards;

        expectedCorrespondence.put(godLikePlayer.getNickname(), deck.get(1).getGodName());
        String secondPlayer = spyGame.getPlayersHandler().getNextPlayer(godLikePlayer.getNickname()).getNickname();
        expectedCorrespondence.put(secondPlayer, deck.get(0).getGodName());

        Mockito.doReturn(new ChosenCardResponse(spyState.cardsAvailable.get(0)))
                .doReturn(new ChosenCardResponse(spyState.cardsAvailable.get(1)))
                .when(spyGame).sendRequest(any(), any(), any());
        Mockito.doNothing().when(spyState).findNextState();

        spyState.executeState();

        for (String player : expectedCorrespondence.keySet()) {
            String effectiveGodNameSaved = spyGame.getCardsHandler().getPlayerCard(player).getGodName();
            assertEquals(expectedCorrespondence.get(player), effectiveGodNameSaved);
        }
    }

    @Test
    public void findNextState() {
        spyGame.setCurrentState(spyGame.getTurnStateMap().get(1));

        Mockito.doNothing().when(spyGame).transitToNextState();

        spyState.findNextState();

        assertTrue(spyGame.getNextState() instanceof ChooseWorkerState);
    }
}