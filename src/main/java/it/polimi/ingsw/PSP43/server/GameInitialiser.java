package it.polimi.ingsw.PSP43.server;

import it.polimi.ingsw.PSP43.Color;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.modelHandlersException.NicknameAlreadyInUseException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class GameInitialiser {
    public static GameSession initialiseGame() throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException, NicknameAlreadyInUseException {
        GameSession game = new GameSession(9);
        Color[] colors = new Color[]{Color.ANSI_RED, Color.ANSI_GREEN};
        String[] players = new String[]{"Gibi" , "Rob"};
        for (int i=0; i<players.length; i++) {
            game.getPlayersHandler().createNewPlayer(players[i]);
        }
        int[] workerIds = new int[2];
        for (int i=0; i<game.getPlayersHandler().getNumOfPlayers(); i++) {
            for (int j=0; j<2; j++) {
                workerIds[i] = game.getWorkersHandler().addNewWorker(colors[i]);
            }
            game.getPlayersHandler().getPlayer(players[i]).setWorkersIdsArray(workerIds[0], workerIds[1]);
        }
        Coord coord1 = new Coord(4,3);
        Coord coord2 = new Coord(1,1);
        Cell cell1 = new Cell(coord1, new BoardObserver());
        cell1.setOccupiedByWorker(true);
        Cell cell2 = new Cell(coord2, new BoardObserver());
        cell2.setOccupiedByWorker(true);
        Coord coord3 = new Coord(0, 0);
        Cell cell3 = new Cell(coord3, new BoardObserver());
        cell3.setOccupiedByWorker(true);
        Coord coord4 = new Coord(4, 2);
        Cell cell4 = new Cell(coord4, new BoardObserver());
        cell3.setOccupiedByWorker(true);

        Coord[] coords = new Coord[]{coord1, coord2, coord3, coord4};

        game.getCellsHandler().changeStateOfCell(cell1, coord1);
        game.getCellsHandler().changeStateOfCell(cell2, coord2);
        game.getCellsHandler().changeStateOfCell(cell3, coord3);
        game.getCellsHandler().changeStateOfCell(cell4, coord4);

        int i=0;
        for (Worker w : game.getWorkersHandler().getWorkers()) {
            w.setCurrentPosition(coords[i]);
            i++;
        }

        return game;
    }
}
