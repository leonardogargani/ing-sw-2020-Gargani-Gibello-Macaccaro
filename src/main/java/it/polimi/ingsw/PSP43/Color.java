package it.polimi.ingsw.PSP43;


import it.polimi.ingsw.PSP43.client.Client;
import it.polimi.ingsw.PSP43.client.cli.CliGraphicHandler;
import it.polimi.ingsw.PSP43.client.gui.GuiGraphicHandler;

import java.io.Serializable;

public enum Color {

    ANSI_WHITE("\u001B[37m"),
    ANSI_GREEN("\u001B[32m"),
    ANSI_RED("\u001B[31m"),
    ANSI_BLUE("\u001B[34m");

    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String RESET = "\u001B[0m";

    private final String escape;


    /**
     * Non default constructor that sets the ansi code of the color.
     * @param escape sequence of byte representing the ansi code of the color
     */
    Color(String escape) {
        this.escape = escape;
    }


    /**
     * This method overrides the default toString() in order to return
     * the ansi code of the color.
     * @return ansi code of the color
     */
    // println() calls valueOf(), which calls toString()
    @Override
    public String toString()
    {
        return escape;
    }


    /**
     * This method prints the name of a color given its ansi code.
     * @param ansiCode ansi code of the color I want its name
     */
    public static void printName(Color ansiCode) {
        switch(ansiCode) {
            case ANSI_BLUE:
                System.out.println("BLUE");
                break;
            case ANSI_RED:
                System.out.println("RED");
                break;
            case ANSI_GREEN:
                System.out.println("GREEN");
                break;
            default:
                System.out.println("This color doesn't exist");
                break;
        }
    }


}
