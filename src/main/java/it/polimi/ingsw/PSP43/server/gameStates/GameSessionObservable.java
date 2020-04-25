package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.client.Client;
import it.polimi.ingsw.PSP43.client.networkMessages.ClientMessage;
import it.polimi.ingsw.PSP43.client.networkMessages.LeaveGameMessage;
import it.polimi.ingsw.PSP43.client.networkMessages.RegistrationMessage;
import it.polimi.ingsw.PSP43.server.ClientListener;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.networkMessages.EndGameMessage;
import it.polimi.ingsw.PSP43.server.networkMessages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class GameSessionObservable {
    private int idGame;
    protected int maxNumPlayers;

    protected TurnState currentState;

    private HashMap<String, ClientListener> listenersHashMap;

    public GameSessionObservable(int idGame) {
        this.idGame = idGame;
        this.listenersHashMap = new HashMap<>();
    }

    public synchronized int registerToTheGame(RegistrationMessage message, ClientListener player) throws IOException, ClassNotFoundException {
        if (getListenersHashMap().size() != maxNumPlayers) {
            listenersHashMap.put(message.getNick(), player);
            currentState.executeState(message);
            return idGame;
        }
        else return -1;
    }

    public synchronized boolean unregisterFromGame(LeaveGameMessage message, ClientListener player) throws IOException {
        EndGameMessage endGameMessage = new EndGameMessage("We are sorry, due to connection problems the play is ended.");
        String nicknameLeft = null;
        for (String s : listenersHashMap.keySet()) {
            if (listenersHashMap.get(s).equals(player)) nicknameLeft = s;
        }
        ArrayList<String> listExcluded = new ArrayList<>();
        listExcluded.add(nicknameLeft);
        sendBroadCast(endGameMessage, listExcluded);
        return true;
    }

    public void eliminatePlayer(Player playerEliminated) {
        // TODO : send a message to the player eliminated
        listenersHashMap.remove(playerEliminated.getNickname());
    }

    public void sendBroadCast(ServerMessage message) throws IOException {
        for (String s : getListenersHashMap().keySet()) {
            getListenersHashMap().get(s).sendMessage(message);
        }
    }

    public void sendBroadCast(ServerMessage message, ArrayList<String> nicksExcluded) throws IOException {
        for (String s : getListenersHashMap().keySet()) {
            if (!nicksExcluded.contains(getListenersHashMap().get(s)))
                getListenersHashMap().get(s).sendMessage(message);
        }
    }

    public boolean sendRequest(ServerMessage message, String addressee, ClientMessage typeResponse) throws IOException, ClassNotFoundException {
        ClientListener listenerAddressee = listenersHashMap.get(addressee);
        ClientMessage messageArrived = listenerAddressee.sendRequest(message);
        if (messageArrived.getClass().isInstance(typeResponse)) {
            typeResponse = messageArrived;
            return true;
        }
        else return false;
    }

    /**
     * Getter method for gamers arraylist
     * @return gamers ,that is the gamers list
     */
    public HashMap<String, ClientListener> getListenersHashMap() { return listenersHashMap; }


    /**
     * Method to get the game id
     * @return idGame is the id of that gamesession
     */
    public int getIdGame(){
        return idGame;
    }

}
