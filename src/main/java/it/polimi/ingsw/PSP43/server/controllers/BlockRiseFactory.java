package it.polimi.ingsw.PSP43.server.controllers;

import it.polimi.ingsw.PSP43.server.controllers.decorators.BlockRiseDecorator;

public class BlockRiseFactory implements DecoratorFactory {
    public AbstractGodCard buildDecorator(AbstractGodCard param) {
        return new BlockRiseDecorator(param);
    }

    public AbstractGodCard buildDecorator() {
        return new BlockRiseDecorator();
    }
}
