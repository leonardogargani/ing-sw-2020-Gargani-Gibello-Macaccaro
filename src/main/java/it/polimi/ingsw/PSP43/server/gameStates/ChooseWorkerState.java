package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.server.ClientListener;
import it.polimi.ingsw.PSP43.server.model.GameSession;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.modelHandlers.CardsHandler;
import it.polimi.ingsw.PSP43.server.modelHandlers.PlayersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.ActionForbiddenException;
import it.polimi.ingsw.PSP43.server.modelHandlersException.CardAlreadyInUseException;
import it.polimi.ingsw.PSP43.server.networkMessages.ChosenCardMessage;
import it.polimi.ingsw.PSP43.server.networkMessages.NoticeMessage;

public class ChooseWorkerState extends TurnState {
    public ChooseWorkerState(GameSession gameSession) {
        super(gameSession);
    }

    public void handleCommand(ChosenCardMessage message) throws ActionForbiddenException {
        GameSession game = super.getGameSession();
        PlayersHandler playersHandler = game.getPlayersHandler();
        Player newOwner = playersHandler.getPlayer(message.getOwner());
        if (!message.getOwner().equals(newOwner.getNickname())) {
            throw new ActionForbiddenException("Sorry but it is not your turn to choose a card.");
        }

        CardsHandler cardsHandler = game.getCardsHandler();
        try {
            cardsHandler.setCardToPlayer(newOwner.getNickname(), message.getNameCardChosen());
        } catch (CardAlreadyInUseException e) {
            ClientListener receiver = game.getListenersHashMap().get(newOwner.getNickname());
            NoticeMessage message1 = new NoticeMessage(game.getIdGame(), e.getMessage());
            // TODO : invoke the method on clientListener to send a message to the client;
        }
        int position = playersHandler.getNumOfPlayers()-1;
        String latestPlayer = playersHandler.getPlayer(position).getNickname();
        if (latestPlayer.equals(newOwner.getNickname())) {

        }
    }
}
