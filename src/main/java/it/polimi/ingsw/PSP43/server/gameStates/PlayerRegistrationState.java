package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.client.Screens;
import it.polimi.ingsw.PSP43.client.networkMessages.PlayersNumberResponse;
import it.polimi.ingsw.PSP43.client.networkMessages.RegistrationMessage;
import it.polimi.ingsw.PSP43.server.modelHandlers.PlayersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.networkMessages.PlayersNumberRequest;
import it.polimi.ingsw.PSP43.server.networkMessages.StartGameMessage;

/**
 * This is the initial state where the game accept new players and, for the first one,
 * asks for the number of participants required.
 */
public class PlayerRegistrationState extends TurnState {
    Boolean first;

    public PlayerRegistrationState(GameSession gameSession) {
        super(gameSession, TurnName.PLAYER_REGISTRATION_STATE);
        first = Boolean.TRUE;
    }

    /**
     * This method is the one which has to initialise the data of the new player connected.
     * If the nickname chosen by the player is already used, it sends an error to the client
     * asking him to re-insert the username. In that case the player will be treated as a
     * new player connecting to the server.
     *
     * @param message This is the message sent from the client.
     */
    public void executeState(RegistrationMessage message) {
        GameSession game = super.getGameSession();
        PlayersHandler playersHandler = game.getPlayersHandler();
        playersHandler.createNewPlayer(message.getNick());
        int numberOfPlayers = game.getPlayersHandler().getNumOfPlayers();
        int num;
        if (first == Boolean.TRUE) {
            first = Boolean.FALSE;
            num = askNumberPlayers(game, message);
            game.maxNumPlayers = num;
        }

        if (game.maxNumPlayers == numberOfPlayers) {
            this.findNextState();
        } else if (game.maxNumPlayers != -1) {
            StartGameMessage clientMessage = new StartGameMessage(Screens.CONNECTING_WITH_OTHERS.toString());
            game.sendMessage(clientMessage, message.getNick());
        }
    }

    /**
     * This method asks to the first player of the game how many opponents does he want.
     *
     * @param gameSession This is a reference to the main access to the game database.
     * @param message     This is the message sent from the client.
     */
    protected int askNumberPlayers(GameSession gameSession, RegistrationMessage message) {
        PlayersNumberRequest request = new PlayersNumberRequest();
        PlayersNumberResponse response;
        try {
            response = gameSession.sendRequest(request, message.getNick(), PlayersNumberResponse.class);
        } catch (GameEndedException e) {
            gameSession.setActive();
            return -1;
        }
        return response.getNumberOfPlayer();

    }

    /**
     * This method finds the next turn of the game (saving it into a variable in the GameSession database),
     * which will be always a ChooseCardState.
     */
    public void findNextState() {
        GameSession game = super.getGameSession();

        for (TurnState t : game.getTurnStateMap()) {
            if (t.getTurnName() == TurnName.CHOOSE_CARD_STATE)
                game.setNextState(t);
        }
    }
}