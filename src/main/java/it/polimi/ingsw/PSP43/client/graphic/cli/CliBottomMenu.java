package it.polimi.ingsw.PSP43.client.graphic.cli;


import it.polimi.ingsw.PSP43.server.model.Color;

public class CliBottomMenu {

    private String content;


    /**
     * Method that prints the content attribute to the cli.
     */
    public void show() {
        System.out.println(content);
    }


    /**
     * Method that sets the content of the menu which will be displayed at the bottom of the screen.
     * @param content simple statement to display
     */
    public void setContent(String content) {
        this.content = content;
    }


    /**
     * Method that clears the content of this menu.
     */
    public void clear() {
        this.setContent("");
    }


    /**
     * Method that, given the nickname of a player, sets the content of the menu
     * to a nicely formatted "*nickname* won the game".
     * @param nick nickname of the player playing his own turn at the moment
     */
    public void setContentWithNick(String nick) {
        this.content =
                Color.ANSI_RED +
                "          -------------------------------------          \n" +
                "                    " + nick + " won the game            \n" +
                "          -------------------------------------          \n" +
                Color.RESET;
    }

}
