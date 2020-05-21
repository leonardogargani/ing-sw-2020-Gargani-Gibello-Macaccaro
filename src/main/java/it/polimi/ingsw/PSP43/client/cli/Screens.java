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

    DISCONNECTED(
            Color.ANSI_RED +
                    "* * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n" +
                    "*                                                       *\n" +
                    "*          You  disconnected  from  the  game!          *\n" +
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
    ),

    PLAYERS_NUMBER_REQUEST(
            Color.ANSI_WHITE +
                    "Choose between a 2 or 3 game: " +
                    Color.RESET
    ),

    INITIAL_2_CARDS_REQUEST(
            Color.ANSI_WHITE +
                    "Choose 2 cards (you will receive the latest not chosen by other players):\n" +
                    Color.RESET
    ),

    INITIAL_3_CARDS_REQUEST(
            Color.ANSI_WHITE +
                    "Choose 3 cards (you will receive the latest not chosen by other players):\n" +
                    Color.RESET
    ),

    CARD_REQUEST(
            Color.ANSI_WHITE +
                    "Choose a card:\n" +
                    Color.RESET
    ),

    WORKERS_COLOR_REQUEST(
            Color.ANSI_WHITE +
            "Choose a color for your workers:\n" +
            Color.RESET
    ),

    BLOCK_POSITION_REQUEST(
            Color.ANSI_WHITE +
            "Choose a position where to build a block:\n" +
            Color.RESET
    ),

    WORKER_PLACEMENT_REQUEST(
            Color.ANSI_WHITE +
            "Choose a position to place your worker in:\n" +
            Color.RESET
    ),

    WORKER_MOVE_REQUEST(
            Color.ANSI_WHITE +
                    "Choose a position where to place your worker next:\n" +
                    Color.RESET
    ),

    WORKER_SECOND_MOVE_REQUEST(
            Color.ANSI_WHITE +
                    "Choose a position where to place your worker next (second move):\n" +
                    Color.RESET
    ),

    DOME_POSITION_REQUEST(
            Color.ANSI_WHITE +
                    "Choose a position where to build a dome:\n" +
                    Color.RESET
    ),

    FORCE_OPPONENT_REQUEST(
            Color.ANSI_WHITE +
            "Choose a position where to force your opponent:\n" +
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
