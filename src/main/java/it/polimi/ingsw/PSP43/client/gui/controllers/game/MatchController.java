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
    private static RequestMessage requestMessage = null;
    private static CellMessage cellMessage = null;
    private static BoardController boardController;
    private static PlayersController playersController;

    enum Decision {NOT_DECIDED, YES, NO}
    private static Decision decision;
    //
    private static Label topLabel;
    private static Label bottomLabel;

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
        playersController = new PlayersController(labels, images);
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
        player1Nick.setText(getNick());
    }


    //This method calls checkAction on mouse clicked event on a cell of the board

    /**
     * Mouse event catcher for board cells
     * @param event is a generic mouse event on a cell of the board
     */
    @FXML
    public synchronized void onCellClicked(MouseEvent event) {
        if (actionRequest != null) {
            Map<Coord, ArrayList<Coord>> hashMap = actionRequest.getCellsAvailable();
            ImageView img = (ImageView) event.getSource();
            ActionResponse actionResponse = boardController.checkAction(hashMap, img);
            if (actionResponse != null) {
                bottomMenu.setText("Are you sure?");
                while (decision == Decision.NOT_DECIDED) {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (decision == Decision.YES) {
                    clientBG.sendMessage(actionResponse);
                    boardController.removeUnderline();
                    actionRequest = null;
                } else {
                    bottomMenu.setText("Choose another cell to move your worker");
                }
                decision = Decision.NOT_DECIDED;
            } else {
                bottomMenu.setText("You can't go there !");
            }
        } else {
            bottomMenu.setText("Wait for your turn !");
        }
    }

    /**
     * Mouse event catcher for decisions take by confirm and delete button
     * @param event is a generic mouse event on one of that two images
     */
    @FXML
    public void onDecisionTake(javafx.scene.input.MouseEvent event) {
        if (actionRequest != null | requestMessage != null) {
            if (event.getSource() == confirm) {
                decision = Decision.YES;
            } else if(event.getSource() == delete) {
                decision = Decision.NO;
            } else bottomMenu.setText("You can only chose V or X now");
        } else {
            bottomMenu.setText("Wait for your turn !");
        }
    }

    /**
     *
     */
    @FXML
    public void onHomeClicked() {
        bottomMenu.setText("Are you sure you want to exit ?");
        decision = Decision.NOT_DECIDED;
        while (decision == Decision.NOT_DECIDED) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (decision == Decision.YES) {
            super.handleExit();
        }
        decision = Decision.NOT_DECIDED;
        clearLabel(bottomMenu);
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
        bottomLabel.setText(request.getMessage());
        boardController.underlineAvailableCells(actionRequest);
    }

    //method to handle scene update when a playerListMessage arrives
    /**
     *
     * @param playersListMessage
     */
    public static void updateScene(PlayersListMessage playersListMessage){
        playersController.showUpdate(playersListMessage);
    }

    //This method calls the updateCell method of the boardController

    /**
     *
     * @param message
     */
    public static void updateBoard(CellMessage message) {
        cellMessage = message;
        boardController.updateCell(cellMessage);
    }

    //Update method for TextMessages

    /**
     *
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
        requestMessage = message;
        bottomLabel.setText(requestMessage.getMessage());
        decision = Decision.NOT_DECIDED;
        while (decision == Decision.NOT_DECIDED) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (decision == Decision.YES) {
            clientBG.sendMessage(new ResponseMessage(true));
        } else {
            clientBG.sendMessage(new ResponseMessage(false));
        }
        clearLabel(topLabel);
        decision = Decision.NOT_DECIDED;
        requestMessage = null;
    }

    /**
     *
     * @param label
     */
    public static void clearLabel(Label label) {
        label.setText("");
    }

    //Probably useless
    public BoardController getBoardController() {
        return boardController;
    }

    public PlayersController getPlayersController(){
        return playersController;
    }
}
