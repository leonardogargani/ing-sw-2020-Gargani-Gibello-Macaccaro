package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.Color;
import it.polimi.ingsw.PSP43.client.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.client.networkMessages.WorkersColorResponse;
import it.polimi.ingsw.PSP43.server.DataToAction;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlers.PlayersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlers.WorkersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;
import it.polimi.ingsw.PSP43.server.networkMessages.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ChooseWorkerState extends TurnState {
    private static final int FIRSTPOSITION = 0;
    private ArrayList<Color> availableColors;
    private HashMap<Player, Color> colorsChosen;

    public ChooseWorkerState(GameSession gameSession) {
        super(gameSession);
        availableColors = new ArrayList<>();
        availableColors.add(Color.ANSI_RED);
        availableColors.add(Color.ANSI_BLUE);
        availableColors.add(Color.ANSI_GREEN);
        colorsChosen = new HashMap<>();
    }

    public void initState() throws IOException, ClassNotFoundException, WinnerCaughtException, InterruptedException {
        GameSession game = super.getGameSession();
        PlayersHandler playersHandler = game.getPlayersHandler();
        game.setCurrentPlayer(playersHandler.getPlayer(FIRSTPOSITION));

        StartGameMessage openingMessage = new StartGameMessage("You are going to choose the color and the initial position for your workers!");
        game.sendBroadCast(openingMessage);

        executeState();
    }

    public void executeState() throws IOException, ClassNotFoundException, WinnerCaughtException, InterruptedException {
        GameSession game = super.getGameSession();
        PlayersHandler playersHandler = game.getPlayersHandler();
        WorkersHandler workersHandler = game.getWorkersHandler();
        String latestPlayer;
        Player currentPlayer;

        do {
            currentPlayer = game.getCurrentPlayer();
            String nicknameCurrentPlayer = currentPlayer.getNickname();
            int latestPosition;
            WorkersColorRequest colorRequest = new WorkersColorRequest("Choose a color of the worker you will own.", availableColors);
            WorkersColorResponse colorResponse;
            do {
                colorResponse = game.sendRequest(colorRequest, nicknameCurrentPlayer, WorkersColorResponse.class);
            } while (colorResponse == null);

            int[] workersIds = new int[2];
            for (int i=0; i<2; i++) {
                workersIds[i] = workersHandler.addNewWorker(colorResponse.getColor());
            }
            availableColors.remove(colorResponse.getColor());
            colorsChosen.put(currentPlayer, colorResponse.getColor());

            HashMap<Coord, ArrayList<Coord>> hashAvailablePositions = null;
            ActionRequest placementRequest;

            for (int i = 0; i<workersIds.length; i++) {
                ActionResponse response;
                ArrayList<Coord> availablePositions = game.getCellsHandler().findAllCellsFree();
                hashAvailablePositions = new HashMap<>();
                hashAvailablePositions.put(new Coord(0, 0), availablePositions);
                placementRequest = new ActionRequest("Choose where to place your worker " + i + " .",
                        hashAvailablePositions);
                do {
                    response = game.sendRequest(placementRequest, nicknameCurrentPlayer, ActionResponse.class);
                } while(response==null);
                Coord coordChosen = response.getPosition();
                DataToAction data = new DataToAction(game, currentPlayer, workersHandler.getWorker(workersIds[i]), coordChosen);
                currentPlayer.getAbstractGodCard().move(data);
            }
            latestPosition = playersHandler.getNumOfPlayers()-1;
            latestPlayer = playersHandler.getPlayer(latestPosition).getNickname();
            game.setCurrentPlayer(playersHandler.getNextPlayer(currentPlayer.getNickname()));
        } while (!latestPlayer.equals(currentPlayer.getNickname()));

        findNextState();
    }

    public void findNextState() throws IOException, ClassNotFoundException, WinnerCaughtException, InterruptedException {
        GameSession game = super.getGameSession();
        int indexCurrentState;
        indexCurrentState = game.getTurnMap().indexOf(game.getCurrentState());
        game.setNextState(game.getTurnMap().get(indexCurrentState + 1));
        HashMap<Player, AbstractGodCard> cardsChosen = new HashMap<>();
        for (String s : game.getCardsHandler().getMapOwnerCard().keySet()) {
            cardsChosen.put(game.getPlayersHandler().getPlayer(s), game.getPlayersHandler().getPlayer(s).getAbstractGodCard());
        }
        PlayersListMessage listOfPlayers = new PlayersListMessage(null, cardsChosen, colorsChosen);
        game.sendBroadCast(listOfPlayers);
        game.transitToNextState();
    }
}
