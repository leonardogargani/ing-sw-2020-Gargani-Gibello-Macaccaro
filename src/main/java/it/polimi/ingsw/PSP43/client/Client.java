package it.polimi.ingsw.PSP43.client;

import it.polimi.ingsw.PSP43.client.cli.Screens;


public class Client {

    private GraphicHandler graphics;


    /**
     * Non default constructor that initializes the GraphicHandler attribute with an object
     * which will be either a CliGraphicHandler or a GuiGraphicHandler at runtime.
     * @param graphics CliGraphicHandler or GuiGraphicHandler object, based on the player's choice
     */
    public Client(GraphicHandler graphics) {
        this.graphics = graphics;
    }


    /**
     * Method that returns the object that handles the graphic side of the client.
     * @return object that handles the graphic side of the client
     */
    public GraphicHandler getGraphics() {
        return graphics;
    }


    /**
     * This method takes the client to the welcome page. right before the nickname request.
     */
    public void start() {
        System.out.println(Screens.WELCOME);
    }


}
