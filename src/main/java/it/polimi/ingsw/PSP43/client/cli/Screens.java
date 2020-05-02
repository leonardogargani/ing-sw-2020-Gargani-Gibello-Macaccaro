package it.polimi.ingsw.PSP43.client.cli;

import it.polimi.ingsw.PSP43.Color;


public enum Screens {

    WELCOME(
            Color.ANSI_GREEN +
                    "* * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n" +
                    "*                                                       *\n" +
                    "*                       W e l c o m e                   *\n" +
                    "*                            T o                        *\n" +
                    "*                     S A N T O R I N I                 *\n" +
                    "*                                                       *\n" +
                    "* * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n" +
                    Color.RESET
    ),

    LOSER(
            Color.ANSI_RED +
                    "* * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n" +
                    "*                                                       *\n" +
                    "*          Y o u   l o s t   t h e   g a m e !          *\n" +
                    "*                                                       *\n" +
                    "* * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n" +
                    Color.RESET
    ),

    WINNER(
            Color.ANSI_GREEN +
                    "* * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n" +
                    "*                                                       *\n" +
                    "*           Y o u   w o n   t h e   g a m e !           *\n" +
                    "*                                                       *\n" +
                    "* * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n" +
                    Color.RESET
    ),

    YOUR_TURN(
            Color.ANSI_WHITE +
                    "          -------------------------------------          \n" +
                    "          |          It's your turn           |          \n" +
                    "          -------------------------------------          \n" +
                    Color.RESET
    );

    private final String message;


    Screens(String message) {
        this.message = message;
    }


    /**
     * toString() method overridden in order to print the message by simply
     * adding it to the println()
     * @return message to print out in the cli/gui
     */
    @Override
    public String toString() {
        return message;
    }


}
