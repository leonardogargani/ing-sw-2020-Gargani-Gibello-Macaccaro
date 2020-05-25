package it.polimi.ingsw.PSP43.server.controllers;

public interface DecoratorFactory {
    AbstractGodCard buildDecorator(AbstractGodCard param);
    AbstractGodCard buildDecorator();
}
