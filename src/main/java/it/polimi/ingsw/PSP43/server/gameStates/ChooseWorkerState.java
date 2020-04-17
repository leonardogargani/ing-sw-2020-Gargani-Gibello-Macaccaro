package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.server.ClientListener;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.modelHandlers.PlayersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlers.WorkersHandler;
import it.polimi.ingsw.PSP43.server.networkMessages.ChosenColorMessage;

public class ChooseWorkerState extends TurnState {
    public ChooseWorkerState(GameSession gameSession) {
        super(gameSession);
    }

    public void initState() {
        GameSession game = super.getGameSession();
        PlayersHandler playersHandler = game.getPlayersHandler();
        WorkersHandler workersHandler = game.getWorkersHandler();
        Worker[] workersArray = (Worker[]) workersHandler.getWorkers().toArray();

        Player currentPlayer = game.getCurrentPlayer();
        ClientListener receiver = game.getListenersHashMap().get(currentPlayer.getNickname());
        String message = "Choose a color of the worker you will own.";
        // TODO : SEND TO THE CLIENT THE MESSAGE REQUESTING THE COLOR OF HIS WORKER

        // TODO : receive the answer message
        ChosenColorMessage colorMessageReceived = null;
        int[] workersIds = new int[2];
        for (int i=0; i<2; i++) {
            workersIds[i] = workersHandler.addNewWorker(colorMessageReceived.getColorChosen());
        }
        currentPlayer.setWorkersIdsArray(workersIds[0], workersIds[1]);

        for (int i : workersIds) {
            Coord[] freeCells = game.getCellsHandler().findAllCellsFree();
            // TODO : ask to the player for a position for the player
            Coord coordChosen = null;
            workersHandler.getWorker(i).setCurrentPosition(coordChosen);
        }

        int latestPosition = playersHandler.getNumOfPlayers()-1;
        String latestPlayer = playersHandler.getPlayer(latestPosition).getNickname();
        if (latestPlayer.equals(currentPlayer.getNickname())) {
            game.setCurrentState(game.getTurnMap().get(3));
            // TODO : how to choose the first player to move?
            game.setCurrentPlayer(playersHandler.getPlayer(0));
            game.initNextState();
            // TODO : send a message to all the players that the game is starting!!
        }
        else {
            game.setCurrentPlayer(playersHandler.getNextPlayer(currentPlayer.getNickname()));
        }
        game.initNextState();
    }
}
