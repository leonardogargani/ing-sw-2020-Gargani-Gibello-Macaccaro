package it.polimi.ingsw.PSP43.client.cli;

import it.polimi.ingsw.PSP43.Color;
import it.polimi.ingsw.PSP43.client.ClientBG;
import it.polimi.ingsw.PSP43.client.GraphicHandler;
import it.polimi.ingsw.PSP43.client.networkMessages.*;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.networkMessages.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;


public class CliGraphicHandler extends GraphicHandler {

    private final CliBoard board = new CliBoard();
    private final CliTopMenu topMenu = new CliTopMenu();
    private final CliMiddleMenu middleMenu = new CliMiddleMenu();
    private final CliBottomMenu bottomMenu = new CliBottomMenu();


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
     * This method updates the cli or the gui changing the color of a cell, based
     * on the color of the worker that has been moved.
     * @param workerMessage message containing the worker that has been moved
     */
    @Override
    public void updateBoardChange(WorkerMessage workerMessage) {
        Worker worker = workerMessage.getWorker();
        CliCell newCell = board.getCell(worker.getCurrentPosition());

        // I need to "free" the previous position only if the worker was already on the board:
        // if it has just been place it doesn't have a previous position
        if (worker.getPreviousPosition() != null) {
            CliCell oldCell = board.getCell(worker.getPreviousPosition());
            oldCell.setColor(Color.ANSI_WHITE);
        }

        newCell.setColor(worker.getColor());
        this.render();
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
        this.render();
    }


