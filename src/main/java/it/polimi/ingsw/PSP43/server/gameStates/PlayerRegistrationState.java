package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.server.ClientListener;
import it.polimi.ingsw.PSP43.server.model.GameSession;
import it.polimi.ingsw.PSP43.server.modelHandlers.PlayersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.NicknameAlreadyInUseException;
import it.polimi.ingsw.PSP43.server.networkMessages.GenericMessage;
import it.polimi.ingsw.PSP43.server.networkMessages.NoticeMessage;
import it.polimi.ingsw.PSP43.server.networkMessages.RegistrationMessage;

import java.util.HashMap;
import java.util.Random;

public class PlayerRegistrationState extends TurnState{
    int maxNumPlayers;

    public PlayerRegistrationState(GameSession gameSession) {
        super(gameSession);
        maxNumPlayers = 1;
    }

    public void handleCommand(RegistrationMessage message) throws NicknameAlreadyInUseException {
        GameSession game = super.getGameSession();
        HashMap<String, ClientListener> gamers = game.getListenersHashMap();
        ClientListener actualClient = (ClientListener) gamers.get(message.getNickPlayerId());
        PlayersHandler playersHandler = game.getPlayersHandler();
        try {
            playersHandler.createNewPlayer(message.getNickPlayerId());
            if (gamers.size() == 1) {
                // TODO : request to the player of how many opponents does he want and put it into the field numMaxPlayers
            }
            GenericMessage clientMessage;
            // TODO : send message to the player
            if (maxNumPlayers == gamers.size()) {
                game.setFull(true);
                clientMessage = new NoticeMessage(game.getIdGame(), "Ready to play!");
                // TODO : send the message to all the players
                game.setCurrentState(game.getTurnMap().get(1));
                Random random = new Random();
                game.setCurrentPlayer(playersHandler.getPlayer(random.nextInt(playersHandler.getNumOfPlayers())));
            }
            else {
                clientMessage = new NoticeMessage(game.getIdGame(), "We are connecting you with other players!");
                // TODO : send the message to the new player
            }
        } catch (NicknameAlreadyInUseException e) {
            NoticeMessage error = new NoticeMessage(super.getGameSession().getIdGame(), "We are sorry, but " + message.getNickPlayerId() +
                    "is already in use.");
            // TODO : invoke a method on ClientListener to send the message through the net.
        }
    }
}