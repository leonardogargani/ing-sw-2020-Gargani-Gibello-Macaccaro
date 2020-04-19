package it.polimi.ingsw.PSP43.client.cli;

import it.polimi.ingsw.PSP43.Color;


public class CliCell {

    public static final String[] SYMBOLS = {
            "\u2B1C",   // 0: Ground
            "\u2460",   // 1: Level 1
            "\u2461",   // 2: Level 2
            "\u2462",   // 3: Level 3
            "\u274E"    // 4: Dome
    };

    private String background;
    private Color color;
    private String symbol;


    /**
     * Non default constructor that sets the ansi code of the background, the ansi
     * code of the color and the ansi code of the symbol of a single cell in the cli.
     */
    // color will be chosen based on the worker
    public CliCell() {
        this.background = Color.RESET;
        this.color = Color.ANSI_WHITE;
        this.symbol = CliCell.SYMBOLS[0];
    }


    /**
     * This method sets the color of the background, which can be either yellow
     * (if the cell is marked as free for a worker to move into) or default
     * @param isFree
     */
    public void markAsFree(boolean isFree) {
        this.background = isFree ? Color.ANSI_YELLOW_BACKGROUND : Color.RESET;
    }


    /**
     * This method sets the color of the cell, which corresponds
     * to the color of the worker occupying that cell.
     * @param color color of the worker on cell
     */
    public void setColor(Color color) {
        this.color = color;
    }


    /**
     * This method sets the color of the cell, which corresponds
     * to the color of the worker occupying that cell.
     * @param symbol ansi code of: empty square if level0, circled number
     *               if level 1-3, cross if level 4 (dome)
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }


    /**
     * This method overrides the default toString() in order to return the ansi code of the
     * color, the ansi code of the symbol and the ansi code of the default style of the text
     * @return ansi code of: background, color, symbol, default style
     */
    @Override
    public String toString() {
        // the order of the elements is important since the default value for background is Color.RESET,
        // while the one for color is Color.WHITE (in this way default background cannot reset color)
        return this.background + this.color + this.symbol + Color.RESET;
    }

}
