package it.polimi.ingsw.PSP43.client;

import it.polimi.ingsw.PSP43.client.cli.CliGraphicHandler;
import it.polimi.ingsw.PSP43.client.cli.InputHandler;
import it.polimi.ingsw.PSP43.client.cli.QuitPlayerException;
import it.polimi.ingsw.PSP43.client.gui.GuiGraphicHandler;

import java.util.Scanner;


public class ClientMain {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Insert 1 for the cli or 2 for the gui");
        int chosenInterface = scanner.nextInt();
        boolean choiceOK = false;
        do {
            if(chosenInterface == 1 | chosenInterface == 2)
                choiceOK = true;
        }while(!choiceOK);
        ClientManager clientManager = new ClientManager(chosenInterface);
        Thread clientManagerThread = new Thread(clientManager);
        clientManagerThread.start();
    }


    /*private static final String SERVER_IP = "127.0.0.1";


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

        ClientManager clientManager;

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
                        clientManager = new ClientManager(clientBG, cliGraphicHandler);
                        Thread clientThread1 = new Thread(clientManager);
                        clientThread1.start();
                        clientBG.setClientManager(clientManager);
                        break;
                    case 2:
                        System.out.println("GUI chosen. Starting the game...");
                        GuiGraphicHandler guiGraphicHandler = new GuiGraphicHandler(clientBG);
                        clientManager = new ClientManager(clientBG, guiGraphicHandler);
                        guiGraphicHandler.setClientBG(clientManager.getClientBG());
                        Thread clientThread2 = new Thread(clientManager);
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

    }*/


}
