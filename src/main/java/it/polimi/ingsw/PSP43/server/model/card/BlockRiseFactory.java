package it.polimi.ingsw.PSP43.server.model.card;

import it.polimi.ingsw.PSP43.server.model.card.decorators.BlockRiseDecorator;

public class BlockRiseFactory implements DecoratorFactory {
    public AbstractGodCard buildDecorator(AbstractGodCard param) {
        return new BlockRiseDecorator(param);
    }

    public AbstractGodCard buildDecorator() {
        return new BlockRiseDecorator();
    }
}
