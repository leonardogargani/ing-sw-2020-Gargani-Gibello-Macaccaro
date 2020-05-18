package it.polimi.ingsw.PSP43.server.model.card;

public interface DecoratorFactory {
    AbstractGodCard buildDecorator(AbstractGodCard param);
    AbstractGodCard buildDecorator();
}
