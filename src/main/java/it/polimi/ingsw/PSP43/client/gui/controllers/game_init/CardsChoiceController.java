package it.polimi.ingsw.PSP43.client.gui.controllers.game_init;

import it.polimi.ingsw.PSP43.server.controllers.AbstractGodCard;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import java.util.List;


public class CardsChoiceController {

    public Label borderLabel;
    public Label bottomLabel;
    public ImageView leftArrowImage;
    public ImageView rightArrowImage;
    public ImageView cardImage;
    public ImageView confirmImage;

    private List<AbstractGodCard> cardsList;
    private int numberOfPlayers;

    private int currentCardIndex = 0;


    // TODO find a way to add the chosen cards to the border label (and also a way to remove a chosen card)


    /**
     * Method called as soon as the controlled fxml file gets loaded, here used to set css ids and classes.
     */
    @FXML
    private void initialize() {
        leftArrowImage.getStyleClass().add("arrow");
        rightArrowImage.getStyleClass().add("arrow");
        confirmImage.setId("confirm-image");
    }


    /**
     * Method that displays the information about the first card (the only one visible).
     * It is called by GuiGraphicHandler and it's the first method to be executed (apart from initialize())
     * inside this controller.
     */
    public void customInit() {

        AbstractGodCard firstCard = cardsList.get(0);
        bottomLabel.setText(firstCard.getGodName() + "\n" + firstCard.getDescription() + "\n" + firstCard.getPower());

        // TODO associate and display the image of the first card

    }


    /**
     * Method that sets the attribute of the controller containing the number of players
     * of the current game session (which is also the number of cards that must be chosen).
     * @param numberOfPlayers number of players of the current game session
     */
    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }


    /**
     * Method that sets the attribute of the controller containing the list of all the available cards.
     * @param cardsList list of all the available cards
     */
    public void setCardsList(List<AbstractGodCard> cardsList) {
        this.cardsList = cardsList;
    }


    /**
     * Method that handles a mouse event performed on the image to confirm that the current card
     * will be one of the chosen ones for this game session.
     * @param event mouse event performed on the image
     */
    @FXML
    private void handleConfirmImage(MouseEvent event) {

        // TODO to be implemented

    }


    /**
     * Method that handles a mouse event performed on the image to show the next card, checking
     * if the currently displayed one is the last card or not.
     * @param event mouse event performed on the image
     */
    @FXML
    private void handleRightArrowImage(MouseEvent event) {

        // if the displayed card is the last one do nothing, else display the next one
        if ((currentCardIndex + 1) < cardsList.size()) {
            currentCardIndex++;
            AbstractGodCard cardToDisplay = cardsList.get(currentCardIndex);
            bottomLabel.setText(cardToDisplay.getGodName() + "\n" + cardToDisplay.getDescription() + "\n" + cardToDisplay.getPower());

            // TODO associate and display the image of the current card

        }

    }


    /**
     * Method that handles a mouse event performed on the image to show the previous card, checking
     *      * if the currently displayed one is the first card or not.
     * @param event mouse event performed on the image
     */
    @FXML
    private void handleLeftArrowImage(MouseEvent event) {

        // if the displayed card is the first one do nothing, else display the previous one
        if (currentCardIndex > 0) {
            currentCardIndex--;
            AbstractGodCard cardToDisplay = cardsList.get(currentCardIndex);
            bottomLabel.setText(cardToDisplay.getGodName() + "\n" + cardToDisplay.getDescription() + "\n" + cardToDisplay.getPower());

            // TODO associate and display the image of the current card

        }

    }

}
