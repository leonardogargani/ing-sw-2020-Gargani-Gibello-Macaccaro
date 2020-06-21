package it.polimi.ingsw.PSP43.client.graphic.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class CliInputHandler {

    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private String line;


    /**
     * Method that request a generic input from the player through a BufferedReader.
     * @return generic String written by the player
     * @throws QuitPlayerException exception thrown if a player writes "quit" (ignore capitalization) in the cli
     */
    public String requestInputString() throws QuitPlayerException {
        try {
            do {
                System.out.print("> ");
                line = reader.readLine();
            } while (line.isEmpty());
            if (line.toLowerCase().equals("quit")) {
                throw new QuitPlayerException("A player has decided to quit the game.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return line;
    }


    /**
     * Method that request an integer input from the player through a BufferedReader.
     * @return int written by the player
     * @throws QuitPlayerException exception thrown if a player writes "quit" (ignore capitalization) in the cli
     */
    public int requestInputInt() throws QuitPlayerException {

        int inputInt = 0;
        boolean inputIsCorrect = false;
        
        do {
            try {
                inputInt = Integer.parseInt(requestInputString());
                inputIsCorrect = true;
            } catch (NumberFormatException e) {
                System.out.println("Input not valid, try again");
            }
        } while(!inputIsCorrect);

        return inputInt;
    }


    /**
     * Method that asks a player for a nickname.
     * @return String containing the nickname chosen by the player
     * @throws QuitPlayerException exception thrown if a player writes "quit" (ignore capitalization) in the cli
     */
    public String requestNickname() throws QuitPlayerException {

        System.out.println("Choose a nickname:");
        return requestInputString();

    }


    /**
     * Method that asks a player for the IP address of the server.
     * @return line containing the IP address of the server chosen by the player
     * @throws QuitPlayerException exception thrown if a player writes "quit" (ignore capitalization) in the cli
     */
    public String requestServerIP() throws QuitPlayerException {

        System.out.println("Insert the server IP:");
        System.out.print("> ");
        try {
            line = reader.readLine();
            if (line.toLowerCase().equals("quit")) {
                throw new QuitPlayerException("A player has decided to quit the game.");
            }
        }catch (IOException e){e.printStackTrace();}
        return line;
    }


}
