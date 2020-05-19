package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.client.networkMessages.ClientMessage;
import it.polimi.ingsw.PSP43.client.networkMessages.LeaveGameMessage;
import it.polimi.ingsw.PSP43.client.networkMessages.RegistrationMessage;
import it.polimi.ingsw.PSP43.server.ClientListener;
import it.polimi.ingsw.PSP43.server.RegisterClientListener;
import it.polimi.ingsw.PSP43.server.Sender;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.networkMessages.ChangeNickRequest;
import it.polimi.ingsw.PSP43.server.networkMessages.EndGameMessage;
import it.polimi.ingsw.PSP43.server.networkMessages.ServerMessage;

import java.util.ArrayList;
import java.util.HashMap;

public class GameSessionObservable implements Runnable {
    private final int idGame;
    protected int maxNumPlayers;
    private int numOfPlayers;

    private TurnState currentState;

    private final HashMap<String, ClientListener> listenersHashMap;

    public GameSessionObservable(int idGame) {
        this.idGame = idGame;
        this.listenersHashMap = new HashMap<>();
        numOfPlayers = 0;
        maxNumPlayers = 1;
    }

    public void run() {}

    public synchronized int registerToTheGame(RegistrationMessage message, ClientListener player) {
        if (numOfPlayers < maxNumPlayers) {
            for (String s : listenersHashMap.keySet()) {
                if (s.equals(message.getNick())) {
                    ChangeNickRequest notifyChangeNick = new ChangeNickRequest("We are sorry, but " + message.getNick() +
                            "is already in use.");
                    player.sendMessage(notifyChangeNick);
                    return -2;
                }
            }
            listenersHashMap.put(message.getNick(), player);
            numOfPlayers = listenersHashMap.size();
            currentState.executeState(message);
            return idGame;
        }
        return -1;
    }

    public synchronized void unregisterFromGame(LeaveGameMessage message, ClientListener player) {
        EndGameMessage endGameMessage = new EndGameMessage("We are sorry, due to connection problems the play is ended.");
        String nicknameLeft = null;
        for (String s : listenersHashMap.keySet()) {
            if (listenersHashMap.get(s).equals(player)) nicknameLeft = s;
        }
        ArrayList<String> listExcluded = new ArrayList<>();
        listExcluded.add(nicknameLeft);
        sendBroadCast(endGameMessage, listExcluded);
    }

    public void eliminatePlayer(Player playerEliminated) {
        EndGameMessage message = new EndGameMessage("We are sorry but you have lost the game.");
        sendMessage(message, playerEliminated.getNickname());
        listenersHashMap.remove(playerEliminated.getNickname());
        numOfPlayers = listenersHashMap.size();
    }

    public void sendMessage(ServerMessage genericMessage, String addressee) {
        for (String s : getListenersHashMap().keySet()) {
            if (addressee.equals(s))
                getListenersHashMap().get(s).sendMessage(genericMessage);
        }
    }

    public void sendMessage(EndGameMessage genericMessage, String addressee) {
        for (String s : getListenersHashMap().keySet()) {
            if (addressee.equals(s))
                getListenersHashMap().get(s).sendMessage(genericMessage);
        }
    }

    public void sendBroadCast(ServerMessage message) {
        for (String s : getListenersHashMap().keySet()) {
            getListenersHashMap().get(s).sendMessage(message);
        }
    }

    public void sendBroadCast(ServerMessage message, ArrayList<String> nicksExcluded) {
        for (String s : getListenersHashMap().keySet()) {
            if (!nicksExcluded.contains(s))
                getListenersHashMap().get(s).sendMessage(message);
        }
    }

    public void sendBroadCast(EndGameMessage message, ArrayList<String> nicksExcluded) {
        for (String s : getListenersHashMap().keySet()) {
            if (!nicksExcluded.contains(s))
                getListenersHashMap().get(s).sendMessage(message);
        }
    }

    public<T extends ClientMessage> T sendRequest(ServerMessage message, String addressee, Class<T> typeExpected) throws GameEndedException {
        ClientListener listenerAddressee = listenersHashMap.get(addressee);

        Sender newSender = new Sender(listenerAddressee, message);

        while (true) {
            ClientMessage messageArrived = newSender.call();
            if (typeExpected.isInstance(messageArrived)) {
                return typeExpected.cast(messageArrived);
            }
            else if (messageArrived instanceof LeaveGameMessage) {
                sendBroadCast(new EndGameMessage("We are sorry, but for connecting problems the game is ended!"));
                throw new GameEndedException();
            }
        }
    }

    public void sendEndingMessage(EndGameMessage messageToLosers, EndGameMessage messageForTheWinner, ArrayList<String> nicksExcluded) {
        for (String s : listenersHashMap.keySet()) {
            if (!nicksExcluded.contains(s)) {
                listenersHashMap.get(s).sendMessage(messageToLosers);
            }
        }

        ClientListener listernerLoser = listenersHashMap.get(nicksExcluded.get(0));
        if (listernerLoser != null)
            listenersHashMap.get(nicksExcluded.get(0)).sendMessage(messageForTheWinner);
        RegisterClientListener ending = new RegisterClientListener();
        ending.removeGameSession(this.idGame);
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

    public int getMaxNumPlayers() {
        return maxNumPlayers;
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public void setCurrentState(TurnState currentState) {
        this.currentState = currentState;
    }
}