    /**
     * This method updates the graphics of the client displaying the message of the players number
     * request, waiting and sending the response through the ClientBG object.
     * @param request message containing the request for the number of the players
     */
    @Override
    public void updateMenuChange(PlayersNumberRequest request) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            bottomMenu.setContent(request.getMessage());
            int chosenNumber;
            do {
                // I need to show() only the CliTopMenu, containing the request
                bottomMenu.show();
                chosenNumber = Integer.parseInt(reader.readLine());
            } while (chosenNumber != 2 && chosenNumber != 3);
            PlayersNumberResponse response = new PlayersNumberResponse(chosenNumber);
            super.getClientBG().sendMessage(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * This method updates the graphics of the client displaying the message of the initial cards
     * request, waiting and sending the response through the ClientBG object.
     * @param request message containing the request for the cards chosen for this game
     */
    @Override
    public void updateMenuChange(InitialCardsRequest request) {

        // display all the cards before making the request
        ArrayList<AbstractGodCard> cards = request.getCards();
        for (AbstractGodCard card : cards) {
            // the name of every God is preceded by its index in the ArrayList
            System.out.printf(" %d - ", cards.indexOf(card));
            card.print();
        }
        bottomMenu.setContent(request.getMessage());

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {

            int playersNumber = request.getNumberOfCard();

            // I read as many numbers as how many cards have to be chosen (so that the player cannot
            // insert neither less nor more than the requested value)

            ArrayList<Integer> chosenIndexes = null;
            int chosenIndex;
            for (int i = 0; i < playersNumber; i++) {
                bottomMenu.show();
                System.out.printf("Write a single number and press Enter (%d remaining): ", playersNumber - i);
                chosenIndex = Integer.parseInt(reader.readLine());
                // avoid repetition of the same Gods
                if (!chosenIndexes.contains(chosenIndex)) {
                    chosenIndexes.add(chosenIndex);
                } else {
                    System.out.println("You have already chosen this God");
                    i--;
                }
            }

            // constructing the ArrayList of cards that needs to be sent as a response
            ArrayList<AbstractGodCard> chosenCards = null;
            for (Integer index : chosenIndexes) {
                chosenCards.add(cards.get(index));
            }

            ChosenCardsResponse response = new ChosenCardsResponse(chosenCards);
            super.getClientBG().sendMessage(response);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * This method updates the graphics of the client displaying the message of the single card
     * request, waiting and sending the response through the ClientBG object.
     * @param request message containing the request for the card chosen by a player
     */
    @Override
    public void updateMenuChange(CardRequest request) {
        ArrayList<AbstractGodCard> availableCards = request.getCards();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            bottomMenu.setContent(request.getMessage());

            for (AbstractGodCard card : availableCards) {
                // the name of every God is preceded by its index in the ArrayList
                System.out.printf(" [%d] - ", availableCards.indexOf(card));
                card.print();
            }

            int chosenIndex;
            while (true) {
                bottomMenu.show();
                chosenIndex = Integer.parseInt(reader.readLine());
                if (0 < chosenIndex && chosenIndex < availableCards.size()) {
                    // in this case the chosen index is valid, I want to keep its value
                    break;
                }
                System.out.println("The number you wrote is not valid");
            }

            ChosenCardResponse response = new ChosenCardResponse(availableCards.get(chosenIndex));
            super.getClientBG().sendMessage(response);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * This method updates the graphics of the client displaying the message of the workers color
     * request, waiting and sending the response through the ClientBG object.
     * @param request message containing the request for the color of player's workers
     */
    @Override
    public void updateMenuChange(WorkersColorRequest request) {
        ArrayList<Color> availableColors = request.getColorsAvailable();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            bottomMenu.setContent(request.getMessage());

            for (Color color : availableColors) {
                // the name of every color is preceded by its index in the ArrayList
                System.out.printf(" [%d] - ", availableColors.indexOf(color));
                Color.printName(color);
            }

            int chosenIndex;
            while (true) {
                bottomMenu.show();
                chosenIndex = Integer.parseInt(reader.readLine());
                if (0 < chosenIndex && chosenIndex < availableColors.size()) {
                    // in this case the chosen index is valid, I want to keep its value
                    break;
                }
                System.out.println("The number you wrote is not valid");
            }

            WorkersColorResponse response = new WorkersColorResponse(availableColors.get(chosenIndex));
            super.getClientBG().sendMessage(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * This method updates the graphics of the client displaying the message of the action
     * request, waiting and sending the response through the ClientBG object.
     * @param request message containing the request for the action a player wants to make
     */
    @Override
    public void updateMenuChange(ActionRequest request) {

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

        HashMap<Coord,ArrayList<Coord>> hashMap = request.getCellsAvailable();
        bottomMenu.setContent(request.getMessage());

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {

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
                chosenX = Integer.parseInt(reader.readLine());
                System.out.print("y: ");
                chosenY = Integer.parseInt(reader.readLine());
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
                if (hashMap.get(startCoord).contains(chosenCoord)) {
                    possibleStartCoords.add(startCoord);
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
                    chosenIndex = Integer.parseInt(reader.readLine());
                    if (0 < chosenIndex && chosenIndex < possibleStartCoords.size()) {
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
            this.render();

            super.getClientBG().sendMessage(response);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * This method updates the graphics of the client displaying the message of the generic request
     * that needs a boolean as a response, waiting and sending the response through the ClientBG object.
     * @param request message containing the generic boolean request
     */
    @Override
    public void updateMenuChange(RequestMessage request) {

        bottomMenu.setContent(request.getMessage());
        this.render();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            int intChoice;
            boolean booleanChoice = false;

            while (true) {
                System.out.println(" [0] YES \n [1] NO ");
                intChoice = Integer.parseInt(reader.readLine());
                if (intChoice == 0 || intChoice == 1) {
                    break;
                }
                System.out.println("Choice not valid, choose again");
            }

            switch (intChoice) {
                case 0:
                    booleanChoice = true;
                    break;
                case 1:
                    booleanChoice = false;
                    break;
            }

            ResponseMessage response = new ResponseMessage(booleanChoice);
            super.getClientBG().sendMessage(response);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * This method updates the cli menu displaying a message that notifies players the game has ended.
     * @param message message containing the sentence to display
     */
    @Override
    public void updateMenuChange(EndGameMessage message) {
        bottomMenu.setContent(message.getMessage());
        bottomMenu.show();
    }


    /**
     * This method updates the graphics of the client displaying the message of the request
     * for a change of the nick, since the chosen one is already in use.
     * @param request message that notifies the client that the nick he has just chosen
     *                is already taken
     */
    @Override
    public void updateMenuChange(ChangeNickRequest request) {
        bottomMenu.setContent(request.getMessage());
        bottomMenu.show();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            String nickname;
            System.out.print("Choose another nickname:");
            nickname = reader.readLine();
            RegistrationMessage message = new RegistrationMessage(nickname);
            super.getClientBG().sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

        HashMap<Player, AbstractGodCard> godsHashMap = message.getPlayers();
        HashMap<Player, Color> colorsHashmap = message.getColor();

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
     *
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
    @Override
    public void render() {
        // the cli is made of these four graphical components, printed to the screen in the right order
        topMenu.show();
        middleMenu.show();
        board.show();
        bottomMenu.show();
    }


}
