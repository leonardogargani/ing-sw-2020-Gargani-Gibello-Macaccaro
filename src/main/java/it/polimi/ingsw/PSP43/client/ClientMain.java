package it.polimi.ingsw.PSP43.client;

import it.polimi.ingsw.PSP43.client.cli.CliGraphicHandler;
import it.polimi.ingsw.PSP43.client.cli.InputHandler;
import it.polimi.ingsw.PSP43.client.cli.QuitPlayerException;
import it.polimi.ingsw.PSP43.client.gui.GuiGraphicHandler;


public class ClientMain {

    private static final String SERVER_IP = "127.0.0.1";


    public static void main(String[] args) {

        System.out.println("I'm connecting to the server at 127.0.0.1");

        ClientBG clientBG = new ClientBG(SERVER_IP);
        Thread clientBGThread = new Thread(clientBG);
        clientBGThread.start();

        while (!clientBG.isStarted()) {
            System.out.print("-\b");
        }

        int chosenMode;
        System.out.println("Choose a mode:\n [1] CLI\n [2] GUI");

        Client client;

        InputHandler inputHandler = new InputHandler();
        String line;

        // GUI/CLI decision is taken through the cli for now

        try {
            do {
                line = inputHandler.requestInput();
                chosenMode = Integer.parseInt(line);
                switch (chosenMode) {
                    case 1:
                        System.out.println("CLI chosen. Starting the game...");
                        CliGraphicHandler cliGraphicHandler = new CliGraphicHandler(clientBG);
                        client = new Client(clientBG, cliGraphicHandler);
                        Thread clientThread1 = new Thread(client);
                        clientThread1.start();
                        clientBG.setClient(client);
                        break;
                    case 2:
                        System.out.println("GUI chosen. Starting the game...");
                        GuiGraphicHandler guiGraphicHandler = new GuiGraphicHandler(clientBG);
                        client = new Client(clientBG, guiGraphicHandler);
                        guiGraphicHandler.setClientBG(client.getClientBG());
                        Thread clientThread2 = new Thread(client);
                        clientThread2.start();
                        break;
                    default:
                        System.out.println("Choice not valid, try again:");
                        break;
                }
            } while (chosenMode != 1 && chosenMode != 2);

        } catch (QuitPlayerException e) {
            // TODO implement the handling of a QuitPlayerException when a player writes "quit" in the cli
        }

    }


}
