package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.server.ClientListener;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.model.GameSession;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.modelHandlers.CardsHandler;
import it.polimi.ingsw.PSP43.server.modelHandlers.PlayersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.ActionForbiddenException;
import it.polimi.ingsw.PSP43.server.modelHandlersException.CardAlreadyInUseException;
import it.polimi.ingsw.PSP43.server.networkMessages.ChosenCardMessage;
import it.polimi.ingsw.PSP43.server.networkMessages.NoticeMessage;

public class ChooseCardState extends TurnState {

    public ChooseCardState(GameSession gameSession) {
        super(gameSession);
    }

    public void handleCommand(ChosenCardMessage message) throws ActionForbiddenException{
        GameSession game = super.getGameSession();

        PlayersHandler playersHandler = game.getPlayersHandler();
        int position = playersHandler.getNumOfPlayers()-1;
        String nicknameLatestPlayer = playersHandler.getPlayer(position).getNickname();

        Player current = game.getCurrentPlayer();
        String nicknameCurrentPlayer = current.getNickname();

        CardsHandler cardsHandler = game.getCardsHandler();

        if (!message.getOwner().equals(nicknameCurrentPlayer)) {
            throw new ActionForbiddenException("Sorry but it is not your turn to choose a card.");
        }

        try {
            cardsHandler.setCardToPlayer(nicknameCurrentPlayer, message.getNameCardChosen());
        } catch (CardAlreadyInUseException e) {
            ClientListener receiver = game.getListenersHashMap().get(nicknameCurrentPlayer);
            NoticeMessage message1 = new NoticeMessage(game.getIdGame(), e.getMessage());
            // TODO : SEND A MESSAGE TO THE receiver TO TELL THAT THE CARD IS NOT AVAILABLE
        }

        AbstractGodCard abstractGodCardChosen = cardsHandler.getCardsInUse().get(nicknameCurrentPlayer);
        // TODO : abstractGodCardChosen.initTurns(nicknameCurrentPlayer);
        if (nicknameLatestPlayer.equals(nicknameCurrentPlayer)) {
            game.setCurrentState(new ChooseWorkerState(game));
            game.setCurrentPlayer(playersHandler.getPlayer(0));
        }
        else {
            game.setCurrentPlayer(playersHandler.getNextPlayer(nicknameCurrentPlayer));
        }
    }
}
