package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.client.networkMessages.NumberPlayerResponse;
import it.polimi.ingsw.PSP43.client.networkMessages.RegistrationMessage;
import it.polimi.ingsw.PSP43.server.ClientListener;
import it.polimi.ingsw.PSP43.server.modelHandlers.PlayersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.NicknameAlreadyInUseException;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;
import it.polimi.ingsw.PSP43.server.networkMessages.ChangeNickRequest;
import it.polimi.ingsw.PSP43.server.networkMessages.NumberPlayerRequest;
import it.polimi.ingsw.PSP43.server.networkMessages.TextMessage;

import java.io.IOException;
import java.util.HashMap;

public class PlayerRegistrationState extends TurnState{

    public PlayerRegistrationState(GameSession gameSession) {
        super(gameSession);
    }

    public void executeState(RegistrationMessage message) throws IOException, ClassNotFoundException {
        GameSession game = super.getGameSession();
        int numberOfPlayers = game.getListenersHashMap().size();
        PlayersHandler playersHandler = game.getPlayersHandler();
        boolean registered = false;
        do {
            try {
                registered = playersHandler.createNewPlayer(message.getNick());
                if (numberOfPlayers == 1) {
                    NumberPlayerRequest request = new NumberPlayerRequest("Choose between a 2 or 3 game play.");
                    // TODO : is it possible to have and empty constructor for messages?
                    NumberPlayerResponse response = null;
                    boolean delivered;
                    do {
                        delivered = game.sendRequest(request, message.getNick(), response);
                    } while (!delivered);
                    game.maxNumPlayers = response.getNumberOfPlayer();
                }
                if (game.maxNumPlayers == numberOfPlayers) {
                    findNextState();
                }
                else {
                    TextMessage clientMessage = new TextMessage("We are connecting you with other players!");
                    game.sendMessage(clientMessage, message.getNick());
                }
                registered = true;
            } catch (NicknameAlreadyInUseException e) {
                ChangeNickRequest notifyChangeNick = new ChangeNickRequest("We are sorry, but " + message.getNick() +
                        "is already in use.");
                game.sendMessage(notifyChangeNick, message.getNick());
            } catch (IOException | ClassNotFoundException | WinnerCaughtException e) {
                e.printStackTrace();
            }
        } while (!registered);
    }

    public void findNextState() throws IOException, ClassNotFoundException, WinnerCaughtException {
        GameSession game = super.getGameSession();
        int indexCurrentState;
        indexCurrentState = game.getTurnMap().indexOf(game.getCurrentState());
        game.setNextState(game.getTurnMap().get(indexCurrentState + 1 ));

        game.transitToNextState();
    }
}