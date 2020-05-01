package it.polimi.ingsw.PSP43.client;

import it.polimi.ingsw.PSP43.client.cli.Screens;
import it.polimi.ingsw.PSP43.client.networkMessages.PlayersNumberResponse;
import it.polimi.ingsw.PSP43.client.networkMessages.RegistrationMessage;
import it.polimi.ingsw.PSP43.server.gameStates.PlayerRegistrationState;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class Client implements Runnable{

    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 50000;
    private GraphicHandler graphics;
    private ClientBG clientBG;


    /**
     * Non default constructor that initializes the GraphicHandler attribute with an object
     * which will be either a CliGraphicHandler or a GuiGraphicHandler at runtime.
     * @param graphics CliGraphicHandler or GuiGraphicHandler object, based on the player's choice
     */
    public Client(GraphicHandler graphics) {
        this.graphics = graphics;
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
        Socket server;
        try {
            server = new Socket(SERVER_IP, SERVER_PORT);
        } catch (IOException e) {
            System.out.println("server unreachable");
            return;
        }
        System.out.println("Connected");

        try {
            clientBG = new ClientBG(server,this);
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
     * This method takes the client to the welcome page, right before the nickname request.
     */
    public void execute() {
        System.out.println(Screens.WELCOME);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String nickname;
            System.out.print("Choose a nickname:");
            nickname = reader.readLine();
            RegistrationMessage message = new RegistrationMessage(nickname);
            clientBG.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
