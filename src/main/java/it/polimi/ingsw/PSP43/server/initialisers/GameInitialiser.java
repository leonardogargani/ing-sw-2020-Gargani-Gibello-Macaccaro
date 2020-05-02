package it.polimi.ingsw.PSP43.server.initialisers;

import it.polimi.ingsw.PSP43.Color;
import it.polimi.ingsw.PSP43.server.BoardObserver;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlers.CardsHandler;
import it.polimi.ingsw.PSP43.server.modelHandlers.PlayersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.NicknameAlreadyInUseException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;

public class GameInitialiser {

    public static GameSession initialiseGame() throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException, NicknameAlreadyInUseException {
        GameSession game = new GameSession(9);

        return game;
    }

    public static GameSession initialisePlayers(GameSession gameSession) throws NicknameAlreadyInUseException {
        String[] players = new String[]{"Gibi" , "Rob"};
        for (int i=0; i<players.length; i++) {
            gameSession.getPlayersHandler().createNewPlayer(players[i]);
        }

        return gameSession;
    }

    public static GameSession initialiseWorkers(GameSession gameSession) {
        Color[] colors = new Color[]{Color.ANSI_RED, Color.ANSI_GREEN};
        int[] workerIds = new int[2];

        for (int i=0; i<gameSession.getPlayersHandler().getNumOfPlayers(); i++) {
            String nicknameCurrent = gameSession.getPlayersHandler().getPlayer(i).getNickname();
            for (int j=0; j<2; j++) {
                workerIds[i] = gameSession.getWorkersHandler().addNewWorker(colors[i]);
            }
            gameSession.getPlayersHandler().getPlayer(nicknameCurrent).setWorkersIdsArray(workerIds[0], workerIds[1]);
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

        gameSession.getCellsHandler().changeStateOfCell(cell1, coord1);
        gameSession.getCellsHandler().changeStateOfCell(cell2, coord2);
        gameSession.getCellsHandler().changeStateOfCell(cell3, coord3);
        gameSession.getCellsHandler().changeStateOfCell(cell4, coord4);

        int i=0;
        for (Worker w : gameSession.getWorkersHandler().getWorkers()) {
            w.setCurrentPosition(coords[i]);
            i++;
        }

        return gameSession;
    }

    public static GameSession initialiseCards(GameSession gameSession) {
        PlayersHandler playersHandler = gameSession.getPlayersHandler();
        CardsHandler cardsHandler = gameSession.getCardsHandler();
        ArrayList<AbstractGodCard> cards = gameSession.getCardsHandler().getDeckOfAbstractGodCards();

        for (int i=0; i<playersHandler.getNumOfPlayers(); i++) {
            cardsHandler.setCardToPlayer(playersHandler.getPlayer(i).getNickname(), cards.get(i).getGodName());
            playersHandler.getPlayer(i).setAbstractGodCard(cards.get(i));
        }

        return gameSession;
    }
}
