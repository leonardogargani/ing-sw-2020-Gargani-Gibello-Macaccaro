package it.polimi.ingsw.PSP43.server;

import it.polimi.ingsw.PSP43.client.networkMessages.LeaveGameMessage;
import it.polimi.ingsw.PSP43.client.networkMessages.RegistrationMessage;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.gameStates.GameSessionObservable;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * This class implements Runnable and it put new players in a match(gameSession), it is also used for the removal of a
 * ended gameSession
 */
public class RegisterClientListener implements Runnable {
    private static final ArrayList<GameSessionObservable> gameSessions = new ArrayList<>();
    private ClientListener player;
    private RegistrationMessage message;

    /**
     * Not default constructor for RegisterClientListener class
     *
     * @param player  that is creating this RegisterClientListener
     * @param message is the registration message with the nick inside
     */
    public RegisterClientListener(ClientListener player, RegistrationMessage message) {
        this.player = player;
        this.message = message;
    }

    /**
     * Constructor without parameters
     */
    public RegisterClientListener() {
    }

    /**
     * Override of the run method, it contains the algorithms that checks if there is a game that hasn't started yet or if
     * a new game session is needed
     */
    @Override
    public void run() {
        try {
            int idGame = -1, counter = 0;

            // until there are available gameSessions to inspect in the list I check them if they are available
            while (idGame == -1 && gameSessions.size() > counter) {
                GameSessionObservable gameSession = gameSessions.get(counter);


                // if the gameSession is in registration phase for the first player, I wait
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

            // if there is not a gameSession available, I create a new one
            if (idGame == -1 & !player.isDisconnected()) {
                GameSession game = new GameSession(gameSessions.size());
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
     * Synchronized method to remove a gameSession
     *
     * @param idGameSession is the id of the gameSession we are going to remove
     */
    public synchronized void removeGameSession(int idGameSession) {
        gameSessions.removeIf(gameSessionObservable -> gameSessionObservable.getIdGame() == idGameSession);
    }

    /**
     * This method notifies to the gameSession that a player left the game, other players must be notified and
     * after that gameSession must be deleted
     *
     * @param idGameSession is the id of the match that will be deleted
     */
    public void notifyDisconnection(int idGameSession) {
        synchronized (gameSessions) {
            for (Iterator<GameSessionObservable> gameSessionIterator = gameSessions.iterator(); gameSessionIterator.hasNext(); ) {
                GameSessionObservable gameSession = gameSessionIterator.next();
                if (gameSession.getIdGame() == idGameSession) {
                    gameSession.unregisterFromGame(new LeaveGameMessage(), player);
                    gameSessionIterator.remove();
                }
            }
        }
    }
}
