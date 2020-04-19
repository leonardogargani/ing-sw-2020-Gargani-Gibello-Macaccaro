package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.server.ClientListener;
import it.polimi.ingsw.PSP43.server.modelHandlers.PlayersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.NicknameAlreadyInUseException;
import it.polimi.ingsw.PSP43.server.networkMessages.GenericMessage;
import it.polimi.ingsw.PSP43.server.networkMessages.NoticeMessage;
import it.polimi.ingsw.PSP43.server.networkMessages.RegistrationMessage;

import java.util.HashMap;

public class PlayerRegistrationState extends TurnState{
    int maxNumPlayers;

    public PlayerRegistrationState(GameSession gameSession) {
        super(gameSession);
        maxNumPlayers = 1;
    }

    public void executeState(RegistrationMessage message) {
        GameSession game = super.getGameSession();
        HashMap<String, ClientListener> gamers = game.getListenersHashMap();
        ClientListener actualClient = (ClientListener) gamers.get(message.getNickPlayerId());
        PlayersHandler playersHandler = game.getPlayersHandler();
        boolean registered;
        do {
            try {
                registered = playersHandler.createNewPlayer(message.getNickPlayerId());
                if (gamers.size() == 1) {
                    // TODO : request to the player of how many opponents does he want and put it into the field numMaxPlayers
                }
                GenericMessage clientMessage;
                // TODO : send message to the player
                if (maxNumPlayers == gamers.size()) {
                    game.setFull(true);
                    findNextState();
                }
                else {
                    clientMessage = new NoticeMessage(game.getIdGame(), "We are connecting you with other players!");
                    // TODO : send the message to the new player
                }
            } catch (NicknameAlreadyInUseException e) {
                registered = false;
                NoticeMessage error = new NoticeMessage(super.getGameSession().getIdGame(), "We are sorry, but " + message.getNickPlayerId() +
                        "is already in use.");
                // TODO : invoke a method on ClientListener to send the message through the net.
            }
        } while (registered);
    }

    public void findNextState() {
        GameSession game = super.getGameSession();
        int indexCurrentState;
        indexCurrentState = game.getTurnMap().indexOf(game.getCurrentState());
        game.setNextState(game.getTurnMap().get(indexCurrentState + 1 ));

        game.transitToNextState();
    }
}