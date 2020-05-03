package it.polimi.ingsw.PSP43.client;

import it.polimi.ingsw.PSP43.client.cli.CliGraphicHandler;
import it.polimi.ingsw.PSP43.client.gui.GuiGraphicHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;


public class ClientMain {

    public static void main(String[] args) throws IOException {
        System.out.println("Insert server IP address(usually 127.0.0.1)");
        Scanner scanner = new Scanner(System.in);

        String serverIp = scanner.nextLine();
        ClientBG clientBG = new ClientBG(serverIp);
        Thread clientBGThread = new Thread(clientBG);
        clientBGThread.start();

        while (!clientBG.isStarted())
        {
            System.out.println("-\b");
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String chosenMode;

        System.out.println("Choose a mode:\n [1] CLI\n [2] GUI");

        Client client;
        // GUI/CLI decision is taken through the cli for now
        do {
            chosenMode = reader.readLine();
            switch(chosenMode) {
                case "1":
                    System.out.println("CLI chosen. Starting the game...");
                    CliGraphicHandler cliGraphicHandler = new CliGraphicHandler(clientBG);
                    client = new Client(clientBG , cliGraphicHandler);
                    Thread clientThread1 = new Thread(client);
                    clientThread1.start();
                    clientBG.setClient(client);

                    break;
                case "2":
                    System.out.println("GUI chosen. Starting the game...");
                    GuiGraphicHandler guiGraphicHandler = new GuiGraphicHandler(clientBG);
                    client = new Client(clientBG,guiGraphicHandler);
                    guiGraphicHandler.setClientBG(client.getClientBG());
                    Thread clientThread2 = new Thread(client);
                    clientThread2.start();
                    break;
                default:
                    System.out.println("Choice not valid, try again:");
                    break;
            }
        } while(!chosenMode.equals("1") && !chosenMode.equals("2"));


    }


}
