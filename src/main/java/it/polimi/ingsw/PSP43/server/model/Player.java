package it.polimi.ingsw.PSP43.server.model;

import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;

import java.io.Serializable;

/**
 * Player is the class that represents the actual person who is playing the game.
 * He has a nickname previously chosen, two workers he can move and a abstractGodCard of a god.
 */
public class Player implements Serializable {

    private static final long serialVersionUID = -5073826840767054806L;

    private final String nickname;
    private final Integer[] workersIdsArray;
    private AbstractGodCard abstractGodCard;


    /**
     * Non-default constructor, it initializes a Player with his nickname.
     * @param nickname nickname of the player
     */
    public Player(String nickname) {
        this.nickname = nickname;
        this.workersIdsArray = new Integer[]{0, 1};
        this.abstractGodCard = null;
    }


    /**
     * This method returns the nickname of the player.
     * @return nickname of the player
     */
    public String getNickname() {
        return nickname;
    }


    /**
     * This method returns both the workers in an array of two elements.
     * @return copy of the array with the two workers of the player
     */
    public Integer[] getWorkersIdsArray() {
        return workersIdsArray.clone();
    }

    /**
     * This method assigns a couple of workers, represented by their ids, to their owner.
     * @param workersIdsArray ids of the workers owned by the player
     */
    public void setWorkersIdsArray(int[] workersIdsArray) {
        for (int i=0; i<workersIdsArray.length && i<this.workersIdsArray.length; i++) {
            this.workersIdsArray[i] = workersIdsArray[i];
        }
    }

        /**
         * This method returns the God Power abstractGodCard owned by the player.
         * @return abstractGodCard owned by the player
         */
    public AbstractGodCard getAbstractGodCard() {
        return abstractGodCard;
    }


    /**
     * This method assigns a God Power abstractGodCard to a player who'll become its owner.
     * @param abstractGodCard abstractGodCard of a God Power
     */
    public void setAbstractGodCard(AbstractGodCard abstractGodCard) {
        this.abstractGodCard = abstractGodCard;
    }
}
