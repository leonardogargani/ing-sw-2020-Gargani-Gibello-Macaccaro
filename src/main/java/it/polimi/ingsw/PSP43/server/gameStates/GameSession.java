package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.client.networkMessages.LeaveGameMessage;
import it.polimi.ingsw.PSP43.server.BoardObserver;
import it.polimi.ingsw.PSP43.server.ClientListener;
import it.polimi.ingsw.PSP43.server.RegisterClientListener;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.modelHandlers.CardsHandler;
import it.polimi.ingsw.PSP43.server.modelHandlers.CellsHandler;
import it.polimi.ingsw.PSP43.server.modelHandlers.PlayersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlers.WorkersHandler;
import it.polimi.ingsw.PSP43.server.networkMessages.EndGameMessage;
import it.polimi.ingsw.PSP43.server.networkMessages.PlayersListMessage;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class GameSession extends GameSessionObservable {
    private static final int FIRSTPOSITION = 0;
    private Boolean active;

    private Player currentPlayer;

    private TurnState currentState;
    private TurnState nextState;
    private final List<TurnState> turnMap;

    private final CellsHandler cellsHandler;
    private final PlayersHandler playersHandler;
    private final WorkersHandler workersHandler;
    private final CardsHandler cardsHandler;

    private final BoardObserver boardObserver;

    public GameSession(int idgame) throws ParserConfigurationException, SAXException, IOException {
        super(idgame);
        this.maxNumPlayers = 1;

        ArrayList<TurnState> turnMap = new ArrayList<>(Arrays.asList(   new PlayerRegistrationState(this),
                                                                        new ChooseCardState(this),
                                                                        new ChooseWorkerState(this),
                                                                        new MoveState(this),
                                                                        new BuildState(this),
                                                                        new WinState(this)));
        this.turnMap = Collections.unmodifiableList(turnMap);

        this.currentState = turnMap.get(FIRSTPOSITION);
        super.setCurrentState(turnMap.get(FIRSTPOSITION));
        this.nextState = turnMap.get(FIRSTPOSITION);

        this.boardObserver = new BoardObserver(this);

        this.cellsHandler = new CellsHandler(this);
        this.playersHandler = new PlayersHandler();
        this.workersHandler = new WorkersHandler(this);
        this.cardsHandler = new CardsHandler();

        this.active = Boolean.TRUE;
    }

    public void run() {
        while (active) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) { e.printStackTrace(); }
            if (!(currentState.getClass().isInstance(nextState))) {
                this.transitToNextState();
            }
        }

        RegisterClientListener ending = new RegisterClientListener();
        ending.removeGameSession(super.getIdGame());
    }

    /**
     * This method returns true if the thread is running, false otherwise.
     * @return a boolean that represents the state of the thread (true = running, false = stopped).
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * This method sets the state of the thread (true = running, false = stopped).
     */
    public void setActive() {
        this.active = Boolean.FALSE;
    }

    /**
     * This method does the transition during the game to one state of the play to the following.
     */
    protected void transitToNextState() {
        TurnState nextState = this.getNextState();

        for (TurnState t : turnMap) {
            if (nextState.getTurnName() == t.getTurnName()) {
                this.setCurrentState(nextState);
                super.setCurrentState(nextState);
            }
        }

        currentState.initState();
    }

    /**
     * This method eliminates a player from the game when he looses the match.
     * @param playerEliminated This represents the data of the player who's lost the game and has to be
     *                         removed from the database.
     */
    public void eliminatePlayer(Player playerEliminated) {
        cardsHandler.removeCardToPlayer(playerEliminated.getNickname());
        Player playerToRemove = playersHandler.getPlayer(playerEliminated.getNickname());
        playersHandler.deletePlayer(playerEliminated.getNickname());

        Integer[] workersToRemove = playerToRemove.getWorkersIdsArray();
        workersHandler.removeWorkers(workersToRemove);

        super.eliminatePlayer(playerEliminated, new PlayersListMessage(playerEliminated.getNickname() +
                " has lost the game.", this));
    }

    /**
     * This method is given to know the turn state in which the game is.
     * @return the state in which the game is.
     */
    public TurnState getCurrentState() {
        return currentState;
    }

    /**
     * This method sets the current turn in which the game is.
     * @param currentState The state in which the game is.
     */
    public void setCurrentState(TurnState currentState) {
        this.currentState = currentState;
        super.setCurrentState(currentState);
    }

    /**
     * This method returns the following state in which the game will be.
     * @return the next state in which the game will be.
     */
    public TurnState getNextState() {
        return this.nextState;
    }

    public void setNextState(TurnState nextState) {
        this.nextState = nextState;
    }

    /**
     * This method returns the List representing all the states in a game.
     * @return a List representing all the states in a game.
     */
    public List<TurnState> getTurnMap() {
        return turnMap;
    }

    /**
     * This method returns the current player of the game.
     * @return the current player of the game.
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * This method sets the current player of the game.
     * @param currentPlayer The player that represents the current player of the game.
     */
    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * This method returns the class which handles the cells during the game.
     * @return the cells handler of the game.
     */
    public CellsHandler getCellsHandler() {
        return cellsHandler;
    }

    /**
     * This method returns the class which handles players' data during the game.
     * @return the players handler of the game.
     */
    public PlayersHandler getPlayersHandler() {
        return playersHandler;
    }

    /**
     * This method returns the class which handles workers data during the game.
     * @return the workers handler of the game.
     */
    public WorkersHandler getWorkersHandler() {
        return workersHandler;
    }

    /**
     * This method returns the class which handles cards data during the game.
     * @return the cards handler of the game.
     */
    public CardsHandler getCardsHandler() {
        return cardsHandler;
    }

    /**
     * This method returns the observer of the graphical part of the game, so that every
     * cell or worker change is notified to the client.
     * @return the observer of the graphical part of the game.
     */
    public BoardObserver getBoardObserver() {
        return boardObserver;
    }

    /**
     * This method calls the super method and then sets to false the boolean "active" to stop the thread.
     * @param message The message from the client to unsubscribe himself from the game.
     * @param player The reference to the listener of the client who left the game.
     */
    @Override
    public synchronized void unregisterFromGame(LeaveGameMessage message, ClientListener player) {
        super.unregisterFromGame(message, player);
        this.active = Boolean.FALSE;
    }

    /**
     * This method calls the super method and then switches to false the boolean "active" to stop the thread.
     * @param messageToLosers The message that has to be delivered to all the losers of the game.
     * @param messageForTheWinner The message that has to be delivered to the winner of the game.
     * @param nicksExcluded All the nicknames of the players excluded from receiving the "messageForTheLosers".
     */
    @Override
    public void sendEndingMessage(EndGameMessage messageToLosers, EndGameMessage messageForTheWinner, ArrayList<String> nicksExcluded) {
        super.sendEndingMessage(messageToLosers, messageForTheWinner, nicksExcluded);
        this.active = Boolean.FALSE;
    }
}