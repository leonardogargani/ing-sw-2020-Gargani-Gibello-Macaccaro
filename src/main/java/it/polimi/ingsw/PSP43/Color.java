package it.polimi.ingsw.PSP43;


public enum Color {

    ANSI_GREEN("\u001B[32m"),
    ANSI_WHITE("\u001B[37m"),
    ANSI_RED("\u001B[31m"),
    ANSI_YELLOW("\u001B[33m"),
    ANSI_BLUE("\u001B[34m");

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

}
