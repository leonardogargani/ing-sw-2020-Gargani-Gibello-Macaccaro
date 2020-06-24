package it.polimi.ingsw.PSP43.client.graphic.gui.controllers.game;

import it.polimi.ingsw.PSP43.client.graphic.gui.controllers.AbstractController;
import it.polimi.ingsw.PSP43.client.network.ClientBG;
import it.polimi.ingsw.PSP43.client.network.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.client.network.networkMessages.LeaveGameMessage;
import it.polimi.ingsw.PSP43.client.network.networkMessages.ResponseMessage;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.network.networkMessages.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.Map;

public class MatchController extends AbstractController {

    @FXML private Label topMenu;
    @FXML private Label bottomMenu;
    @FXML private Label player1Nick;
    @FXML private Label player2Nick;
    @FXML private Label player3Nick;
    @FXML private Label player1CardDescription;
    @FXML private ImageView player1Card;
    @FXML private ImageView player2Card;
    @FXML private ImageView player3Card;
    @FXML private ImageView player1Worker;
    @FXML private ImageView player2Worker;
    @FXML private ImageView player3Worker;
    @FXML private ImageView confirm;
    @FXML private ImageView delete;
    @FXML private ImageView exitButton;
    @FXML private ImageView helperButton;
    @FXML private ImageView c00;
    @FXML private ImageView c10;
    @FXML private ImageView c20;
    @FXML private ImageView c30;
    @FXML private ImageView c40;
    @FXML private ImageView c01;
    @FXML private ImageView c11;
    @FXML private ImageView c21;
    @FXML private ImageView c31;
    @FXML private ImageView c41;
    @FXML private ImageView c02;
    @FXML private ImageView c12;
    @FXML private ImageView c22;
    @FXML private ImageView c32;
    @FXML private ImageView c42;
    @FXML private ImageView c03;
    @FXML private ImageView c13;
    @FXML private ImageView c23;
    @FXML private ImageView c33;
    @FXML private ImageView c43;
    @FXML private ImageView c04;
    @FXML private ImageView c14;
    @FXML private ImageView c24;
    @FXML private ImageView c34;
    @FXML private ImageView c44;

    private static ClientBG clientBG;
    private static ActionRequest actionRequest;
    private static BoardController boardController;
    private static PlayersController playersController;
    private static Label topLabel;
    private static Label bottomLabel;
    //Initialization moved in initialize method
    private static int counter;
    private static int numberOfActionRequestArrived;
    private Coord startPosition;
    private static ImageView confirmButton;
    private static ImageView deleteButton;

    /**
     * Initialize method for this controller.
     */
    public void initialize() {
        actionRequest = null;
        numberOfActionRequestArrived = 0;
        counter = 0;
        ImageView[][] board = new ImageView[5][5];
        board[0] = new ImageView[]{c00, c01, c02, c03, c04};
        board[1] = new ImageView[]{c10, c11, c12, c13, c14};
        board[2] = new ImageView[]{c20, c21, c22, c23, c24};
        board[3] = new ImageView[]{c30, c31, c32, c33, c34};
        board[4] = new ImageView[]{c40, c41, c42, c43, c44};
        boardController = new BoardController(board);
        Label[] labels = new Label[]{player1Nick, player2Nick, player3Nick, player1CardDescription};
        ImageView[] images = new ImageView[]{player1Card, player2Card, player3Card, player1Worker, player2Worker, player3Worker};
        playersController = new PlayersController(labels, images, getNick());
        exitButton.setId("decision-button");
        helperButton.setId("decision-button");
        confirm.setId("decision-button");
        delete.setId("decision-button");
        player2Card.setId("decision-button");
        player3Card.setId("decision-button");
        clientBG = AbstractController.getClientBG();
        topMenu.setId("top-label");
        bottomMenu.setId("bottom-label");
        player1Nick.setId("player1-label");
        player2Nick.setId("player2-label");
        player3Nick.setId("player3-label");
        player1CardDescription.setId("player1CardDescription-label");
        confirmButton = confirm;
        confirm.setDisable(true);
        deleteButton = delete;
        delete.setDisable(true);
        topLabel = topMenu;
        bottomLabel = bottomMenu;
    }


