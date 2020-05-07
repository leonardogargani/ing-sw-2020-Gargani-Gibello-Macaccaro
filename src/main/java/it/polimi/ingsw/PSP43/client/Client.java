package it.polimi.ingsw.PSP43.client;

import it.polimi.ingsw.PSP43.client.cli.InputHandler;
import it.polimi.ingsw.PSP43.client.cli.QuitPlayerException;
import it.polimi.ingsw.PSP43.client.cli.Screens;
import it.polimi.ingsw.PSP43.client.networkMessages.PlayersNumberResponse;
import it.polimi.ingsw.PSP43.client.networkMessages.RegistrationMessage;
import it.polimi.ingsw.PSP43.server.gameStates.PlayerRegistrationState;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class Client implements Runnable{


    private GraphicHandler graphics;
    private ClientBG clientBG;


    /**
     * Non default constructor that initializes the GraphicHandler attribute with an object
     * which will be either a CliGraphicHandler or a GuiGraphicHandler at runtime.
     * @param graphics CliGraphicHandler or GuiGraphicHandler object, based on the player's choice
     */
    public Client(ClientBG clientBG,GraphicHandler graphics) {
        this.graphics = graphics;
        this.clientBG = clientBG;
    }


    /**
     * This method returns the clientBG attribute, which is used to send messages to the server.
     * @return clientBG attribute, which is used to send messages to the server.
     */
    public ClientBG getClientBG() {
        return clientBG;
    }


    @Override
    public void run() {
        execute();
    }


    /**
     * Method that returns the object that handles the graphic side of the client.
     * @return object that handles the graphic side of the client
     */
    public GraphicHandler getGraphics() {
        return graphics;
    }


    /**
     * This method takes the client to the welcome page, right before the nickname request.
     */
    public void execute() {
        InputHandler inputHandler = new InputHandler();

        System.out.println(Screens.WELCOME);

        try {
            String nickname;
            System.out.println("Choose a nickname:");
            do {
                nickname = inputHandler.requestInput();
            } while (nickname.equals(""));
            RegistrationMessage message = new RegistrationMessage(nickname);
            clientBG.sendMessage(message);
        } catch (QuitPlayerException e) {
            // TODO implement the handling of a QuitPlayerException when a player writes "quit" in the cli
        } catch (IOException i) {
            i.printStackTrace();
        }

    }

}
