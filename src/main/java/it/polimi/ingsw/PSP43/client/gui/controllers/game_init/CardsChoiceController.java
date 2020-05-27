package it.polimi.ingsw.PSP43.client.gui.controllers.game_init;

import it.polimi.ingsw.PSP43.client.ClientBG;
import it.polimi.ingsw.PSP43.client.networkMessages.ChosenCardsResponse;
import it.polimi.ingsw.PSP43.server.controllers.AbstractGodCard;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CardsChoiceController {

    public Label rightLabel;
    public Label leftLabel;
    public Label bottomLabel;
    public ImageView leftArrowImage;
    public ImageView rightArrowImage;
    public ImageView cardImage;
    public ImageView confirmImage;
    public Label infoLabel;

    private List<AbstractGodCard> cardsList;
    private int numberOfPlayers;

    private static ClientBG clientBG;

    private final ArrayList<AbstractGodCard> chosenCards = new ArrayList<>();
    private int currentCardIndex = 0;


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
        displayCard(cardsList.get(0));
    }


    /**
     * Method that sets the ClientBG attribute of the controller, it will be invoked inside
     * the GuiGraphicHandler constructor so that the controller will have already the attribute set
     * once it will be utilized.
     * @param clientBG clientBG of the current client
     */
    public static void setClientBG(ClientBG clientBG) {
        CardsChoiceController.clientBG = clientBG;
    }


    /**
     * Method that, given a card, displays its name, description, power and image on the scene.
     * @param card card to be displayed on the scene
     */
    private void displayCard(AbstractGodCard card) {

        bottomLabel.setText(card.getGodName().toUpperCase() + "\n" + card.getDescription());
        leftLabel.setText(card.getPower());

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
    public void handleConfirmImage(MouseEvent event) {

        AbstractGodCard currentCard = cardsList.get(currentCardIndex);

        // let the card be chosen only if it has not been selected yet
        if (chosenCards.contains(currentCard)) {
            infoLabel.setText("already selected");
        } else {
            chosenCards.add(currentCard);
            rightLabel.setText(stringifyChosenCards());
        }

        // if the number of the chosen cards has reached the number of the players, send the response to the server
        if (chosenCards.size() == numberOfPlayers) {
            ChosenCardsResponse response = new ChosenCardsResponse(chosenCards);
            clientBG.sendMessage(response);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/FXML/miscellaneous/wait.fxml"));
            try {
                Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                Scene scene = new Scene(loader.load());

                WaitController controller = loader.getController();
                controller.setLabelText("wait for other players to choose their cards...");

                scene.getStylesheets().add(getClass().getResource("/CSS/game_init/style.css").toExternalForm());
                stage.setScene(scene);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * Method that stringifies all the chosen cards, creating a String with the names of the chosen gods
     * each one on a separate line.
     * @return String containing the names of the chosen gods (on separate lines)
     */
    private String stringifyChosenCards() {
        StringBuilder stringBuilder = new StringBuilder();
        for (AbstractGodCard chosenCard : chosenCards) {
            stringBuilder.append(chosenCard.getGodName());
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }


    /**
     * Method that handles a mouse event performed on the image to show the next card, checking
     * if the currently displayed one is the last card or not.
     * @param event mouse event performed on the image
     */
    @FXML
    public void handleRightArrowImage(MouseEvent event) {
        // if the displayed card is the last one do nothing, else display the next one
        if ((currentCardIndex + 1) < cardsList.size()) {
            currentCardIndex++;
            displayCard(cardsList.get(currentCardIndex));
        }
    }


    /**
     * Method that handles a mouse event performed on the image to show the previous card, checking
     * if the currently displayed one is the first card or not.
     * @param event mouse event performed on the image
     */
    @FXML
    public void handleLeftArrowImage(MouseEvent event) {
        // if the displayed card is the first one do nothing, else display the previous one
        if (currentCardIndex > 0) {
            currentCardIndex--;
            displayCard(cardsList.get(currentCardIndex));
        }
    }


}
