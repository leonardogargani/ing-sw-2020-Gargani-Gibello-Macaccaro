package it.polimi.ingsw.PSP43.client;

import it.polimi.ingsw.PSP43.client.cli.CliInputHandler;
import it.polimi.ingsw.PSP43.client.cli.QuitPlayerException;

/**
 * This is the starter class, here a player chooses CLI or GUI
 */

public class ClientStarter {

    public static void main(String[] args) {

        int chosenMode;
        System.out.println("Choose a mode:\n [1] CLI\n [2] GUI");
        CliInputHandler inputHandler = new CliInputHandler();
        try {
            do {
                chosenMode = inputHandler.requestInputInt();
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

            ClientManager clientManager = new ClientManager(chosenMode, true);
            Thread clientManagerThread = new Thread(clientManager);
            clientManagerThread.start();

        } catch (QuitPlayerException e) {
            System.exit(0);
        }

    }

}
