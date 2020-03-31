package it.polimi.ingsw.model;

import it.polimi.ingsw.*;
import it.polimi.ingsw.modelHandlers.CardsHandler;
import it.polimi.ingsw.modelHandlers.CellsHandler;
import it.polimi.ingsw.modelHandlers.PlayersHandler;
import it.polimi.ingsw.modelHandlers.WorkersHandler;

import java.util.ArrayList;

public class GameSession {

    private ArrayList<ClientListener> gamers;
    private int idGame;
    private TurnState currentState;
    private Player currentPlayer;
    private CellsHandler cellsHandler;
    private PlayersHandler playerHandler;
    private WorkersHandler workersHandler;
    private CardsHandler cardsHandler;

    //costruttore non di default

    /**
     * Not default constructor to inizialize a GameSession
     * @param idgame refers to the id of the game
     */
    public GameSession(int idgame){
        idGame=idgame;
        this.cellsHandler = new CellsHandler();
        this.playerHandler = new PlayersHandler();
        this.workersHandler = new WorkersHandler();
        this.cardsHandler = new CardsHandler();
        this.gamers = new ArrayList<ClientListener>(2);
    }


    /**
     * Getter method for gamers arraylist
     * @return gamers ,that is the gamers list
     */
    public ArrayList<ClientListener> getGamers() {
        return gamers;
    }

    /**
     * Method to add players in the game
     * @param player ,is a ClientListner, who is connected in the game
     * @return the result of the add, true if it has gone well, false otherwise
     */
    public boolean addGamer(ClientListener player) {
        return gamers.add(player);
    }

    /**
     * method to remove a player from the game
     * @param player ,is a ClientListner, who is connected in the game
     * @return the result of the remove ì, true if it has gone well, false otherwise
     */
    public boolean removeGamer(ClientListener player){
        return gamers.remove(player);
    }


        /*
        public boolean changeModelDescriptor(GenericDescriptor description){
           return TurnState.handleCommand(description);
        }*/

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
     * Method that initializes the class who is going to handle cells in this game session
     * @return cellHandler is the class that handles cells in the game
     */
    public CellsHandler getCellsHandler() {
        return cellsHandler;
    }

    /**
     * Method that initializes the class who is going to handle players in this game session
     * @return playerHandler is the class that handles players in the game
     */
    public PlayersHandler getPlayerHandler() {
        return playerHandler;
    }

    /**
     * Method that initializes the class who is going to handle workers in this game session
     * @return workersHandler is the class that handles workers in the game
     */
    public WorkersHandler getWorkersHandler() {
        return workersHandler;
    }

    /**
     * Method that initializes the class who is going to handle cards in this game session
     * @return cardsHandler is the class that handles cards in the game
     */
    public CardsHandler getCardsHandler() {
        return cardsHandler;
    }
}