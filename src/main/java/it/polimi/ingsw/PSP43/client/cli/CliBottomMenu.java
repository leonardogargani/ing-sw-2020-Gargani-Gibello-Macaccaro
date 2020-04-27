package it.polimi.ingsw.PSP43.client.cli;


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
    

}
