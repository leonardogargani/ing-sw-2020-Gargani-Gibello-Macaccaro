package it.polimi.ingsw.PSP43.server.model;

import it.polimi.ingsw.PSP43.server.ClientListener;
import it.polimi.ingsw.PSP43.server.gameStates.PlayerRegistrationState;
import it.polimi.ingsw.PSP43.server.gameStates.TurnState;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlers.CardsHandler;
import it.polimi.ingsw.PSP43.server.modelHandlers.CellsHandler;
import it.polimi.ingsw.PSP43.server.modelHandlers.PlayersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlers.WorkersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.FullGameSessionException;
import it.polimi.ingsw.PSP43.server.networkMessages.GenericMessage;
import it.polimi.ingsw.PSP43.server.networkMessages.RegistrationMessage;

import java.util.ArrayList;
import java.util.HashMap;

public class GameSession {
    private int idGame;
    private boolean isFull;
    private HashMap<String, ClientListener> listenersHashMap;
    private TurnState currentState;
    private HashMap<String, ArrayList<TurnState>> turnMap;
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
        this.turnMap = new HashMap<>(2);
        this.cellsHandler = new CellsHandler();
        this.playersHandler = new PlayersHandler();
        this.workersHandler = new WorkersHandler(this);
        this.cardsHandler = new CardsHandler();
    }

    /**
     * This method handles messages arriving from the clients in different ways depending on the current state of the game
     * @param message The message containing the command to handle (i.e. Registration data, move data)
     */
    public void handleCommand(GenericMessage message) {
        currentState.handleCommand(message);
    }

    public void setFull(boolean full) {
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
     * Method to add players in the game
     * @param player ,is a ClientListner, who is connected in the game
     * @throws FullGameSessionException if the game is already full of players
     */
    public void addGamer(ClientListener player, RegistrationMessage message) throws FullGameSessionException {
        if (listenersHashMap.size()!=0 && !(currentState instanceof PlayerRegistrationState)) throw new FullGameSessionException();
        ArrayList<TurnState> actual = new ArrayList<>();
        actual.add(new PlayerRegistrationState(this));
        this.currentState = actual.get(0);
        turnMap.put(message.getNickPlayerId(), actual);
        listenersHashMap.put(message.getNickPlayerId(), player);
    }

    /**
     * method to remove a player from the game
     * @param message The message that contains all the information about the player to remove
     */
    public void removeGamer(RegistrationMessage message){
        if (message.isUnregistration()) {
            listenersHashMap.remove(message.getNickPlayerId());
        }
    }

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

    /**
     * This method returns the HashMap representing the correspondence between players and the related game turns
     * @return The HashMap representing the correspondence between players and the related game turns
     */
    public HashMap<String, ArrayList<TurnState>> getTurnMap() {
        return (HashMap<String, ArrayList<TurnState>>) turnMap.clone();
    }

    public void setTurnMap(String nickName, AbstractGodCard abstractGodCardChosen) {};

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
}