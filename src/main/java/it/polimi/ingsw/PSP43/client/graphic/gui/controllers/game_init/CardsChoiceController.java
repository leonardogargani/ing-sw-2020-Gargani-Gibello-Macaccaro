package it.polimi.ingsw.PSP43.client.graphic.gui.controllers.game_init;

import it.polimi.ingsw.PSP43.client.graphic.gui.GuiStarter;
import it.polimi.ingsw.PSP43.client.graphic.gui.controllers.AbstractController;
import it.polimi.ingsw.PSP43.client.network.networkMessages.ChosenCardsResponse;
import it.polimi.ingsw.PSP43.client.network.networkMessages.LeaveGameMessage;
import it.polimi.ingsw.PSP43.server.controller.cards.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.network.networkMessages.InitialCardsRequest;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CardsChoiceController extends AbstractController {

    @FXML private Label rightLabel;
    @FXML private Label leftLabel;
    @FXML private Label bottomLabel;
    @FXML private ImageView leftArrowImage;
    @FXML private ImageView rightArrowImage;
    @FXML private ImageView cardImage;
    @FXML private ImageView confirmImage;
    @FXML private Label infoLabel;
    @FXML private ImageView exitImage;

    private List<AbstractGodCard> cardsList;
    private int numberOfPlayers;

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
        leftArrowImage.setPickOnBounds(false);
        rightArrowImage.setPickOnBounds(false);
        exitImage.setPickOnBounds(false);
        exitImage.getStyleClass().add("exit-image");
        leftLabel.getStyleClass().add("info-label");
        bottomLabel.getStyleClass().add("info-label");
    }


    /**
     * Method that displays the information about the first card (the only one visible) and dispatches the
     * request message extracting its filed. It is called by GuiGraphicHandler and it's the first method to
     * be executed (apart from initialize()) inside this controller.
     * @param request request message sent by the server
     */
    public void customInit(InitialCardsRequest request) {
        setNumberOfPlayers(request.getNumberOfCard());
        displayCard(cardsList.get(0));
    }


    /**
     * Method that, given a card, displays its name, description, power and image on the scene.
     * @param card card to be displayed on the scene
     */
    private void displayCard(AbstractGodCard card) {

        bottomLabel.setText(card.getGodName().toUpperCase() + "\n" + card.getDescription());
        leftLabel.setText(card.getPower());

        String filepath = "/images/gods/" + card.getGodName() + "_1.png";
        cardImage.setImage(new Image(getClass().getResource(filepath).toExternalForm()));

    }


    /**
     * Method that sets the attribute of the controller containing the number of players
     * of the current game session (which is also the number of cards that must be chosen).
     * @param numberOfPlayers number of players of the current game session
     */
    private void setNumberOfPlayers(int numberOfPlayers) {
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
     */
    @FXML
    private void handleConfirmImage() {

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
            getClientBG().sendMessage(response);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/FXML/miscellaneous/wait.fxml"));
            try {
                Stage stage = GuiStarter.getPrimaryStage();
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
     */
    @FXML
    private void handleRightArrowImage() {
        // if the displayed card is the last one do nothing, else display the next one
        if ((currentCardIndex + 1) < cardsList.size()) {
            currentCardIndex++;
            displayCard(cardsList.get(currentCardIndex));
            infoLabel.setText("");
        }
    }


    /**
     * Method that handles a mouse event performed on the image to show the previous card, checking
     * if the currently displayed one is the first card or not.
     */
    @FXML
    private void handleLeftArrowImage() {
        // if the displayed card is the first one do nothing, else display the previous one
        if (currentCardIndex > 0) {
            currentCardIndex--;
            displayCard(cardsList.get(currentCardIndex));
            infoLabel.setText("");
        }
    }


    /**
     * Method that handles an event performed on the image to exit and return back to the home screen.
     */
    @FXML
    private void handleExitImage() {
        getClientBG().sendMessage(new LeaveGameMessage());
        super.handleExit();
    }

}
