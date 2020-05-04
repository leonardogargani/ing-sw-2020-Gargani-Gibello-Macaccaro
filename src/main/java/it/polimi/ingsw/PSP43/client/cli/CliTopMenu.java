package it.polimi.ingsw.PSP43.client.cli;


public class CliTopMenu {

    private String content = "";


    /**
     * Method that prints the content attribute to the cli.
     */
    public void show() {
        System.out.println(content);
    }


    /**
     * Method that sets the content of the menu with an already nicely formatted text.
     * @param content already nicely formatted content I want to be displayed
     */
    public void setContent(String content) {
        this.content = content;
    }


    /**
     * Method that, given the nickname of a player, sets the content of the menu
     * to a nicely formatted "It's *nickname*'s turn".
     * @param nick nickname of the player playing his own turn at the moment
     */
    public void setContentWithNick(String nick) {
        this.content =
                "          -------------------------------------          \n" +
                "                    It's " + nick + "'s turn\n" +
                "          -------------------------------------          \n";
    }


}
