package it.polimi.ingsw.PSP43.client;

import it.polimi.ingsw.PSP43.client.cli.InputHandler;
import it.polimi.ingsw.PSP43.client.cli.QuitPlayerException;


public class ClientMain {

    public static void main(String[] args) {

        int chosenMode;
        System.out.println("Choose a mode:\n [1] CLI\n [2] GUI");
        InputHandler inputHandler = new InputHandler();
        String line;
        try {
            do {
                line = inputHandler.requestInput();
                chosenMode = Integer.parseInt(line);
                switch (chosenMode) {
                    case 1:
                        System.out.println("CLI chosen. Starting the game...");
                        break;
                    case 2:
                        System.out.println("GUI chosen. Starting the game...");
                        break;
                    default:
                        System.out.println("Choice not valid, try again:");
                        break;
                }
            } while (chosenMode != 1 && chosenMode != 2);

            ClientManager clientManager = new ClientManager(chosenMode);
            Thread clientManagerThread = new Thread(clientManager);
            clientManagerThread.start();

        } catch (QuitPlayerException e) {
            System.exit(0);
        }

    }

}
