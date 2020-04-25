package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.client.networkMessages.NumberPlayerResponse;
import it.polimi.ingsw.PSP43.client.networkMessages.RegistrationMessage;
import it.polimi.ingsw.PSP43.server.ClientListener;
import it.polimi.ingsw.PSP43.server.modelHandlers.PlayersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.NicknameAlreadyInUseException;
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
        HashMap<String, ClientListener> gamers = game.getListenersHashMap();
        ClientListener actualClient = (ClientListener) gamers.get(message.getNick());
        PlayersHandler playersHandler = game.getPlayersHandler();
        boolean registered = false;
        do {
            try {
                registered = playersHandler.createNewPlayer(message.getNick());
                if (gamers.size() == 1) {
                    NumberPlayerRequest request = new NumberPlayerRequest("Choose between a 2 or 3 game play.");
                    NumberPlayerResponse response = (NumberPlayerResponse) actualClient.sendRequest(request);
                    game.maxNumPlayers = response.getNumberOfPlayer();
                }
                if (game.maxNumPlayers == gamers.size()) {
                    findNextState();
                }
                else {
                    TextMessage clientMessage = new TextMessage("We are connecting you with other players!");
                    actualClient.sendMessage(clientMessage);
                }
                registered = true;
            } catch (NicknameAlreadyInUseException e) {
                ChangeNickRequest request = new ChangeNickRequest("We are sorry, but " + message.getNick() +
                        "is already in use.");
                message = (RegistrationMessage) actualClient.sendRequest(request);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } while (!registered);
    }

    public void findNextState() throws IOException, ClassNotFoundException {
        GameSession game = super.getGameSession();
        int indexCurrentState;
        indexCurrentState = game.getTurnMap().indexOf(game.getCurrentState());
        game.setNextState(game.getTurnMap().get(indexCurrentState + 1 ));

        game.transitToNextState();
    }
}