package it.polimi.ingsw.PSP43.server;

import it.polimi.ingsw.PSP43.client.networkMessages.LeaveGameMessage;
import it.polimi.ingsw.PSP43.client.networkMessages.RegistrationMessage;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.gameStates.GameSessionObservable;
import it.polimi.ingsw.PSP43.server.networkMessages.StartGameMessage;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * This class implements Runnable and it put new players in a match(gameSession), it is also used for the removal of a
 * ended gameSession
 */
public class RegisterClientListener implements Runnable {
    private static final ArrayList<GameSession> gameSessions = new ArrayList<>();
    private ClientListener player;
    private RegistrationMessage message;

    /**
     * Not default constructor for RegisterClientListener class.
     * @param player The reference to the ClientListener that is creating this RegisterClientListener.
     * @param message The registration message with the nick of the player.
     */
    public RegisterClientListener(ClientListener player, RegistrationMessage message) {
        this.player = player;
        this.message = message;
    }

    public RegisterClientListener() {}

    /**
     * Override of the run method. It contains the algorithm that checks if there is a game that hasn't started yet or if
     * a new game session is needed.
     */
    @Override
    public void run() {
        try {
            int idGame = -1, counter = 0;

            StartGameMessage startGameMessage = new StartGameMessage("\nWe are looking for an existent game for you. Otherwise " +
                    "we will create a new game and you will decide the number of participants.");
            player.sendMessage(startGameMessage);

            // Until there are available gameSessions to inspect in the list, check them if they are available.
            // If it is like that, try to register to that game.
            while (idGame == -1 && gameSessions.size() > counter) {
                GameSessionObservable gameSession = gameSessions.get(counter);

                // If the gameSession is in PlayerRegistrationState with another first player, wait until the player
                // has decided the maximum number of players.
                while (gameSession.getMaxNumPlayers() == 1) {
                    TimeUnit.SECONDS.sleep(1);
                }

                if (player.isDisconnected())
                    break;

                if (gameSession.getMaxNumPlayers() > gameSession.getNumOfPlayers()) {
                    idGame = gameSession.registerToTheGame(message, player);
                }

                counter++;
            }

            // If there is not a gameSession available, create a new one
            if (idGame == -1 & !player.isDisconnected()) {
                GameSession game = new GameSession(getRandIdGame());
                gameSessions.add(game);
                Thread gameThread = new Thread(game);
                gameThread.start();

                idGame = game.registerToTheGame(message, player);
            }

            player.setIdGame(idGame);
        } catch (IOException | InterruptedException | SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Synchronized method to remove a gameSession.
     * @param idGameSession This is the id of the gameSession that is going to be removed.
     */
    public synchronized void removeGameSession(int idGameSession) {
        gameSessions.removeIf(gameSessionObservable -> gameSessionObservable.getIdGame() == idGameSession);
    }

    /**
     * This method notifies to the gameSession that a player left the game. Other players must be notified and
     * after that gameSession must be deleted.
     * @param idGameSession The id of the match that will be deleted.
     */
    public void notifyDisconnection(int idGameSession) {
        synchronized (gameSessions) {
            for (Iterator<GameSession> gameSessionIterator = gameSessions.iterator(); gameSessionIterator.hasNext(); ) {
                GameSession gameSession = gameSessionIterator.next();
                if (gameSession.getIdGame() == idGameSession) {
                    gameSession.unregisterFromGame(new LeaveGameMessage(), player);
                    gameSessionIterator.remove();
                }
            }
        }
    }

    public synchronized int getRandIdGame() {
        int randId;
        Random r = new Random();

        while (true) {
            randId = r.nextInt(1000);

            if (gameSessions.size() == 0) return randId;

            for (Iterator<GameSession> gameSessionIterator = gameSessions.iterator(); gameSessionIterator.hasNext(); ) {
                GameSession gameSession = gameSessionIterator.next();
                if (!(gameSessionIterator.hasNext()) && gameSession.getIdGame() != randId) {
                    return randId;
                }
            }
        }
    }
}
