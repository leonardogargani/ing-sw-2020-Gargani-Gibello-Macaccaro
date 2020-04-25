package it.polimi.ingsw.PSP43.client;

import it.polimi.ingsw.PSP43.client.cli.CliGraphicHandler;
import it.polimi.ingsw.PSP43.client.gui.GuiGraphicHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class ClientMain {

    private static Client client;


    public static void main(String[] args) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String chosenMode;

        System.out.println("Choose a mode:\n [1] CLI\n [2] GUI");


        // GUI/CLI decision is taken through the cli for now
        do {
            chosenMode = reader.readLine();
            switch(chosenMode) {
                case "1":
                    System.out.println("CLI chosen. Starting the game...");
                    client = new Client(new CliGraphicHandler());
                    break;
                case "2":
                    System.out.println("GUI chosen. Starting the game...");
                    client = new Client(new GuiGraphicHandler());
                    break;
                default:
                    System.out.println("Choice not valid, try again:");
                    break;
            }
        } while(!chosenMode.equals("1") && !chosenMode.equals("2"));


        // start effectively the client and display welcome message
        client.start();


    }



}