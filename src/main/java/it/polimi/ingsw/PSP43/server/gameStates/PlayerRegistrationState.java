package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.client.networkMessages.PlayersNumberResponse;
import it.polimi.ingsw.PSP43.client.networkMessages.RegistrationMessage;
import it.polimi.ingsw.PSP43.server.modelHandlers.PlayersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.modelHandlersException.NicknameAlreadyInUseException;
import it.polimi.ingsw.PSP43.server.networkMessages.ChangeNickRequest;
import it.polimi.ingsw.PSP43.server.networkMessages.PlayersNumberRequest;
import it.polimi.ingsw.PSP43.server.networkMessages.StartGameMessage;

import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This is the initial state where the game accept new players and, for the first one,
 * asks for the number of participants required.
 */
public class PlayerRegistrationState extends TurnState {
    private Lock lock;
    Boolean first;

    public PlayerRegistrationState(GameSession gameSession) {
        super(gameSession, TurnName.PLAYER_REGISTRATION_STATE);
        lock = new ReentrantLock();
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
    public void executeState(RegistrationMessage message) throws IOException {
        GameSession game = super.getGameSession();
        PlayersHandler playersHandler = game.getPlayersHandler();
        try {
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
            } else {
                StartGameMessage clientMessage = new StartGameMessage("We are connecting you with other players!");
                game.sendMessage(clientMessage, message.getNick());
            }
        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method asks to the first player of the game how many opponents does he want.
     *
     * @param gameSession This is a reference to the center of the game database.
     * @param message     This is the message sent from the client.
     */
    protected int askNumberPlayers(GameSession gameSession, RegistrationMessage message) throws IOException, ClassNotFoundException, InterruptedException {
        PlayersNumberRequest request = new PlayersNumberRequest("Choose between a 2 or 3 game play.");
        PlayersNumberResponse response;
        try {
            response = gameSession.sendRequest(request, message.getNick(), new PlayersNumberResponse());
        } catch (GameEndedException e) {
            gameSession.setActive();
            return -1;
        }
        return response.getNumberOfPlayer();

    }

    /**
     * Finds the next state for the game, saving it in a variable in GameSession, and calls on the
     * instance of GameSession the method to transit to the next state of play.
     */
    public void findNextState() {
        GameSession game = super.getGameSession();
        int indexCurrentState;
        indexCurrentState = game.getTurnMap().indexOf(game.getCurrentState());
        game.setNextState(game.getTurnMap().get(indexCurrentState + 1));
    }
}