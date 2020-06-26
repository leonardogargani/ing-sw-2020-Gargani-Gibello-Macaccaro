package it.polimi.ingsw.PSP43.server.controller.cards;

/**
 * This class is an interface that is used to implement different types of wrapping-up cards with extra functionalities.
 */
public interface DecoratorFactory {
    AbstractGodCard buildDecorator(AbstractGodCard param);
}
