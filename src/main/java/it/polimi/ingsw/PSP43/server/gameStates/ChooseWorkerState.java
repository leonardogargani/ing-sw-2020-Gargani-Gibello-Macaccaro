package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.Color;
import it.polimi.ingsw.PSP43.client.Screens;
import it.polimi.ingsw.PSP43.client.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.client.networkMessages.WorkersColorResponse;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.modelHandlers.PlayersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlers.WorkersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.networkMessages.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * This is the method where the players are asked to choose a color to assign to thei workers
 * and to choose the initial positions of those workers.
 */
public class ChooseWorkerState extends TurnState {
    private static final int FIRST_POSITION = 0;
    private final ArrayList<Color> availableColors;

    public ChooseWorkerState(GameSession gameSession) {
        super(gameSession, TurnName.CHOOSE_WORKER_STATE);
        availableColors = new ArrayList<>();
        availableColors.add(Color.ANSI_RED);
        availableColors.add(Color.ANSI_BLUE);
        availableColors.add(Color.ANSI_GREEN);
    }

    /**
     * This method initialises the first player of the game, the god-like one (and for this reason the first logged into the
     * game in our conception of the game).
     */
    public void initState() {
        GameSession game = super.getGameSession();
        PlayersHandler playersHandler = game.getPlayersHandler();
        game.setCurrentPlayer(playersHandler.getPlayer(FIRST_POSITION + 1));
        Player currentPlayer = game.getCurrentPlayer();

        StartGameMessage openingMessage = new StartGameMessage(Screens.CHOOSING_INITIAL_POSITION.toString());
        game.sendBroadCast(openingMessage, currentPlayer.getNickname());

        ArrayList<String> nicks = new ArrayList<>();
        for (int i = 0; i < playersHandler.getNumOfPlayers(); i++) {
            nicks.add(playersHandler.getPlayer(i).getNickname());
        }
        ChooseStarterMessage chooseStarterMessage =
                new ChooseStarterMessage(nicks, Screens.STARTER_REQUEST.toString(), TextMessage.PositionInScreen.LOW_CENTER);
        // TODO add choose card response

        executeState();
    }


    /**
     * This method asks, one at a time, to the players the color chosen for their workers
     * and also where they want to place them.
     */
    public void executeState() {
        GameSession game = super.getGameSession();
        PlayersHandler playersHandler = game.getPlayersHandler();
        WorkersHandler workersHandler = game.getWorkersHandler();
        String latestPlayer = playersHandler.getPlayer(FIRST_POSITION).getNickname();
        ;
        Player currentPlayer = game.getCurrentPlayer();
        String nicknameCurrentPlayer = currentPlayer.getNickname();
        Player godLikePlayer = playersHandler.getPlayer(FIRST_POSITION);

        Integer[] workersIds = new Integer[2];
        // In this while cycle all the players are asked to choose the color of their workers, beginning from the Starter
        do {
            if (!latestPlayer.equals(currentPlayer.getNickname())) {
                WorkersColorRequest colorRequest = new WorkersColorRequest(Collections.unmodifiableList(availableColors));
                WorkersColorResponse colorResponse;
                do {
                    try {
                        colorResponse = game.sendRequest(colorRequest, nicknameCurrentPlayer, WorkersColorResponse.class);
                    } catch (GameEndedException e) {
                        game.setActive();
                        return;
                    }
                } while (colorResponse == null);

                // here all the new workers of the player are added into the workersHandler with the color chosen, then the color
                // is removed from available colors
                for (int i = 0; i < 2; i++) {
                    workersIds[i] = workersHandler.addNewWorker(colorResponse.getColor());
                }
                availableColors.remove(colorResponse.getColor());

                // then the ids of the workers are set into the related owner
                currentPlayer.setWorkersIdsArray(workersIds);

                game.sendMessage(new StartGameMessage("\nWait for other players that are " +
                        "choosing the color of their workers.\n"), currentPlayer.getNickname());
            } else {
                for (int i = 0; i < 2; i++) {
                    workersIds[i] = workersHandler.addNewWorker(availableColors.get(FIRST_POSITION));
                }
                godLikePlayer.setWorkersIdsArray(workersIds);

                game.sendMessage(new StartGameMessage("\nWait for other players that are " +
                        "choosing the positions of their workers.\n"), currentPlayer.getNickname());
                break;
            }

            game.setCurrentPlayer(playersHandler.getNextPlayer(currentPlayer.getNickname()));
            currentPlayer = game.getCurrentPlayer();
            nicknameCurrentPlayer = currentPlayer.getNickname();
        } while (true);

        game.setCurrentPlayer(playersHandler.getPlayer(FIRST_POSITION + 1));
        // In this while cycle all the players are asked to place their workers, beginning from the Starter
        do {
            currentPlayer = playersHandler.getPlayer(game.getCurrentPlayer().getNickname());
            nicknameCurrentPlayer = currentPlayer.getNickname();

            workersIds = currentPlayer.getWorkersIdsArray();

            HashMap<Coord, ArrayList<Coord>> hashAvailablePositions;
            ActionRequest placementRequest;

            // here I ask to the player where he wants to place his workers (one at a time I ask him)
            for (int i = 0; i < workersIds.length; i++) {
                ActionResponse response;
                ArrayList<Coord> availablePositions = game.getCellsHandler().findAllFreeCoords();
                hashAvailablePositions = new HashMap<>();
                hashAvailablePositions.put(new Coord(0, 0), availablePositions);
                placementRequest = new ActionRequest("Choose where to place your worker " + i + " .",
                        Collections.unmodifiableMap(hashAvailablePositions));
                do {
                    try {
                        response = game.sendRequest(placementRequest, nicknameCurrentPlayer, ActionResponse.class);
                    } catch (GameEndedException e) {
                        game.setActive();
                        return;
                    }
                } while (response == null);
                Coord coordChosen = response.getPosition();

                game.getWorkersHandler().setInitialPosition(workersIds[i], coordChosen);
            }

            latestPlayer = playersHandler.getPlayer(FIRST_POSITION + 1).getNickname();

            game.sendMessage(new StartGameMessage("\nWait for other players that are " +
                    "choosing the positions for their workers.\n"), currentPlayer.getNickname());

            game.setCurrentPlayer(playersHandler.getNextPlayer(currentPlayer.getNickname()));
        } while (!latestPlayer.equals(game.getCurrentPlayer().getNickname()));

        findNextState();
    }

    /**
     * This method finds the next turn of the game (saving it into a variable in the GameSession database),
     * which will be always a MoveState. It sends also to all the players the infos to display
     * name of players, gods chosen and the color of every player.
     */
    public void findNextState() {
        GameSession game = super.getGameSession();

        for (TurnState t : game.getTurnStateMap()) {
            if (t.getTurnName() == TurnName.MOVE_STATE)
                game.setNextState(t);
        }

        // here I send all the infos to the client to display name of players, gods chosen and the color of every player
        PlayersListMessage listOfPlayers = new PlayersListMessage(null, game);
        game.sendBroadCast(listOfPlayers);
    }
}