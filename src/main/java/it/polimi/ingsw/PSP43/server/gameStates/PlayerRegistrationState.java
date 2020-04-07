package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.server.ClientListener;
import it.polimi.ingsw.PSP43.server.model.GameSession;
import it.polimi.ingsw.PSP43.server.modelHandlers.PlayersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.NicknameAlreadyInUseException;
import it.polimi.ingsw.PSP43.server.networkMessages.ErrorMessage;
import it.polimi.ingsw.PSP43.server.networkMessages.GenericMessage;
import it.polimi.ingsw.PSP43.server.networkMessages.NoticeMessage;
import it.polimi.ingsw.PSP43.server.networkMessages.RegistrationMessage;

import java.util.HashMap;

public class PlayerRegistrationState extends TurnState{

    public PlayerRegistrationState(GameSession gameSession) {
        super(gameSession);
    }

    public void handleCommand(RegistrationMessage message) throws NicknameAlreadyInUseException {
        HashMap<String, ClientListener> gamers = super.getGameSession().getGamers();
        ClientListener actualClient = (ClientListener) gamers.get(message.getNickPlayerId());
        PlayersHandler playersHandler = super.getGameSession().getPlayerHandler();
        try {
            playersHandler.createNewPlayer(message.getNickPlayerId());
            if (gamers.size() == 1) {
                // TODO : request to the player of how many opponents does he want and put it into the field in GameSession numMaxPlayers
            }
            GenericMessage clientMessage = new NoticeMessage(super.getGameSession().getIdGame(), "Ready to play! We are connecting you with other players!");
            // TODO : send message to the player
            if (super.getGameSession().getMaxNumPlayers() == gamers.size()) {
            }
        } catch (NicknameAlreadyInUseException e) {
            ErrorMessage error = new ErrorMessage(super.getGameSession().getIdGame(), "We are sorry, but " + message.getNickPlayerId() +
                    "is already in use.");
            // TODO : invoke a method on ClientListener to send the message through the net.
        }
    }
}