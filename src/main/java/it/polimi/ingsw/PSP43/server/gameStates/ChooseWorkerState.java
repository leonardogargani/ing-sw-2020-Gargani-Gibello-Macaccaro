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

/**
 * This is the method where the players are asked to choose a color to assign to thei workers
 * and to choose the initial positions of those workers.
 */
public class ChooseWorkerState extends TurnState {
    private static final int FIRSTPOSITION = 0;
    private final ArrayList<Color> availableColors;
    private final HashMap<Player, Color> colorsChosen;

    public ChooseWorkerState(GameSession gameSession) {
        super(gameSession);
        availableColors = new ArrayList<>();
        availableColors.add(Color.ANSI_RED);
        availableColors.add(Color.ANSI_BLUE);
        availableColors.add(Color.ANSI_GREEN);
        colorsChosen = new HashMap<>();
    }

    /**
     * This method initialises the first player of the game, the god-like one (and for this reason the first logged into the
     * game in our conception of the game).
     */
    public void initState() throws IOException, ClassNotFoundException, WinnerCaughtException, InterruptedException {
        GameSession game = super.getGameSession();
        PlayersHandler playersHandler = game.getPlayersHandler();
        game.setCurrentPlayer(playersHandler.getPlayer(FIRSTPOSITION));

        StartGameMessage openingMessage = new StartGameMessage("You are going to choose the color and the initial position for your workers!");
        game.sendBroadCast(openingMessage);

        executeState();
    }


    /**
     * This method asks, one at a time, to the players the color chosen for their workers and also where they want to place them.
     */
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

            // here all the new workers of the player are added into the playersHandler with the color chosen, then the color
            // is removed from available colors
            int[] workersIds = new int[2];
            for (int i=0; i<2; i++) {
                workersIds[i] = workersHandler.addNewWorker(colorResponse.getColor());
            }
            availableColors.remove(colorResponse.getColor());
            colorsChosen.put(currentPlayer, colorResponse.getColor());

            HashMap<Coord, ArrayList<Coord>> hashAvailablePositions;
            ActionRequest placementRequest;

            // here I ask to the player where he wants to place his workers (one at a time I ask him)
            for (int i = 0; i<workersIds.length; i++) {
                ActionResponse response;
                ArrayList<Coord> availablePositions = game.getCellsHandler().findAllCoordsFree();
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

    /**
     * This method has to find the next state and also to send all the infos to the client to display name of players, gods chosen and the color of every player.
     */
    public void findNextState() throws IOException, ClassNotFoundException, WinnerCaughtException, InterruptedException {
        GameSession game = super.getGameSession();
        int indexCurrentState;
        indexCurrentState = game.getTurnMap().indexOf(game.getCurrentState());
        game.setNextState(game.getTurnMap().get(indexCurrentState + 1));

        // here I send all the infos to the client to display name of players, gods chosen and the color of every player
        HashMap<Player, AbstractGodCard> cardsChosen = new HashMap<>();
        for (String s : game.getCardsHandler().getMapOwnersCard().keySet())
            cardsChosen.put(game.getPlayersHandler().getPlayer(s), game.getPlayersHandler().getPlayer(s).getAbstractGodCard());
        PlayersListMessage listOfPlayers = new PlayersListMessage(null, cardsChosen, colorsChosen);
        game.sendBroadCast(listOfPlayers);

        game.transitToNextState();
    }
}
