package it.polimi.ingsw.PSP43.server;

import it.polimi.ingsw.PSP43.client.networkMessages.RegistrationMessage;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.gameStates.GameSessionObservable;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;

public class RegisterClientListener implements Runnable {
    private static ArrayList<GameSessionObservable> gameSessions = new ArrayList<>();
    private ClientListener player;
    private RegistrationMessage message;

    public RegisterClientListener(ClientListener player, RegistrationMessage message) {
        this.player = player;
        this.message = message;
    }

    @Override
    public void run() {
        try {
            int idGame = -1, counter = 0;

            // until there are available gameSessions to inspect in the list I check them if they are available
            while (idGame == -1 && gameSessions.size() > counter) {
                GameSessionObservable gameSession = gameSessions.get(counter);

                // if the gameSession is in registration phase for the first player, I wait
                while (gameSession.getMaxNumPlayers() == 1) {}
                if (gameSession.getMaxNumPlayers() >= gameSession.getNumOfPlayers()) {
                    idGame = gameSessions.get(counter).registerToTheGame(message, player);
                }

                counter++;
            }

            // if there is not a gameSession available, I create a new one
            if (idGame == -1) {
                GameSession game = new GameSession(gameSessions.size());
                gameSessions.add(game);
                Thread gameThread = new Thread(game);
                gameThread.start();

                game.registerToTheGame(message, player);
            }

            player.setIdGame(idGame);
        } catch (IOException | WinnerCaughtException | InterruptedException | ClassNotFoundException | SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
}
