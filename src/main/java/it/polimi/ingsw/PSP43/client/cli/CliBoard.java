package it.polimi.ingsw.PSP43.client.cli;

import it.polimi.ingsw.PSP43.server.model.Coord;


public class CliBoard {

    private static final int DIM = 5;
    CliCell[][] cells = new CliCell[DIM][DIM];


    /**
     * Non default constructor that initialises all the cell
     * to empty cells (white colored and with a height of zero).
     */
    public CliBoard() {
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                cells[i][j] = new CliCell();
            }
        }
    }


    /**
     * This method displays the board on the cli, so it will be called
     * every time a visual change occurs.
     */
    public void show() {
        System.out.print("\n\n\t\t\t0\t\t1\t\t2\t\t3\t\t4\n\n\n");
        for (int i = 0; i < DIM; i++) {
            System.out.print("\t" + i + "\t\t");
            for (int j = 0; j < DIM; j++) {
                System.out.print(cells[i][j] + "\t\t");
            }
            System.out.print("\n\n\n");
        }
        System.out.print("\n\n");
    }


    /**
     * This method returns a single cell of the cli given its coordinate.
     * @param coord coordinate of the cell
     * @return cell at the given coordinate
     */
    public CliCell getCell(Coord coord) {
        return cells[coord.getX()][coord.getY()];
    }

}