    /**
     * Mouse event catcher for board cells.
     * @param event is a generic mouse event on a cell of the board
     */
    @FXML
    public void onCellClicked(MouseEvent event) {
        if (actionRequest != null) {
            Map<Coord, ArrayList<Coord>> hashMap = actionRequest.getCellsAvailable();
            ImageView img = (ImageView) event.getSource();
            if (numberOfActionRequestArrived <= 1) {
                ActionResponse response = boardController.checkAvailability(hashMap, img);
                if (response != null) {
                    clientBG.sendMessage(response);
                    bottomLabel.setText("");
                    numberOfActionRequestArrived++;
                    actionRequest=null;
                } else bottomLabel.setText("You can't go there!\nChose another position to place your worker");
            } else if (counter == 0) {
                startPosition = boardController.checkWorkerChosen(hashMap, img);
                if (startPosition != null) {
                    boardController.removeUnderlineWorkers(hashMap);
                    bottomLabel.setText(actionRequest.getMessage());
                    counter++;
                    boardController.underlineMoves(hashMap, startPosition);
                } else {
                    bottomLabel.setText("You haven't a worker in the selected cell!\nSelect one of your workers!");
                }
            } else {
                Coord response = boardController.checkAction(hashMap, img, startPosition);
                if (response != null) {
                    clientBG.sendMessage(new ActionResponse(startPosition, response));
                    counter = 0;
                    boardController.removeUnderlineMoves(hashMap, startPosition);
                    actionRequest = null;
                    numberOfActionRequestArrived++;
                    bottomLabel.setText("");
                } else {
                    bottomLabel.setText("This cell isn't available for your action!\nChoose another cell!");
                }
            }
        } else {
            //String prev = bottomMenu.getText();
            //bottomMenu.setText("Wait for your turn!\n" + prev);
            bottomMenu.setText("Wait for your turn!");
        }
    }

    /**
     * Mouse event catcher for decisions take by confirm and delete button.
     * @param event is a generic mouse event on one of that two images
     */
    @FXML
    public void onDecisionTake(javafx.scene.input.MouseEvent event) {
        if(!confirm.isDisable() && !delete.isDisable()){
            if (event.getSource() == confirm) {
                clientBG.sendMessage(new ResponseMessage(true));
                confirmButton.setDisable(true);
                deleteButton.setDisable(true);
                bottomLabel.setText("");
            } else if (event.getSource() == delete) {
                clientBG.sendMessage(new ResponseMessage(false));
                confirmButton.setDisable(true);
                deleteButton.setDisable(true);
                bottomLabel.setText("");
            } else {
                String prev = bottomMenu.getText();
                bottomMenu.setText("You can only chose V or X now\n" + prev);}
        }
    }

    /**
     * Event method called when you click on the exit button, it sends a LeaveGameMessage and then sets the home scene.
     */
    @FXML
    public void onExitClicked() {
        clientBG.sendMessage(new LeaveGameMessage());
        super.handleExit();
    }

    /**
     * Event method called when you click on an opponent player's god card.
     * @param event is the MouseEvent generated when you click on an opponent player's god card
     */
    public void onCardClicked(MouseEvent event) {
        playersController.showGod((ImageView) event.getSource());
    }


    public void onHelpClicked(){ playersController.showRules();}


    /**
     * This method when an ActionRequest arrives, puts it in the local variable request.
     * After that it sets the text of the label and it underlines the workers that you can use.
     * @param request is the ActionRequest message that contains cells that are available for the move or the build
     */
    public static void setActionRequest(ActionRequest request) {
        actionRequest = request;
        topLabel.setText("It's your turn");
        if (numberOfActionRequestArrived <= 1) {
            bottomLabel.setText("Place your worker " + numberOfActionRequestArrived);
        } else {
            bottomLabel.setText("Choose the worker you want for the action");
            boardController.underlineWorkers(request.getCellsAvailable());
        }
    }


    /**
     * This method, when a playerListMessage arrives calls the showUpdate method on the playersController
     * to show players information updates.
     * @param playersListMessage contains players information that are nickname, card and color for each one
     */
    public static void updateScene(PlayersListMessage playersListMessage) {
        playersController.showUpdate(playersListMessage);
    }


    /**
     * This method, when a CellMessage arrives, calls the updateCell method on the board controller to update the cell.
     * @param message contains the cell to be updated
     */
    public static void updateBoard(CellMessage message) {
        boardController.updateCell(message);
    }


    /**
     * This method, when a TextMessage arrives, shows its content on the top label.
     * @param textMessage contains a String message to be shown
     */
    public static void showInTopMenu(TextMessage textMessage) {
        if (textMessage.getPositionInScreen() == TextMessage.PositionInScreen.LOW_CENTER)
            topLabel.setText(textMessage.getMessage());
        else topLabel.setText("It's " + textMessage.getMessage() + "'s turn");
    }


    /**
     * This method, when a RequestMessage arrives , shows its content on the bottom label.
     * @param message contains a String message to be shown
     */
    public static void askQuestion(RequestMessage message) {
        bottomLabel.setText(message.getMessage());
        confirmButton.setDisable(false);
        deleteButton.setDisable(false);
    }
}