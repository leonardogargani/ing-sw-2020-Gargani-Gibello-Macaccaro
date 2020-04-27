package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.client.networkMessages.ClientMessage;
import it.polimi.ingsw.PSP43.server.GraphicObserver;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.modelHandlers.CardsHandler;
import it.polimi.ingsw.PSP43.server.modelHandlers.CellsHandler;
import it.polimi.ingsw.PSP43.server.modelHandlers.PlayersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlers.WorkersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;

public class GameSession extends GameSessionObservable {
    private Player currentPlayer;

    private TurnState currentState;
    private TurnState nextState;
    private ArrayList<TurnState> turnMap;

    private CellsHandler cellsHandler;
    private PlayersHandler playersHandler;
    private WorkersHandler workersHandler;
    private CardsHandler cardsHandler;

    private GraphicObserver graphicObserver;

    /**
     * Not default constructor to inizialize a GameSession
     * @param idgame refers to the id of the game
     */
    public GameSession(int idgame) throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException {
        super(idgame);
        this.maxNumPlayers = 1;

        this.turnMap = new ArrayList<>();
        this.turnMap.add(0, new PlayerRegistrationState(this));
        this.currentState = turnMap.get(0);
        super.currentState = turnMap.get(0);
        this.turnMap.add(1, new ChooseCardState(this));
        this.turnMap.add(2, new ChooseWorkerState(this));
        this.turnMap.add(3, new MoveState(this));
        this.turnMap.add(4, new BuildState(this));
        this.turnMap.add(5, new WinState(this));
        this.nextState = null;

        this.cellsHandler = new CellsHandler(this);
        this.playersHandler = new PlayersHandler(this);
        this.workersHandler = new WorkersHandler(this);
        this.cardsHandler = new CardsHandler();
        this.graphicObserver = new GraphicObserver();
    }

    public boolean validateMessage(ClientMessage messageArrived, ClientMessage typeMessageRequested) {
        if (messageArrived.getClass().isInstance(typeMessageRequested)) {
            return true;
        }
        else return false;
    }

    protected void transitToNextState() throws IOException, ClassNotFoundException, WinnerCaughtException {
        int indexNextState = turnMap.indexOf(nextState);
        currentState = turnMap.get(indexNextState);
        super.currentState = turnMap.get(indexNextState);
        currentState.initState();
    }

    public void eliminatePlayer(Player playerEliminated) throws IOException {
        cardsHandler.removeCardToPlayer(playerEliminated.getNickname());
        Player playerToRemove = playersHandler.getPlayer(playerEliminated.getNickname());

        int[] workersToRemove = playerToRemove.getWorkersIdsArray();
        workersHandler.removeWorkers(workersToRemove);

        super.eliminatePlayer(playerEliminated);
    }

    // FROM HERE ARE ALL SETTERS AND GETTERS \\

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

    public void setTurnMap(ArrayList<TurnState> turnMap) {
        this.turnMap = turnMap;
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

    /**
     * This method returns the observer of the graphical part of the game, so that every
     * change to a cell or a worker is notified to the client.
     * @return observer of the graphical part of the game
     */
    public GraphicObserver getGraphicObserver() {
        return graphicObserver;
    }

    // TODO : setters/getters that are not listed above if we want to use xml
}