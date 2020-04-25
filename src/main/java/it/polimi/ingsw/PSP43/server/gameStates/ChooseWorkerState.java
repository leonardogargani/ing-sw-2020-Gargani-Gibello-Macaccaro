package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.Color;
import it.polimi.ingsw.PSP43.client.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.client.networkMessages.ClientMessage;
import it.polimi.ingsw.PSP43.client.networkMessages.WorkerColorResponse;
import it.polimi.ingsw.PSP43.server.ClientListener;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.modelHandlers.PlayersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlers.WorkersHandler;
import it.polimi.ingsw.PSP43.server.networkMessages.PossibleMovesMessage;
import it.polimi.ingsw.PSP43.server.networkMessages.TextMessage;
import it.polimi.ingsw.PSP43.server.networkMessages.WorkerColorRequest;

import java.io.IOException;
import java.util.ArrayList;

public class ChooseWorkerState extends TurnState {
    private static final int FIRSTPOSITION = 0;
    private ArrayList<Color> availableColors;

    public ChooseWorkerState(GameSession gameSession) {
        super(gameSession);
        availableColors = new ArrayList<>();
        // TODO : why is the color constructor private?
    }

    public void initState() throws IOException, ClassNotFoundException {
        GameSession game = super.getGameSession();
        PlayersHandler playersHandler = game.getPlayersHandler();
        game.setCurrentPlayer(playersHandler.getPlayer(FIRSTPOSITION));

        TextMessage openingMessage = new TextMessage("You are going to choose the color and the initial position for your workers!");
        game.sendBroadCast(openingMessage);

        executeState();
    }

    public void executeState() throws IOException, ClassNotFoundException {
        GameSession game = super.getGameSession();
        PlayersHandler playersHandler = game.getPlayersHandler();
        WorkersHandler workersHandler = game.getWorkersHandler();
        Worker[] workersArray = (Worker[]) workersHandler.getWorkers().toArray();

        Player currentPlayer = game.getCurrentPlayer();
        String nicknameCurrentPlayer = currentPlayer.getNickname();
        int latestPosition;
        String latestPlayer;
        String message = "Choose a color of the worker you will own.";
        WorkerColorRequest colorRequest = new WorkerColorRequest("Choose a color of the worker you will own.", availableColors);
        WorkerColorResponse colorResponse = null;
        boolean delivered;
        do {
            delivered = game.sendRequest(colorRequest, nicknameCurrentPlayer, colorResponse);
        } while (!delivered);

        int[] workersIds = new int[2];

        do {
            message = "Choose where to place your workers.";
            ArrayList<Coord> availablePositions = game.getCellsHandler().findAllCellsFree();
            PossibleMovesMessage placementRequest = new PossibleMovesMessage("Choose a color of the worker you will own.");
            ActionResponse response = null;
            do {
                delivered = game.sendRequest(placementRequest, nicknameCurrentPlayer, response);
            } while (!delivered);
            // TODO : receive the answer message
            Color colorMessageReceived = null;

            for (int i=0; i<2; i++) {
                workersIds[i] = workersHandler.addNewWorker(colorMessageReceived);
            }
            currentPlayer.setWorkersIdsArray(workersIds[0], workersIds[1]);

            for (int i : workersIds) {
                freeCells = game.getCellsHandler().findAllCellsFree();
                // TODO : ask to the player for a position for the player
                coordChosen = null;
                workersHandler.getWorker(i).setCurrentPosition(coordChosen);
            }

            latestPosition = playersHandler.getNumOfPlayers()-1;
            latestPlayer = playersHandler.getPlayer(latestPosition).getNickname();
            game.setCurrentPlayer(playersHandler.getNextPlayer(currentPlayer.getNickname()));
        } while (!latestPlayer.equals(currentPlayer.getNickname()));

        // TODO : send a message to all the players that the game is starting!!
        findNextState();
    }

    public void findNextState() throws IOException, ClassNotFoundException {
        GameSession game = super.getGameSession();
        int indexCurrentState;
        indexCurrentState = game.getTurnMap().indexOf(game.getCurrentState());
        game.setNextState(game.getTurnMap().get(indexCurrentState + 1));

        game.transitToNextState();
    }
}
