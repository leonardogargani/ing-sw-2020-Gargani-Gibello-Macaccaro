package it.polimi.ingsw.PSP43.client;

import it.polimi.ingsw.PSP43.client.cli.Screens;

import java.io.IOException;
import java.net.Socket;


public class Client implements Runnable{
private static final String ipServer = "127.0.0.1";
private static final int serverPort = 50000;
private GraphicHandler graphics;


    /**
     * Non default constructor that initializes the GraphicHandler attribute with an object
     * which will be either a CliGraphicHandler or a GuiGraphicHandler at runtime.
     * @param graphics CliGraphicHandler or GuiGraphicHandler object, based on the player's choice
     */
    public Client(GraphicHandler graphics) {
        this.graphics = graphics;
    }

    @Override
    public void run() {
        Socket server;
        try {
            server = new Socket(ipServer,serverPort);
        } catch (IOException e) {
            System.out.println("server unreachable");
            return;
        }
        System.out.println("Connected");

        try {
            ClientBG clientBG = new ClientBG(server,this);
            Thread clientBg = new Thread(clientBG);
            clientBg.start();
        } catch (IOException e) {
            System.out.println("Problems opening client background");
        }
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
