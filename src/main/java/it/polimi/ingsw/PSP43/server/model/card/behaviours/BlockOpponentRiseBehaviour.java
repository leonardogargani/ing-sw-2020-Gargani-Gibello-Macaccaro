package it.polimi.ingsw.PSP43.server.model.card.behaviours;

import it.polimi.ingsw.PSP43.server.DataToAction;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.model.card.decorators.BlockRiseDecorator;
import it.polimi.ingsw.PSP43.server.model.card.decorators.PowerGodDecorator;
import it.polimi.ingsw.PSP43.server.modelHandlers.CardsHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;

import java.io.IOException;
import java.util.HashMap;

public class BlockOpponentRiseBehaviour extends PowerGodDecorator implements MoveBehavior {

    public BlockOpponentRiseBehaviour() {
    }

    public BlockOpponentRiseBehaviour(AbstractGodCard godComponent) {
        super(godComponent);
    }

    private void disablePowers(GameSession gameSession) throws ClassNotFoundException {
        HashMap<String, AbstractGodCard> mapCardsOwned = gameSession.getCardsHandler().getMapOwnerCard();
        String godName;
        AbstractGodCard card, newCard;

        for (String s : mapCardsOwned.keySet()) {
            godName = mapCardsOwned.get(s).getGodName();
            if (!godName.equals("Athena")) {
                card = mapCardsOwned.get(s);
                newCard = card.cleanFromEffects("it.polimi.ingsw.PSP43.server.model.card.decorators.BlockRiseDecorator");
                mapCardsOwned.put(s, newCard);
            }
        }

    }

    private void enablePowers(GameSession gameSession) {
        CardsHandler cardsHandler = gameSession.getCardsHandler();
        AbstractGodCard cardOwned;
        for (String s : cardsHandler.getMapOwnerCard().keySet()) {
            cardOwned = cardsHandler.getMapOwnerCard().get(s);
            if (!cardOwned.getGodName().equals("Athena")) {
                cardsHandler.getMapOwnerCard().put(s, new BlockRiseDecorator(cardOwned));
            }
        }
    }

    public void handleMove(DataToAction dataToAction) throws ClassNotFoundException, IOException, WinnerCaughtException {
        disablePowers(dataToAction.getGameSession());
        super.move(dataToAction);
        Coord newCoord = dataToAction.getWorker().getCurrentPosition();
        Cell newCell = dataToAction.getGameSession().getCellsHandler().getCell(newCoord);
        Coord oldCoord = dataToAction.getWorker().getPreviousPosition();
        Cell oldCell = dataToAction.getGameSession().getCellsHandler().getCell(oldCoord);

        if (newCell.getHeight() - oldCell.getHeight() > 0) {
            enablePowers(dataToAction.getGameSession());
        }
    }
}
