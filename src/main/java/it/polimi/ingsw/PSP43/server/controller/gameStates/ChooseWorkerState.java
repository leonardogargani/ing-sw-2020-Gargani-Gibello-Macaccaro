package it.polimi.ingsw.PSP43.server.controller.gameStates;

import it.polimi.ingsw.PSP43.server.model.Color;
import it.polimi.ingsw.PSP43.client.graphic.Screens;
import it.polimi.ingsw.PSP43.client.network.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.client.network.networkMessages.StarterResponse;
import it.polimi.ingsw.PSP43.client.network.networkMessages.WorkersColorResponse;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.PlayersHandler;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.WorkersHandler;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.network.networkMessages.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * This is the method where the players are asked to choose a color to assign to thei workers
 * and to choose the initial positions of those workers.
 */
public class ChooseWorkerState extends TurnState {
    private static final int FIRST_POSITION = 0;
    private final ArrayList<Color> availableColors;
    protected String starterPlayer;

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
        game.setCurrentPlayer(playersHandler.getPlayer(FIRST_POSITION)); // the most god-like player is the first arrived ;)
        Player currentPlayer = game.getCurrentPlayer();
        StartGameMessage openingMessage = new StartGameMessage(Screens.CHOOSING_INITIAL_POSITION.toString());
        game.sendBroadCast(openingMessage, currentPlayer.getNickname());

        List<String> nicks = playersHandler.getNickNames(currentPlayer.getNickname());

        if (nicks.size() != 1){
            ChooseStarterMessage chooseStarterMessage =
                    new ChooseStarterMessage(nicks, Screens.STARTER_REQUEST.toString(), TextMessage.PositionInScreen.LOW_CENTER);
            StarterResponse starterResponse;
            try {
                starterResponse = game.sendRequest(chooseStarterMessage, currentPlayer.getNickname(), StarterResponse.class);
            } catch (GameEndedException e) {
                game.setActive();
                return;
            }
            starterPlayer = starterResponse.getStarterPlayerName();
        }
        else {
            starterPlayer = nicks.get(0);
        }

        game.sendMessage(openingMessage, currentPlayer.getNickname());

        currentPlayer = playersHandler.getPlayer(starterPlayer);
        game.setCurrentPlayer(currentPlayer);

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
        Player currentPlayer = game.getCurrentPlayer();
        String nicknameCurrentPlayer = currentPlayer.getNickname();
        String latestPlayer = currentPlayer.getNickname();

        Integer[] workersIds = new Integer[2];
        // In this while cycle all the players are asked to choose the color of their workers, beginning from the Starter
        do {
            if (availableColors.size() != 1) {
                WorkersColorRequest colorRequest = new WorkersColorRequest(Collections.unmodifiableList(availableColors), WorkersColorRequest.WorkersColorRequestHeader.CHOICE);
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

                game.sendMessage(new TextMessage("\nWait for other players that are choosing the color of their workers.\n", TextMessage.PositionInScreen.LOW_CENTER),
                        currentPlayer.getNickname());
            } else {
                // I send this request to skip the choice in the latest player
                WorkersColorRequest workersColorRequest =
                        new WorkersColorRequest(new ArrayList<>(), WorkersColorRequest.WorkersColorRequestHeader.SKIP_CHOICE);
                game.sendMessage(workersColorRequest, currentPlayer.getNickname());

                for (int i = 0; i < 2; i++) {
                    workersIds[i] = workersHandler.addNewWorker(availableColors.get(FIRST_POSITION));
                }
                currentPlayer.setWorkersIdsArray(workersIds);

                game.sendMessage(new TextMessage("\nWait for other players that are choosing the color of their workers.\n", TextMessage.PositionInScreen.LOW_CENTER),
                        currentPlayer.getNickname());
            }

            game.setCurrentPlayer(playersHandler.getNextPlayer(currentPlayer.getNickname()));
            currentPlayer = game.getCurrentPlayer();
            nicknameCurrentPlayer = currentPlayer.getNickname();
        } while (!(latestPlayer.equals(nicknameCurrentPlayer)));

        // In this while cycle all the players are asked to place their workers, beginning from the Starter
        do {
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

            game.sendMessage(new TextMessage("\nWait for other players that are choosing the positions for their workers.\n", TextMessage.PositionInScreen.LOW_CENTER),
                    currentPlayer.getNickname());

            game.setCurrentPlayer(playersHandler.getNextPlayer(currentPlayer.getNickname()));
            currentPlayer = game.getCurrentPlayer();
            nicknameCurrentPlayer = currentPlayer.getNickname();
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