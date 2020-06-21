package it.polimi.ingsw.PSP43.server.controller.cards;

import it.polimi.ingsw.PSP43.server.controller.cards.decorators.BlockRiseDecorator;

public class BlockRiseFactory implements DecoratorFactory {
    public AbstractGodCard buildDecorator(AbstractGodCard param) {
        return new BlockRiseDecorator(param);
    }
}
