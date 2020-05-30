package it.polimi.ingsw.PSP43.client.gui.controllers.game;

import it.polimi.ingsw.PSP43.client.ClientBG;
import it.polimi.ingsw.PSP43.client.gui.controllers.AbstractController;
import it.polimi.ingsw.PSP43.client.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.client.networkMessages.ResponseMessage;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.networkMessages.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
    @FXML private ImageView homeButton;
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
    private static ActionRequest actionRequest = null;
    private static BoardController boardController;
    private static PlayersController playersController;
    private static String nick;

    enum Decision {NOT_DECIDED, YES, NO}
    private static Decision decision;
    //
    private static boolean DecisionActive = false;
    private static Label topLabel;
    private static Label bottomLabel;

    private int counter = 0;
    private int number = 0;
    private Coord startPosition;

    /**
     * Initialize method for this controller
     */
    public void initialize() {
        ImageView[][] board = new ImageView[5][5];
        board[0] = new ImageView[]{c00, c10, c20, c30, c40};
        board[1] = new ImageView[]{c01, c11, c21, c31, c41};
        board[2] = new ImageView[]{c02, c12, c22, c32, c42};
        board[3] = new ImageView[]{c03, c13, c23, c33, c43};
        board[4] = new ImageView[]{c04, c14, c24, c34, c44};
        boardController = new BoardController(board,bottomMenu,decision);
        Label[] labels = new Label[]{player1Nick, player2Nick, player3Nick, player1CardDescription};
        ImageView[] images = new ImageView[]{player1Card, player2Card, player3Card, player1Worker, player2Worker, player3Worker};
        playersController = new PlayersController(labels, images, nick);
        for (int i=0;i<5;i++)
            for (int j=0;j<5;j++)
                board[i][j].setId("cell-button");
        homeButton.setId("decision-button");
        confirm.setId("decision-button");
        delete.setId("decision-button");
        clientBG = AbstractController.getClientBG();
        //
        topLabel = topMenu;
        bottomLabel = bottomMenu;
        player1Nick.setText(nick);
    }


    //This method calls checkAction on mouse clicked event on a cell of the board

    /**
     * Mouse event catcher for board cells
     *
     * @param event is a generic mouse event on a cell of the board
     */
    @FXML
    public void onCellClicked(MouseEvent event) {
        if (actionRequest != null) {
            Map<Coord, ArrayList<Coord>> hashMap = actionRequest.getCellsAvailable();
            ImageView img = (ImageView) event.getSource();
            if (counter == 0 & number > 1) {
                startPosition = boardController.checkWorkerChosen(hashMap, img);
                if (startPosition != null) {
                    bottomLabel.setText(actionRequest.getMessage());
                    counter++;
                } else {
                    bottomLabel.setText("You haven't a worker in the selected cell !");
                }
            } else if (number <=1) {
                ActionResponse response = boardController.checkAvailability(hashMap, img);
                if (response != null) {
                    clientBG.sendMessage(response);
                    number++;
                } else bottomLabel.setText("You can't go there !");
            } else {
                Coord response = boardController.checkAction(hashMap, img, startPosition);
                if (response != null) {
                    clientBG.sendMessage(new ActionResponse(startPosition, response));
                    counter = 0;
                    boardController.removeUnderline(actionRequest);
                    actionRequest = null;
                    number++;
                } else {
                    bottomLabel.setText("You can't go there !");
                }
            }
        } else {
            bottomMenu.setText("Wait for your turn !");
        }
        //clearLabel(bottomMenu);
    }

    /**
     * Mouse event catcher for decisions take by confirm and delete button
     * @param event is a generic mouse event on one of that two images
     */
    @FXML
    public void onDecisionTake(javafx.scene.input.MouseEvent event) {
        if (DecisionActive) {
            if (event.getSource() == confirm) {
                    clientBG.sendMessage(new ResponseMessage(true));
            } else if (event.getSource() == delete) {
                    clientBG.sendMessage(new ResponseMessage(false));
            } else bottomMenu.setText("You can only chose V or X now");
        } else {
            bottomMenu.setText("Wait for your turn !");
        }
        DecisionActive = false;
    }

    /**
     *
     */
    @FXML
    public void onHomeClicked() {
        super.handleExit();
    }


    public void onCardClicked(MouseEvent event){
        playersController.showGod((ImageView) event.getSource());
    }


    /**
     *
     * @param request
     */
    public static void setActionRequest(ActionRequest request) {
        actionRequest = request;
        //bottomLabel.setText("Choose the worker you want for the action");
        bottomLabel.setText(request.getMessage());
        boardController.underlineAvailableCells(actionRequest);
    }

    //method to handle scene update when a playerListMessage arrives
    /**
     * OK!
     * @param playersListMessage
     */
    public static void updateScene(PlayersListMessage playersListMessage){
        playersController.showUpdate(playersListMessage);
    }

    //This method calls the updateCell method of the boardController

    /**
     * OK!
     * @param message
     */
    public static void updateBoard(CellMessage message) {
        if(boardController!=null)
            boardController.updateCell(message);
    }

    //Update method for TextMessages

    /**
     * OK!
     * @param textMessage
     */
    public static void showInTopMenu(TextMessage textMessage) {
        topLabel.setText(textMessage.getMessage());
    }

    //Method for request messages

    /**
     *
     * @param message
     */
    public static void askQuestion(RequestMessage message) {
        bottomLabel.setText(message.getMessage());
        DecisionActive = true;
    }

    public static void setNick(String nick) {
        MatchController.nick = nick;
    }

}
