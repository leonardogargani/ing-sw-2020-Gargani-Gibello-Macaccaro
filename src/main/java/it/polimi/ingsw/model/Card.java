package it.polimi.ingsw.model;


/**
 * Card is the class that represents the cards with God Powers, thus it is associated to a God and the
 * corresponding description of its power.
 * Each player owns a single card for the entire duration of the game.
 */
public class Card {

    private String godName;
    private String description;


    /**
     * Non-default constructor, it initializes the card with the name of the god and its description.
     * @param godName name of the god whose power has been chosen by the player
     * @param description description of the ability of the god
     */
    public Card(String godName, String description) {
        this.godName = godName;
        this.description = description;
    }


    /**
     * Method that returns the name of the god.
     * @return name of the god
     */
    public String getGodName() {
        return godName;
    }


    /**
     * Method that returns the description of the power of the god.
     * @return description of the power of the god
     */
    public String getDescription() {
        return description;
    }


}
