package it.polimi.ingsw.PSP43.server.gameStates;


import it.polimi.ingsw.PSP43.server.ClientListener;
import it.polimi.ingsw.PSP43.server.gameStates.*;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlers.CardsHandler;
import it.polimi.ingsw.PSP43.server.modelHandlers.CellsHandler;
import it.polimi.ingsw.PSP43.server.modelHandlers.PlayersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlers.WorkersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.CellAlreadyOccupiedExeption;
import it.polimi.ingsw.PSP43.server.modelHandlersException.CellHeightException;
import it.polimi.ingsw.PSP43.server.networkMessages.GenericMessage;
import it.polimi.ingsw.PSP43.server.networkMessages.RegistrationMessage;

import java.util.ArrayList;
import java.util.HashMap;

public class GameSession {
    private int idGame;
    private boolean isFull;
    private HashMap<String, ClientListener> listenersHashMap;

    private TurnState currentState;
    private TurnState nextState;
    private ArrayList<TurnState> turnMap;

    private Player currentPlayer;

    private CellsHandler cellsHandler;
    private PlayersHandler playersHandler;
    private WorkersHandler workersHandler;
    private CardsHandler cardsHandler;


    /**
     * Not default constructor to inizialize a GameSession
     * @param idgame refers to the id of the game
     */
    public GameSession(int idgame){
        this.listenersHashMap = new HashMap<>(2);
        this.idGame = idgame;
        this.isFull = false;
        this.turnMap = new ArrayList<>();
        this.turnMap.add(0, new PlayerRegistrationState(this));
        this.currentState = turnMap.get(0);
        this.turnMap.add(1, new ChooseCardState(this));
        this.turnMap.add(2, new ChooseWorkerState(this));
        this.turnMap.add(3, new MoveState(this));
        this.turnMap.add(4, new BuildState(this));
        this.turnMap.add(5, new WinState(this));
        this.nextState = null;
        this.cellsHandler = new CellsHandler();
        this.playersHandler = new PlayersHandler();
        this.workersHandler = new WorkersHandler(this);
        this.cardsHandler = new CardsHandler();
    }

    protected void transitToNextState() {
        int indexNextState = turnMap.indexOf(nextState);
        currentState = turnMap.get(indexNextState);
        currentState.initState();
    }

    public synchronized boolean registerToTheGame(RegistrationMessage message, ClientListener player) {
        if (!this.isFull()) {
            listenersHashMap.put(message.getNickPlayerId(), player);
            currentState.executeState(message);
            return true;
        }
        else return false;
    }

    public synchronized boolean unregisterFromGame(RegistrationMessage message, ClientListener player) {
        // TODO :   send a message to all the other players telling them that the game is finished due to unregistration
        //          of a player or connection problems
        return true;
    }

    public void eliminatePlayer(Player playerEliminated) {
        cardsHandler.removeCardToPlayer(playerEliminated.getNickname());
        Player playerToRemove = playersHandler.getPlayer(playerEliminated.getNickname());

        int[] workersToRemove = playerToRemove.getWorkersIdsArray();
        workersHandler.removeWorkers(workersToRemove);

        // TODO : send a message to the player eliminated
        listenersHashMap.remove(playerEliminated.getNickname());
    }

    public synchronized void setFull(boolean full) {
        isFull = full;
    }

    public boolean isFull() {
        return isFull;
    }

    /**
     * Getter method for gamers arraylist
     * @return gamers ,that is the gamers list
     */
    public HashMap<String, ClientListener> getListenersHashMap() { return listenersHashMap; }

    /**
     * Method to get the game id
     * @return idGame is the id of that gamesession
     */
    public int getIdGame(){
        return idGame;
    }

    /**
     * Method to know the kind of turn state the current player is in
     * @return currentState ,that is the state
     */
    public TurnState getCurrentState() {
        return currentState;
    }

    /**
     * Method to set the kind of turn state
     * @param currentState, refers to the kind of turn state
     */
    public void setCurrentState(TurnState currentState) {
        this.currentState = currentState;
    }

    public TurnState getNextState() {
        return nextState;
    }

    public void setNextState(TurnState nextState) {
        this.nextState = nextState;
    }

    /**
     * This method returns the HashMap representing the correspondence between players and the related game turns
     * @return The HashMap representing the correspondence between players and the related game turns
     */
    public ArrayList<TurnState> getTurnMap() {
        return turnMap;
    }

    /**
     * Methods to get the current player of that turn
     * @return currentPlayer is the player that is joining his turn at the moment
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Method that sets the current player of that turn
     * @param currentPlayer is the player that is joining his turn at the moment
     */
    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * Method that returns the class who is going to handle cells in this game session
     * @return cellHandler is the class that handles cells in the game
     */
    public CellsHandler getCellsHandler() {
        return cellsHandler;
    }

    /**
     * Method that returns the class who is going to handle players in this game session
     * @return playerHandler is the class that handles players in the game
     */
    public PlayersHandler getPlayersHandler() {
        return playersHandler;
    }

    /**
     * Method that returns the class who is going to handle workers in this game session
     * @return workersHandler is the class that handles workers in the game
     */
    public WorkersHandler getWorkersHandler() {
        return workersHandler;
    }

    /**
     * Method that returns the class who is going to handle cards in this game session
     * @return cardsHandler is the class that handles cards in the game
     */
    public CardsHandler getCardsHandler() {
        return cardsHandler;
    }

    // TODO : setters/getters that are not listed above if we want to use xml
}