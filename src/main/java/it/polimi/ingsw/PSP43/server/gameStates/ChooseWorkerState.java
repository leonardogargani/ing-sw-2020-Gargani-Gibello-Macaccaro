package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.server.ClientListener;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.modelHandlers.PlayersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlers.WorkersHandler;
import it.polimi.ingsw.PSP43.server.networkMessages.ChosenColorMessage;

public class ChooseWorkerState extends TurnState {
    private static final int FIRSTPOSITION = 0;

    public ChooseWorkerState(GameSession gameSession) {
        super(gameSession);
    }

    public void initState() {
        GameSession game = super.getGameSession();
        PlayersHandler playersHandler = game.getPlayersHandler();
        game.setCurrentPlayer(playersHandler.getPlayer(FIRSTPOSITION));
        executeState();
    }

    public void executeState() {
        GameSession game = super.getGameSession();
        PlayersHandler playersHandler = game.getPlayersHandler();
        WorkersHandler workersHandler = game.getWorkersHandler();
        Worker[] workersArray = (Worker[]) workersHandler.getWorkers().toArray();

        Player currentPlayer;
        String nicknameCurrentPlayer;
        int latestPosition;
        String latestPlayer;
        ClientListener receiver;
        String message = "Choose a color of the worker you will own.";
        ChosenColorMessage colorMessageReceived;
        int[] workersIds = new int[2];
        Coord[] freeCells;
        Coord coordChosen;

        do {
            currentPlayer = game.getCurrentPlayer();
            nicknameCurrentPlayer = currentPlayer.getNickname();
            receiver = game.getListenersHashMap().get(nicknameCurrentPlayer);
            // TODO : SEND TO THE CLIENT THE MESSAGE REQUESTING THE COLOR OF HIS WORKER

            // TODO : receive the answer message
            colorMessageReceived = null;

            for (int i=0; i<2; i++) {
                workersIds[i] = workersHandler.addNewWorker(colorMessageReceived.getColorChosen());
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

    public void findNextState() {
        GameSession game = super.getGameSession();
        int indexCurrentState;
        indexCurrentState = game.getTurnMap().indexOf(game.getCurrentState());
        game.setNextState(game.getTurnMap().get(indexCurrentState + 1));

        game.transitToNextState();
    }
}
