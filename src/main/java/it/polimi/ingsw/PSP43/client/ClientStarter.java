package it.polimi.ingsw.PSP43.client;

import it.polimi.ingsw.PSP43.client.graphic.cli.CliInputHandler;
import it.polimi.ingsw.PSP43.client.graphic.cli.QuitPlayerException;
import it.polimi.ingsw.PSP43.client.network.ClientManager;

/**
 * This is the starter class, here a player chooses CLI or GUI.
 */

public class ClientStarter {
    public static void main(String[] args) {
        if (args.length == 1)
            if (args[0].toLowerCase().equals("-cli")) {
                ClientManager clientManager = new ClientManager(1);
                Thread clientManagerThread = new Thread(clientManager);
                clientManagerThread.start();
            } else {
                System.out.println("Your entered parameter is invalid\n");
                System.out.println("Remember:\n-don't write nothing for the GUI\n-write -cli for the CLI ");
                System.exit(0);
            }
        else if (args.length > 1) {
            System.out.println("You entered to much parameters\n");
            System.out.println("Remember:\n-don't write nothing for the GUI\n-write -cli for the CLI ");
        } else {
            ClientManager clientManager = new ClientManager(2);
            Thread clientManagerThread = new Thread(clientManager);
            clientManagerThread.start();
        }
    }
}