package it.polimi.ingsw.PSP43.server.model;


/**
 * Player is the class that represents the actual person who is playing the game.
 * He has a nickname previously chosen, two workers he can move and a card of a god.
 */
public class Player {

    private String nickname;
    private int[] workersIdsArray;
    private Card card;


    /**
     * Non-default constructor, it initializes a Player with his nickname.
     * @param nickname nickname of the player
     */
    public Player(String nickname) {
        this.nickname = nickname;
        this.workersIdsArray = null;
        this.card = null;
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
    public int[] getWorkersIdsArray() {
        return workersIdsArray.clone();
    }


    /**
     * This method assigns a couple of workers, represented by their ids, to their owner.
     * @param id1 id of the first worker owned by the same player
     * @param id2 id of the second worker owned by the same player
     */
    public void setWorkersIdsArray(int id1, int id2) {
        // I don't need to use a copy of the array since the parameters
        // are two int values and not a single int[]
        this.workersIdsArray = new int[]{id1, id2};
    }


        /**
         * This method returns the God Power card owned by the player.
         * @return card owned by the player
         */
    public Card getCard() {
        return card;
    }


    /**
     * This method assigns a God Power card to a player who'll become its owner.
     * @param card card of a God Power
     */
    public void setCard(Card card) {
        this.card = card;
    }


}
