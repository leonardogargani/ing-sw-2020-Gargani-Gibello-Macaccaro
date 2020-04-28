package it.polimi.ingsw.PSP43.client.cli;

import it.polimi.ingsw.PSP43.Color;
import it.polimi.ingsw.PSP43.client.GraphicHandler;
import it.polimi.ingsw.PSP43.client.networkMessages.ChosenCardResponse;
import it.polimi.ingsw.PSP43.client.networkMessages.ChosenCardsResponse;
import it.polimi.ingsw.PSP43.client.networkMessages.PlayersNumberResponse;
import it.polimi.ingsw.PSP43.client.networkMessages.WorkersColorResponse;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.networkMessages.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class CliGraphicHandler extends GraphicHandler {

    private CliBoard board = new CliBoard();
    private CliTopMenu topMenu = new CliTopMenu();
    private CliMiddleMenu middleMenu = new CliMiddleMenu();
    private CliBottomMenu bottomMenu = new CliBottomMenu();


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
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
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

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {


            int playersNumber = 2; // TODO -> INITIALIZE WITH THE INT CONTAINED IN THE REQUEST (number of players)

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

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            bottomMenu.setContent(request.getMessage());

            for (AbstractGodCard card : availableCards) {
                // the name of every God is preceded by its index in the ArrayList
                System.out.printf(" %d - ", availableCards.indexOf(card));
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

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            bottomMenu.setContent(request.getMessage());

            for (Color color : availableColors) {
                // the name of every color is preceded by its index in the ArrayList
                System.out.printf(" %d - ", availableColors.indexOf(color));
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

    }


    /**
     * This method updates the graphics of the client displaying the message of the generic request
     * that needs a boolean as a response, waiting and sending the response through the ClientBG object.
     * @param request message containing the generic boolean request
     */
    @Override
    public void updateMenuChange(RequestMessage request) {

    }


    /**
     * This method updates the cli menu displaying a message that notifies players the game has ended.
     * @param message message containing the sentence to display
     */
    @Override
    public void updateMenuChange(EndGameMessage message) {

    }


    /**
     * This method updates the graphics of the client displaying the message of the request
     * for a change of the nick, since the chosen one is already in use.
     * @param request message that notifies the client that the nick he has just chosen
     *                is already taken
     */
    @Override
    public void updateMenuChange(ChangeNickRequest request) {

    }


    /**
     * This method updates the graphics of the client displaying the message at the top
     * of the screen (used to write that it's someone else's turn).
     * @param message message to be displayed at the top of the screen
     */
    @Override
    public void updateMenuChange(TextMessage message) {

    }


    /**
     * This method updates the graphics of the client displaying players' nicknames,
     * the Gods they've chosen and their workers' color.
     * @param message message containing workers, their colors and the chosen gods
     */
    @Override
    public void updateMenuChange(PlayersListMessage message) {

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
