package it.polimi.ingsw.PSP43.client.graphic.cli;


import it.polimi.ingsw.PSP43.server.model.Coord;

public class CliBoard {

    private static final int DIM = 5;
    CliCell[][] cells = new CliCell[DIM][DIM];


    /**
     * Non default constructor that initialises all the cell
     * to empty cells (white colored and with a height of zero).
     */
    public CliBoard() {
        for (int y = 0; y < DIM; y++) {
            for (int x = 0; x < DIM; x++) {
                cells[y][x] = new CliCell();
            }
        }
    }


    /**
     * This method displays the board on the cli, so it will be called
     * every time a visual change occurs.
     */
    public void show() {
        System.out.print("\n\n\t\t\t0\t\t1\t\t2\t\t3\t\t4\n\n\n");
        for (int y = 0; y < DIM; y++) {
            System.out.print("\t" + y + "\t\t");
            for (int x = 0; x < DIM; x++) {
                System.out.print(cells[y][x] + "\t\t");
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
        return cells[coord.getY()][coord.getX()];
    }


}
