package it.polimi.ingsw.PSP43.client.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class InputHandler {

    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private String line;


    /**
     * Method that request an input from the player through a BufferedReader
     * @return String written by the player
     * @throws QuitPlayerException exception thrown if a player writes "quit" (ignore capitalization) in the cli
     */
    public String requestInput() throws QuitPlayerException {
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

}
