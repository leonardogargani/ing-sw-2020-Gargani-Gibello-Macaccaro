package it.polimi.ingsw.PSP43.server.controller.cards;

import it.polimi.ingsw.PSP43.server.controller.cards.decorators.BlockRiseDecorator;

/**
 * This class is a factory to wrap-up a card with some extra-functionalities. Here a blocking functionality due to Athena's behaviour is added.
 */
public class BlockRiseFactory implements DecoratorFactory {
    public AbstractGodCard buildDecorator(AbstractGodCard param) {
        return new BlockRiseDecorator(param);
    }
}
