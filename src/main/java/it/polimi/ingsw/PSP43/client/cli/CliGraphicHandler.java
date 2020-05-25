package it.polimi.ingsw.PSP43.client.cli;

import it.polimi.ingsw.PSP43.Color;
import it.polimi.ingsw.PSP43.client.ClientBG;
import it.polimi.ingsw.PSP43.client.GraphicHandler;
import it.polimi.ingsw.PSP43.client.networkMessages.*;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.controllers.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.networkMessages.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class CliGraphicHandler extends GraphicHandler {

    private final CliBoard board = new CliBoard();
    private final CliTopMenu topMenu = new CliTopMenu();
    private final CliMiddleMenu middleMenu = new CliMiddleMenu();
    private final CliBottomMenu bottomMenu = new CliBottomMenu();
    private final CliInputHandler inputHandler = new CliInputHandler();


    /**
     * Non default constructor that sets the clientGB attribute.
     * @param clientBG clientBG attribute of the client
     */
    public CliGraphicHandler(ClientBG clientBG) {
        super(clientBG);
    }


    /**
     * This method returns the whole board of the cli.
     * @return whole board of the cli
     */
    public CliBoard getBoard() {
        return board;
    }


    /**
     * This method updates the cli board changing the symbol of a cell, based
     * on the fact that a worker can build on a cell.
     * @param cellMessage message containing the cell that has changed (a worker has built on it)
     */
    @Override
    public void updateBoardChange(CellMessage cellMessage) {
        Cell serverCell = cellMessage.getCell();
        CliCell cell = board.getCell(serverCell.getCoord());
        cell.setSymbol(CliCell.SYMBOLS[serverCell.getHeight()]);
        cell.setColor(cellMessage.getCell().getColor());
        this.render();
    }


    /**
     * This method updates the graphics of the client displaying the message of the players number
     * request, waiting and sending the response through the ClientBG object.
     * @param request message containing the request for the number of the players
     * @throws QuitPlayerException exception thrown if a player writes "quit" (ignore capitalization) in the cli
     */
    @Override
    public void updateMenuChange(PlayersNumberRequest request) throws QuitPlayerException {

        bottomMenu.setContent(Screens.PLAYERS_NUMBER_REQUEST.toString());
        int chosenNumber;
        do {
            // I need to show() only the CliTopMenu, containing the request
            bottomMenu.show();
            chosenNumber = inputHandler.requestInputInt();
        } while (chosenNumber != 2 && chosenNumber != 3);
        bottomMenu.clear();
        PlayersNumberResponse response = new PlayersNumberResponse(chosenNumber);
        super.getClientBG().sendMessage(response);
    }


    /**
     * This method updates the graphics of the client displaying the message of the initial cards
     * request, waiting and sending the response through the ClientBG object.
     * @param request message containing the request for the cards chosen for this game
     */
    @Override
    public void updateMenuChange(InitialCardsRequest request) throws QuitPlayerException {

        // display all the cards before making the request
        List<AbstractGodCard> cards = request.getCards();
        for (AbstractGodCard card : cards) {
            // the name of every God is preceded by its index in the ArrayList
            System.out.printf(" %d - ", cards.indexOf(card));
            card.print();
        }

        int playersNumber = request.getNumberOfCard();

        if (playersNumber == 2) {
            bottomMenu.setContent(Screens.INITIAL_2_CARDS_REQUEST.toString());
        } else {
            bottomMenu.setContent(Screens.INITIAL_3_CARDS_REQUEST.toString());
        }

        // I read as many numbers as how many cards have to be chosen (so that the player cannot
        // insert neither less nor more than the requested value)

        ArrayList<Integer> chosenIndexes = new ArrayList<>();
        int chosenIndex;
        for (int i = 0; i < playersNumber; i++) {
            bottomMenu.show();
            System.out.printf("Write a single number and press Enter (%d remaining):\n", playersNumber - i);
            chosenIndex = inputHandler.requestInputInt();
            // avoid repetition of the same Gods
            if (!chosenIndexes.contains(chosenIndex)) {
                chosenIndexes.add(chosenIndex);
            } else {
                System.out.println("You have already chosen this God");
                i--;
            }
        }

        // constructing the ArrayList of cards that needs to be sent as a response
        ArrayList<AbstractGodCard> chosenCards = new ArrayList<>();
        for (Integer index : chosenIndexes) {
            chosenCards.add(cards.get(index));
        }

        bottomMenu.clear();
        ChosenCardsResponse response = new ChosenCardsResponse(chosenCards);
        super.getClientBG().sendMessage(response);
    }


    /**
     * This method updates the graphics of the client displaying the message of the single card
     * request, waiting and sending the response through the ClientBG object.
     * @param request message containing the request for the card chosen by a player
     */
    @Override
    public void updateMenuChange(CardRequest request) throws QuitPlayerException {
        List<AbstractGodCard> availableCards = request.getCards();

        bottomMenu.setContent(Screens.CARD_REQUEST.toString());

        for (AbstractGodCard card : availableCards) {
            // the name of every God is preceded by its index in the ArrayList
            System.out.printf(" [%d] - ", availableCards.indexOf(card));
            card.print();
        }

        int chosenIndex;
        while (true) {
            bottomMenu.show();
            chosenIndex = inputHandler.requestInputInt();
            if (0 <= chosenIndex && chosenIndex < availableCards.size()) {
                // in this case the chosen index is valid, I want to keep its value
                break;
            }
            System.out.println("The number you wrote is not valid");
        }

        System.out.println("Perfect, you have chosen " + availableCards.get(chosenIndex).getGodName() + "!");

        bottomMenu.clear();
        ChosenCardResponse response = new ChosenCardResponse(availableCards.get(chosenIndex));

        super.getClientBG().sendMessage(response);

    }


    /**
     * This method updates the graphics of the client displaying the message of the workers color
     * request, waiting and sending the response through the ClientBG object.
     * @param request message containing the request for the color of player's workers
     */
    @Override
    public void updateMenuChange(WorkersColorRequest request) throws QuitPlayerException {
        List<Color> availableColors = request.getColorsAvailable();

        bottomMenu.setContent(Screens.WORKERS_COLOR_REQUEST.toString());

        for (Color color : availableColors) {
            // the name of every color is preceded by its index in the ArrayList
            System.out.printf(" [%d] - ", availableColors.indexOf(color));
            Color.printName(color);
        }

        int chosenIndex;
        while (true) {
            bottomMenu.show();
            chosenIndex = inputHandler.requestInputInt();
            if (0 <= chosenIndex && chosenIndex < availableColors.size()) {
                // in this case the chosen index is valid, I want to keep its value
                break;
            }
            System.out.println("The number you wrote is not valid");
        }

        bottomMenu.clear();
        WorkersColorResponse response = new WorkersColorResponse(availableColors.get(chosenIndex));
        super.getClientBG().sendMessage(response);

    }


    /**
     * This method updates the graphics of the client displaying the message of the action
     * request, waiting and sending the response through the ClientBG object.
     * @param request message containing the request for the action a player wants to make
     */
    @Override
    public void updateMenuChange(ActionRequest request) throws QuitPlayerException {

        /*
        Given an HashMap<Coord,ArrayList<Coord>>  I need to choose a couple formed by one of
        the keys (Coord) and one of the related elements (Coord) in its value (ArrayList). Use cases:
        - where to first place a worker (server will ignore the key Coord, it only needs the second Coord)
        - where to build
        - where to move a worker
        - ...
        Note that for the first placement of a worker I will have only one ArrayList: I need to select a Coord in it
        (and of course the unique key Coord, there aren't 2 iterations as in the other cases. However, I must do
        the iteration since the method is shared with other scenarios)
         */

        Map<Coord, ArrayList<Coord>> hashMap = request.getCellsAvailable();
        bottomMenu.setContent(request.getMessage());
        topMenu.setContent(Screens.YOUR_TURN.toString());

            // graphically render all the received coordinates as free (yellow background)
            for (Coord startCoord : hashMap.keySet()) {
                for (Coord endCoord : hashMap.get(startCoord)) {
                    board.getCell(endCoord).markAsFree(true);
                }
            }
            this.render();

            int chosenX;
            int chosenY;
            Coord chosenCoord;
            // obtain the second coordinate of the requested couple
            loop: while (true) {
                System.out.print("x: ");
                chosenX = inputHandler.requestInputInt();
                System.out.print("y: ");
                chosenY = inputHandler.requestInputInt();
                for (Coord startCoord : hashMap.keySet()) {
                    for (Coord endCoord : hashMap.get(startCoord)) {
                        if (chosenX == endCoord.getX() && chosenY == endCoord.getY()) {
                            chosenCoord = new Coord(chosenX, chosenY);
                            break loop;
                        }
                    }
                }
                System.out.println("The coordinates you wrote are not valid, try again!");
            }

            // create an ArrayList with the possible values for the first coordinate of the couple
            ArrayList<Coord> possibleStartCoords = new ArrayList<>();
            for (Coord startCoord : hashMap.keySet()) {
                ArrayList<Coord> currentArraylist = hashMap.get(startCoord);
                for (Coord coord : currentArraylist) {
                    if (coord.getX() == chosenCoord.getX() && coord.getY() == chosenCoord.getY()) {
                        possibleStartCoords.add(startCoord);
                    }
                }
            }

            // obtain the first coordinate of the couple (among the 1 or 2 possible ones)
            ActionResponse response;
            if (possibleStartCoords.size() == 1) {
                response = new ActionResponse(possibleStartCoords.get(0), chosenCoord);
            } else {
                System.out.println("Choose the worker you want to make the action perform:");
                for (Coord start : possibleStartCoords) {
                    System.out.printf(" [%d] - (%d, %d)\n", possibleStartCoords.indexOf(start),
                            start.getX(), start.getY());
                }
                int chosenIndex;
                while (true) {
                    chosenIndex = inputHandler.requestInputInt();
                    if (0 <= chosenIndex && chosenIndex < possibleStartCoords.size()) {
                        break;
                    }
                    System.out.println("The number you wrote is not valid, try again!");
                }
                response = new ActionResponse(possibleStartCoords.get(chosenIndex), chosenCoord);
            }

            // graphically render back all the received coordinates as normal
            for (Coord startCoord : hashMap.keySet()) {
                for (Coord endCoord : hashMap.get(startCoord)) {
                    board.getCell(endCoord).markAsFree(false);
                }
            }
            bottomMenu.clear();
        super.getClientBG().sendMessage(response);

    }


    /**
     * This method updates the graphics of the client displaying the message of the generic request
     * that needs a boolean as a response, waiting and sending the response through the ClientBG object.
     * @param request message containing the generic boolean request
     */
    @Override
    public void updateMenuChange(RequestMessage request) throws QuitPlayerException {

        bottomMenu.setContent(request.getMessage());
        this.render();

        int intChoice;
        boolean booleanChoice = false;

        while (true) {
            System.out.println(" [0] YES \n [1] NO ");
            intChoice = inputHandler.requestInputInt();
            if (intChoice == 0 || intChoice == 1) {
                break;
            }
            System.out.println("Choice not valid, try again!");
        }

        switch (intChoice) {
            case 0:
                booleanChoice = true;
                break;
            case 1:
                booleanChoice = false;
                break;
        }

        bottomMenu.clear();
        ResponseMessage response = new ResponseMessage(booleanChoice);
        super.getClientBG().sendMessage(response);

    }


    /**
     * This method updates the cli menu displaying a message that notifies players the game has ended.
     * @param message message containing the sentence to display
     */
    @Override
    public void updateMenuChange(EndGameMessage message) {

        for (int i = 0; i < 15; i++) {
            System.out.println("\n");
        }

        EndGameMessage.EndGameHeader endGameHeader = message.getEndGameHeader();
        switch (endGameHeader) {
            case WINNER:
                bottomMenu.setContent(Screens.WINNER.toString());
                break;
            case LOSER:
                String nick = message.getMessage();
                bottomMenu.setContentWithNick(nick);
                break;
            case DISCONNECTED:
                bottomMenu.setContent(Screens.DISCONNECTED.toString());
                break;
            default:
                System.out.println("An error occurred");
        }

        bottomMenu.show();
    }


    /**
     * This method updates the graphics of the client displaying the message of the request
     * for a change of the nick, since the chosen one is already in use.
     * @param request message that notifies the client that the nick he has just chosen
     *                is already taken
     */
    @Override
    public void updateMenuChange(ChangeNickRequest request) throws QuitPlayerException {
        bottomMenu.setContent(request.getMessage() + " is already in use, choose another nickname:");
        bottomMenu.show();
        String nickname;
        nickname = inputHandler.requestInputString();
        RegistrationMessage message = new RegistrationMessage(nickname);
        super.getClientBG().sendMessage(message);
    }


    /**
     * This method updates the graphics of the client displaying the message at the top
     * of the screen (used to write that it's someone else's turn).
     * @param message message to be displayed at the top of the screen
     */
    @Override
    public void updateMenuChange(TextMessage message) {
        topMenu.setContentWithNick(message.getMessage());
        this.render();
    }


    /**
     * This method updates the graphics of the client displaying players' nicknames,
     * the Gods they've chosen and their workers' color.
     * @param message message containing workers, their colors and the chosen gods
     */
    @Override
    public void updateMenuChange(PlayersListMessage message) {

        // TODO use the String message field to print who lose in the cliBottomMenu

        Map<Player, AbstractGodCard> godsHashMap = message.getPlayers();
        Map<Player, Color> colorsHashmap = message.getColor();

        ArrayList<String> playersInfo = new ArrayList<>();

        for (Player player : colorsHashmap.keySet()) {
            playersInfo.add(
                    colorsHashmap.get(player) + player.getNickname() + Color.RESET + ": " +
                    godsHashMap.get(player).getGodName() + " (" +
                    godsHashMap.get(player).getDescription() + ")");
        }

        middleMenu.setContentWithInfo(playersInfo);
        this.render();

    }


    /**
     * This method updates the graphics of the client displaying, at the beginning of
     * the game, some useful information about the state of the game preparation.
     * @param message message to be displayed
     */
    @Override
    public void updateMenuChange(StartGameMessage message) {
        bottomMenu.setContent(message.getMessage());
        bottomMenu.show();
    }


    /**
     * This method renders all the graphic aspects of the cli.
     */
    public void render() {
        // the cli is made of these four graphical components, printed to the screen in the right order
        topMenu.show();
        middleMenu.show();
        board.show();
        bottomMenu.show();
    }


}
