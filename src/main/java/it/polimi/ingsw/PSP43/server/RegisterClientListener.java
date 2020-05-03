package it.polimi.ingsw.PSP43.server;

import it.polimi.ingsw.PSP43.client.networkMessages.ClientMessage;
import it.polimi.ingsw.PSP43.client.networkMessages.RegistrationMessage;
import it.polimi.ingsw.PSP43.server.gameStates.GameSessionObservable;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;

import java.io.IOException;

public class RegisterClientListener implements Runnable {
    GameSessionObservable gameSessionObservable;
    ClientListener player;
    ClientMessage message;

    public RegisterClientListener(GameSessionObservable gameSessionObservable, ClientListener player, ClientMessage message) {
        this.gameSessionObservable = gameSessionObservable;
        this.player = player;
        this.message = message;
    }

    @Override
    public void run() {
        try {
            gameSessionObservable.registerToTheGame((RegistrationMessage) message, player);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (WinnerCaughtException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
