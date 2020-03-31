package it.polimi.ingsw.model;


/**
 * Player is the class that represents the actual person who is playing the game.
 * He has a nickname previously chosen, two workers he can move and a card of a god.
 */
public class Player {

    private String nickname;
    private Worker[] workersArray = new Worker[2];
    private Card card;


    /**
     * Non-default constructor, it initializes a Player with his nickname.
     * @param nickname nickname of the player
     */
    public Player(String nickname) {
        this.nickname = nickname;
        this.workersArray = null;
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
    public Worker[] getWorkersArray() {
        Worker[] copy = new Worker[this.workersArray.length];
        System.arraycopy(this.workersArray, 0, copy, 0, copy.length);
        return copy;
    }


    /**
     * This method assigns a couple of workers, represented by an array, to its owner.
     * @param workersArray array containing two workers owned by the same player
     */
    public void setWorkersArray(Worker[] workersArray) {
        this.workersArray = workersArray;
    }  // DEVO PASSARE UNA COPIA DI WORKERSARRAY


        /**
         * This method returns the God Power card owned by the player.
         * @return card owned by the player
         */
    public Card getCard() {
        return card;
    }  // DEVO RITORNARE UNA COPIA DI CARD


    /**
     * This method assigns a God Power card to a player who'll become its owner.
     * @param card card of a God Power
     */
    public void setCard(Card card) {
        this.card = card;
    }  // DEVO PASSARE UNA COPIA DI CARD


}
