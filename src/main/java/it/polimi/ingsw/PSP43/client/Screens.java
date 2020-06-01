package it.polimi.ingsw.PSP43.client;

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
                    "Choose between a 2 or 3 game: "
    ),

    INITIAL_2_CARDS_REQUEST(
                    "Choose 2 cards (you will receive the latest not chosen by other players):\n"
    ),

    INITIAL_3_CARDS_REQUEST(
                    "Choose 3 cards (you will receive the latest not chosen by other players):\n"
    ),

    CARD_REQUEST(
                    "Choose a card:\n"
    ),

    WORKERS_COLOR_REQUEST(
            "Choose a color for your workers:\n"
    ),

    BLOCK_POSITION_REQUEST(
            "Choose a position where to build a block:\n"
    ),

    WORKER_PLACEMENT_REQUEST(
            "\nChoose a position to place your worker in:\n"
    ),

    WORKER_MOVE_REQUEST(
                    "\nChoose a position where to place your worker next:\n"
    ),

    WORKER_SECOND_MOVE_REQUEST(
                    "\nChoose a position where to place your worker next (second move):\n"
    ),

    DOME_POSITION_REQUEST(
                    "\nChoose a position where to build a dome:\n"
    ),

    FORCE_OPPONENT_REQUEST(
            "\nChoose a position where to force your opponent:\n"
    ),

    CONNECTING_WITH_OTHERS(
            "\nWait for other players to connect...\n"
    ),

    CHOOSING_A_CARD(
            "\nYou're going to choose a God Card...\n"
    ),

    CHOOSING_INITIAL_POSITION(
            "\nYou're going to choose color and initial position of your worker...\n"
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
